import { Routes, Route } from "react-router-dom";
import Header from "./components/Header";
import Home from "./pages/Home";
import BooksPage from "./pages/BooksPage";
import BookDetails from "./pages/BooksDetails.jsx";
import "./App.css";


export default function App() {
    return (
        <>
            <Header />
            <main style={{ paddingTop: "5rem", textAlign: "center" }}>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/books" element={<BooksPage />} />
                    <Route path="/books/:id" element={<BookDetails />} />
                </Routes>
            </main>
        </>
    );
}
