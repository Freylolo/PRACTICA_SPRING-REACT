package ec.sasf.ms_comp_prueba_freya_lopez.web.Mapper;

import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.LikeEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.LikeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "createdAt", target = "createdAt")
    LikeDTO toDTO(LikeEntity likeEntity);

    // Si necesitas mapear de DTO a entidad
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    LikeEntity toEntity(LikeDTO dto);
}
