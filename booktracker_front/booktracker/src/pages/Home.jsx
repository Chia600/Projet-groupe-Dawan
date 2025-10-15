import React from "react";
import SearchBar from "../components/SearchBar";
import "../assets/home.css";

export default function Home() {
    return (
        <div className="home-page">
            <div className="home-center">
                <h1>Bienvenue sur BookTracker</h1>
                <p>Recherchez vos livres favoris 📚</p>

                {/* Barre de recherche test */}
                <SearchBar />
            </div>
        </div>
    );
}
