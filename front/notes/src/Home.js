import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from './axiosInstance';

function Home() {
  const [notes, setNotes] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios.get('/getNotesHeaders')
      .then(response => {
        const headers = response.data;
        const sorted = headers.sort(
          (a, b) => new Date(b.lastModified) - new Date(a.lastModified)
        );
        setNotes(sorted);
      })
      .catch(err => console.error(err));
  }, []);

  const handleDelete = (id) => {
    if (window.confirm('Вы уверены, что хотите удалить заметку?')) {
      axios.delete(`/deleteNote?id=${id}`)
        .then(response => {
          if (response.status === 200) {
            setNotes(notes.filter(note => note.id !== id));
          }
        })
        .catch(err => console.error(err));
    }
  };

  return (
    <div>
      <h1>Заметки</h1>
      <button onClick={() => navigate('/create')}>Создать заметку</button>
      <ul>
        {notes.map(note => (
          <li key={note.id} style={{ margin: '8px 0' }}>
            <span
              style={{ cursor: 'pointer', textDecoration: 'underline', marginRight: '10px' }}
              onClick={() => navigate(`/note/${note.id}`)}
            >
              {note.title} – {new Date(note.lastModified).toLocaleString()}
            </span>
            <button onClick={() => handleDelete(note.id)}>×</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Home;