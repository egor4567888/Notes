import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './Home';
import NoteDetail from './NoteDetail';
import CreateNote from './CreateNote';
import Login from './Login';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<Home />} />
        <Route path="/note/:id" element={<NoteDetail />} />
        <Route path="/create" element={<CreateNote />} />
      </Routes>
    </Router>
  );
}

export default App;