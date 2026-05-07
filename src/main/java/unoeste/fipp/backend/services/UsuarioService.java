package unoeste.fipp.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unoeste.fipp.backend.entities.Usuario;
import unoeste.fipp.backend.repositories.UsuarioRepository;

@Service

public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // NIVEL 1 = ADMINISTRADOR, NIVEL 2 = USUARIO

    public Usuario criaUsuario(String cpf,String email,String senha){

        Usuario novoUsuario = new Usuario(cpf,email,senha,2);

        if(cpf == null || cpf.trim().isEmpty()) // validar com regex no javascript depois?
            throw new IllegalArgumentException("CPF nulo ou vazio");

        if(email == null || email.trim().isEmpty()) // validar com regex no javascript depois?
            throw new IllegalArgumentException("Email nulo ou vazio");

        if(senha == null || senha.trim().isEmpty()) // criptografar com bcypt depois?
            throw new IllegalArgumentException("Senha nula ou vazia");

        return usuarioRepository.save(novoUsuario);
    }
}
