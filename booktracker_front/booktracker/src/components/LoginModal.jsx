import React, { useState } from "react";
import "./LoginModal.css";

export default function LoginModal({ onClose, onRegister, onLoginSuccess }) {
    // États internes
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(false);

    // Simulation de connexion
    const handleLogin = (e) => {
        e.preventDefault();

        if (email.toLowerCase().includes("test")) {
            localStorage.setItem("token", "fake-token");
            onLoginSuccess?.();
            onClose();
        } else {
            setError(true);
            setTimeout(() => setError(false), 600);
        }
    };

    return (
        <div className="modal-overlay">
            <div className={`modal-content ${error ? "shake" : ""}`}>
                {/* Titre */}
                <h2 className="modal-title">Se connecter</h2>
                <hr className="divider" />

                {/* Formulaire */}
                <form className="login-form" onSubmit={handleLogin}>
                    <label>
                        Email :
                        <input
                            type="email"
                            placeholder="Votre email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className={error ? "error" : ""}
                            required
                        />
                    </label>

                    <label>
                        Mot de passe :
                        <input
                            type="password"
                            placeholder="Votre mot de passe"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className={error ? "error" : ""}
                            required
                        />
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

                {/* Message d’erreur */}
                {error && (
                    <p className="error-message">Identifiants incorrects</p>
                )}

                {/* Ligne de séparation */}
                <hr className="divider" />

                {/* Boutons bas */}
                <div className="modal-actions">
                    <button type="button" className="cancel-btn" onClick={onClose}>
                        Annuler
                    </button>
                    <button type="submit" className="login-btn" onClick={handleLogin}>
                        Se connecter
                    </button>
                </div>
            </div>
        </div>
    );
}
