import React, {useState} from "react";
import { useNavigate } from "react-router-dom";
import API from "../api/axios";

export default function Register(){
    // state for form inputs
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();
    // handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await API.post('/register', { username, email, password });
            navigate('/login');
        } catch (error) {
            console.error('There was an error registering!', error);
        }
    };

    return (
        // html with bootstrap form for registration page
        <div className="container"></div>
    );
    }