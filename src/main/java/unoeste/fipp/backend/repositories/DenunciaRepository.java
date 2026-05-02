package unoeste.fipp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unoeste.fipp.backend.entities.Denuncia;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia,Long> {
}
