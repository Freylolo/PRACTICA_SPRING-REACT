package ec.sasf.ms_comp_prueba_freya_lopez.web.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ReportDto {
    private Long id;
    private String motivo;
    private String status;
    private LocalDateTime createdAt;
    private Long emisorId;
    private String emisorName;
    private Long denunciadoId;
    private String denunciadoName;
}
