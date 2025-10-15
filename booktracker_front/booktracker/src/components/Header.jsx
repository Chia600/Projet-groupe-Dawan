import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Navbar from "./Navbar";
import SearchBar from "./SearchBar";
import "../assets/header.css";

export default function Header() {
    const location = useLocation();
    const navigate = useNavigate();
    const isHome = location.pathname === "/";

    // Redirection vers la home quand on clique sur le titre
    const handleGoHome = () => {
        navigate("/");
    };

    return (
        <header className="header">
            <div className="header-row">
                {/* Titre cliquable */}
                <h1 className="header-title" onClick={handleGoHome}>
                    📚 BookTracker
                </h1>

                {/* Barre visible sauf sur la page d'accueil */}
                {!isHome && (
                    <div className="header-search">
                        <SearchBar />
                    </div>
                )}

                <div className="header-nav">
                    <Navbar />
                </div>
            </div>
        </header>
    );
}
