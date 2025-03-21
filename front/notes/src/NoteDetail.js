import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

function NoteDetail() {
  const { id } = useParams();
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [lastModified, setLastModified] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetch(`http://localhost:8080/getNote?id=${id}`)
      .then(res => res.json())
      .then(data => {
        setTitle(data.title);
        setContent(data.content);
        setLastModified(data.lastModified);
      })
      .catch(err => console.error(err));
  }, [id]);

  const handleSave = () => {
    const noteDto = {
      id,
      title,
      content,
      lastModified: new Date().toISOString()
    };

    fetch('http://localhost:8080/saveNote', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(noteDto)
    })
      .then(response => {
        if (response.ok) {
          navigate('/');
        }
      })
      .catch(err => console.error(err));
  };

  if (!lastModified) return <div>Загрузка...</div>;

  return (
    <div>
      <h2>Редактирование заметки</h2>
      <div>
        <label>Дата изменения: </label>
        <span>{new Date(lastModified).toLocaleString()}</span>
      </div>
      <div>
        <input
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
      </div>
      <div>
        <textarea
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />
      </div>
      <button onClick={handleSave}>Сохранить</button>
      <button onClick={() => navigate(-1)}>Назад</button>
    </div>
  );
}

export default NoteDetail;