package ec.sasf.ms_comp_prueba_freya_lopez.web.Controller;

import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.UserEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.service.UserService;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.UserRequestDTO;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.UserResponseDTO;
import ec.sasf.ms_comp_prueba_freya_lopez.web.Mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Gestión de usuarios")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario")
    public ResponseEntity<UserResponseDTO> crearUsuario(@RequestBody @Validated UserRequestDTO userDto) {
        UserResponseDTO created = userService.crearUsuario(userDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Obtiene un usuario por su ID")
    public ResponseEntity<UserResponseDTO> buscarPorId(@PathVariable Long id) {
        UserResponseDTO user = userService.buscarPorId(id); // ya retorna DTO
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios")
    public ResponseEntity<List<UserResponseDTO>> listarUsuarios() {
        List<UserResponseDTO> dtos = userService.listarUsuarios(); // ya retorna DTOs
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario")
    public ResponseEntity<UserResponseDTO> actualizarUsuario(@PathVariable Long id,
                                                             @RequestBody @Validated UserRequestDTO userDto) {
        UserResponseDTO updated = userService.actualizarUsuario(id, userDto); // retorna DTO
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su ID")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        userService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Información del usuario logueado", description = "Obtiene los datos del usuario autenticado")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.buscarPorEmailEntity(userDetails.getUsername());
        return ResponseEntity.ok(userMapper.toResponseDTO(user));
    }
    
    @PutMapping("/admin/block/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Bloquear usuario", description = "Bloquea un usuario (solo admin)")
    public ResponseEntity<Void> bloquearUsuario(@PathVariable Long id) {
        userService.bloquearUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/unblock/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desbloquear usuario", description = "Desbloquea un usuario y resetea intentos fallidos (solo admin)")
    public ResponseEntity<Void> desbloquearUsuario(@PathVariable Long id) {
        userService.desbloquearUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
