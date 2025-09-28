package ec.sasf.ms_comp_prueba_freya_lopez.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalUserService {
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    public void cargarUsuariosExternos() throws Exception {
        String url = "https://api.escuelajs.co/api/v1/users";
        String response = restTemplate.getForObject(url, String.class);

        List<UserRequestDTO> users = objectMapper.readValue(response, new TypeReference<>() {});

        for (UserRequestDTO dto : users) {
            // Cambiar dominio del email
            String email = dto.getEmail().split("@")[0] + "@app.net";
            dto.setEmail(email);
            dto.setRole("USER");

            // Crear usuario si no existe
            try {
                userService.buscarPorEmailEntity(email);
            } catch (Exception e) {
                userService.crearUsuario(dto);
            }
        }
    }
}
