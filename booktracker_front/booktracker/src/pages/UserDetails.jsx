import { useNavigate } from "react-router-dom";
import React, { useEffect, useState } from "react";
import axios from "axios";
import "../assets/userDetails.css";

export default function UserDetails() {

   const [user, setUser] = useState(null);
   const [error, setError] = useState(null);
   const navigate = useNavigate();

   const userId = localStorage.getItem("userId");

   useEffect(() => {
      const fetchProfileDetails = async () => {
         try {
            const res = await axios.get(`http://localhost:8080/api/users/${userId}`);

            setUser(res.data);
         } catch (err) {
            console.error("Erreur de chargement de l'utilisateur :", err);
            setError("Impossible de charger les détails de l'utilisateur.");
         }

      };
      fetchProfileDetails();
   }, [userId]);

   if (error) return <p>{error}</p>;
   if (!user) return <p>Chargement...</p>;

   // Gestion du clic sur "Editer"
   const goToParameters = (e) => {
      e.preventDefault(); // empêche la navigation immédiate
      navigate("/profile/edit"); // redirige vers les paramètres du profil si connecté
   };

   return (
      <div className="user-page">
         <h1>Mon profil</h1>

         <div className="user-detail-page">
            <div className="user-detail-container">
               {/* --- Photo profil à gauche --- */}
               <div className="user-img-section">
                  <img
                     src={user.picture || "https://via.placeholder.com/200x300"}
                     alt="avatar"
                     className="user-detail-img"
                  />
               </div>

               {/* --- Infos user à droite --- */}
               <div className="user-info-section">
                  <h2 className="user-name">{user.firstname} {user.lastname}</h2>

                  {/* --- Détails secondaires --- */}
                  <div className="user-extra">
                     <p><strong>Inscrit depuis le :</strong> {user.subscriptionDate}</p>
                     <p><strong>Mon email :</strong> {user.email}</p>
                     <p><strong>Nombre de livres dans ma collection :</strong> {user.bookIds.length}</p>
                     <p><strong>Nombre de critiques écrites :</strong> {user.reviewIds.length}</p>
                  </div>
               </div>
               <div className="profile-details-edit-btn">
                  <button type="button" onClick={goToParameters}>
                     Modifier mon profil
                  </button>
               </div>
            </div>
         </div>
      </div>
   );
}
