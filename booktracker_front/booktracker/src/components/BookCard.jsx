import React from "react";
import "./BookCard.css"; // fichier CSS pour le hover

export default function BookCard({ book }) {
  return (
    <div className="book-card">
      {book.coverUrl ? (
        <img src={book.coverUrl} alt={book.title} className="book-cover" />
      ) : (
        <div className="book-cover placeholder">Pas de couverture</div>
      )}

      <div className="book-overlay">
        <h3>{book.title}</h3>
        {book.description && <p>{book.description.length > 150 ? book.description.slice(0, 150) + "..." : book.description}</p>}
        {book.publishedDate && <small>Publié le : {book.publishedDate}</small>}
      </div>
    </div>
  );
}
