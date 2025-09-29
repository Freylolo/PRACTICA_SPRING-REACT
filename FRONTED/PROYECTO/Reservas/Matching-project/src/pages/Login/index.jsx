import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import Alert from "@mui/material/Alert";
import Stack from "@mui/material/Stack";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);


  const API_BASE = import.meta.env.VITE_API_BASE;
  console.log("API_BASE:", API_BASE);
  

  const login = async (e) => {
  e.preventDefault();

  if (!email || !password) {
    setError("Ingresa email y contraseña");
    setSuccess("");
    return;
  }

  try {
    setLoading(true);

    const response = await fetch(`${API_BASE}/api/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });

    if (!response.ok) {
    let errorMessage = "Error en el login";

    try {
        const errData = await response.json();
        // Si el backend envía mensaje personalizado
        errorMessage = errData.mensaje || errData.error || errorMessage;
    } catch {
        errorMessage = await response.text();
    }

    // Sobrescribir mensaje para usuario bloqueado
    if (errorMessage.includes("Usuario bloqueado")) {
        errorMessage = "Usuario bloqueado: demasiados intentos fallidos";
    } else if (response.status === 401 || response.status === 403) {
        errorMessage = "Usuario o contraseña incorrectos";
    }

    throw new Error(errorMessage);
}
    const data = await response.json();
    const token = data.token;
    const payload = JSON.parse(atob(token.split(".")[1]));
    const role = payload.rol || payload.role || "USER";
    const userEmail = payload.sub || data.email;

    localStorage.setItem("token", token);
    localStorage.setItem("role", role);
    localStorage.setItem("email", userEmail);

    setError("");
    setSuccess("Login exitoso");

    setTimeout(() => navigate("/home"), 1500);
  } catch (err) {
    // Manejar fallo de conexión
    if (err.message === "Failed to fetch") {
      setError("No se puede conectar con el servidor. Verifica tu conexión o intenta más tarde.");
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
      <div className="bg-white p-10 rounded-2xl shadow-2xl w-full max-w-md border border-gray-200">
        <h2 className="text-4xl font-serif text-center text-blue-900 mb-8">
          Bienvenido
        </h2>

        <Stack spacing={2} className="mb-6">
          {success && <Alert severity="success">{success}</Alert>}
          {error && <Alert severity="error">{error}</Alert>}
        </Stack>

        <form onSubmit={login}>
          <div className="mb-5">
            <label className="block text-gray-700 font-medium mb-2">Correo</label>
            <input
              type="email"
              placeholder="ejemplo@correo.com"
              className="w-full border border-gray-300 bg-gray-50 p-3 rounded-lg shadow-sm focus:ring-2 focus:ring-blue-300 focus:outline-none transition-all"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
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
            />
          </div>
         <button className="w-full bg-gradient-to-r from-blue-500 to-blue-600 text-white font-semibold p-3 rounded-lg hover:from-blue-600 hover:to-blue-700 hover:scale-105 transition transform shadow-md"
         disabled={loading}>
         {loading ? "Ingresando..." : "Ingresar"}
        </button>
        </form>
        <p className="mt-6 text-center text-gray-600">
          ¿No tienes cuenta?{" "}
          <Link
            to="/registro"
            className="text-blue-500 font-medium hover:underline"
          >
            Crear usuario
          </Link>
        </p>
      </div>
    </div>
  );
}