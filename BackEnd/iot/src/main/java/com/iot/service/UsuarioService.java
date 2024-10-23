package com.iot.service;

import com.iot.model.Usuario;
import com.iot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Injeta o PasswordEncoder ao invés de instanciar diretamente
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<Usuario> criarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha())); // Codifica a senha antes de salvar
        return usuarioRepository.save(usuario);
    }

    public Mono<Usuario> buscarPorEmailESenha(String email, String senha) {
        return usuarioRepository.findByEmail(email)
            .filter(usuario -> passwordEncoder.matches(senha, usuario.getSenha())); // Valida a senha após buscar pelo email
    }

    public Mono<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Mono<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Mono<Void> deletarUsuario(Long id) {
        return usuarioRepository.deleteById(id);
    }
}
