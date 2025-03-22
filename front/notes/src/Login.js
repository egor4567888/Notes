import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from './axiosInstance';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
    axios.post('/login', { username, password })
      .then(response => {
        const { accessToken, refreshToken } = response.data;
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        navigate('/');
      })
      .catch(err => {
        console.error(err);
        alert('Неверные учетные данные');
      });
  };

  const handleRegister = (e) => {
    e.preventDefault();
    axios.post('/registr', { username, password })
      .then(response => {
        const { accessToken, refreshToken } = response.data;
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        navigate('/');
      })
      .catch(err => {
        console.error(err);
        alert('Ошибка регистрации');
      });
  };

  return (
    <div>
      <h2>Авторизация</h2>
      <form onSubmit={handleLogin}>
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
        <button type="submit">Войти</button>
        <button onClick={handleRegister}>Регистрация</button>
      </form>
    </div>
  );
}

export default Login;