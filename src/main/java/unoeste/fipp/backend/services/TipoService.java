package unoeste.fipp.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unoeste.fipp.backend.entities.Tipo;
import unoeste.fipp.backend.repositories.TipoRepository;

import java.util.List;

@Service

public class TipoService {

    @Autowired
    private TipoRepository tipoRepository;

    public Tipo criaTipo(Tipo novoTipo){

        if(novoTipo.getNome() == null || novoTipo.getNome().trim().isEmpty())
            return null;

        return tipoRepository.save(novoTipo);
    }

    public List<Tipo> consultaTipos(String nome){

        if(nome == null || nome.trim().isEmpty())
            return tipoRepository.findAll();

        return tipoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Tipo alterarTipo(Tipo novoTipo){

        tipoRepository.save(novoTipo);

        if(novoTipo.getNome() == null || novoTipo.getNome().trim().isEmpty())
            return null;

        return tipoRepository.save(novoTipo);
    }

    public boolean deletarTipo(Long id){

        Tipo exists = tipoRepository.findById(id).orElse(null);

        if(exists != null){

            tipoRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
