package ec.sasf.ms_comp_prueba_freya_lopez.web.Mapper;

import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.ExternalUserDTO;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.UserRequestDTO;

public interface ExternalUserMapper {
    public static UserRequestDTO toUserRequestDTO(ExternalUserDTO externalDto) {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName(externalDto.getName());
        // Cambiamos el dominio a @app.net
        String email = externalDto.getEmail().split("@")[0] + "@app.net";
        dto.setEmail(email);
        dto.setPassword(externalDto.getPassword()); // luego se encripta al guardar
        dto.setAvatar(externalDto.getAvatar());
        dto.setEstadoCivil(null); // opcional
        dto.setRole("USER"); // asignamos USER
        return dto;
    }
}
