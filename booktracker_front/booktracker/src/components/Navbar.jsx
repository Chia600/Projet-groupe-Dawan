import React, { useState, useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import LoginModal from "./LoginModal";
import RegisterModal from "./RegisterModal";
import "../assets/navbar.css";

export default function Navbar() {
    const [showLoginModal, setShowLoginModal] = useState(false);
    const [showRegisterModal, setShowRegisterModal] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("token"));
    const [userName, setUserName] = useState(localStorage.getItem("username") || "Utilisateur");
    const [showMenu, setShowMenu] = useState(false);
    const navigate = useNavigate();
    const menuRef = useRef(null);

    const openLoginModal = () => setShowLoginModal(true);
    const closeLoginModal = () => setShowLoginModal(false);
    const openRegisterModal = () => setShowRegisterModal(true);
    const closeRegisterModal = () => setShowRegisterModal(false);

    const goToRegister = () => {
        closeLoginModal();
        setTimeout(() => openRegisterModal(), 150);
    };

    const handleCollectionClick = (e) => {
        e.preventDefault();
        if (isLoggedIn) navigate("/collection");
        else openLoginModal();
    };

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("username");
        setIsLoggedIn(false);
        setUserName("Utilisateur");
        setShowMenu(false);
        navigate("/");
    };

    // Ferme le menu si clic à l’extérieur
    useEffect(() => {
        const handleClickOutside = (e) => {
            if (menuRef.current && !menuRef.current.contains(e.target)) {
                setShowMenu(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    // Avatar par défaut
    const userAvatar = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png";

    return (
        <>
            <nav className="navbar">
                <ul className="navbar-links">
                    <li>
                        <Link to="/" onClick={handleCollectionClick}>
                            Ma collection
                        </Link>
                    </li>
                    <li>
                        <Link to="/books">Livres</Link>
                    </li>
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
                                            onClick={() => alert("Page profil à venir !")}
                                        >
                                            Profil
                                        </button>
                                        <button
                                            className="dropdown-item"
                                            onClick={() => alert("Paramètres à venir !")}
                                        >
                                            Paramètres
                                        </button>
                                        <div className="divider"></div>
                                        <button className="logout-item" onClick={handleLogout}>
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

            {/* === Pop-up Connexion === */}
            {showLoginModal && (
                <LoginModal
                    onClose={closeLoginModal}
                    onRegister={goToRegister}
                    onLoginSuccess={(username) => {
                        // sauvegarde du pseudo saisi
                        localStorage.setItem("username", username);
                        setUserName(username);
                        setIsLoggedIn(true);
                        closeLoginModal();
                        alert(`Bienvenue ${username} !`);
                    }}
                />
            )}

            {/* === Pop-up Inscription === */}
            {showRegisterModal && (
                <RegisterModal
                    onClose={closeRegisterModal}
                    onRegister={() => {
                        closeRegisterModal();
                        alert("Compte créé avec succès ! Vous pouvez maintenant vous connecter.");
                        setTimeout(() => openLoginModal(), 300);
                    }}
                />
            )}
        </>
    );
}
