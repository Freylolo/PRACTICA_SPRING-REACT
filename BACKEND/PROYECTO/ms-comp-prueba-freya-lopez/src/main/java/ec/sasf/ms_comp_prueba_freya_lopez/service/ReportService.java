package ec.sasf.ms_comp_prueba_freya_lopez.service;

import ec.sasf.ms_comp_prueba_freya_lopez.exception.UserNotFoundException;
import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.ReportEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.UserEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.persistence.repository.ReportRepository;
import ec.sasf.ms_comp_prueba_freya_lopez.persistence.repository.UserRepository;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.ReportDto;
import ec.sasf.ms_comp_prueba_freya_lopez.web.Mapper.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReportMapper reportMapper;
    private final NotificationService notificationService;

    // Crear reporte
    public ReportDto crearReporte(Long emisorId, Long denunciadoId, String motivo) {
        UserEntity emisor = userRepository.findById(emisorId)
                .orElseThrow(() -> new UserNotFoundException("Usuario emisor no encontrado"));
        UserEntity denunciado = userRepository.findById(denunciadoId)
                .orElseThrow(() -> new UserNotFoundException("Usuario denunciado no encontrado"));

        ReportEntity report = new ReportEntity();
        report.setEmisor(emisor);
        report.setDenunciado(denunciado);
        report.setMotivo(motivo);
        report.setStatus("PENDIENTE");
        report.setFeCreacion(LocalDateTime.now());

        ReportEntity saved = reportRepository.save(report);
        return reportMapper.toDto(saved);
    }

    // Obtener todos los reportes
    public List<ReportDto> listarReportes() {
        return reportRepository.findAll()
                .stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }

    // Obtener reportes por usuario denunciado
    public List<ReportDto> obtenerReportesPorUsuario(Long usuarioId) {
        UserEntity usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        return reportRepository.findByDenunciado(usuario)
                .stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }

    // Actualizar estado del reporte (aceptado, rechazado)
    public ReportDto actualizarEstado(Long reportId, String estado) {
        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        report.setStatus(estado);
        ReportEntity updated = reportRepository.save(report);

        // Crear notificación automática para el emisor
        String contenido;
        if ("ACEPTADA".equalsIgnoreCase(estado)) {
            // Bloquear usuario denunciado
            UserEntity denunciado = report.getDenunciado();
            denunciado.setAccountNonLocked(false);
            userRepository.save(denunciado);

            contenido = "Tu denuncia contra " + denunciado.getName() + " ha sido aceptada. El usuario ha sido bloqueado.";
        } else if ("RECHAZADA".equalsIgnoreCase(estado)) {
            contenido = "Tu denuncia contra " + report.getDenunciado().getName() + " ha sido rechazada.";
        } else {
            contenido = "El estado de tu denuncia ha sido actualizado a: " + estado;
        }

        notificationService.crearNotificacion(
                report.getDenunciado().getId(),
                report.getEmisor().getId(),
                contenido,
                "PENDIENTE"
        );

        return reportMapper.toDto(updated);
    }

}
