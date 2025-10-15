import React, { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import axios from "axios";
import "../assets/booksPage.css";
import BookCard from "../components/BookCard";

export default function BooksPage() {
   const [books, setBooks] = useState([]);          // liste de livres récupérés
   const [page, setPage] = useState(1);             // page courante
   let booksPerPage = 12;                         // taille des pages
   const [totalPages, setTotalPages] = useState(1); // total renvoyé par backend
   const [loading, setLoading] = useState(false);   // affichage "chargement"
   const [error, setError] = useState(null);        // gestion d’erreurs

   const [params] = useSearchParams();
   const q = params.get("search")?.trim() || "";    // critère de recherche

   useEffect(() => {
      const fetchBooks = async () => {
         setLoading(true);
         setError(null);
         try {
            const backendPage = page - 1;

            if (backendPage === 4) {
               booksPerPage = 4;
            }
            // Appel backend Spring Boot
            const baseUrl = "http://localhost:8080/api/books";
            const url = q
               ? `${baseUrl}/${backendPage}/${booksPerPage}/${encodeURIComponent(q)}`
               : `${baseUrl}/${backendPage}/${booksPerPage}`;

            const res = await axios.get(url);

            setBooks(res.data.content || []);
            setTotalPages(4);

         } catch (err) {
            console.error("Erreur de chargement des livres :", err);
            setError("Impossible de charger les livres pour le moment.");
         } finally {
            setLoading(false);
         }
      };

      fetchBooks();
   }, [page, q]);

   // affichage du contenu
   return (
      <div className="books-page">
         <h1>{q ? `Résultats pour : "${q}"` : "Catalogue des livres"}</h1>

         {loading ? (
            <p>Chargement...</p>
         ) : error ? (
            <p className="error">{error}</p>
         ) : (
            <>
               <div className="book-grid">
                  {books.length > 0 ? (
                     books.map((book) => (
                        <BookCard key={book.id || book.bookId || book.googleId} book={book} />
                     ))
                  ) : (
                     <p>Aucun livre trouvé :(</p>
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
            </>
         )}
      </div>
   );
}
