package ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class AuditableEntity {

    @CreationTimestamp
    @Column(name = "fe_creacion", updatable = false, nullable = false)
    private LocalDateTime feCreacion;

    @UpdateTimestamp
    @Column(name = "fe_actualizacion")
    private LocalDateTime feActualizacion;

    @Column(name = "estado", nullable = false)
    private String estado = "ACTIVO";
}
