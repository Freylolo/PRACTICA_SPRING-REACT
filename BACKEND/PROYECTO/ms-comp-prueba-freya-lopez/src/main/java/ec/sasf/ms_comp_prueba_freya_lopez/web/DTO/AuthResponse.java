package ec.sasf.ms_comp_prueba_freya_lopez.web.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String token;
    private String role;
}
