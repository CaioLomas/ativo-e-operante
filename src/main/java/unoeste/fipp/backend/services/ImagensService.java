package unoeste.fipp.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unoeste.fipp.backend.entities.Denuncia;
import unoeste.fipp.backend.entities.Imagens;
import unoeste.fipp.backend.repositories.ImagensRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ImagensService {

    @Autowired
    private ImagensRepository imagensRepository;

    private final String DIR_RAIZ = System.getProperty("user.dir") + "/uploads/denuncias/";

    public void salvarImagens(List<MultipartFile> imagens, Denuncia denuncia) {

        if(imagens != null)
        {
            File diretorio = new File(DIR_RAIZ);
            if(!diretorio.exists()) diretorio.mkdirs();

            for (MultipartFile arquivo : imagens) {

                try {
                    if (arquivo.isEmpty()) continue;

                    String nomeUnico = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();

                    Path caminhoCerto = Paths.get(DIR_RAIZ + nomeUnico);

                    Files.write(caminhoCerto, arquivo.getBytes());

                    Imagens novaImagem = new Imagens(nomeUnico, denuncia);
                    imagensRepository.save(novaImagem);

                } catch (IOException e) {

                    throw new RuntimeException("Falha ao salvar a imagem no HD: " + e.getMessage());
                }
            }
        }
    }
}