import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import Alert from "@mui/material/Alert";
import Stack from "@mui/material/Stack";

export default function Register() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const API_BASE = import.meta.env.VITE_API_BASE;

  const register = async (e) => {
    e.preventDefault();
    setLoading(true);

    const userData = { name, email, password };

    try {
      const response = await fetch(`${API_BASE}/api/users`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData),
      });

      if (!response.ok) {
        let errorMessage = "Error al crear usuario";
        if (response.status === 0) {
          errorMessage = "No hay conexión con el servidor";
        } else if (response.status === 400) {
          try {
            const errData = await response.json();
            errorMessage = errData.mensaje || errData.error || errorMessage;
          } catch {
            errorMessage = "Error en la validación de datos";
          }
        } else if (response.status === 409) {
          errorMessage = "El correo ya está registrado";
        } else if (response.status >= 500) {
          errorMessage = "Error del servidor, inténtalo más tarde";
        }
        throw new Error(errorMessage);
      }

      const data = await response.json();
      console.log("Usuario creado:", data);
      setError("");
      setSuccess("Usuario registrado correctamente!");
      setTimeout(() => navigate("/login"), 2000);
    } catch (err) {
      if (err.message === "Failed to fetch") {
        setError("No se puede conectar con el servidor. Verifica tu internet.");
      } else {
        setError(err.message);
      }
      setSuccess("");
    } finally {
      setLoading(false);
    }
  };

  return (
   <div className="flex items-center justify-center min-h-screen bg-gradient-to-b from-gray-100 to-gray-200">
  <div className="bg-white p-6 rounded-2xl shadow-2xl w-full max-w-sm border border-gray-200">
    <h2 className="text-4xl font-serif text-center text-blue-900 mb-4">
          Crear cuenta
        </h2>

        <Stack spacing={2} className="mb-6">
          {success && <Alert severity="success">{success}</Alert>}
          {error && <Alert severity="error">{error}</Alert>}
        </Stack>

        <form onSubmit={register}>
          <div className="mb-5">
            <label className="block text-gray-700 font-medium mb-2">Nombre</label>
            <input
              type="text"
              placeholder="Tu nombre completo"
              className="w-full border border-gray-300 bg-gray-50 p-3 rounded-lg shadow-sm focus:ring-2 focus:ring-blue-300 focus:outline-none transition-all"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>

          <div className="mb-5">
            <label className="block text-gray-700 font-medium mb-2">Correo</label>
            <input
              type="email"
              placeholder="ejemplo@correo.com"
              className="w-full border border-gray-300 bg-gray-50 p-3 rounded-lg shadow-sm focus:ring-2 focus:ring-blue-300 focus:outline-none transition-all"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="mb-6">
            <label className="block text-gray-700 font-medium mb-2">Contraseña</label>
            <input
              type="password"
              placeholder="********"
              className="w-full border border-gray-300 bg-gray-50 p-3 rounded-lg shadow-sm focus:ring-2 focus:ring-blue-300 focus:outline-none transition-all"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button
            className="w-full bg-gradient-to-r from-blue-500 to-blue-600 text-white font-semibold p-3 rounded-lg hover:from-blue-600 hover:to-blue-700 hover:scale-105 transition transform shadow-md"
            disabled={loading}
          >
            {loading ? "Registrando..." : "Registrar"}
          </button>
        </form>

        <p className="mt-6 text-center text-gray-600">
          ¿Ya tienes cuenta?{" "}
          <Link
            to="/login"
            className="text-blue-500 font-medium hover:underline"
          >
            Regresar al login
          </Link>
        </p>
      </div>
    </div>
  );
}
