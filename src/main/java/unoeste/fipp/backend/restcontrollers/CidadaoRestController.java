package unoeste.fipp.backend.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unoeste.fipp.backend.entities.Denuncia;
import unoeste.fipp.backend.entities.Orgao;
import unoeste.fipp.backend.entities.Tipo;
import unoeste.fipp.backend.services.*;

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

    @Autowired
    private ImagensService imagensService;

    public record DenunciaDTO(
            String titulo,
            String texto,
            int urgencia,
            Long org_id,
            Long tip_id,
            Long usu_id
    ){}

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

    /*      ENDPOINTS DENUNCIA      */

    @PostMapping(value = "/denuncia", consumes = {"multipart/form-data"})
    public ResponseEntity<Denuncia> postDenuncia(

            @RequestPart("dados") DenunciaDTO denunciaDTO,

            @RequestPart(value = "fotos", required = false) List<MultipartFile> fotos
    )
    {

        Denuncia novaDenuncia = denunciaService.criaDenuncia(denunciaDTO);

        imagensService.salvarImagens(fotos, novaDenuncia);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaDenuncia);
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
