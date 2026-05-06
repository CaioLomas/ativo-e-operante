package unoeste.fipp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unoeste.fipp.backend.entities.Denuncia;

import java.util.List;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia,Long> {

    List<Denuncia> findByUsuarioId(Long id);
}
