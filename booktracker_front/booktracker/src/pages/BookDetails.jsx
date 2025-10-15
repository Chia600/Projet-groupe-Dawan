// src/pages/BookDetails.jsx
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import "../assets/bookDetails.css";

export default function BookDetails() {
    const { id } = useParams();
    const [book, setBook] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchBookDetails = async () => {
            try {
                const res = await axios.get(`http://localhost:8080/api/books/details/${id}`);

                console.log("Données reçues :", res.data);
                console.log("Description brute :", res.data.description);

                setBook(res.data);
            } catch (err) {
                console.error("Erreur de chargement du livre :", err);
                setError("Impossible de charger les détails du livre.");
            }

        };
        fetchBookDetails();
    }, [id]);

    if (error) return <p>{error}</p>;
    if (!book) return <p>Chargement...</p>;

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
                </div>

                {/* --- Infos du livre à droite --- */}
                <div className="book-info-section">
                    <h2 className="book-title">{book.title}</h2>
                    <p className="book-author">{book.author}</p>

                    {/* --- Étoiles + moyenne globale (factice ici) --- */}
                    <div className="book-rating">
                        <div className="stars">
                            {"★".repeat(0)}{"☆".repeat(5)} {/* Placeholder */}
                        </div>
                        <div className="global-average">Moyenne globale</div>
                    </div>

                    {/* --- Description --- */}
                    <p className="book-description">
                        {book.description?.length > 350
                            ? book.description.replace(/<\/?[^>]+(>|$)/g, "").slice(0, 350) + "..."
                            : book.description.replace(/<\/?[^>]+(>|$)/g, "")}
                    </p>

                    {/* --- Détails secondaires --- */}
                    <div className="book-extra">
                        <p><strong>Genre :</strong> {book.category}</p>
                        <p><strong>Nombre de pages :</strong> {book.pageNumber}</p>
                        <p><strong>Année de publication :</strong> {book.publicationDate}</p>
                    </div>
                </div>
            </div>
        </div>
    );
}
