package unoeste.fipp.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unoeste.fipp.backend.entities.Orgao;
import unoeste.fipp.backend.repositories.OrgaoRepository;

import java.util.List;

@Service

public class OrgaoService {

    @Autowired
    private OrgaoRepository orgaoRepository;

    public Orgao criaOrgao(Orgao novoOrgao){

        if(novoOrgao.getNome() == null || novoOrgao.getNome().trim().isEmpty())
            return null;

        return orgaoRepository.save(novoOrgao);
    }

    public List<Orgao> consultaOrgaos(String nome){

        if(nome == null || nome.trim().isEmpty())
            return orgaoRepository.findAll();

        return orgaoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Orgao alterarOrgao(Orgao novoOrgao){

        orgaoRepository.save(novoOrgao);

        if(novoOrgao.getNome() == null || novoOrgao.getNome().trim().isEmpty())
            return null;

        return orgaoRepository.save(novoOrgao);
    }

    public boolean deletarOrgao(Long id){

        Orgao exists = orgaoRepository.findById(id).orElse(null);

        if(exists != null){

            orgaoRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
