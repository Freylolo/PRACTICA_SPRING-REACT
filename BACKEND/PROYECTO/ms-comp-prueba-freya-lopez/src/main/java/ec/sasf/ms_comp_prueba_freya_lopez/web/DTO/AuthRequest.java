package ec.sasf.ms_comp_prueba_freya_lopez.web.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String email;
    private String password;
}
