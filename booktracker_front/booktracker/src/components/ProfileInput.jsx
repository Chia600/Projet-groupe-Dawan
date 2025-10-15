import React, { useState, useRef } from 'react'
import { useNavigate } from "react-router-dom";
import "../assets/profileInput.css";
import SuppressionModal from './SuppressionModal';

const ProfileInput = () => {
   // États internes
   const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("token")); // init depuis le stockage
   const [email, setEmail] = useState("");
   const [pseudo, setPseudo] = useState("");
   const [avatar, setAvatar] = useState(null);
   const [error, setError] = useState(false);
   const [showSuppressionModal, setShowSuppressionModal] = useState(false);

   const navigate = useNavigate();
   const fileInput = useRef(null);

   const openSuppressionModal = () => setShowSuppressionModal(true);
   const closeSuppressionModal = () => setShowSuppressionModal(false);

   const goToSuppress = () => {
      localStorage.removeItem("token"); // coupe le token
      setIsLoggedIn(false); // met à jour l’état local  
      closeSuppressionModal();
      navigate("/");
   };


   // Sélection avatar
   const handleImageChange = (event) => {
      const file = event.target.files[0];
      if (file) {
         const imageUrl = URL.createObjectURL(file);
         setAvatar(imageUrl);
      }
   };

   // Simulation de connexion
   const handleSave = (e) => {
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
      <>
         <div className="profile-param-container">
            <form className="profile-param-form" onSubmit={handleSave}>
               <div className="profile-param-data">
                  <label>
                     ✏️Pseudo
                     <input
                        type="text"
                        value={pseudo}
                        onChange={(e) => setPseudo(e.target.value)}
                        className={error ? "error" : ""}
                     />
                  </label>

                  <label>
                     ✏️Adresse mail
                     <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className={error ? "error" : ""}
                     />
                  </label>
                  <button type="submit">
                     Sauvegarder les modifications
                  </button>
               </div>
               <div className="profile-param-avatar">
                  <div className="profil-param-card">
                     <img src={avatar} />
                  </div>
                  <input type="file" ref={fileInput} onChange={handleImageChange} style={{ display: 'none' }} />
                  <button className="upload-btn" onClick={() => fileInput.current.click()}>
                     Modifier votre image de profil
                  </button>
                  <button type="button" className="supp-btn" onClick={openSuppressionModal}>
                     Supprimer le compte
                  </button>
               </div>
            </form>
         </div>

         {/* Pop-up de suppression */}
         {showSuppressionModal && (
            <SuppressionModal
               onClose={closeSuppressionModal}
               onSuppress={goToSuppress}
            />
         )}
      </>
   );
};

export default ProfileInput;
