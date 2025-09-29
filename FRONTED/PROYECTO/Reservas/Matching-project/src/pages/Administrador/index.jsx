import { useEffect, useState } from "react";
import { IconButton, Paper, Stack, Alert, Snackbar } from "@mui/material";
import { FaBan, FaCheck, FaTrash } from "react-icons/fa";
import Header from "../../components/Header.jsx";
import useUsers from "../../hooks/useUsers.js";

export default function Administrador() {
  const token = localStorage.getItem("token");
  const {
    users,
    loading,
    success,
    error,
    page,
    setPage,
    fetchUsers,
    eliminarUsuario,
    bloquearUsuario,
    desbloquearUsuario,
  } = useUsers(token);

  const [openAlert, setOpenAlert] = useState(false);
  const [alertMessage, setAlertMessage] = useState("");
  const [alertSeverity, setAlertSeverity] = useState("info");

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers, page]);

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
      <h1 className="text-3xl font-bold text-center text-blue-900 mb-6">
        Administración de Usuarios
      </h1>

      {loading ? (
        <p className="text-center text-gray-600">Cargando usuarios...</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {users.map((user) => (
            <Paper
              key={user.id}
              className="p-4 rounded-xl flex flex-col items-center"
              elevation={3}
            >
              <img
                src={user.avatar || `https://i.pravatar.cc/150?u=${user.email}`}
                alt={user.name}
                className="w-20 h-20 rounded-full mb-2"
              />
              <p className="font-semibold">{user.name}</p>
              <p className="text-gray-500 text-sm">{user.email}</p>

              <Stack direction="row" spacing={1} className="mt-2">
                <IconButton
                  onClick={() => bloquearUsuario(user.id)}
                  disabled={!user.accountNonLocked}
                  title="Bloquear usuario"
                  className="text-red-500"
                  style={{
                  color: user.accountNonLocked ? "red" : "gray", 
                  cursor: user.accountNonLocked ? "pointer" : "not-allowed",
                  }}
                >
                  <FaBan />
                </IconButton>

                <IconButton
                  onClick={() => desbloquearUsuario(user.id)}
                  disabled={user.accountNonLocked}
                  title="Desbloquear usuario"
                  className="text-green-500"
                  style={{
                  color: !user.accountNonLocked ? "green" : "gray", 
                  cursor: !user.accountNonLocked ? "pointer" : "not-allowed",
                   }}
                >
                  <FaCheck />
                </IconButton>

                <IconButton
                  onClick={() => eliminarUsuario(user.id)}
                  title="Eliminar usuario"
                  className="text-gray-800"
                   style={{
                   color: "black",
                   cursor: "pointer",
                  }}
                >
                  <FaTrash />
                </IconButton>
              </Stack>
            </Paper>
          ))}
        </div>
      )}

      {/* Paginación */}
      <div className="flex justify-center mt-6 gap-4">
        <button
          className="px-4 py-2 bg-blue-500 text-white rounded-lg disabled:bg-gray-400"
          disabled={page === 0}
          onClick={() => setPage(page - 1)}
        >
          Anterior
        </button>
        <button
          className="px-4 py-2 bg-blue-500 text-white rounded-lg disabled:bg-gray-400"
          disabled={users.length < 5}
          onClick={() => setPage(page + 1)}
        >
          Siguiente
        </button>
      </div>

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
