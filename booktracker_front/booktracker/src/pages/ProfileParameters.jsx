import React, { useState } from "react";
import { Link } from "react-router-dom";
import ProfileInput from '../components/ProfileInput';
import "../assets/profileParameters.css";

export default function ProfileParameters() {

   return (
      <div className="edit-page">
         <h1>Paramètres du compte</h1>
         <div>
            <ProfileInput />
         </div>

      </div>
   );
}
