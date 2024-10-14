package com.iot.service;

import com.iot.model.Usuario;
import com.iot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Mono<Usuario> criarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Mono<Usuario> buscarPorEmailESenha(String email, String senha) {
        return usuarioRepository.findByEmail(email)
                .filter(usuario -> usuario.getSenha().equals(senha)); // Valida a senha após buscar pelo email
    }

    public Mono<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Mono<Void> deletarUsuario(Long id) {
        return usuarioRepository.deleteById(id);
    }
}
