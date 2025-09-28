package ec.sasf.ms_comp_prueba_freya_lopez.web.Mapper;

import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.UserEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.UserRequestDTO;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface  UserMapper {
    // De UserRequestDTO a UserEntity
    @Mapping(target = "id", ignore = true) // Se genera en la base de datos
    @Mapping(target = "role", ignore = true) // Se asigna según lógica (ADMIN/USER)
    @Mapping(target = "accountNonLocked", ignore = true) // Control de bloqueo
    UserEntity toEntity(UserRequestDTO dto);

    // De UserEntity a UserResponseDTO
    @Mapping(target = "accountNonLocked", source = "accountNonLocked")
    UserResponseDTO toResponseDTO(UserEntity entity);

}
