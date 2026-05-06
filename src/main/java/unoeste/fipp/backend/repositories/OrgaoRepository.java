package unoeste.fipp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unoeste.fipp.backend.entities.Orgao;

import java.util.List;

@Repository

public interface OrgaoRepository extends JpaRepository<Orgao,Long> {

//    @Query("SELECT o FROM Orgao o WHERE UPPER(o.nome) LIKE UPPER(CONCAT('%', :nome, '%'))")
//    List<Orgao> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    public List<Orgao> findByNomeContainingIgnoreCase(String nome);

//    @Modifying
//    @Query("UPDATE Orgao o SET o.nome = :nome WHERE o.id = :id")
//    public int updateOrgao(Long id,String nome);

//    @Modifying
//    @Query("DELETE FROM Orgao o WHERE o.id = :id")
//    public int deleteOrgao(Long id);
}
