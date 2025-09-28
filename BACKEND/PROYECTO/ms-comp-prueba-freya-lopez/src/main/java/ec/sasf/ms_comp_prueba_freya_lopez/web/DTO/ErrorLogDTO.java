package ec.sasf.ms_comp_prueba_freya_lopez.web.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorLogDTO {
    private Long id;
    private String message;
    private String stackTrace;
    private LocalDateTime timestamp;
    private Long userId;
}
