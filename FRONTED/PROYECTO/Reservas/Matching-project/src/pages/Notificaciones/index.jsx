import { useEffect } from "react";
import Header from "../../components/Header.jsx";
import Alert from "@mui/material/Alert";
import Stack from "@mui/material/Stack";
import CircularProgress from "@mui/material/CircularProgress";
import { FaCheckCircle } from "react-icons/fa";
import useNotifications from "../../hooks/useNotifications.js";

export default function Notifications() {
  const token = localStorage.getItem("token");
  const { notifications, loading, error, markAsRead, fetchNotifications } = useNotifications(token);

  // Refrescar notificaciones al entrar
  useEffect(() => {
    fetchNotifications();
  }, [fetchNotifications]);

  return (
    <div className="p-6 bg-gray-100 min-h-screen font-serif">
      <Header />
      <br></br>
      <h1 className="text-3xl font-bold text-blue-900 text-center mb-6">
        Mis Notificaciones
      </h1>

      <Stack spacing={2} className="mb-4">
        {error && <Alert severity="error">{error}</Alert>}
      </Stack>

      {loading ? (
        <div className="flex justify-center mt-10">
          <CircularProgress />
        </div>
      ) : notifications.length === 0 ? (
        <p className="text-center text-gray-600 mt-10">
          No tienes notificaciones todavía 📭
        </p>
      ) : (
        <div className="space-y-4 max-w-2xl mx-auto">
          {notifications.map((n) => (
            <div
              key={n.id}
              className={`p-4 rounded-lg shadow-md flex justify-between items-center transition ${
                n.status === "LEIDO" ? "bg-gray-200" : "bg-white"
              }`}
            >
              <div>
                <p className="text-gray-800">{n.content}</p>
                <p className="text-sm text-gray-500">
                  {new Date(n.createdAt).toLocaleString()}
                </p>
              </div>
              {n.status !== "LEIDO" && (
                <button
                  onClick={() => markAsRead(n.id)}
                  className="ml-4 text-green-600 hover:text-green-800 flex items-center gap-1"
                >
                  <FaCheckCircle /> Marcar como leído
                </button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
