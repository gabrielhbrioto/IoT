package com.iot.service;

import com.iot.model.Usuario;
import com.iot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Mono<Usuario> criarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Mono<Usuario> buscarPorEmailESenha(String email, String senha) {
        System.out.printf("Buscando usuario pelo email %s...\n", email);
        return usuarioRepository.findByEmail(email)
            .filter(usuario -> passwordEncoder.matches(senha, usuario.getSenha())); // Valida a senha após buscar pelo email
    }

    public Mono<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Mono<Void> deletarUsuario(Long id) {
        return usuarioRepository.deleteById(id);
    }
}
