import React from "react";
import "./LoginModal.css";

export default function LoginModal({ onClose, onRegister }) {
    return (
        <div className="modal-overlay">
            <div className="modal-content">
                {/* Titre */}
                <h2 className="modal-title">Se connecter</h2>
                <hr className="divider" />

                {/* Formulaire */}
                <form className="login-form" onSubmit={(e) => e.preventDefault()}>
                    <label>
                        Email :
                        <input type="email" placeholder="Votre email" required />
                    </label>

                    <label>
                        Mot de passe :
                        <input type="password" placeholder="Votre mot de passe" required />
                    </label>

                    {/* Lien "Nouveau ?" aligné à droite sous le mot de passe */}
                    <div className="register-container">
                        <button
                            type="button"
                            className="register-link"
                            onClick={onRegister}
                        >
                            Nouveau ?
                        </button>
                    </div>
                </form>

                {/* Ligne de séparation */}
                <hr className="divider" />

                {/* Boutons bas */}
                <div className="modal-actions">
                    <button type="button" className="cancel-btn" onClick={onClose}>
                        Annuler
                    </button>
                    <button type="submit" className="login-btn">
                        Se connecter
                    </button>
                </div>
            </div>
        </div>
    );
}
