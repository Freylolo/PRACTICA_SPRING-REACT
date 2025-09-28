package ec.sasf.ms_comp_prueba_freya_lopez.persistence.repository;

import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.NotificationEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByReceiver(UserEntity receiver);

    List<NotificationEntity> findByStatus(String status);
}