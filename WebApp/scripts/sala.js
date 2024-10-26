document.addEventListener('DOMContentLoaded', () => {
    // Obtém o ID da sala da URL
    const params = new URLSearchParams(window.location.search);
    const salaId = params.get('id');

    if (salaId) {
        const token = sessionStorage.getItem('token');

        // Faz a requisição para obter os dados da sala
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
            // Preenche as informações da sala na página
            document.getElementById('salaTitulo').textContent = `Sala ${sala.nome}`;
            document.getElementById('salaId').textContent = `ID: ${sala.id}`;

            // Faz a requisição para obter o nome do criador da sala
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
            // Preenche as informações do criador da sala na página
            document.getElementById('salaCriador').textContent = `Criador: ${usuario.nome}`;
        })
        .catch(error => console.error('Erro ao carregar dados:', error));
    }
});
