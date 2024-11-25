package com.iot.controller;

import com.iot.model.Usuario;
import com.iot.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controlador REST para gerenciar operações relacionadas a usuários.
 * Mapeia requisições HTTP para os métodos correspondentes.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Construtor para injetar a dependência do serviço de usuário.
     * 
     * @param usuarioService Serviço de usuário
     */
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Cria um novo usuário.
     * 
     * @param usuario Objeto usuário a ser criado
     * @return Mono contendo a resposta HTTP com o usuário criado
     */
    @PostMapping
    public Mono<ResponseEntity<Usuario>> createUsuario(@RequestBody Usuario usuario) {
        return usuarioService.criarUsuario(usuario)
                .map(novoUsuario -> ResponseEntity.ok(novoUsuario));
    }

    /**
     * Busca um usuário pelo ID.
     * 
     * @param id ID do usuário a ser buscado
     * @return Mono contendo a resposta HTTP com o usuário encontrado ou 404 se não encontrado
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Usuario>> getUsuarioById(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(usuario -> ResponseEntity.ok(usuario))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Atualiza um usuário existente.
     * 
     * @param id ID do usuário a ser atualizado
     * @param usuario Objeto usuário com os novos dados
     * @return Mono contendo a resposta HTTP com o usuário atualizado ou 404 se não encontrado
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Usuario>> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.buscarPorId(id)
                .flatMap(existingUsuario -> {
                    existingUsuario.setEmail(usuario.getEmail());
                    existingUsuario.setNome(usuario.getNome());
                    return usuarioService.criarUsuario(existingUsuario);
                })
                .map(updatedUsuario -> ResponseEntity.ok(updatedUsuario))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Deleta um usuário pelo ID.
     * 
     * @param id ID do usuário a ser deletado
     * @return Mono contendo a resposta HTTP com status 204 (No Content) ou 404 se não encontrado
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteUsuario(@PathVariable Long id) {
        return usuarioService.deletarUsuario(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}
