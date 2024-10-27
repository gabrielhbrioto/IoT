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

            console.log("sala.idCriador = " + sala.idCriador);
            console.log("parseInt(userId, 10) = " + parseInt(userId, 10));

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
    fetch(`http://localhost:8080/salas/${salaId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.ok) {
            alert('Sala excluída com sucesso!');
            // Redireciona ou atualiza a página
            window.location.href = 'caminho_para_a_pagina_de_salvar'; // Altere conforme necessário
        } else {
            throw new Error('Erro ao excluir a sala');
        }
    })
    .catch(error => console.error('Erro ao excluir sala:', error));
}

function cancelarInscricao(salaId) {
    const userId = sessionStorage.getItem('id');
    const token = sessionStorage.getItem('token');

    fetch(`http://localhost:8080/inscricoes/${salaId}/${userId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.ok) {
            alert('Inscrição cancelada com sucesso!');
            // Redireciona ou atualiza a página
            window.location.href = 'caminho_para_a_pagina_de_salvar'; // Altere conforme necessário
        } else {
            throw new Error('Erro ao cancelar a inscrição');
        }
    })
    .catch(error => console.error('Erro ao cancelar inscrição:', error));
}
