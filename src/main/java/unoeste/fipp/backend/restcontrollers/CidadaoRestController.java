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
@RequestMapping("/cidadao")
public class CidadaoRestController {
    //endpoints
    //Criar denuncia

    @Autowired
    private OrgaoService orgaoService;

    @Autowired
    private TipoService tipoService;

    @Autowired
    private DenunciaService denunciaService;

    @Autowired
    private FeedbackService feedbackService;

    public record FeedbackResponse(String texto){}

    @GetMapping("/orgao/list")
    public ResponseEntity<List<Orgao>> getOrgaos(@RequestParam(required = false) String nome){

        List<Orgao> orgaos;

        orgaos = orgaoService.consultaOrgaos(nome);

        return ResponseEntity.status(HttpStatus.OK).body(orgaos);
    }

    @GetMapping("/tipo/list")
    public ResponseEntity<List<Tipo>> getTipos(@RequestParam(required = false) String nome){

        List<Tipo> tipos;

        tipos = tipoService.consultaTipos(nome);

        return ResponseEntity.status(HttpStatus.OK).body(tipos);
    }

    @GetMapping("/denuncia/list/{id}") // CORRIGIR QUANDO IMPLEMENTAR O JWT, POIS AQUI O ACESSO ESTÁ PÚBLICO PARA QUALQUER ID COLOCADO
    public ResponseEntity<List<Denuncia>> getDenunciasCidadao(@PathVariable Long id){

        List<Denuncia> denuncias;

        denuncias = denunciaService.consultaDenunciasCidadao(id);

        return ResponseEntity.status(HttpStatus.OK).body(denuncias);
    }

    @GetMapping("/denuncia/{id}/feedback")
    public ResponseEntity<String> getFeedback(@PathVariable Long id){

        return ResponseEntity.status(HttpStatus.OK).body(feedbackService.consultaFeedbackByDenuncia(id));
    }
}
