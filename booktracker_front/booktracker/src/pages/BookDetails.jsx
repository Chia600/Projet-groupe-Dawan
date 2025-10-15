import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import API from "../api/axios";

export default function BookDetails() {
   const { id } = useParams();
   const [book, setBook] = useState(null);

   useEffect(() => {
      API.get(`/books/${id}`).then((res) => setBook(res.data));
   }, [id]);

   if (!book) return <p>Chargement...</p>;

   return (
      <div style={{ padding: "2rem", textAlign: "center" }}>
         <img
            src={book.cover || "https://via.placeholder.com/200x300"}
            alt={book.title}
            style={{ width: 200, borderRadius: 8 }}
         />
         <h2>{book.title}</h2>
         <p>{book.description}</p>
         <small>Publié le : {book.publishedDate}</small>
      </div>
   );
}
