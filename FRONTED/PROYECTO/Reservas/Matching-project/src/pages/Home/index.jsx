import { useState } from "react";
import Header from "../../components/Header.jsx";
import Alert from "@mui/material/Alert";
import Stack from "@mui/material/Stack";
import Modal from "@mui/material/Modal";
import Box from "@mui/material/Box";
import Snackbar from "@mui/material/Snackbar";
import toast from "react-hot-toast";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import { FaHeart, FaFlag ,FaBan } from "react-icons/fa";
import useUsers from "../../hooks/useUsers.js";


export default function Home() {
 const token = localStorage.getItem("token");
  const { users, loading, success, error, info, giveLike, reportUser, likedUsers, page, setPage, bloquearUsuario, } = useUsers(token);
  const [reportModalOpen, setReportModalOpen] = useState(false);
  const [reportUserId, setReportUserId] = useState(null);
  const [reportReason, setReportReason] = useState("");
  const [openAlert, setOpenAlert] = useState(false);


 const handleLike = (userId) => {
  if (likedUsers.has(userId)) {
    toast.error("Ya le diste like a este usuario");
    return;
  }
  giveLike(userId);
  toast.success("¡Like enviado!");
};

  const handleReport = (userId) => {
    setReportUserId(userId);
    setReportModalOpen(true);
  };

  const submitReport = async () => {
  if (!reportReason) return;
  await reportUser(reportUserId, reportReason); 
  setReportModalOpen(false);
  setReportReason("");
  };

  return (
    <div className="p-6 bg-gray-100 min-h-screen font-serif">
      <Header />
      <br></br>
      <h1 className="text-4xl text-blue-900 text-center mb-6">Usuarios</h1>

      <Stack spacing={2} className="mb-4">
        {success && <Alert severity="success">{success}</Alert>}
        {error && <Alert severity="error">{error}</Alert>}
        {info && <Alert severity="info">{info}</Alert>}
      </Stack>

      {loading ? (
        <p className="text-center text-gray-600">Cargando usuarios...</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {users.map((user) => (
            <div
              key={user.id}
              className="bg-white p-4 rounded-xl shadow-lg relative group hover:shadow-xl transition transform hover:-translate-y-1"
            >
              <img
                src={user.avatar || `https://i.pravatar.cc/150?u=${user.email}`}
                alt={user.name}
                className="w-20 h-20 rounded-full mx-auto mb-4"
              />
              <h2 className="text-xl font-semibold text-center">{user.name}</h2>
              {user.estadoCivil && (
                <p className="text-center text-gray-500">{user.estadoCivil}</p>
              )}
              <p className="text-center text-gray-600 mt-2">{user.email}</p>
             {/* Botón Dar Like */}
             <button onClick={() => handleLike(user.id)} className={`absolute top-2 right-2 text-sm transition ${ likedUsers.has(user.id)? "text-red-500" : "text-gray-400 hover:text-red-500"}`}
             title={likedUsers.has(user.id) ? "Ya le diste like" : "Darle like"}>
              <FaHeart size={18} />
             </button>
             {/* Botón Reportar */}
             <button onClick={() => handleReport(user.id)} className="absolute top-2 left-2 text-sm text-gray-400 hover:text-yellow-500 transition" title="Reportar usuario">
             <FaFlag size={18} />
             </button>
               {/* Botón Bloquear */}
              <button onClick={() => bloquearUsuario(user.id)} className="absolute bottom-2 left-2 text-sm text-gray-400 hover:text-red-600 transition"
              title="Bloquear usuario">
              <FaBan size={18} />
              </button>
            </div>
          ))}
        </div>
      )}

      {/* Modal Reporte */}
      <Modal open={reportModalOpen} onClose={() => setReportModalOpen(false)}>
        <Box className="bg-white p-6 rounded-xl shadow-xl w-96 mx-auto mt-20">
          <h2 className="text-2xl font-bold mb-4 text-gray-800 text-center">Reportar Usuario</h2>
          <TextField
            label="Motivo del reporte"
            fullWidth
            multiline
            rows={4}
            value={reportReason}
            onChange={(e) => setReportReason(e.target.value)}
            variant="outlined"
          />
          <div className="flex justify-end mt-4 gap-2">
            <Button variant="outlined" onClick={() => setReportModalOpen(false)}>
              Cancelar
            </Button>
            <Button variant="contained" color="error" onClick={submitReport}>
              Enviar
            </Button>
          </div>
        </Box>
      </Modal>

      {/* Paginación */}
      <div className="flex justify-center mt-6 space-x-4">
        <button
          className="px-4 py-2 bg-blue-500 text-white rounded-lg disabled:bg-gray-400"
          disabled={page === 0}
          onClick={() => setPage(page - 1)}
        >
          Anterior
        </button>
        <button
          className="px-4 py-2 bg-blue-500 text-white rounded-lg disabled:bg-gray-400"
          disabled={users.length < 6} 
          onClick={() => setPage(page + 1)}
        >
          Siguiente
        </button>
      </div>
      <Snackbar open={openAlert}
       autoHideDuration={3000}
       onClose={() => setOpenAlert(false)}
       anchorOrigin={{ vertical: "top", horizontal: "center" }}
       >
     <Alert severity="info" onClose={() => setOpenAlert(false)}>
       {info}
     </Alert>
    </Snackbar>
    </div>
  );
}