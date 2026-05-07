package unoeste.fipp.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unoeste.fipp.backend.entities.Denuncia;
import unoeste.fipp.backend.repositories.DenunciaRepository;

import java.util.List;

@Service

public class DenunciaService {
    //save
    //delete
    //findOne
    //findAll(filtro)

    @Autowired
    private DenunciaRepository denunciaRepository;

//    public Denuncia consultaDenuncia(Long id){
//
//        return denunciaRepository.findById(id).orElse(null);
//    }

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
