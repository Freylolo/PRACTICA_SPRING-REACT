package ec.sasf.ms_comp_prueba_freya_lopez.web.Controller;

import ec.sasf.ms_comp_prueba_freya_lopez.service.ReportService;
import ec.sasf.ms_comp_prueba_freya_lopez.service.UserService;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.ReportDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Gestión de reportes/denuncias")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final UserService userService;

    @Operation(summary = "Crear denuncia", description = "Permite a un usuario denunciar a otro")
    @PostMapping
    public ResponseEntity<ReportDto> crearReporte(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long denunciadoId,
            @RequestParam String motivo
    ) {
        Long emisorId = userService.buscarPorEmailEntity(userDetails.getUsername()).getId();
        ReportDto reporte = reportService.crearReporte(emisorId, denunciadoId, motivo);
        return ResponseEntity.ok(reporte);
    }

    @Operation(summary = "Listar todos los reportes (Admin)", description = "Lista todas las denuncias. Solo accesible por Admin")
    @GetMapping
    public ResponseEntity<List<ReportDto>> listarReportes() {
        List<ReportDto> reportes = reportService.listarReportes();
        return ResponseEntity.ok(reportes);
    }

    @Operation(summary = "Listar denuncias de un usuario", description = "Obtiene todas las denuncias que recibió un usuario")
    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<List<ReportDto>> obtenerReportesPorUsuario(@PathVariable Long usuarioId) {
        List<ReportDto> reportes = reportService.obtenerReportesPorUsuario(usuarioId);
        return ResponseEntity.ok(reportes);
    }

    @Operation(summary = "Actualizar estado de denuncia (Admin)", description = "Permite al Admin aceptar o rechazar una denuncia")
    @PutMapping("/{reportId}")
    public ResponseEntity<ReportDto> actualizarEstado(
            @PathVariable Long reportId,
            @RequestParam String estado
    ) {
        ReportDto reporteActualizado = reportService.actualizarEstado(reportId, estado);
        return ResponseEntity.ok(reporteActualizado);
    }
}
