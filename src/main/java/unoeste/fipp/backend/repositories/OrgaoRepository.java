package unoeste.fipp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unoeste.fipp.backend.entities.Denuncia;
import unoeste.fipp.backend.entities.Orgao;

@Repository
public interface OrgaoRepository extends JpaRepository<Orgao,Long> {
}
