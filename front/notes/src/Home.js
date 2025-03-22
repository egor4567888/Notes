import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from './axiosInstance';

function Home() {
  const [notes, setNotes] = useState([]);
  const navigate = useNavigate();
  const username = localStorage.getItem('username') || "Пользователь";

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

  const handleDelete = (e, id) => {
    e.stopPropagation();
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

  const handleLogout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('username');
    navigate('/login');
  };

  const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    const now = new Date();
    const dateDay = date.getDate();
    const dateMonth = date.getMonth();
    const dateYear = date.getFullYear();
    const nowDay = now.getDate();
    const nowMonth = now.getMonth();
    const nowYear = now.getFullYear();

    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    if (dateYear === nowYear && dateMonth === nowMonth) {
      if (dateDay === nowDay) {
        return `сегодня в ${hours}:${minutes}`;
      } else if (dateDay === nowDay - 1) {
        return `вчера в ${hours}:${minutes}`;
      }
    }
    const dd = String(dateDay).padStart(2, '0');
    const mm = String(dateMonth + 1).padStart(2, '0');
    const yy = String(dateYear).slice(2);
    return `${dd}.${mm}.${yy} в ${hours}:${minutes}`;
  };

  return (
    <div className="container">
      <header>
        <div>
          <h1>Заметки</h1>
          <span style={{ marginLeft: '10px', fontSize: '1rem', color: '#555' }}>
            {username}
          </span>
        </div>
        <button onClick={handleLogout}>Выйти</button>
      </header>
      <button onClick={() => navigate('/create')} style={{ marginTop: '20px' }}>
        Создать заметку
      </button>
      <ul style={{ marginTop: '20px' }}>
        {notes.map(note => (
          <li 
            key={note.id} 
            onClick={() => navigate(`/note/${note.id}`)}
            style={{ margin: '8px 0', cursor: 'pointer' }}
          >
            <span>
              {(note.title && note.title.trim() !== '') ? note.title : 'Новая заметка'} – {formatDate(note.lastModified)}
            </span>
            <button 
              onClick={(e) => handleDelete(e, note.id)}
              style={{ marginLeft: '10px' }}
            >
              ×
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Home;