import React, { useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';

function CreateNote() {
  const navigate = useNavigate();
  const hasCreated = useRef(false);

  useEffect(() => {
    if (hasCreated.current) return;
    hasCreated.current = true;

    fetch('http://localhost:8080/createNote', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        title: '',
        content: '',
        lastModified: new Date().toISOString()
      })
    })
      .then(response => response.json())
      .then(data => {
        if (data.id) {
          navigate(`/note/${data.id}`, { replace: true });
        }
      })
      .catch(err => console.error(err));
  }, [navigate]);

  return <div>Создание новой заметки...</div>;
}

export default CreateNote;