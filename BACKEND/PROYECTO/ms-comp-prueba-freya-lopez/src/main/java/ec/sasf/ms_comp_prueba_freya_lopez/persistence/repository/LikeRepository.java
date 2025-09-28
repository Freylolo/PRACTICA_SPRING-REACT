package ec.sasf.ms_comp_prueba_freya_lopez.persistence.repository;

import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.LikeEntity;
import ec.sasf.ms_comp_prueba_freya_lopez.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    boolean existsBySenderAndReceiver(UserEntity sender, UserEntity receiver);

    Optional<LikeEntity> findBySenderAndReceiver(UserEntity sender, UserEntity receiver);
}