import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./BooksPage.css";

export default function BooksPage() {
    const [q, setQ] = useState("");
    const [page, setPage] = useState(1);
    const booksPerPage = 12;

    // Données mockées
    const mockBooks = [
        { id: 1, title: "Le Seigneur des Anneaux", cover: "https://m.media-amazon.com/images/I/81t2CVWEsUL._AC_UF1000,1000_QL80_.jpg" },
        { id: 2, title: "Harry Potter à l'école des sorciers", cover: "https://m.media-amazon.com/images/I/71rOzy4cyAL._AC_UF1000,1000_QL80_.jpg" },
        { id: 3, title: "1984", cover: "https://m.media-amazon.com/images/I/71kxa1-0mfL._AC_UF1000,1000_QL80_.jpg" },
        { id: 4, title: "L'Étranger", cover: "https://m.media-amazon.com/images/I/81t5uCl7n0L._AC_UF1000,1000_QL80_.jpg" },
        { id: 5, title: "Le Petit Prince", cover: "https://m.media-amazon.com/images/I/61s8qrxG5HL._AC_UF1000,1000_QL80_.jpg" },
        { id: 6, title: "Fahrenheit 451", cover: "https://m.media-amazon.com/images/I/71UwSHSZRnS._AC_UF1000,1000_QL80_.jpg" },
        { id: 7, title: "Les Misérables", cover: "https://m.media-amazon.com/images/I/81h2gWPTYJL._AC_UF1000,1000_QL80_.jpg" },
        { id: 8, title: "Dune", cover: "https://m.media-amazon.com/images/I/91zUasf8F+L._AC_UF1000,1000_QL80_.jpg" },
        { id: 9, title: "La Horde du Contrevent", cover: "https://m.media-amazon.com/images/I/81o2FYcRxiL._AC_UF1000,1000_QL80_.jpg" },
        { id: 10, title: "La Peste", cover: "https://m.media-amazon.com/images/I/71cV8rMGYQL._AC_UF1000,1000_QL80_.jpg" },
        { id: 11, title: "Les Fleurs du mal", cover: "https://m.media-amazon.com/images/I/81nAAuszh8L._AC_UF1000,1000_QL80_.jpg" },
        { id: 12, title: "Candide", cover: "https://m.media-amazon.com/images/I/81G+5kzI9PL._AC_UF1000,1000_QL80_.jpg" },
        { id: 13, title: "Le Rouge et le Noir", cover: "https://m.media-amazon.com/images/I/81vG8j4cLjL._AC_UF1000,1000_QL80_.jpg" },
        { id: 14, title: "L'Alchimiste", cover: "https://m.media-amazon.com/images/I/81L5yWiCh9L._AC_UF1000,1000_QL80_.jpg" },
        { id: 15, title: "Orgueil et Préjugés", cover: "https://m.media-amazon.com/images/I/71tLpJvAf0L._AC_UF1000,1000_QL80_.jpg" },
        { id: 16, title: "Crime et Châtiment", cover: "https://m.media-amazon.com/images/I/91BflMfS5dL._AC_UF1000,1000_QL80_.jpg" },
        { id: 17, title: "Les Trois Mousquetaires", cover: "https://m.media-amazon.com/images/I/91xg4LE1V2L._AC_UF1000,1000_QL80_.jpg" },
        { id: 18, title: "Le Comte de Monte-Cristo", cover: "https://m.media-amazon.com/images/I/91rj5bYx6vL._AC_UF1000,1000_QL80_.jpg" },
    ];

    // Filtrage par recherche
    const filteredBooks = mockBooks.filter((b) =>
        b.title.toLowerCase().includes(q.toLowerCase())
    );

    // Pagination logique
    const startIndex = (page - 1) * booksPerPage;
    const paginatedBooks = filteredBooks.slice(startIndex, startIndex + booksPerPage);
    const totalPages = Math.ceil(filteredBooks.length / booksPerPage);

    return (
        <div className="books-page">
            <h1>Catalogue des livres</h1>

            {/* Barre de recherche */}
            <div className="search-container">
                <input
                    type="text"
                    placeholder="Rechercher un livre..."
                    value={q}
                    onChange={(e) => {
                        setQ(e.target.value);
                        setPage(1);
                    }}
                />
            </div>

            {/* Grille livres */}
            <div className="book-grid">
                {paginatedBooks.map((book) => (
                    <div key={book.id} className="book-card">
                        <div className="book-cover-container">
                            {book.cover ? (
                                <img src={book.cover} alt={book.title} className="book-cover" />
                            ) : (
                                <div className="book-placeholder">Couverture de livre</div>
                            )}
                        </div>
                        <h3 className="book-title">{book.title}</h3>
                        <Link to={`/books/${book.id}`} className="details-button">
                            Détails
                        </Link>
                    </div>
                ))}
            </div>

            {/* Pagination */}
            {totalPages > 1 && (
                <div className="pagination">
                    {Array.from({ length: totalPages }, (_, i) => (
                        <button
                            key={i}
                            onClick={() => setPage(i + 1)}
                            className={page === i + 1 ? "active" : ""}
                        >
                            {i + 1}
                        </button>
                    ))}
                </div>
            )}
        </div>
    );
}
