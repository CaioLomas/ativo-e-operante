package unoeste.fipp.backend.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unoeste.fipp.backend.entities.Usuario;
import unoeste.fipp.backend.services.UsuarioService;

@RestController
@RequestMapping("/acesso")
public class AcessoRestController {

    //@PostMapping("logar")
    //endpoint logar

    //@PostMapping("cadastrar")
    //endpoint cadastrar

    @Autowired
    private UsuarioService usuarioService;

    public record UsuarioDTO(String cpf,String email,String senha){}
    public record UsuarioResponse(Long id,String cpf,String email,int nivel){}

    @PostMapping("cadastrar")
    public ResponseEntity<UsuarioResponse> cadastro(@RequestBody UsuarioDTO usuarioDTO){

        Usuario novoUsuario = usuarioService.criaUsuario(usuarioDTO.cpf(),usuarioDTO.email(), usuarioDTO.senha());

        UsuarioResponse response = new UsuarioResponse(novoUsuario.getId(),novoUsuario.getCpf(),
                                        novoUsuario.getEmail(),novoUsuario.getNivel());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
