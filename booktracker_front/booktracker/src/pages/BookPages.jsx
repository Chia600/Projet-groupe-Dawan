import React, { useEffect, useState } from "react";
import API from "../api/axios";
import BookCard from "../components/BookCard";

export default function BookPages() {
  const [books, setBooks] = useState([]);
  const [q, setQ] = useState("");

  const fetchBooks = async () => {
    try {
      const res = await API.get("/books", { params: { q } });
      setBooks(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => { fetchBooks(); }, [q]);

  return (
    <div>
      <h1>Catalogue</h1>
      <input placeholder="Rechercher..." value={q} onChange={e => setQ(e.target.value)} />
      <div className="book-grid">
        {books.map(b => <BookCard key={b.id} book={b} />)}
      </div>
    </div>
  );
}
