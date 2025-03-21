import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import QuillMarkdown from 'quill-markdown-shortcuts';

function NoteDetail() {
  const { id } = useParams();
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [lastModified, setLastModified] = useState(null);
  const navigate = useNavigate();

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
      <button onClick={handleSave}>Сохранить</button>
      <button onClick={() => navigate(-1)}>Назад</button>
    </div>
  );
}

export default NoteDetail;