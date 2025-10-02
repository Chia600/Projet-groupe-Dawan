import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../api/axios";

export default function Login(){
    // state for form inputs
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();
    // handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await API.post('/login', { username, password });
            navigate('/');
        } catch (error) {
            console.error('There was an error logging in!', error);
        }
    };

    return (
        // html with bootstrap form for login page
    );
}
