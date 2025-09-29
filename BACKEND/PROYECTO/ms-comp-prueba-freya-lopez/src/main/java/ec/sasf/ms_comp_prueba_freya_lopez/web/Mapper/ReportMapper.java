package ec.sasf.ms_comp_prueba_freya_lopez.web.Mapper;

import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.ReportEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.ReportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    @Mapping(target = "feCreacion", ignore = true)
    @Mapping(target = "feActualizacion", ignore = true)
    @Mapping(target = "estado", ignore = true)
    ReportEntity toEntity(ReportDto dto);

    @Mapping(source = "emisor.id", target = "emisorId")
    @Mapping(source = "denunciado.id", target = "denunciadoId")
    @Mapping(source = "emisor.name", target = "emisorName")
    @Mapping(source = "denunciado.name", target = "denunciadoName")
    @Mapping(source = "feCreacion", target = "createdAt")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "motivo", target = "motivo")
    ReportDto toDto(ReportEntity entity);
}
