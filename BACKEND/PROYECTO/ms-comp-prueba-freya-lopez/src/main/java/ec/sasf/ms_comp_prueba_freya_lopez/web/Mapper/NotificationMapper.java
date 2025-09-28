package ec.sasf.ms_comp_prueba_freya_lopez.web.Mapper;

import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.NotificationEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.NotificationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "createdAt", target = "createdAt")
    NotificationDTO toDTO(NotificationEntity notification);

    // Si quieres mapear de DTO a entidad
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    NotificationEntity toEntity(NotificationDTO dto);
}
