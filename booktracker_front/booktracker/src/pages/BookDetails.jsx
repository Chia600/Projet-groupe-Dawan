// src/pages/BookDetails.jsx
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import "../assets/bookDetails.css";

export default function BookDetails() {
    const { id } = useParams();
    const [book, setBook] = useState(null);
    const [error, setError] = useState(null);
    const [userReview, setUserReview] = useState(null);
    const [isInFavorites, setIsInFavorites] = useState(false);
    
    // Modal states
    const [showReviewModal, setShowReviewModal] = useState(false);
    const [reviewText, setReviewText] = useState("");
    const [rating, setRating] = useState(0);

    const userId = localStorage.getItem('userId');
    const baseUrl = "http://localhost:8080/api";

    useEffect(() => {
        fetchBookDetails();
        if (userId) {
            checkUserReview();
            checkIfInFavorites();
        }
    }, [id]);

    const fetchBookDetails = async () => {
        try {
            const res = await axios.get(`${baseUrl}/books/details/${id}`);
            console.log("Données reçues :", res.data);
            console.log("Description brute :", res.data.description);
            setBook(res.data);
        } catch (err) {
            console.error("Erreur de chargement du livre :", err);
            setError("Impossible de charger les détails du livre.");
        }
    };

    const checkUserReview = async () => {
        try {
            const response = await axios.get(`${baseUrl}/reviews/${userId}/${id}`);
            setUserReview(response.data);
            setReviewText(response.data.review || "");
            setRating(response.data.rating || 0);
        } catch (err) {
            if (err.response?.status !== 404) {
                console.error("Erreur lors de la vérification de la review:", err);
            }
        }
    };

    const checkIfInFavorites = async () => {
        try {
            const response = await axios.get(`${baseUrl}/users/${userId}/books`);
            const favoriteIds = response.data;
            setIsInFavorites(favoriteIds.includes(parseInt(id)));
        } catch (err) {
            console.error("Erreur lors de la vérification des favoris:", err);
        }
    };

    const handleAddToFavorites = async () => {
        if (!userId) {
            alert("Vous devez être connecté pour ajouter des livres à vos favoris");
            return;
        }

        try {
            await axios.post(`${baseUrl}/users/${userId}/books`, book.id);
            setIsInFavorites(true);
            alert("✅ Livre ajouté à vos favoris !");
            setShowReviewModal(true);
        } catch (err) {
            console.error("Erreur lors de l'ajout aux favoris:", err);
            alert("Erreur lors de l'ajout du livre aux favoris");
        }
    };

    const handleRemoveFromFavorites = async () => {
        if (!window.confirm("Voulez-vous vraiment retirer ce livre de vos favoris ?")) {
            return;
        }

        try {
            await axios.delete(`${baseUrl}/users/${userId}/books/${book.id}`);
            
            if (userReview) {
                await axios.delete(`${baseUrl}/reviews/${userId}/${book.id}`);
            }
            
            setIsInFavorites(false);
            setUserReview(null);
            setReviewText("");
            setRating(0);
            alert("✅ Livre retiré de vos favoris");
        } catch (err) {
            console.error("Erreur lors de la suppression:", err);
            alert("Erreur lors de la suppression");
        }
    };

    const handleSaveReview = async () => {
        if (rating === 0) {
            alert("Veuillez sélectionner une note");
            return;
        }

        try {
            const reviewData = {
                userId: parseInt(userId),
                bookId: book.id,
                review: reviewText,
                rating: rating,
                creationDate: userReview?.creationDate || new Date().toISOString().split('T')[0]
            };

            if (userReview) {
                await axios.put(`${baseUrl}/reviews/${userId}/${book.id}`, reviewData);
                alert("✅ Critique mise à jour !");
            } else {
                await axios.post(`${baseUrl}/reviews`, reviewData);
                alert("✅ Critique ajoutée !");
            }

            setShowReviewModal(false);
            checkUserReview();
        } catch (err) {
            console.error("Erreur lors de la sauvegarde:", err);
            alert("Erreur lors de la sauvegarde de votre critique");
        }
    };

    const renderStars = (rating) => {
        return "★".repeat(Math.round(rating)) + "☆".repeat(5 - Math.round(rating));
    };

    const StarRating = ({ value, onChange }) => {
        return (
            <div className="star-rating-input">
                {[1, 2, 3, 4, 5].map((star) => (
                    <span
                        key={star}
                        onClick={() => onChange(star)}
                        style={{ 
                            cursor: 'pointer', 
                            fontSize: '2rem',
                            color: star <= value ? '#ffc107' : '#ddd',
                            marginRight: '0.25rem'
                        }}
                    >
                        ★
                    </span>
                ))}
            </div>
        );
    };

    const calculateAverageRating = () => {
        if (!book?.reviews || book.reviews.length === 0) return 0;
        const sum = book.reviews.reduce((acc, review) => acc + review.rating, 0);
        return (sum / book.reviews.length).toFixed(1);
    };

    if (error) return <p>{error}</p>;
    if (!book) return <p>Chargement...</p>;

    const avgRating = calculateAverageRating();

    return (
        <div className="book-detail-page">
            <div className="book-detail-container">
                {/* --- Couverture à gauche --- */}
                <div className="book-cover-section">
                    <img
                        src={book.cover || "https://via.placeholder.com/200x300"}
                        alt={book.title}
                        className="book-detail-cover"
                    />
                    <p className="book-cover-text">Couverture du livre</p>

                    {/* Boutons d'action */}
                    {userId && (
                        <div className="book-actions" style={{ marginTop: '1rem' }}>
                            {isInFavorites ? (
                                <>
                                    <button 
                                        onClick={() => setShowReviewModal(true)}
                                        className="btn-action btn-primary"
                                        style={{ marginBottom: '0.5rem' }}
                                    >
                                        {userReview ? '✏️ Modifier ma critique' : '➕ Ajouter une critique'}
                                    </button>
                                    <button 
                                        onClick={handleRemoveFromFavorites}
                                        className="btn-action btn-danger"
                                    >
                                        🗑️ Retirer des favoris
                                    </button>
                                </>
                            ) : (
                                <button 
                                    onClick={handleAddToFavorites}
                                    className="btn-action btn-success"
                                >
                                    ⭐ Ajouter à mes favoris
                                </button>
                            )}
                        </div>
                    )}

                    {/* La critique */}
                    {userReview && (
                        <div style={{ 
                            marginTop: '1.5rem', 
                            padding: '1rem', 
                            backgroundColor: '#f8f9fa',
                            borderRadius: '8px',
                            borderLeft: '4px solid #007bff'
                        }}>
                            <h4 style={{ margin: '0 0 0.5rem 0' }}>Ma note</h4>
                            <div className="stars">{renderStars(userReview.rating)}</div>
                            {userReview.review && (
                                <>
                                    <h4 style={{ margin: '1rem 0 0.5rem 0' }}>Ma critique</h4>
                                    <p style={{ margin: 0, fontSize: '0.9rem' }}>{userReview.review}</p>
                                </>
                            )}
                        </div>
                    )}
                </div>

                {/* --- Infos du livre à droite --- */}
                <div className="book-info-section">
                    <h2 className="book-detail-title">{book.title}</h2>
                    <p className="book-author">{book.author}</p>

                    {/* --- Étoiles + moyenne globale --- */}
                    <div className="book-rating">
                        <div className="stars">
                            {renderStars(avgRating)}
                        </div>
                        <div className="global-average">
                            {avgRating > 0 ? `Moyenne globale: ${avgRating}/5` : 'Moyenne globale'}
                            {book.reviews?.length > 0 && ` (${book.reviews.length} avis)`}
                        </div>
                    </div>

                    {/* --- Description --- */}
                    <p className="book-description">
                        {book.description?.length > 350
                            ? book.description.replace(/<\/?[^>]+(>|$)/g, "").slice(0, 350) + "..."
                            : book.description?.replace(/<\/?[^>]+(>|$)/g, "")}
                    </p>

                    {/* --- Détails secondaires --- */}
                    <div className="book-extra">
                        <p><strong>Genre :</strong> {book.category}</p>
                        <p><strong>Nombre de pages :</strong> {book.pageNumber}</p>
                        <p><strong>Année de publication :</strong> {book.publicationDate}</p>
                    </div>

                    {/* --- Toutes les critiques --- */}
                    {book.reviews && book.reviews.length > 0 && (
                        <div className="all-reviews" style={{ marginTop: '2rem' }}>
                            <h3>Avis des lecteurs</h3>
                            {book.reviews.map((review, index) => (
                                <div key={index} style={{
                                    backgroundColor: '#f8f9fa',
                                    padding: '1rem',
                                    borderRadius: '8px',
                                    marginBottom: '1rem',
                                    borderLeft: '3px solid #007bff'
                                }}>
                                    <div className="stars" style={{ marginBottom: '0.5rem' }}>
                                        {renderStars(review.rating)}
                                    </div>
                                    {review.review && <p style={{ margin: 0 }}>{review.review}</p>}
                                    {review.creationDate && (
                                        <small style={{ color: '#888' }}>
                                            {new Date(review.creationDate).toLocaleDateString('fr-FR')}
                                        </small>
                                    )}
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>

            {/* Modal de critique */}
            {showReviewModal && (
                <div 
                    className="modal-overlay" 
                    style={{
                        position: 'fixed',
                        top: 0,
                        left: 0,
                        right: 0,
                        bottom: 0,
                        backgroundColor: 'rgba(0, 0, 0, 0.5)',
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'center',
                        zIndex: 1000
                    }}
                    onClick={() => setShowReviewModal(false)}
                >
                    <div 
                        className="modal-content"
                        style={{
                            backgroundColor: 'white',
                            padding: '2rem',
                            borderRadius: '12px',
                            maxWidth: '600px',
                            width: '90%',
                            maxHeight: '90vh',
                            overflow: 'auto'
                        }}
                        onClick={(e) => e.stopPropagation()}
                    >
                        <h2 style={{ marginTop: 0 }}>
                            {userReview ? 'Modifier ma critique' : 'Ajouter une critique'}
                        </h2>
                        
                        <div style={{ marginBottom: '1.5rem' }}>
                            <label style={{ display: 'block', fontWeight: 'bold', marginBottom: '0.5rem' }}>
                                Ma note *
                            </label>
                            <StarRating value={rating} onChange={setRating} />
                        </div>

                        <div style={{ marginBottom: '1.5rem' }}>
                            <label style={{ display: 'block', fontWeight: 'bold', marginBottom: '0.5rem' }}>
                                Ma critique (optionnel)
                            </label>
                            <textarea
                                value={reviewText}
                                onChange={(e) => setReviewText(e.target.value)}
                                placeholder="Partagez votre avis sur ce livre..."
                                rows={6}
                                style={{
                                    width: '100%',
                                    padding: '0.75rem',
                                    border: '1px solid #ddd',
                                    borderRadius: '6px',
                                    fontSize: '1rem',
                                    fontFamily: 'inherit',
                                    resize: 'vertical'
                                }}
                            />
                        </div>

                        <div style={{ display: 'flex', gap: '1rem', justifyContent: 'flex-end' }}>
                            <button 
                                onClick={() => setShowReviewModal(false)}
                                style={{
                                    padding: '0.75rem 1.5rem',
                                    backgroundColor: '#6c757d',
                                    color: 'white',
                                    border: 'none',
                                    borderRadius: '6px',
                                    cursor: 'pointer',
                                    fontSize: '1rem'
                                }}
                            >
                                Annuler
                            </button>
                            <button 
                                onClick={handleSaveReview}
                                style={{
                                    padding: '0.75rem 1.5rem',
                                    backgroundColor: '#007bff',
                                    color: 'white',
                                    border: 'none',
                                    borderRadius: '6px',
                                    cursor: 'pointer',
                                    fontSize: '1rem'
                                }}
                            >
                                Enregistrer
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
