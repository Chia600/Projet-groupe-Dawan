import React from "react";
import Navbar from "./Navbar";
import "./Header.css";

export default function Header() {
    return (
        <header className="header">
            <div className="header-top">
                <h1>📚 BookTracker</h1>
            </div>
            <Navbar />
        </header>
    );
}
