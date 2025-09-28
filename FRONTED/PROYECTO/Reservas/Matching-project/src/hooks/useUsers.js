import { useState, useEffect, useCallback } from "react";

export default function useUsers(token) {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");
  const [info, setInfo] = useState("");
  const [page, setPage] = useState(0);
  const [likedUsers, setLikedUsers] = useState(new Set());
  const [currentUser, setCurrentUser] = useState(null);
  const [loadingCurrentUser, setLoadingCurrentUser] = useState(false);

  const API_USERS = import.meta.env.VITE_API_USERS;
  const API_LIKES = import.meta.env.VITE_API_LIKES;
  const API_REPORTS = import.meta.env.VITE_API_REPORTS;

  // Obtener usuarios paginados
  const fetchUsers = useCallback(async () => {
    setLoading(true);
    try {
      const response = await fetch(`${API_USERS}/paginated?page=${page}&size=6`, {
        headers: token
          ? { Authorization: `Bearer ${token}`, "Content-Type": "application/json" }
          : { "Content-Type": "application/json" },
      });
      if (!response.ok) throw new Error("No se pudieron cargar los usuarios");
      const data = await response.json();
      setUsers(data.content || data);
      setError("");
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [token, API_USERS, page]);

  // Likes enviados
  const fetchSentLikes = useCallback(async () => {
    if (!token) return;
    try {
      const response = await fetch(`${API_LIKES}/sent`, {
        headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
      });
      if (!response.ok) throw new Error("No se pudieron cargar los likes enviados");
      const data = await response.json();
      const likedIds = new Set(data.map(like => like.receiverId));
      setLikedUsers(likedIds);
    } catch (err) {
      console.error(err.message);
    }
  }, [token, API_LIKES]);

  // Dar like
  const giveLike = async (targetUserId) => {
    if (!token) return setInfo("Debes estar logueado para dar like");
    try {
      const response = await fetch(`${API_LIKES}/${targetUserId}`, {
        method: "POST",
        headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
      });
      if (!response.ok) {
        const errData = await response.json().catch(() => null);
        throw new Error(errData?.mensaje || errData?.error || "Error al dar like");
      }
      setSuccess("¡Like enviado correctamente!");
      setLikedUsers(prev => new Set([...prev, targetUserId]));
      await fetchSentLikes();
    } catch (err) {
      setError(err.message);
    }
  };

  // Reportar usuario
  const reportUser = async (targetUserId, motivo) => {
    if (!token) return setInfo("Debes estar logueado para reportar");
    if (!motivo) return setInfo("Debes especificar un motivo para reportar");
    try {
      const params = new URLSearchParams({ denunciadoId: targetUserId, motivo });
      const response = await fetch(`${API_REPORTS}?${params.toString()}`, {
        method: "POST",
        headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
      });
      if (!response.ok) {
        const errData = await response.json().catch(() => null);
        throw new Error(errData?.mensaje || errData?.error || "Error al reportar usuario");
      }
      setSuccess("Reporte enviado correctamente");
    } catch (err) {
      setError(err.message);
    }
  };

  // Obtener usuario actual
  const fetchCurrentUser = useCallback(async () => {
    if (!token) return;
    setLoadingCurrentUser(true);
    try {
      const res = await fetch(`${API_USERS}/me`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("No se pudo cargar tu información");
      const data = await res.json();
      setCurrentUser(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoadingCurrentUser(false);
    }
  }, [API_USERS, token]);

  // Actualizar usuario
  const updateUser = async (userData) => {
    if (!token) return setInfo("Debes estar logueado");
    try {
      const res = await fetch(`${API_USERS}/${currentUser.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(userData),
      });
      if (!res.ok) {
        const errData = await res.json().catch(() => null);
        throw new Error(errData?.mensaje || errData?.error || "Error al actualizar usuario");
      }
      const updated = await res.json();
      setCurrentUser(updated);
      setSuccess("Usuario actualizado correctamente");
    } catch (err) {
      setError(err.message);
    }
  };

  // Cargar usuarios, likes y usuario actual al inicio
  useEffect(() => {
    fetchUsers();
    fetchCurrentUser();
    fetchSentLikes();
  }, [fetchUsers, fetchCurrentUser, fetchSentLikes]);

  // Limpieza de mensajes automáticos
  useEffect(() => {
    if (success || error || info) {
      const timer = setTimeout(() => {
        setSuccess("");
        setError("");
        setInfo("");
      }, 3000);
      return () => clearTimeout(timer);
    }
  }, [success, error, info]);

  return {
    users,
    loading,
    loadingCurrentUser,
    success,
    error,
    info,
    fetchUsers,
    fetchCurrentUser,
    updateUser,
    giveLike,
    reportUser,
    likedUsers,
    page,
    setPage,
    currentUser,
  };
}
