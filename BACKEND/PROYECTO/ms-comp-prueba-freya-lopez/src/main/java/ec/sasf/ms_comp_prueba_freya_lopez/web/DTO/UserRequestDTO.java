package ec.sasf.ms_comp_prueba_freya_lopez.web.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    private String name;
    private String email;
    private String password;
    private String avatar;
    private String estadoCivil;
    private String role;
}
