import { useState, useEffect, useCallback } from "react";

export default function useReports(token) {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");

  const API_REPORTS = import.meta.env.VITE_API_REPORTS;

  // Obtener todos los reportes (admin)
  const fetchReports = useCallback(async () => {
    if (!token) return;
    setLoading(true);
    try {
      const res = await fetch(`${API_REPORTS}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("No se pudieron cargar los reportes");
      const data = await res.json();
      setReports(data);
      setError("");
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [token, API_REPORTS]);

  // Actualizar estado del reporte (aceptar/rechazar)
  const updateReportStatus = async (reportId, estado) => {
    if (!token) return setError("Debes estar logueado como admin");
    try {
      const res = await fetch(`${API_REPORTS}/${reportId}?estado=${estado}`, {
        method: "PUT",
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) {
        const errData = await res.json().catch(() => null);
        throw new Error(errData?.mensaje || errData?.error || "Error al actualizar el reporte");
      }
      setSuccess(`Reporte ${estado} correctamente`);
      await fetchReports();
    } catch (err) {
      setError(err.message);
    }
  };

  useEffect(() => {
    fetchReports();
  }, [fetchReports]);

  return { reports, loading, success, error, fetchReports, updateReportStatus };
}
