import '../../css/sala.css';

let graficoConsumo;

document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    const salaId = params.get('id');
    const userId = sessionStorage.getItem('ID'); // Obtém o ID do usuário
    const token = sessionStorage.getItem('token');

    if (salaId) {
        // Faz a requisição para obter o estado inicial da sala
        fetch(`http://backend:8080/salas/${salaId}/estado`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao carregar estado da sala');
            }
            return response.text(); // Recebe o estado como texto
        })
        .then(estado => {
            // Define o botão inicial com base no estado recebido
            switch (estado.toLowerCase()) {
                case 'acender':
                    selecionarOpcao(1);
                    break;
                case 'automatico':
                    selecionarOpcao(2);
                    break;
                case 'apagar':
                    selecionarOpcao(3);
                    break;
                default:
                    console.warn('Estado desconhecido:', estado);
            }
        })
        .catch(error => console.error('Erro ao carregar estado da sala:', error));

        // Carrega os dados gerais da sala
        fetch(`http://backend:8080/salas/${salaId}`, {
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

            return fetch(`http://backend:8080/usuarios/${sala.idCriador}`, {
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

    // Adiciona event listeners ao botão e elementos de período
    const btnEnviar = document.getElementById('btnEnviar');
    const periodoRadios = document.querySelectorAll('input[name="consumo"]');
    const periodoSelect = document.getElementById('periodo');

    // Função para capturar o período selecionado e enviar a requisição
    function capturarPeriodoEEnviarRequisicao() {
        let inicio;
        let fim = new Date().toISOString(); // Data de término é agora em formato ISO
        const agora = new Date();
        const valorSelecionado = document.querySelector('input[name="consumo"]:checked').value;

        switch (valorSelecionado) {
            case '24 horas':
                inicio = new Date(agora.getTime() - 24 * 60 * 60 * 1000).toISOString(); // 24 horas atrás
                break;
            case '1 semana':
                inicio = new Date(agora.getTime() - 7 * 24 * 60 * 60 * 1000).toISOString(); // 1 semana atrás
                break;
            case '1 mês':
                inicio = new Date(agora.setMonth(agora.getMonth() - 1)).toISOString(); // 1 mês atrás
                break;
            case 'outro':
                const outroPeriodo = parseInt(document.getElementById('outro').value, 10);
                const unidadePeriodo = periodoSelect.value;
                if (isNaN(outroPeriodo) || outroPeriodo <= 0) {
                    alert('Por favor, insira um valor válido.');
                    return;
                }

                // Calcula o início baseado na unidade selecionada
                if (unidadePeriodo === 'hora') {
                    inicio = new Date(agora.getTime() - outroPeriodo * 60 * 60 * 1000).toISOString();
                } else if (unidadePeriodo === 'dia') {
                    inicio = new Date(agora.getTime() - outroPeriodo * 24 * 60 * 60 * 1000).toISOString();
                } else if (unidadePeriodo === 'semana') {
                    inicio = new Date(agora.getTime() - outroPeriodo * 7 * 24 * 60 * 60 * 1000).toISOString();
                } else if (unidadePeriodo === 'mes') {
                    inicio = new Date(agora.setMonth(agora.getMonth() - outroPeriodo)).toISOString();
                }
                break;
            default:
                alert('Selecione um período válido.');
                return;
        }

        // Faz a requisição ao backend
        fetch(`http://backend:8080/medidas/periodo?idSala=${salaId}&inicio=${encodeURIComponent(inicio)}&fim=${encodeURIComponent(fim)}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })        
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao carregar medidas');
            }
            return response.json();
        })
        .then(dados => {
            // Manipula os dados recebidos e gera o gráfico
            const labels = dados.map(d => new Date(d.horario).toLocaleTimeString());
            const valores = dados.map(d => d.valor);

            const ctx = document.getElementById('graficoConsumo').getContext('2d');

            if (graficoConsumo) {
                graficoConsumo.destroy();
            }

            graficoConsumo = new Chart(ctx, {
                type: 'line', // Tipo de gráfico
                data: {
                    labels: labels, // Horários como labels
                    datasets: [{
                        label: 'Consumo de energia [W]',
                        data: valores, // Valores do sensor
                        borderColor: 'rgba(192, 135, 75, 1)',
                        borderWidth: 2,
                        pointRadius: 0
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        })
        .catch(error => {
            console.error('Erro ao carregar medidas:', error);
        });
    }

    // Adiciona o event listener ao botão
    btnEnviar.addEventListener('click', capturarPeriodoEEnviarRequisicao);
});

function selecionarOpcao(opcao) {
    const botoes = document.querySelectorAll('.triplo-botao');
    const salaId = new URLSearchParams(window.location.search).get('id'); 
    const token = sessionStorage.getItem('token'); 

    let messageContent;
    switch (opcao) {
        case 1: 
            messageContent = 'ACENDER';
            break;
        case 2: 
            messageContent = 'AUTOMATICO';
            break;
        case 3: 
            messageContent = 'APAGAR';
            break;
    }

    botoes.forEach((botao, index) => {
        if (index === opcao - 1) {
            botao.classList.add('ativo');
        } else {
            botao.classList.remove('ativo');
        }
    });

    const requestBody = {
        mensagem: messageContent
    };

    fetch(`http://backend:8080/salas/${salaId}/estado`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
    .then(response => {
        if (!response.ok) {
            console.error("Erro ao enviar mensagem:", response.status, response.statusText);
        }
    })
    .catch(error => {
        console.error("Erro na requisição:", error);
    });
}

function excluirSala(salaId) {
    const token = sessionStorage.getItem('token');

    fetch(`http://backend:8080/salas/${salaId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.status === 200) {
            alert('Sala excluída com sucesso');
            window.location.href = 'listagem-salas.html';
        } else if (response.status === 403) {
            return response.text().then(text => alert(text));
        } else if (response.status === 404) {
            alert('Sala não encontrada');
        } else {
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

    fetch(`http://backend:8080/inscricoes/usuario/sala/${salaId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            window.location.href = "listagem-salas.html";
        } else {
            console.error("Erro ao cancelar a inscrição:", response.status, response.statusText);
        }
    })
    .catch(error => {
        console.error("Erro na requisição:", error);
    });
}

function voltarParaListagem() {
    window.location.href = "listagem-salas.html";
}

window.selecionarOpcao = selecionarOpcao;
window.excluirSala = excluirSala;
window.cancelarInscricao = cancelarInscricao;
window.voltarParaListagem = voltarParaListagem;