import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../assets/collection.css";

export default function Collection() {
   const navigate = useNavigate();
   const [books, setBooks] = useState([]);
   const [loading, setLoading] = useState(false);
   const [error, setError] = useState(null);

   const [selectedBook, setSelectedBook] = useState(null);
   const [showReviewModal, setShowReviewModal] = useState(false);
   const [showDeleteModal, setShowDeleteModal] = useState(false);
   const [reviewText, setReviewText] = useState('');
   const [rating, setRating] = useState(0);

   const userId = localStorage.getItem('userId');
   const baseUrl = "http://localhost:8080/api";

   useEffect(() => {
      if (userId) {
         fetchFavoriteBooks();
      } else {
         setError("Vous devez être connecté pour voir votre collection");
         setLoading(false);
      }
   }, [userId]);

   const fetchFavoriteBooks = async () => {
      setLoading(true);
      setError(null);
      try {
         console.log("📡 Récupération des favoris pour userId:", userId);

         //Récupère les IDs des livres favoris
         const favoritesResponse = await axios.get(`${baseUrl}/users/${userId}/books`);
         const bookIds = favoritesResponse.data;

         console.log("✅ IDs des favoris:", bookIds);

         if (!bookIds || bookIds.length === 0) {
            setBooks([]);
            setLoading(false);
            return;
         }

         // Pour chaque ID, récupère les détails du livre et sa review
         const booksWithDetails = await Promise.all(
            bookIds.map(async (bookId) => {
               try {
                  // Récupérer les détails du livre
                  const bookResponse = await axios.get(`${baseUrl}/books/${bookId}`);
                  const bookData = bookResponse.data;

                  // Récupère la review de l'utilisateur pour ce livre
                  let userReview = null;
                  try {
                     const reviewResponse = await axios.get(`${baseUrl}/reviews/${userId}/${bookId}`);
                     userReview = reviewResponse.data;
                  } catch (reviewError) {
                     console.log(`ℹ️ Pas de review pour le livre ${bookId}`);
                  }

                  return {
                     ...bookData,
                     userReview: userReview
                  };
               } catch (bookError) {
                  console.error(`❌ Erreur chargement livre ${bookId}:`, bookError);
                  return null;
               }
            })
         );

         // Filtrer les livres null (erreurs de chargement)
         const validBooks = booksWithDetails.filter(book => book !== null);
         setBooks(validBooks);
         console.log("✅ Collection complète:", validBooks);

      } catch (err) {
         console.error("❌ Erreur de chargement:", err);
         setError("Impossible de charger votre collection");
      } finally {
         setLoading(false);
      }
   };

   const StarRating = ({ value, onChange, readonly = false }) => {
      return (
         <div className="star-rating">
            {[1, 2, 3, 4, 5].map((star) => (
               <span
                  key={star}
                  className={star <= value ? 'filled-star' : 'empty-star'}
                  onClick={() => !readonly && onChange && onChange(star)}
                  style={{
                     cursor: readonly ? 'default' : 'pointer',
                     fontSize: '1.5rem',
                     margin: '0 0.1rem'
                  }}
               >
                  ★
               </span>
            ))}
         </div>
      );
   };

   const handleAddOrEditReview = (book) => {
      setSelectedBook(book);
      setReviewText(book.userReview?.review || '');
      setRating(book.userReview?.rating || 0);
      setShowReviewModal(true);
   };

   const handleSaveReview = async () => {
      if (!selectedBook) return;
      if (rating === 0) {
         alert("Veuillez sélectionner une note");
         return;
      }

      try {
         const reviewData = {
            userId: parseInt(userId),
            bookId: selectedBook.id,
            review: reviewText,
            rating: rating,
            creationDate: selectedBook.userReview?.creationDate || new Date().toISOString().split('T')[0]
         };

         if (selectedBook.userReview) {
            await axios.put(`${baseUrl}/reviews/${userId}/${selectedBook.id}`, reviewData);
            alert("✅ Critique mise à jour !");
         } else {
            await axios.post(`${baseUrl}/reviews`, reviewData);
            alert("✅ Critique ajoutée !");
         }

         setShowReviewModal(false);
         setSelectedBook(null);

         // Recharge la collection
         await fetchFavoriteBooks();

      } catch (err) {
         console.error('❌ Erreur sauvegarde:', err);
         alert('Erreur lors de la sauvegarde de votre critique');
      }
   };

   const handleRemoveFromFavorites = (book) => {
      setSelectedBook(book);
      setShowDeleteModal(true);
   };

   const handleDeleteConfirm = async () => {
      if (!selectedBook) return;

      try {
         // Supprime le livre des favoris
         await axios.delete(`${baseUrl}/users/${userId}/books/${selectedBook.id}`);

         // Supprime la review si elle existe
         if (selectedBook.userReview) {
            try {
               await axios.delete(`${baseUrl}/reviews/${userId}/${selectedBook.id}`);
            } catch (reviewError) {
               console.warn("⚠️ Pas de review à supprimer");
            }
         }

         setShowDeleteModal(false);
         setSelectedBook(null);
         alert("✅ Livre retiré de vos favoris");

         // Recharge la collection
         await fetchFavoriteBooks();

      } catch (err) {
         console.error('❌ Erreur suppression:', err);
         alert('Erreur lors de la suppression');
      }
   };

   const formatDate = (dateString) => {
      if (!dateString) return 'N/A';
      const date = new Date(dateString);
      return date.toLocaleDateString('fr-FR');
   };

   const goToBookDetails = (bookId) => {
      navigate(`/book/${bookId}`);
   };

   const renderStars = (rating) => {
      if (!rating) return null;
      const stars = [];
      for (let i = 1; i <= 5; i++) {
         stars.push(
            <span key={i} className={i <= rating ? "filled-star" : "empty-star"}>★</span>
         );
      }
      return stars;
   };

   if (!userId) {
      return (
         <div className="collection-page">
            <div className="error-container">
               <h2>Connexion requise</h2>
               <p>Vous devez être connecté pour voir votre collection</p>
            </div>
         </div>
      );
   }

   return (
      <div className="collection-page">
         <h1>Ma collection de livres</h1>
         <p className="collection-subtitle">
            {books.length} livre{books.length > 1 ? 's' : ''} dans vos favoris
         </p>

         {loading && <p className="loading">Chargement de votre collection...</p>}

         {error && <p className="error">{error}</p>}

         {!loading && !error && (
            <div className="collection-content">
               {books.length > 0 ? (
                  <div className="book-grid">
                     {books.map((book) => (
                        <div key={book.id} className="collection-book-card">
                           <div
                              className="book-cover-container"
                              onClick={() => goToBookDetails(book.idVolume || book.id)}
                              style={{ cursor: 'pointer' }}
                           >
                              <img
                                 src={book.cover || "https://via.placeholder.com/200x300"}
                                 alt={book.title || "Livre"}
                                 className="book-collection-cover"
                              />
                           </div>

                           <div className="collection-book-info">
                              <h3
                                 className="book-title"
                                 onClick={() => goToBookDetails(book.idVolume || book.id)}
                                 style={{ cursor: 'pointer' }}
                              >
                                 {book.title || "Titre inconnu"}
                              </h3>

                              <p className="book-author">
                                 {book.author || "Auteur inconnu"}
                              </p>

                              {book.category && (
                                 <p className="book-category">
                                    <strong>Catégorie :</strong> {book.category}
                                 </p>
                              )}

                              {book.publicationDate && (
                                 <p className="book-date">
                                    <strong>Publié :</strong> {formatDate(book.publicationDate)}
                                 </p>
                              )}

                              {book.userReview ? (
                                 <>
                                    <div className="user-rating">
                                       <strong>Ma note :</strong>
                                       {renderStars(book.userReview.rating)}
                                       <span className="rating-value">({book.userReview.rating}/5)</span>
                                    </div>

                                    {book.userReview.review && (
                                       <div className="user-review">
                                          <strong>Ma critique :</strong>
                                          <p className="review-text">
                                             {book.userReview.review.length > 150
                                                ? book.userReview.review.substring(0, 150) + "..."
                                                : book.userReview.review
                                             }
                                          </p>
                                       </div>
                                    )}

                                    {book.userReview.creationDate && (
                                       <div className="added-date">
                                          <strong>Note ajoutée le :</strong> {formatDate(book.userReview.creationDate)}
                                       </div>
                                    )}
                                 </>
                              ) : (
                                 <p className="no-review">Aucune critique pour ce livre</p>
                              )}

                              <div className="collection-actions">
                                 <button
                                    onClick={() => handleAddOrEditReview(book)}
                                    className="edit-button"
                                 >
                                    {book.userReview ? '✏️ Modifier' : '➕ Ajouter une critique'}
                                 </button>
                                 <button
                                    onClick={() => handleRemoveFromFavorites(book)}
                                    className="delete-button"
                                 >
                                    🗑️ Retirer
                                 </button>
                              </div>
                           </div>
                        </div>
                     ))}
                  </div>
               ) : (
                  <div className="empty-collection">
                     <h2>📚 Votre collection est vide</h2>
                     <p>Commencez par rechercher et ajouter des livres à vos favoris !</p>
                     <button
                        onClick={() => navigate('/books')}
                        className="browse-button"
                     >
                        Parcourir les livres
                     </button>
                  </div>
               )}
            </div>
         )}

         {/* Modal de critique */}
         {showReviewModal && selectedBook && (
            <div className="modal-overlay" onClick={() => setShowReviewModal(false)}>
               <div className="modal" onClick={(e) => e.stopPropagation()}>
                  <h2 className="modal-title">
                     {selectedBook.userReview ? 'Modifier ma critique' : 'Ajouter une critique'}
                  </h2>
                  <h3 className="book-title-modal">{selectedBook.title}</h3>

                  <div className="form-group">
                     <label className="form-label">Ma note * :</label>
                     <StarRating value={rating} onChange={setRating} />
                  </div>

                  <div className="form-group">
                     <label className="form-label">Ma critique :</label>
                     <textarea
                        value={reviewText}
                        onChange={(e) => setReviewText(e.target.value)}
                        placeholder="Partagez votre avis sur ce livre..."
                        className="critique-textarea"
                        rows={6}
                     />
                  </div>

                  <div className="modal-actions">
                     <button
                        onClick={() => setShowReviewModal(false)}
                        className="button button-cancel"
                     >
                        Annuler
                     </button>
                     <button
                        onClick={handleSaveReview}
                        className="button button-validate"
                     >
                        Enregistrer
                     </button>
                  </div>
               </div>
            </div>
         )}

         {/* Modal de confirmation de suppression */}
         {showDeleteModal && selectedBook && (
            <div className="modal-overlay" onClick={() => setShowDeleteModal(false)}>
               <div className="modal modal-small" onClick={(e) => e.stopPropagation()}>
                  <h3 className="modal-title">Retirer de mes favoris</h3>
                  <p className="modal-description">
                     Voulez-vous vraiment retirer "{selectedBook.title}" de vos favoris ?
                     {selectedBook.userReview && " Votre critique sera également supprimée."}
                  </p>
                  <div className="modal-actions-end">
                     <button
                        onClick={() => setShowDeleteModal(false)}
                        className="button button-cancel"
                     >
                        Annuler
                     </button>
                     <button
                        onClick={handleDeleteConfirm}
                        className="button button-confirm"
                     >
                        Confirmer
                     </button>
                  </div>
               </div>
            </div>
         )}
      </div>
   );
}