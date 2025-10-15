// src/App.jsx
import { Routes, Route } from "react-router-dom";
import Layout from "./components/Layout";
import Home from "./pages/Home";
import BooksPage from "./pages/BooksPage";
import BookDetails from "./pages/BookDetails";
import ProfileDetails from "./pages/ProfileDetails";
import ProfileParameters from "./pages/ProfileParameters";
import "./App.css";

export default function App() {
   return (
      <Routes>
         <Route element={<Layout />}>
            <Route path="/" element={<Home />} />
            <Route path="/books" element={<BooksPage />} />
            <Route path="/books/:id" element={<BookDetails />} />
            <Route path="/profile" element={<ProfileDetails />} />
            <Route path="/profile/edit" element={<ProfileParameters />} />
         </Route>
      </Routes>
   );
}
