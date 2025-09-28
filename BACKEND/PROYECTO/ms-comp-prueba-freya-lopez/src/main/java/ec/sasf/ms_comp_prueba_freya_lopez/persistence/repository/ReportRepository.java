package ec.sasf.ms_comp_prueba_freya_lopez.persistence.repository;

import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.ReportEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository  extends JpaRepository<ReportEntity, Long> {

    List<ReportEntity> findByEmisor(UserEntity emisor);

    List<ReportEntity> findByDenunciado(UserEntity denunciado);

    List<ReportEntity> findByStatus(String status);
}