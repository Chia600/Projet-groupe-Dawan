import React, { useState } from "react";
import { useSearchParams } from "react-router-dom"; // 👈 pour lire la recherche dans l’URL
import "./BooksPage.css";
import BookCard from "../components/BookCard";

export default function BooksPage() {
    const [page, setPage] = useState(1);
    const booksPerPage = 12;

    // récupère le paramètre de recherche dans l’URL
    const [params] = useSearchParams();
    const q = params.get("search")?.toLowerCase() || "";

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

    // Filtrage selon la recherche (via URL)
    const filteredBooks = mockBooks.filter((b) =>
        b.title.toLowerCase().includes(q)
    );

    const totalPages = Math.ceil(filteredBooks.length / booksPerPage);
    const startIndex = (page - 1) * booksPerPage;
    const paginatedBooks = filteredBooks.slice(startIndex, startIndex + booksPerPage);

    return (
        <div className="books-page">
            <h1>{q ? `Résultats pour : "${q}"` : "Catalogue des livres"}</h1>

            <div className="book-grid">
                {paginatedBooks.length > 0 ? (
                    paginatedBooks.map((book) => <BookCard key={book.id} book={book} />)
                ) : (
                    <p>Aucun livre trouvé :( </p>
                )}
            </div>

            {/* Pagination */}
            {totalPages > 1 && (
                <div className="pagination">
                    {page > 1 && (
                        <button onClick={() => setPage(page - 1)} className="nav-button">
                            «
                        </button>
                    )}

                    {Array.from({ length: totalPages }, (_, i) => (
                        <button
                            key={i}
                            onClick={() => setPage(i + 1)}
                            className={page === i + 1 ? "active" : ""}
                        >
                            {i + 1}
                        </button>
                    ))}

                    {page < totalPages && (
                        <button onClick={() => setPage(page + 1)} className="nav-button">
                            »
                        </button>
                    )}
                </div>
            )}
        </div>
    );
}
