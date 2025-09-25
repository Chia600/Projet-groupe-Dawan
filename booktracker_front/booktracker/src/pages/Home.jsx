import React, { useEffect, useState } from 'react';
import axios from 'axios';
import bookCard from '../components/BookCard';

export default function Home() {
    // state for books
    const [books, setBooks] = useState([]);

    // fetch books from backend
    useEffect(() => {
        axios.get('http://localhost:8000/api/books')
            .then(response => {
                setBooks(response.data);
            })
            .catch(error => {
                console.error('There was an error fetching the books!', error);
            });
    }, []);

    return (
        <div>
            <h1>Home Page</h1>
            <div className="book-list">
                {books.map(book => (
                    <bookCard key={book.id} book={book} />
                ))}
            </div>
        </div>
    );
}