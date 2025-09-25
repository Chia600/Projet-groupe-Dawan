import React, {useEffect, useState} from "react";
import API from "../api/axios";
import BookCard from "../components/BookCard";

export default function Home(){
  const [books, setBooks] = useState([]);
  const [q, setQ] = useState("");
  const [page, setPage] = useState(0);

  useEffect(()=>{
    API.get('/books', {params: {page, size:12}})
       .then(res => setBooks(res.data.content))
       .catch(console.error);
  }, [page]);

  const onSearch = (e) => {
    e.preventDefault();
    API.get('/books/search-external', {params: {q, page:0, size:12}})
       .then(res => {
         // Google Books returns items differently — map client-side or call import endpoints
         const items = res.data.items || [];
         setBooks(items.map(item => ({
            id: item.id,
            title: item.volumeInfo.title,
            authors: item.volumeInfo.authors?.join(", "),
            cover: item.volumeInfo.imageLinks?.thumbnail
         })));
       });
  };

  return (
    <div>
      <form onSubmit={onSearch}>
        <input value={q} onChange={e=>setQ(e.target.value)} placeholder="Rechercher un livre" />
        <button type="submit">Search</button>
      </form>

      <div className="grid">
        {books.map(b => <BookCard key={b.id || b.googleBookId} book={b} />)}
      </div>

      <div className="pagination">
        <button onClick={()=>setPage(p => Math.max(0,p-1))}>Prev</button>
        <button onClick={()=>setPage(p => p+1)}>Next</button>
      </div>
    </div>
  );
}
