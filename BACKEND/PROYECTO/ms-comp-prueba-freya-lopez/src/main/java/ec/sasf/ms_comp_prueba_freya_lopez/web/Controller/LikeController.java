package ec.sasf.ms_comp_prueba_freya_lopez.web.Controller;

import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.UserEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.service.LikeService;
import ec.sasf.ms_comp_prueba_freya_lopez.service.UserService;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.LikeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
@Tag(name = "Likes", description = "Gestión de 'Me gusta' entre usuarios")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final UserService userService;

    @PostMapping("/{targetUserId}")
    @Operation(summary = "Dar Me Gusta", description = "Permite dar 'Me Gusta' a otro usuario y crea una notificación")
    public ResponseEntity<LikeDTO> darMeGusta(
            @PathVariable Long targetUserId,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long senderId = userService.buscarPorEmailEntity(userDetails.getUsername()).getId();
        return ResponseEntity.ok(likeService.darLike(senderId, targetUserId));
    }

    @GetMapping("/received")
    @Operation(summary = "Likes recibidos", description = "Obtiene todos los likes recibidos del usuario autenticado")
    public ResponseEntity<List<LikeDTO>> obtenerLikesRecibidos(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.buscarPorEmailEntity(userDetails.getUsername()).getId();
        return ResponseEntity.ok(likeService.obtenerLikesRecibidos(userId));
    }

    @GetMapping("/sent")
    @Operation(summary = "Likes enviados", description = "Obtiene todos los likes enviados por el usuario autenticado")
    public ResponseEntity<List<LikeDTO>> obtenerLikesEnviados(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.buscarPorEmailEntity(userDetails.getUsername()).getId();
        return ResponseEntity.ok(likeService.obtenerLikesEnviados(userId));
    }
}
