import { useState, useEffect, useCallback } from "react";

const API_NOTIFICATIONS = import.meta.env.VITE_API_NOTIFICATIONS;

export default function useNotifications(token) {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Obtener notificaciones
  const fetchNotifications = useCallback(async () => {
  if (!token) return;
  setLoading(true);
  try {
    const response = await fetch(`${API_NOTIFICATIONS}/me`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Error al cargar notificaciones");
    const data = await response.json();
    setNotifications(data);
  } catch (err) {
    setError(err.message);
  } finally {
    setLoading(false);
  }
}, [token]);

  // Marcar como leída
  const markAsRead = async (notificationId) => {
  try {
    const response = await fetch(`${API_NOTIFICATIONS}/${notificationId}?status=LEIDO`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    if (!response.ok) throw new Error("Error al actualizar notificación");
    await fetchNotifications(); 
  } catch (err) {
    setError(err.message);
  }
};

  useEffect(() => {
    fetchNotifications();
  }, [fetchNotifications]);

  return { notifications, loading, error, markAsRead, fetchNotifications };
}
