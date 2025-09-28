package ec.sasf.ms_comp_prueba_freya_lopez.web.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class LikeDTO {
    private Long id;
    private LocalDateTime createdAt;
    private Long senderId;
    private Long receiverId;
}
