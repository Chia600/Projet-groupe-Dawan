import React, { useState } from "react";
import "../assets/suppressionModal.css";

export default function SuppressionModal({ onClose, onSuppress }) {
   // États internes
   const [password, setPassword] = useState("");
   const [error, setError] = useState(false);

   return (
      <div className="modal-overlay">
         <div className={`modal-content ${error ? "shake" : ""}`}>
            {/* Titre */}
            <h2 className="modal-title">Suppression du compte</h2>

            <hr className="divider" />
            <label>
               En désactivant votre compte, vous ne pourrez plus vous connecter,
               votre activité sera supprimée de BookTracker et votre nom d'utilisateur
               pourrait être récupéré par un autre membre.
            </label>
            <hr className="divider" />

            {/* Formulaire */}
            <form className="login-form" onSubmit={(e) => e.preventDefault()}>
               <label>
                  Mot de passe
                  <input
                     type="password"
                     placeholder="Votre mot de passe"
                     value={password}
                     onChange={(e) => setPassword(e.target.value)}
                     className={error ? "error" : ""}
                     required
                  />
               </label>
               <div className="modal-actions">
                  <button type="submit" className="supp-btn" onClick={onSuppress}>
                     Supprimer
                  </button>
                  <button type="button" className="cancel-btn" onClick={onClose}>
                     Annuler
                  </button>
               </div>
            </form>

            {/* Message d’erreur */}
            {error && (
               <p className="error-message">Mot de passe incorrect</p>
            )}
         </div>
      </div>
   );
}
