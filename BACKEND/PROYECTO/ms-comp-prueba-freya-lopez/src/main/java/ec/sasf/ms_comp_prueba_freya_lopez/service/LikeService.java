package ec.sasf.ms_comp_prueba_freya_lopez.service;

import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.LikeEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.UserEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.persistence.repository.LikeRepository;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.LikeDTO;
import ec.sasf.ms_comp_prueba_freya_lopez.web.Mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final LikeMapper likeMapper;
    private final NotificationService notificationService;

    public LikeDTO darLike(Long senderId, Long receiverId) {
        UserEntity sender = userService.buscarPorIdEntity(senderId);
        UserEntity receiver = userService.buscarPorIdEntity(receiverId);

        if (likeRepository.existsBySenderAndReceiver(sender, receiver)) {
            throw new RuntimeException("Ya diste like a este usuario");
        }

        LikeEntity like = new LikeEntity();
        like.setSender(sender);
        like.setReceiver(receiver);
        like.setCreatedAt(LocalDateTime.now());

        // Guardar like
        LikeDTO likeDTO = likeMapper.toDTO(likeRepository.save(like));

        // Crear notificación
        notificationService.crearNotificacion(
                senderId,
                receiverId,
                "El usuario " + sender.getName() + " te dio un like",
                "NO_LEIDO"
        );

        return likeDTO;
    }

    public List<LikeDTO> obtenerLikesRecibidos(Long receiverId) {
        UserEntity receiver = userService.buscarPorIdEntity(receiverId);
        return likeRepository.findAll()
                .stream()
                .filter(like -> like.getReceiver().equals(receiver))
                .map(likeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<LikeDTO> obtenerLikesEnviados(Long senderId) {
        UserEntity sender = userService.buscarPorIdEntity(senderId);
        return likeRepository.findAll()
                .stream()
                .filter(like -> like.getSender().equals(sender))
                .map(likeMapper::toDTO)
                .collect(Collectors.toList());
    }

}
