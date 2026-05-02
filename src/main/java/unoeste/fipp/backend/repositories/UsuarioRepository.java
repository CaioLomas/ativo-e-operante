package unoeste.fipp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unoeste.fipp.backend.entities.Denuncia;
import unoeste.fipp.backend.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
}
