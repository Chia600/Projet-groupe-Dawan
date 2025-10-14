import React, { useState, useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import LoginModal from "./LoginModal";
import RegisterModal from "./RegisterModal";
import "./navbar.css";

export default function Navbar() {
    // États pour les deux modales
    const [showLoginModal, setShowLoginModal] = useState(false);
    const [showRegisterModal, setShowRegisterModal] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("token")); // init depuis le stockage
    const [showMenu, setShowMenu] = useState(false); // ouverture/fermeture du menu profil
    const navigate = useNavigate();
    const menuRef = useRef(null); // référence pour détecter le clic extérieur

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

    // Gestion du clic sur "Ma collection"
    const handleCollectionClick = (e) => {
        e.preventDefault(); // empêche la navigation immédiate
        if (isLoggedIn) {
            navigate("/collection"); // redirige vers la collection si connecté
        } else {
            openLoginModal(); // sinon ouvre la popup de connexion
        }
    };

    // Déconnexion
    const handleLogout = () => {
        localStorage.removeItem("token"); // coupe le token
        setIsLoggedIn(false); // met à jour l’état local
        setShowMenu(false);
        navigate("/");
    };

    // Ferme le menu si clic en dehors
    useEffect(() => {
        const handleClickOutside = (e) => {
            if (menuRef.current && !menuRef.current.contains(e.target)) {
                setShowMenu(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    // Simulation avatar (plus tard : URL backend)
    const userAvatar = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png";
    const userName = "Maxence Wurtz"; // simulé pour l'affichage

    return (
        <>
            <nav className="navbar">
                <ul className="navbar-links">
                    <li>
                        <Link to="/" onClick={handleCollectionClick}>
                            Ma collection
                        </Link>
                    </li>
                    <li><Link to="/books">Livres</Link></li>
                    <li>
                        {isLoggedIn ? (
                            <div className="profile-container" ref={menuRef}>
                                <img
                                    src={userAvatar}
                                    alt="Profil"
                                    className="avatar"
                                    onClick={() => setShowMenu(!showMenu)}
                                />
                                {showMenu && (
                                    <div className="profile-dropdown">
                                        <div className="profile-header">
                                            <img src={userAvatar} alt="Profil" className="avatar-small" />
                                            <span className="profile-name">{userName}</span>
                                        </div>
                                        <div className="divider"></div>
                                        <button
                                            className="dropdown-item"
                                            onClick={() => {
                                                alert("Page profil à venir !");
                                                setShowMenu(false);
                                            }}
                                        >
                                            Profil
                                        </button>
                                        <button
                                            className="dropdown-item"
                                            onClick={() => {
                                                alert("Paramètres à venir !");
                                                setShowMenu(false);
                                            }}
                                        >
                                            Paramètres
                                        </button>
                                        <div className="divider"></div>
                                        <button
                                            className="logout-item"
                                            onClick={handleLogout}
                                        >
                                            🔴 Se déconnecter
                                        </button>
                                    </div>
                                )}
                            </div>
                        ) : (
                            <button className="login-link" onClick={openLoginModal}>
                                Se connecter
                            </button>
                        )}
                    </li>
                </ul>
            </nav>

            {/* Pop-up Connexion */}
            {showLoginModal && (
                <LoginModal
                    onClose={closeLoginModal}
                    onRegister={goToRegister}
                    onLoginSuccess={() => setIsLoggedIn(true)} // connexion simulée
                />
            )}

            {/* Pop-up Inscription */}
            {showRegisterModal && (
                <RegisterModal
                    onClose={closeRegisterModal}
                    onRegister={() => {
                        alert("Compte créé !");
                        closeRegisterModal();
                        setIsLoggedIn(true); // simule connexion après inscription
                        localStorage.setItem("token", "fake-token"); // simule stockage token
                    }}
                />
            )}
        </>
    );
}
