package unoeste.fipp.backend.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unoeste.fipp.backend.entities.Orgao;
import unoeste.fipp.backend.services.OrgaoService;
import unoeste.fipp.backend.services.TipoService;

import java.util.List;

@RestController
@RequestMapping("/adm")
public class AdmRestController {
    //endpoints:
    //CRUD de tipo de problema e orgão competente
    //Listar denúncias
    //Excluir denúncia
    //Registrar feedback em denúncia

    @Autowired
    private OrgaoService orgaoService;
    @Autowired
    private TipoService tipoService;

    public record OrgaoDTO(String nome){}
//    public record OrgaoResponseDTO(Long id, String nome){}

    @PostMapping("/orgao")
    public ResponseEntity<Orgao> postOrgao(@RequestBody OrgaoDTO orgaoDTO){

        Orgao novoOrgao = new Orgao(orgaoDTO.nome());

        novoOrgao = orgaoService.criaOrgao(novoOrgao);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoOrgao);
    }

    @GetMapping("/orgao/list")
    public ResponseEntity<List<Orgao>> getOrgaos(@RequestParam(required = false) String nome){

        List<Orgao> orgaos;

        orgaos = orgaoService.consultaOrgaos(nome);

        return ResponseEntity.status(HttpStatus.OK).body(orgaos);
    }

    @PutMapping("/orgao/{id}")
    public ResponseEntity<Orgao> putOrgaos(@PathVariable Long id, @RequestBody OrgaoDTO orgaoDTO){

        Orgao novoOrgao = new Orgao(id,orgaoDTO.nome());

        novoOrgao = orgaoService.alterarOrgao(novoOrgao);

        return ResponseEntity.status(HttpStatus.OK).body(novoOrgao);
    }

    @DeleteMapping("orgao/{id}")
    public ResponseEntity<Boolean> deleteOrgaos(@PathVariable Long id){

        boolean found = orgaoService.deletarOrgao(id);

        return found ? ResponseEntity.status(HttpStatus.OK).body(found) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(found);
    }

}
