package ec.sasf.ms_comp_prueba_freya_lopez.service;

import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.NotificationEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.UserEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.persistence.repository.NotificationRepository;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.NotificationDTO;
import ec.sasf.ms_comp_prueba_freya_lopez.web.Mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final NotificationMapper notificationMapper;

    // Crear notificación
    public NotificationDTO crearNotificacion(Long senderId, Long receiverId, String content, String status) {
        UserEntity sender = userService.buscarPorIdEntity(senderId);
        UserEntity receiver = userService.buscarPorIdEntity(receiverId);

        NotificationEntity notification = new NotificationEntity();
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setContent(content);
        notification.setStatus(status);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationMapper.toDTO(notificationRepository.save(notification));
    }

    // Obtener notificaciones de un usuario receptor
    public List<NotificationDTO> obtenerNotificacionesDeUsuario(Long receiverId) {
        UserEntity receiver = userService.buscarPorIdEntity(receiverId);

        return notificationRepository.findAll()
                .stream()
                .filter(n -> n.getReceiver() != null && n.getReceiver().getId().equals(receiver.getId()))
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Actualizar estado de notificación
    public NotificationDTO actualizarEstado(Long notificationId, String status) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notification.setStatus(status);
        return notificationMapper.toDTO(notificationRepository.save(notification));
    }
}
