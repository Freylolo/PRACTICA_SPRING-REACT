package ec.sasf.ms_comp_prueba_freya_lopez.persistence.repository;

import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.ErrorLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLogEntity, Long> {
}
