import { useEffect, useState } from "react";
import { Paper, Button, Stack, Alert, Snackbar } from "@mui/material";
import Header from "../../components/Header.jsx";
import { FaCheck, FaTimes } from "react-icons/fa";
import useReports from "../../hooks/useReports.js";

export default function Reportes() {
  const token = localStorage.getItem("token");
  const { reports, loading, success, error, updateReportStatus } = useReports(token);

  const [openAlert, setOpenAlert] = useState(false);
  const [alertMessage, setAlertMessage] = useState("");
  const [alertSeverity, setAlertSeverity] = useState("info");

  useEffect(() => {
    if (success) {
      setAlertMessage(success);
      setAlertSeverity("success");
      setOpenAlert(true);
    } else if (error) {
      setAlertMessage(error);
      setAlertSeverity("error");
      setOpenAlert(true);
    }
  }, [success, error]);

  return (
    <div className="p-6 bg-gray-100 min-h-screen font-sans">
      <Header />
      <br />
      <h1 className="text-3xl font-bold text-center text-blue-900 mb-6">Gestión de Reportes</h1>

      {loading ? (
        <p className="text-center text-gray-600">Cargando reportes...</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {reports.map((r) => (
            <Paper key={r.id} className="p-4 rounded-xl flex flex-col items-center" elevation={3}>
              <div className="font-semibold mb-1">{r.emisorName} denunció a {r.denunciadoName}</div>
              <div className="text-gray-500 text-sm mb-2">{r.motivo}</div>

              {r.status === "PENDIENTE" ? (
                <Stack direction="row" spacing={2}>
                  <Button
                    variant="contained"
                    color="success"
                    startIcon={<FaCheck />}
                    onClick={() => updateReportStatus(r.id, "ACEPTADA")}
                  />
                  <Button
                    variant="contained"
                    color="error"
                    startIcon={<FaTimes />}
                    onClick={() => updateReportStatus(r.id, "RECHAZADA")}
                  />
                </Stack>
              ) : (
                <div
                  className={`mt-2 font-semibold ${
                    r.status === "ACEPTADA" ? "text-green-600" : "text-red-600"
                  }`}
                >
                  {r.status === "ACEPTADA" ? "Aceptado" : "Rechazado"}
                </div>
              )}
            </Paper>
          ))}
        </div>
      )}

      <Snackbar
        open={openAlert}
        autoHideDuration={3000}
        onClose={() => setOpenAlert(false)}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert severity={alertSeverity} onClose={() => setOpenAlert(false)}>
          {alertMessage}
        </Alert>
      </Snackbar>
    </div>
  );
}
