package ec.sasf.ms_comp_prueba_freya_lopez.web.Controller;

import ec.sasf.ms_comp_prueba_freya_lopez.exception.InvalidCredentialsException;
import ec.sasf.ms_comp_prueba_freya_lopez.exception.UserBlockedException;
import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.UserEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.security.JwtUtil;
import ec.sasf.ms_comp_prueba_freya_lopez.service.UserService;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.AuthRequest;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Autenticación de usuarios")
@RequiredArgsConstructor
public class AuthContoller {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    private static final int MAX_FAILED_ATTEMPTS = 5;

    @Operation(summary = "Login", description = "Autentica un usuario y devuelve un token JWT")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        // Buscar usuario por email
        UserEntity user = userService.buscarPorEmailEntity(request.getEmail());

        // Verificar si el usuario está bloqueado
        if (!user.isAccountNonLocked()) {
            throw new UserBlockedException("Usuario bloqueado por múltiples intentos fallidos");
        }

        try {
            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Resetear intentos fallidos tras login exitoso
            user.setFailedAttempts(0);
            userService.guardarUsuario(user);

            // Generar JWT
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setRole(user.getRole());

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            // Incrementar intentos fallidos
            int attempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);

            if (attempts >= MAX_FAILED_ATTEMPTS) {
                user.setAccountNonLocked(false);
            }

            userService.guardarUsuario(user);

            throw new InvalidCredentialsException("Credenciales inválidas. Intentos fallidos: " + attempts);
        }
    }
}
