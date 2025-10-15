import { useNavigate } from "react-router-dom";
import "../assets/profiledetails.css";

export default function ProfileDetails() {

   const navigate = useNavigate();

   const userAvatar = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png";

   // Gestion du clic sur "Editer"
   const goToParameters = (e) => {
      e.preventDefault(); // empêche la navigation immédiate
      navigate("/profile/edit"); // redirige vers les paramètres du profil si connecté
   };

   return (
      <div>
         <h1>Profil</h1>
         <div className="profile-details-container">
            <div className="profile-details-data">
               <img src={userAvatar} alt="Image du profil" className="profile-details-img" />
               <div className="profile-details-info">
                  <label>Nom</label>
                  <label>Prenom</label>
                  <label>Nombre de critiques écrites</label>
                  <label>Nombre de livres dans ma collection</label>
               </div>
            </div>
            <div className="profile-details-edit-btn">
               <button type="button" onClick={goToParameters}>
                  Editer
               </button>
            </div>
         </div>
      </div>
   );
}
