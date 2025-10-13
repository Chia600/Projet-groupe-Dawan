import React, { useState } from "react";
import { Link } from "react-router-dom";
import LoginModal from "./LoginModal";
import RegisterModal from "./RegisterModal";
import "./Navbar.css";

export default function Navbar() {
    // États pour les deux modales
    const [showLoginModal, setShowLoginModal] = useState(false);
    const [showRegisterModal, setShowRegisterModal] = useState(false);

    // Fonctions pour LoginModal
    const openLoginModal = () => setShowLoginModal(true);
    const closeLoginModal = () => setShowLoginModal(false);

    // Fonctions pour RegisterModal
    const openRegisterModal = () => setShowRegisterModal(true);
    const closeRegisterModal = () => setShowRegisterModal(false);

    // Quand on clique sur "Nouveau ?"
    const goToRegister = () => {
        closeLoginModal(); // ferme la modale de connexion
        setTimeout(() => {
            openRegisterModal(); // ouvre la modale d'inscription après un léger délai
        }, 150);
    };

    return (
        <>
            <nav className="navbar">
                <ul className="navbar-links">
                    <li><Link to="/">Ma collection</Link></li>
                    <li><Link to="/books">Livres</Link></li>
                    <li>
                        <button className="login-link" onClick={openLoginModal}>
                            Se connecter
                        </button>
                    </li>
                </ul>
            </nav>

            {/* Pop-up Connexion */}
            {showLoginModal && (
                <LoginModal
                    onClose={closeLoginModal}
                    onRegister={goToRegister}
                />
            )}

            {/* Pop-up Inscription */}
            {showRegisterModal && (
                <RegisterModal
                    onClose={closeRegisterModal}
                    onRegister={() => {
                        alert("Compte créé !");
                        closeRegisterModal();
                    }}
                />
            )}
        </>
    );
}
