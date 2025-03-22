import React, { useEffect, useState, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import QuillMarkdown from 'quill-markdown-shortcuts';
import axios from './axiosInstance';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

function NoteDetail() {
  const { id } = useParams();
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [lastModified, setLastModified] = useState(null);
  const navigate = useNavigate();
  const autoSaveTimer = useRef(null);

  useEffect(() => {
    ReactQuill.Quill.register('modules/markdown', QuillMarkdown);
  }, []);

  const modules = {
    toolbar: [
      ['bold', 'italic', 'underline', 'strike'],
      ['blockquote', 'code-block'],
      [{ header: 1 }, { header: 2 }],
      [{ list: 'ordered' }, { list: 'bullet' }],
      [{ script: 'sub' }, { script: 'super' }],
      [{ indent: '-1' }, { indent: '+1' }],
      [{ direction: 'rtl' }],
      [{ size: ['small', false, 'large', 'huge'] }],
      [{ header: [1, 2, 3, 4, 5, 6, false] }],
      [{ color: [] }, { background: [] }],
      [{ font: [] }],
      [{ align: [] }],
      ['clean']
    ],
    markdown: {}
  };

  // WebSocket connection через STOMP
  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      debug: function(str) {
          console.log(str);
      },
      reconnectDelay: 5000,
      onConnect: () => {
        client.subscribe('/topic/note', message => {
          const updatedNote = JSON.parse(message.body);
          if (updatedNote.id === Number(id)) {
            setTitle(updatedNote.title);
            setContent(updatedNote.content);
            setLastModified(updatedNote.lastModified);
          }
        });
      }
    });
    client.activate();
    return () => client.deactivate();
  }, [id]);

  useEffect(() => {
    axios.get(`/getNote?id=${id}`)
      .then(response => {
        const data = response.data;
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

    axios.post('/saveNote', noteDto)
      .then(response => {
        if (response.status === 200) {
          // Обновление времени последнего изменения на клиенте
          setLastModified(new Date().toISOString());
        }
      })
      .catch(err => console.error(err));
  };

  // Автосохранение с задержкой 0.01 секунда после внесения изменений
  useEffect(() => {
    if (autoSaveTimer.current) clearTimeout(autoSaveTimer.current);
    autoSaveTimer.current = setTimeout(() => {
      handleSave();
    }, 10);

    return () => clearTimeout(autoSaveTimer.current);
  }, [title, content]);

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
          placeholder="Заголовок"
        />
      </div>
      <div style={{ marginTop: 10 }}>
        <ReactQuill
          value={content}
          onChange={setContent}
          modules={modules}
          placeholder="Введите текст заметки (Markdown поддерживается)..."
        />
      </div>
      <button onClick={() => navigate(-1)}>Назад</button>
    </div>
  );
}

export default NoteDetail;