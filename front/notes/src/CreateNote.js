import React, { useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from './axiosInstance';

function CreateNote() {
  const navigate = useNavigate();
  const hasCreated = useRef(false);

  useEffect(() => {
    if (hasCreated.current) return;
    hasCreated.current = true;

    const noteDto = {
      title: '',
      content: '',
      lastModified: new Date().toISOString()
    };

    axios.post('/createNote', noteDto)
      .then(response => {
        const data = response.data;
        if (data.id) {
          navigate(`/note/${data.id}`, { replace: true });
        }
      })
      .catch(err => console.error(err));
  }, [navigate]);

  return <div>Создание новой заметки...</div>;
}

export default CreateNote;