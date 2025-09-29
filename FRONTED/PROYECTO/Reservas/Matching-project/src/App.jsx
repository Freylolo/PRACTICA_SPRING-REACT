import { Routes, Route } from 'react-router-dom';
import './App.css'
import Login from './pages/Login/index.jsx';
import Home from './pages/Home/index.jsx';
import Notificaciones from'./pages/Notificaciones/index.jsx'
import Register from './pages/register/index.jsx';
import Perfil from './pages/Usuario/index.jsx';
import Reportes from './pages/Reportes/index.jsx';
import Administrador from './pages/Administrador/index.jsx';

function App() {

  return (
    <Routes>
      <Route path="/" element={<Login />} />
       <Route path="/login" element={<Login />} />
      <Route path="/home" element={<Home />} />
      <Route path="/notificaciones" element={<Notificaciones/>} />
      <Route path="/home" element={<Home />} />
      <Route path="/perfil" element={<Perfil />} />
      <Route path="/registro" element={<Register />} />
      <Route path="/reportes" element={<Reportes />} />
      <Route path="/administrador" element={<Administrador />} />
    </Routes>
  );
}

export default App

