package unoeste.fipp.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unoeste.fipp.backend.entities.Denuncia;
import unoeste.fipp.backend.entities.Feedback;
import unoeste.fipp.backend.repositories.DenunciaRepository;
import unoeste.fipp.backend.repositories.FeedbackRepository;

@Service

public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private DenunciaRepository denunciaRepository;

    public Feedback criaFeedback(Long id, String texto){

        if(feedbackRepository.existsByDenunciaId(id)) throw new IllegalArgumentException("Já existe um feedback para a denúncia com o ID {"+id+"}");

        Denuncia denuncia = denunciaRepository.findById(id).orElse(null);

        if(denuncia == null) throw new IllegalArgumentException("Nenhuma denúncia encontrada com o ID {"+id+"}");

        Feedback novoFeedback = new Feedback(texto,denuncia);

        return feedbackRepository.save(novoFeedback);
    }
}
