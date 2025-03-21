import React, { useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';

function CreateNote() {
  const navigate = useNavigate();
  const hasCreated = useRef(false);

  useEffect(() => {
    if (hasCreated.current) return;
    hasCreated.current = true;

    const lastModified = new Date().toISOString();
    fetch(`http://localhost:8080/createNote?lastModified=${lastModified}`, { method: 'POST' })
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