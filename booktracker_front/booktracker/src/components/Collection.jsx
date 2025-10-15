import React, { useState, useEffect } from 'react';
import api from '../api/axios';
import '../assets/collection.css';
import '../pages/BookDetails';

function Collection() {
  const [reviews, setReviews] = useState([]);
  const [selectedReview, setSelectedReview] = useState(null);
  const [showEditModal, setShowEditModal] = useState(false);
  const [editNote, setEditNote] = useState('');
  const [editRating, setEditRating] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const pageSize = 10;
  const book = {book};
  useEffect(() => {
    fetchCollection();
  }, [currentPage]);

  const fetchCollection = async () => {
    setLoading(true);
    setError(null);
    try {
      // Récupérer l'userId depuis le token (vous devrez peut-être le stocker lors du login)
      const userId = localStorage.getItem('userId'); // ou depuis un context/state global
      
      if (!userId) {
        setError('Utilisateur non connecté');
        setLoading(false);
        return;
      }

      // Utiliser l'endpoint existant avec pagination
      const response = await api.get(`/reviews/${userId}/${book.id}?page=${currentPage}&size=${pageSize}`);
      
      const pageData = response.data;
      setReviews(pageData.content || []);
      setTotalPages(pageData.totalPages || 0);
    } catch (err) {
      console.error('Erreur lors de la récupération de la collection:', err);
      setError(err.response?.data?.message || 'Erreur lors du chargement de votre collection');
    } finally {
      setLoading(false);
    }
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

  const StarRating = ({ value, onChange }) => {
    return (
      <div className="star-rating">
        {[1, 2, 3, 4, 5].map((star) => (
          <span
            key={star}
            className={star <= value ? 'filled-star' : 'empty-star'}
            onClick={() => onChange(star)}
            style={{ cursor: 'pointer', fontSize: '1.5rem' }}
          >
            ★
          </span>
        ))}
      </div>
    );
  };

  const handleEdit = (review) => {
    setSelectedReview(review);
    setEditNote(review.review || '');
    setEditRating(review.rating || 0);
    setShowEditModal(true);
  };

  const handleSave = async () => {
    if (!selectedReview) return;

    try {
      // PUT pour mettre à jour la review
      await api.put(`/reviews`, {
        userId: selectedReview.userId,
        bookId: selectedReview.bookId,
        review: editNote,
        rating: editRating,
        creationDate: selectedReview.creationDate
      });

      // Mise à jour locale
      setReviews(reviews.map(rev => 
        (rev.userId === selectedReview.userId && rev.bookId === selectedReview.bookId)
          ? { ...rev, review: editNote, rating: editRating }
          : rev
      ));

      setShowEditModal(false);
      setSelectedReview(null);
    } catch (err) {
      console.error('Erreur lors de la sauvegarde:', err);
      alert('Erreur lors de la sauvegarde de vos modifications');
    }
  };

  const handleDelete = async () => {
    if (!selectedReview) return;

    try {
      // DELETE pour supprimer la review
      await api.delete(`/reviews/${selectedReview.userId}/${selectedReview.bookId}`);

      // Mise à jour locale
      setReviews(reviews.filter(rev => 
        !(rev.userId === selectedReview.userId && rev.bookId === selectedReview.bookId)
      ));
      
      setShowDeleteConfirm(false);
      setSelectedReview(null);

      // Si la page est vide, revenir à la page précédente
      if (reviews.length === 1 && currentPage > 0) {
        setCurrentPage(currentPage - 1);
      } else {
        await fetchCollection();
      }
    } catch (err) {
      console.error('Erreur lors de la suppression:', err);
      alert('Erreur lors de la suppression de la review');
    }
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
    window.scrollTo(0, 0);
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('fr-FR');
  };

  return (
    <div className="book-tracker">
      <main className="main-content">
        <h1 className="page-title">Ma Collection</h1>

        {loading && <p className="loading">Chargement...</p>}
        {error && <p className="error">{error}</p>}

        {!loading && !error && reviews.length === 0 && (
          <p className="no-books">Votre collection est vide. Commencez par ajouter des livres et noter vos lectures !</p>
        )}

        {!loading && !error && reviews.length > 0 && (
          <div className="books-table-container">
            <table className="books-table">
              <thead>
                <tr>
                  <th>Couverture</th>
                  <th>Titre</th>
                  <th>Auteur</th>
                  <th>Note Moy.</th>
                  <th>Ma note</th>
                  <th>Ma critique</th>
                  <th>Date d'ajout</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {reviews.map((review, index) => {
                  // Récupérer les infos du livre depuis la review
                  const book = review.book || {};
                  
                  return (
                    <tr key={`${review.userId}-${review.bookId}`} className={index === reviews.length - 1 ? 'highlighted-row' : ''}>
                      <td>
                        <div className="book-cover">
                          {book.cover ? (
                            <img src={book.cover} alt={book.title} style={{ width: '50px', height: '75px', objectFit: 'cover' }} />
                          ) : (
                            '📕'
                          )}
                        </div>
                      </td>
                      <td>{book.title || 'Titre inconnu'}</td>
                      <td>{book.author || 'Auteur inconnu'}</td>
                      <td>
                        {book.reviews && book.reviews.length > 0 
                          ? (book.reviews.reduce((sum, r) => sum + r.rating, 0) / book.reviews.length).toFixed(1)
                          : '-'
                        }
                      </td>
                      <td>
                        <div className="rating">{renderStars(review.rating)}</div>
                      </td>
                      <td className="note-cell">{review.review || '-'}</td>
                      <td>{formatDate(review.creationDate)}</td>
                      <td>
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '0.25rem', alignItems: 'center' }}>
                          <button onClick={() => handleEdit(review)} className="edit-button">
                            Edit
                          </button>
                          <button 
                            onClick={() => {
                              setSelectedReview(review);
                              setShowDeleteConfirm(true);
                            }} 
                            className="delete-button"
                          >
                            X
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>

            {totalPages > 1 && (
              <div className="pagination">
                <button 
                  className="pagination-button" 
                  onClick={() => handlePageChange(currentPage - 1)}
                  disabled={currentPage === 0}
                >
                  {'<<'}
                </button>
                
                {Array.from({ length: totalPages }, (_, i) => (
                  <button 
                    key={i}
                    className={`pagination-button ${currentPage === i ? 'active' : ''}`}
                    onClick={() => handlePageChange(i)}
                  >
                    {i + 1}
                  </button>
                ))}
                
                <button 
                  className="pagination-button"
                  onClick={() => handlePageChange(currentPage + 1)}
                  disabled={currentPage === totalPages - 1}
                >
                  {'>>'}
                </button>
              </div>
            )}
          </div>
        )}
      </main>

      {showEditModal && selectedReview && (
        <div className="modal-overlay">
          <div className="modal">
            <h2 className="modal-title">
              {selectedReview.book?.title || 'Livre'} &gt; Critique et notation &gt; Edit
            </h2>
            
            <div className="form-group">
              <label className="form-label">Ma note :</label>
              <StarRating 
                value={editRating} 
                onChange={setEditRating}
              />
            </div>

            <div className="form-group">
              <label className="form-label">Que pensez-vous du livre ?</label>
              <textarea
                value={editNote}
                onChange={(e) => setEditNote(e.target.value)}
                placeholder="Écrivez votre critique (optionnel)"
                className="critique-textarea"
                rows={6}
              />
            </div>

            <div className="modal-actions">
              <button onClick={() => setShowEditModal(false)} className="button button-cancel">
                Annuler
              </button>
              <button onClick={handleSave} className="button button-validate">
                Valider
              </button>
              <button
                onClick={() => {
                  setShowEditModal(false);
                  setShowDeleteConfirm(true);
                }}
                className="button button-delete"
              >
                Retirer le livre de la collection
              </button>
            </div>
          </div>
        </div>
      )}

      {showDeleteConfirm && selectedReview && (
        <div className="modal-overlay">
          <div className="modal modal-small">
            <h3 className="modal-title">Voulez-vous supprimer ce livre ?</h3>
            <p className="modal-description">
              "{selectedReview.book?.title || 'Ce livre'}" sera retiré de votre collection ainsi que votre critique.
            </p>
            <div className="modal-actions-end">
              <button onClick={() => setShowDeleteConfirm(false)} className="button button-cancel">
                Annuler
              </button>
              <button onClick={handleDelete} className="button button-confirm">
                Confirmer
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Collection;