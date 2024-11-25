package com.iot.service;

import com.iot.model.Usuario;
import com.iot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Serviço responsável por operações relacionadas a Usuário.
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor que injeta o PasswordEncoder.
     *
     * @param passwordEncoder Codificador de senhas.
     */
    @Autowired
    public UsuarioService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo usuário com a senha codificada.
     *
     * @param usuario Objeto Usuário a ser criado.
     * @return Mono<Usuario> Usuário criado.
     */
    public Mono<Usuario> criarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    /**
     * Busca um usuário por email e senha.
     *
     * @param email Email do usuário.
     * @param senha Senha do usuário.
     * @return Mono<Usuario> Usuário encontrado.
     */
    public Mono<Usuario> buscarPorEmailESenha(String email, String senha) {
        return usuarioRepository.findByEmail(email)
            .filter(usuario -> passwordEncoder.matches(senha, usuario.getSenha()));
    }

    /**
     * Busca um usuário por email.
     *
     * @param email Email do usuário.
     * @return Mono<Usuario> Usuário encontrado.
     */
    public Mono<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Busca um usuário por ID.
     *
     * @param id ID do usuário.
     * @return Mono<Usuario> Usuário encontrado.
     */
    public Mono<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Deleta um usuário pelo ID.
     *
     * @param id ID do usuário.
     * @return Mono<Void> Indica a conclusão da operação.
     */
    public Mono<Void> deletarUsuario(Long id) {
        return usuarioRepository.deleteById(id);
    }
}
