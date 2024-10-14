package com.iot.controller;

import com.iot.model.Usuario;
import com.iot.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Mono<ResponseEntity<Usuario>> createUsuario(@RequestBody Usuario usuario) {
        return usuarioService.criarUsuario(usuario)
                .map(novoUsuario -> ResponseEntity.ok(novoUsuario));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Usuario>> getUsuarioById(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(usuario -> ResponseEntity.ok(usuario))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Usuario>> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.buscarPorId(id)
                .flatMap(existingUsuario -> {
                    existingUsuario.setEmail(usuario.getEmail());
                    existingUsuario.setNome(usuario.getNome());
                    // Não atualizar a senha aqui a menos que esteja claro que é intencional
                    return usuarioService.criarUsuario(existingUsuario);
                })
                .map(updatedUsuario -> ResponseEntity.ok(updatedUsuario))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUsuario(@PathVariable Long id) {
        return usuarioService.deletarUsuario(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}
