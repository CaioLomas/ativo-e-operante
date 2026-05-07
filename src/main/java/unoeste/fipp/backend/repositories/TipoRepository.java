package unoeste.fipp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unoeste.fipp.backend.entities.Denuncia;
import unoeste.fipp.backend.entities.Orgao;
import unoeste.fipp.backend.entities.Tipo;

import java.util.List;

@Repository
public interface TipoRepository extends JpaRepository<Tipo,Long> {

    public List<Tipo> findByNomeContainingIgnoreCase(String nome);
}
