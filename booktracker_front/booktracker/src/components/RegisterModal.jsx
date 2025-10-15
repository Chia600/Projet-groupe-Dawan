import React, { useState } from "react";
import API from "../api/axios";
import "../assets/registerModal.css";

export default function RegisterModal({ onClose, onRegister }) {
    const [formData, setFormData] = useState({
        firstname: "",
        lastname: "",
        username: "",
        email: "",
        password: "",
        confirmPassword: "",
    });

    const [error, setError] = useState("");
    const [fieldErrors, setFieldErrors] = useState({});
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
        setFieldErrors({ ...fieldErrors, [e.target.name]: "" });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        const newErrors = {};

        // === Vérifications locales ===
        if (!formData.firstname.trim()) newErrors.firstname = "Prénom obligatoire.";
        if (!formData.lastname.trim()) newErrors.lastname = "Nom obligatoire.";
        if (!formData.username.trim()) newErrors.username = "Pseudo obligatoire.";
        if (!formData.email.match(/^[^@\s]+@[^@\s]+\.[^@\s]+$/))
            newErrors.email = "Format d'adresse e-mail invalide.";
        if (formData.password.length < 6)
            newErrors.password = "Minimum 6 caractères.";
        if (formData.password !== formData.confirmPassword)
            newErrors.confirmPassword = "Les mots de passe ne correspondent pas.";

        setFieldErrors(newErrors);
        if (Object.keys(newErrors).length > 0) return; // stop si erreurs locales

        try {
            setLoading(true);
            const response = await API.post("/auth/register", {
                firstname: formData.firstname,
                lastname: formData.lastname,
                username: formData.username,
                email: formData.email,
                password: formData.password,
            });

            if (response.status === 200) onRegister();
        } catch (err) {
            if (err.response?.status === 409)
                setError("Un compte existe déjà avec cet email ou pseudo.");
            else setError("Une erreur est survenue. Veuillez réessayer.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content register-modal">
                <h2 className="modal-title">Créer un compte</h2>
                <hr className="divider" />

                <form className="register-form" onSubmit={handleSubmit}>
                    <div className="form-row">
                        <div className="form-group">
                            <label>Prénom</label>
                            <input
                                name="firstname"
                                value={formData.firstname}
                                onChange={handleChange}
                                className={fieldErrors.firstname ? "error" : ""}
                                required
                            />
                            {fieldErrors.firstname && (
                                <p className="error-text">{fieldErrors.firstname}</p>
                            )}
                        </div>

                        <div className="form-group">
                            <label>Nom</label>
                            <input
                                name="lastname"
                                value={formData.lastname}
                                onChange={handleChange}
                                className={fieldErrors.lastname ? "error" : ""}
                                required
                            />
                            {fieldErrors.lastname && (
                                <p className="error-text">{fieldErrors.lastname}</p>
                            )}
                        </div>
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label>Email</label>
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                className={fieldErrors.email ? "error" : ""}
                                required
                            />
                            {fieldErrors.email && (
                                <p className="error-text">{fieldErrors.email}</p>
                            )}
                        </div>

                        <div className="form-group">
                            <label>Pseudo</label>
                            <input
                                name="username"
                                value={formData.username}
                                onChange={handleChange}
                                className={fieldErrors.username ? "error" : ""}
                                required
                            />
                            {fieldErrors.username && (
                                <p className="error-text">{fieldErrors.username}</p>
                            )}
                        </div>
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label>Mot de passe</label>
                            <input
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                minLength={6}
                                required
                                className={fieldErrors.password ? "error" : ""}
                            />
                            {fieldErrors.password && (
                                <p className="error-text">{fieldErrors.password}</p>
                            )}
                        </div>

                        <div className="form-group">
                            <label>Confirmer le mot de passe</label>
                            <input
                                type="password"
                                name="confirmPassword"
                                value={formData.confirmPassword}
                                onChange={handleChange}
                                required
                                className={fieldErrors.confirmPassword ? "error" : ""}
                            />
                            {fieldErrors.confirmPassword && (
                                <p className="error-text">{fieldErrors.confirmPassword}</p>
                            )}
                        </div>
                    </div>

                    {error && (
                        <p className="error-text" style={{ textAlign: "center" }}>
                            {error}
                        </p>
                    )}

                    <hr className="divider" />

                    <div className="modal-actions">
                        <button
                            type="button"
                            className="cancel-btn"
                            onClick={onClose}
                            disabled={loading}
                        >
                            Annuler
                        </button>
                        <button type="submit" className="register-btn" disabled={loading}>
                            {loading ? "Création..." : "Créer"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}
