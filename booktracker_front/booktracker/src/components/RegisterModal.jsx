import React from "react";
import "./registerModal.css";

export default function RegisterModal({ onClose, onRegister }) {
    return (
        <div className="modal-overlay">
            <div className="modal-content register-modal">
                {/* Titre */}
                <h2 className="modal-title">Créer un compte</h2>
                <hr className="divider" />

                {/* Formulaire : 2 colonnes */}
                <form className="register-form" onSubmit={(e) => e.preventDefault()}>
                    <div className="form-row">
                        <div className="form-group">
                            <label>Prénom</label>
                            <input type="text" placeholder="Votre prénom" required />
                        </div>
                        <div className="form-group">
                            <label>Nom</label>
                            <input type="text" placeholder="Votre nom" required />
                        </div>
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label>Email</label>
                            <input type="email" placeholder="Votre email" required />
                        </div>
                        <div className="form-group">
                            <label>Pseudo</label>
                            <input type="text" placeholder="Votre pseudo" required />
                        </div>
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label>Mot de passe</label>
                            <input type="password" placeholder="Mot de passe" required />
                        </div>
                        <div className="form-group">
                            <label>Confirmer le mot de passe</label>
                            <input type="password" placeholder="Confirmation" required />
                        </div>
                    </div>
                </form>

                <hr className="divider" />

                {/* Boutons */}
                <div className="modal-actions">
                    <button type="button" className="cancel-btn" onClick={onClose}>
                        Annuler
                    </button>
                    <button type="submit" className="register-btn" onClick={onRegister}>
                        Créer
                    </button>
                </div>
            </div>
        </div>
    );
}
