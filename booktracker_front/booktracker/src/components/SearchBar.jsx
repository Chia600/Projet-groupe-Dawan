import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../assets/searchBar.css";

export default function SearchBar() {
    const [query, setQuery] = useState("");
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();

        const trimmed = query.trim();

        if (trimmed === "") {
            // Si la recherche est vide → retourne au catalogue complet
            navigate("/books");
        } else {
            // Sinon → redirige vers la recherche
            navigate(`/books?search=${encodeURIComponent(trimmed)}`);
        }

        // (Optionnel) vide le champ après envoi :
        setQuery("");
    };

    return (
        <form className="search-bar" onSubmit={handleSubmit}>
            <input
                type="text"
                placeholder="Rechercher un livre..."
                value={query}
                onChange={(e) => setQuery(e.target.value)}
            />
            <button type="submit">🔍</button>
        </form>
    );
}
