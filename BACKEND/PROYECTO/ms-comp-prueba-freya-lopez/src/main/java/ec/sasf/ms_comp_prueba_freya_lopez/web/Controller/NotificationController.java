package ec.sasf.ms_comp_prueba_freya_lopez.web.Controller;

import ec.sasf.ms_comp_prueba_freya_lopez.service.NotificationService;
import ec.sasf.ms_comp_prueba_freya_lopez.service.UserService;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.NotificationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Gestión de notificaciones")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    @Operation(summary = "Obtener notificaciones de un usuario", description = "Lista todas las notificaciones recibidas por un usuario.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> obtenerNotificaciones(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.obtenerNotificacionesDeUsuario(userId);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Crear notificación", description = "Crea una nueva notificación para un usuario receptor.")
    @PostMapping
    public ResponseEntity<NotificationDTO> crearNotificacion(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam String content,
            @RequestParam String status
    ) {
        NotificationDTO notification = notificationService.crearNotificacion(senderId, receiverId, content, status);
        return ResponseEntity.ok(notification);
    }

    @Operation(summary = "Actualizar estado de notificación", description = "Permite actualizar el estado de una notificación (ej. leída, aceptada, rechazada).")
    @PutMapping("/{notificationId}")
    public ResponseEntity<NotificationDTO> actualizarEstado(
            @PathVariable Long notificationId,
            @RequestParam String status
    ) {
        NotificationDTO notification = notificationService.actualizarEstado(notificationId, status);
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/me")
    @Operation(summary = "Mis notificaciones", description = "Lista todas las notificaciones del usuario autenticado")
    public ResponseEntity<List<NotificationDTO>> obtenerMisNotificaciones(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.buscarPorEmailEntity(userDetails.getUsername()).getId();
        List<NotificationDTO> notifications = notificationService.obtenerNotificacionesDeUsuario(userId);
        return ResponseEntity.ok(notifications);
    }

}
