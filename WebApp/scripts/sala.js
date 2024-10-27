document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    const salaId = params.get('id');
    const userId = sessionStorage.getItem('ID'); // Obtém o ID do usuário
    const token = sessionStorage.getItem('token');

    if (salaId) {
        fetch(`http://localhost:8080/salas/${salaId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao carregar dados da sala');
            }
            return response.json();
        })
        .then(sala => {
            document.getElementById('salaTitulo').textContent = `Sala ${sala.nome}`;
            document.getElementById('salaId').textContent = `ID: ${sala.id}`;

            // Verifica se o usuário é o criador da sala
            if (sala.idCriador === parseInt(userId, 10)) {
                document.getElementById('btnExcluir').style.display = 'block';
                document.getElementById('btnExcluir').onclick = () => excluirSala(sala.id);
            } else {
                document.getElementById('btnCancelarInscricao').style.display = 'block';
                document.getElementById('btnCancelarInscricao').onclick = () => cancelarInscricao(sala.id);
            }

            return fetch(`http://localhost:8080/usuarios/${sala.idCriador}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao carregar dados do criador da sala');
            }
            return response.json();
        })
        .then(usuario => {
            document.getElementById('salaCriador').textContent = `Criador: ${usuario.nome}`;
        })
        .catch(error => console.error('Erro ao carregar dados:', error));
    }
});

function excluirSala(salaId) {
    const token = sessionStorage.getItem('token');
    console.log(salaId);

    fetch(`http://localhost:8080/salas/${salaId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.status === 200) {
            // Sucesso na exclusão, redireciona para a página de listagem
            alert('Sala excluída com sucesso');
            window.location.href = 'listagem-salas.html';
        } else if (response.status === 403) {
            // O usuário não é autorizado a excluir esta sala
            return response.text().then(text => alert(text));
        } else if (response.status === 404) {
            // Sala não encontrada
            alert('Sala não encontrada');
        } else {
            // Outro erro
            alert('Ocorreu um erro ao tentar excluir a sala');
        }
    })
    .catch(error => {
        console.error('Erro na requisição:', error);
        alert('Erro ao tentar excluir a sala');
    });
}

function cancelarInscricao(salaId) {

    const userId = sessionStorage.getItem('id');
    const token = sessionStorage.getItem('token');

    fetch(`http://localhost:8080/inscricoes/usuario/sala/${salaId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`, // Adiciona o token JWT no cabeçalho de autorização
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            console.log("Inscrição cancelada com sucesso.");
            window.location.href = "listagem-salas.html";
        } else {
            console.error("Erro ao cancelar a inscrição:", response.status, response.statusText);
        }
    })
    .catch(error => {
        console.error("Erro na requisição:", error);
    });
}