import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from './axiosInstance';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
    setErrorMsg('');
    axios.post('/login', { username, password })
      .then(response => {
        const { accessToken, refreshToken } = response.data;
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        localStorage.setItem('username', username);
        navigate('/');
      })
      .catch(err => {
        console.error(err);
        setErrorMsg(err.response ? err.response.data : "Ошибка авторизации");
      });
  };

  const handleRegister = (e) => {
    e.preventDefault();
    setErrorMsg('');
    axios.post('/registr', { username, password })
      .then(response => {
        const { accessToken, refreshToken } = response.data;
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        localStorage.setItem('username', username);
        navigate('/');
      })
      .catch(err => {
        console.error(err);
        setErrorMsg(err.response ? err.response.data : "Ошибка регистрации");
      });
  };

  return (
    <div className="container">
      <form onSubmit={handleLogin}>
        <h2 style={{ textAlign: 'center' }}>Авторизация</h2>
        <div>
          <label>Имя пользователя:</label>
          <input
            type="text"
            value={username}
            onChange={e => setUsername(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Пароль:</label>
          <input
            type="password"
            value={password}
            onChange={e => setPassword(e.target.value)}
            required
          />
        </div>
        <div style={{ textAlign: 'center' }}>
          <button type="submit">Войти</button>
          <button onClick={handleRegister} style={{ marginLeft: '10px' }}>Регистрация</button>
        </div>
        {errorMsg && (
          <div style={{ color: 'red', textAlign: 'center', marginTop: '10px' }}>
            {errorMsg}
          </div>
        )}
      </form>
    </div>
  );
}

export default Login;