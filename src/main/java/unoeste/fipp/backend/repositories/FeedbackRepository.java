package unoeste.fipp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unoeste.fipp.backend.entities.Denuncia;
import unoeste.fipp.backend.entities.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

    public boolean existsByDenunciaId(Long id);
}
