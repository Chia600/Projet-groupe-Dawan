import React from "react";
import "../assets/bookCard.css";
import {Link} from "react-router-dom"; // fichier CSS pour le hover

export default function BookCard({book}) {
    return (
        <div key={book.id} className="book-card">
            <div className="book-cover-container">
                {book.cover ? (
                    <img src={book.cover} alt={book.title} className="book-cover"/>
                ) : (
                    <div className="book-placeholder">Couverture de livre</div>
                )}
            </div>
            <h3 className="book-title">{book.title}</h3>
            <Link to={`/books/${book.id}`} className="details-button">
                Détails
            </Link>
        </div>
    );
}
