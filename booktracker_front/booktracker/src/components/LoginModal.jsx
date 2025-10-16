import React, { useState } from "react";
import API from "../api/axios";
import "../assets/loginModal.css";

export default function LoginModal({ onClose, onRegister, onLoginSuccess }) {
   const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(false);
    const [loading, setLoading] = useState(false);

   const handleLogin = async (e) => {
      e.preventDefault();
      setError(false);

      // Validation rapide
      if (!username.trim() || !password.trim()) {
         setError(true);
         return;
      }

      try {
         setLoading(true);

         const response = await API.post("/auth/login", {
            username,
            password,
         });

         try {
            setLoading(true);

            const response = await API.post("/auth/login", {
                username,
                password,
            });

            if (response.status === 200 && response.data.token) {
                localStorage.setItem("token", response.data.token);
                let accessToken = localStorage.getItem("token");
            // Décoder le JWT pour récupérer l'username
            const base64Url = accessToken.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(
               atob(base64)
                  .split('')
                  .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                  .join('')
            );

            const decoded = JSON.parse(jsonPayload);
            localStorage.setItem("userId", decoded.user_id);
            onLoginSuccess?.(username); // notifie la Navbar
                onClose(); // ferme la modale
            } else {
                setError(true);
            }
        } catch (err) {
            console.error("Erreur de connexion :", err);
            setError(true);
        } finally {
            setLoading(false);
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
                  Nom d'utilisateur :
                  <input
                     type="text"
                     placeholder="Votre identifiant"
                     value={username}
                     onChange={(e) => setUsername(e.target.value)}
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
                            minLength={6}
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

            <hr className="divider" />

                {/* Boutons bas */}
                <div className="modal-actions">
                    <button
                        type="button"
                        className="cancel-btn"
                        onClick={onClose}
                        disabled={loading}
                    >
                        Annuler
                    </button>
                    <button
                        type="submit"
                        className="login-btn"
                        onClick={handleLogin}
                  disabled={loading}
                    >
                        {loading ? "Connexion..." : "Se connecter"}
               </button>
            </div>
         </div>
      </div>
   );
}