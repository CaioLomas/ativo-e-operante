package unoeste.fipp.backend.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unoeste.fipp.backend.entities.Denuncia;
import unoeste.fipp.backend.entities.Feedback;
import unoeste.fipp.backend.entities.Orgao;
import unoeste.fipp.backend.entities.Tipo;
import unoeste.fipp.backend.services.DenunciaService;
import unoeste.fipp.backend.services.FeedbackService;
import unoeste.fipp.backend.services.OrgaoService;
import unoeste.fipp.backend.services.TipoService;

import java.util.List;

@RestController
@RequestMapping("/adm")
public class AdmRestController {

    @Autowired
    private OrgaoService orgaoService;

    @Autowired
    private TipoService tipoService;

    @Autowired
    private DenunciaService denunciaService;

    @Autowired
    private FeedbackService feedbackService;

    public record OrgaoDTO(String nome){}

    public record TipoDTO(String nome){}

    public record FeedbackDTO(String texto){}

    /*      CRUD DE ORGAOS       */

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
    public ResponseEntity<Orgao> putOrgao(@PathVariable Long id, @RequestBody OrgaoDTO orgaoDTO){

        Orgao novoOrgao = new Orgao(id,orgaoDTO.nome());

        novoOrgao = orgaoService.alterarOrgao(novoOrgao);

        return ResponseEntity.status(HttpStatus.OK).body(novoOrgao);
    }

    @DeleteMapping("orgao/{id}")
    public ResponseEntity<Boolean> deleteOrgao(@PathVariable Long id){

        boolean found = orgaoService.deletarOrgao(id);

        return found ? ResponseEntity.status(HttpStatus.OK).build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    /*      CRUD DE TIPOS       */

    @PostMapping("/tipo")
    public ResponseEntity<Tipo> postTipo(@RequestBody TipoDTO tipoDTO){

        Tipo novoTipo = new Tipo(tipoDTO.nome());

        novoTipo = tipoService.criaTipo(novoTipo);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoTipo);
    }

    @GetMapping("/tipo/list")
    public ResponseEntity<List<Tipo>> getTipos(@RequestParam(required = false) String nome){

        List<Tipo> tipos;

        tipos = tipoService.consultaTipos(nome);

        return ResponseEntity.status(HttpStatus.OK).body(tipos);
    }

    @PutMapping("/tipo/{id}")
    public ResponseEntity<Tipo> putTipo(@PathVariable Long id, @RequestBody TipoDTO tipoDTO){

        Tipo novoTipo = new Tipo(id,tipoDTO.nome());

        novoTipo = tipoService.alterarTipo(novoTipo);

        return ResponseEntity.status(HttpStatus.OK).body(novoTipo);
    }

    @DeleteMapping("tipo/{id}")
    public ResponseEntity<Boolean> deleteTipo(@PathVariable Long id){

        boolean found = tipoService.deletarTipo(id);

        return found ? ResponseEntity.status(HttpStatus.OK).build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /*      AÇÕES DENÚNCIAS       */

    @GetMapping("/denuncia/list")
    public ResponseEntity<List<Denuncia>> getDenuncias(@RequestParam(required = false) String titulo){

        List<Denuncia> denuncias;

        denuncias = denunciaService.consultaDenuncias(titulo);

        return ResponseEntity.status(HttpStatus.OK).body(denuncias);
    }

    @DeleteMapping("/denuncia/{id}")
    public ResponseEntity<Boolean> deleteDenuncia(@PathVariable Long id){

        boolean deleted = denunciaService.deletarDenuncia(id);

        return deleted ? ResponseEntity.status(HttpStatus.OK).build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/denuncia/{id}/feedback")
    public ResponseEntity<Feedback> postFeedback(@PathVariable Long id, @RequestBody FeedbackDTO feedbackDTO){

        Feedback novoFeedback = feedbackService.criaFeedback(id,feedbackDTO.texto());

        return ResponseEntity.status(HttpStatus.CREATED).body(novoFeedback);
    }
}
