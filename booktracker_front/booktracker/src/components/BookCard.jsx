// src/components/BookCard.jsx
import React from "react";
import { Link } from "react-router-dom";
import "../assets/bookCard.css";

export default function BookCard({ book }) {
   const id = book.bookId || book.idVolume || book.id || book.googleId;

   return (
      <div className="book-card">
         <div className="book-cover-container">
            {book.cover ? (
               <img src={book.cover} alt={book.title} className="book-cover" />
            ) : (
               <div className="book-placeholder">Couverture de livre</div>
            )}
         </div>
         <h3 className="book-card-title">{book.title}</h3>

         {/* lien dynamique vers le détail */}
         <Link to={`/books/${id}`} className="details-button">
            Détails
         </Link>
      </div>
   );
}
