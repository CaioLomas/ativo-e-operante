package unoeste.fipp.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unoeste.fipp.backend.entities.Denuncia;
import unoeste.fipp.backend.entities.Orgao;
import unoeste.fipp.backend.entities.Tipo;
import unoeste.fipp.backend.entities.Usuario;
import unoeste.fipp.backend.repositories.DenunciaRepository;
import unoeste.fipp.backend.repositories.OrgaoRepository;
import unoeste.fipp.backend.repositories.TipoRepository;
import unoeste.fipp.backend.repositories.UsuarioRepository;
import unoeste.fipp.backend.restcontrollers.CidadaoRestController;

import java.time.LocalDate;
import java.util.List;

@Service

public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private OrgaoRepository orgaoRepository;

    @Autowired
    private TipoRepository tipoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Denuncia criaDenuncia(CidadaoRestController.DenunciaDTO denunciaDTO){

        Orgao orgao = orgaoRepository.getReferenceById(denunciaDTO.org_id());
        Tipo tipo = tipoRepository.getReferenceById(denunciaDTO.tip_id());
        Usuario usuario = usuarioRepository.getReferenceById(denunciaDTO.usu_id());

        Denuncia denuncia = new Denuncia(
                denunciaDTO.titulo(),
                denunciaDTO.texto(),
                denunciaDTO.urgencia(),
                LocalDate.now(),
                orgao,
                tipo,
                usuario
        );

        return denunciaRepository.save(denuncia);
    }

    //ID PRECISA EXISTIR, é o ID que estou logado, não tem necessidade de validação aqui
    public List<Denuncia> consultaDenunciasCidadao(Long id){

        return denunciaRepository.findByUsuarioId(id);
    }

    public List<Denuncia> consultaDenuncias(String titulo){

        if(titulo != null || titulo.trim().isEmpty())
            return denunciaRepository.findAll();

        return denunciaRepository.findByTituloIsContainingIgnoreCase(titulo);
    }

    public boolean deletarDenuncia(Long id){

        Denuncia exists = denunciaRepository.findById(id).orElse(null);

        if(exists != null){

            denunciaRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
