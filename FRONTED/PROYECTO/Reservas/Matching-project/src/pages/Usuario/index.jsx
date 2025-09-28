import { useState, useEffect } from "react";
import useUsers from "../../hooks/useUsers.js";
import Header from "../../components/Header.jsx";
import { TextField, Button, Stack, Alert } from "@mui/material";

export default function EditProfile() {
  const token = localStorage.getItem("token");
  const { currentUser, updateUser, success, error, info, loadingCurrentUser } = useUsers(token);


  const [formData, setFormData] = useState({
    name: "",
    avatar: "",
    estadoCivil: "",
  });

  useEffect(() => {
    if (currentUser) {
      setFormData({
        name: currentUser.name || "",
        avatar: currentUser.avatar || "",
        estadoCivil: currentUser.estadoCivil || "",
      });
    }
  }, [currentUser]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    updateUser(formData);
  };

  if (loadingCurrentUser) return <p className="text-center mt-10">Cargando...</p>;


  return (
    <div className="p-6 bg-gray-100 min-h-screen font-serif">
      <Header />
      <br></br>
      <h1 className="text-4xl text-blue-900 text-center mb-6">Editar Perfil</h1>

      <Stack spacing={2} className="mb-4">
        {success && <Alert severity="success">{success}</Alert>}
        {error && <Alert severity="error">{error}</Alert>}
        {info && <Alert severity="info">{info}</Alert>}
      </Stack>

      <div className="max-w-md mx-auto bg-white p-6 rounded-xl shadow-lg">
        <img
        src={formData.avatar || `https://i.pravatar.cc/150?u=${currentUser?.email}`}
        alt={formData.name}
        className="w-32 h-32 rounded-full mx-auto mb-4"/>

        <form onSubmit={handleSubmit} className="space-y-4">
          <TextField
            label="Nombre"
            name="name"
            value={formData.name}
            onChange={handleChange}
            fullWidth
          />
          <TextField
            label="Avatar URL"
            name="avatar"
            value={formData.avatar}
            onChange={handleChange}
            fullWidth
          />
          <TextField
            label="Estado Civil"
            name="estadoCivil"
            value={formData.estadoCivil}
            onChange={handleChange}
            fullWidth
          />
          <Button type="submit" variant="contained" color="primary" fullWidth>
            Guardar Cambios
          </Button>
        </form>
      </div>
    </div>
  );
}
