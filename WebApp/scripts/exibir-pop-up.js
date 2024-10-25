const openPopupBtn = document.getElementById('openPopupBtn');
const criarSalaPopup = document.getElementById('criarSalaPopup');
const criarSalaContent = document.querySelector('.criar-sala-content');
const inscreverSalaContent = document.querySelector('.inscrever-sala-content');
const salaContainer = document.getElementById('salaContainer');

// Exibe o popup ao clicar no botão de abrir
openPopupBtn.addEventListener('click', () => {
  criarSalaPopup.style.display = 'block';
});

// Fecha o popup
function closePopup() {
  criarSalaPopup.style.display = 'none';
}

// Alterna entre o conteúdo de criar sala e inscrever-se
function changeContent(contentId) {
  if (contentId === 'criar-sala-content') {
    criarSalaContent.classList.add('show');
    inscreverSalaContent.classList.remove('show');
  } else if (contentId === 'inscrever-sala-content') {
    inscreverSalaContent.classList.add('show');
    criarSalaContent.classList.remove('show');
  }
}

// Função para buscar e exibir as inscrições do usuário
function fetchInscricoesUsuario() {
  const token = sessionStorage.getItem('token'); // Recupera o token JWT

  // Salva o botão add-card
  const addCardButton = document.querySelector('.add-card');
  const criarSalaPopupDiv = document.querySelector('.popup');
  const responseModalDiv = document.querySelector('.modal');

  // Limpa o container antes de adicionar novos cards
  salaContainer.innerHTML = '';

  fetch('http://localhost:8080/inscricoes/usuario', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok ' + response.statusText);
    }
    return response.json(); // Converte a resposta para JSON
  })
  .then(data => {
    data.forEach(inscricao => {
      criarCardSala(inscricao);
    });

    // Adiciona o botão add-card de volta ao container
    salaContainer.appendChild(addCardButton);
    salaContainer.appendChild(criarSalaPopupDiv);
    salaContainer.appendChild(responseModalDiv);

  })
  .catch(error => {
    console.error('Error ao buscar inscrições:', error); // Lida com erros
  });
}


// Função para criar um card de sala
function criarCardSala(inscricao) {
  const token = sessionStorage.getItem('token'); // Recupera o token JWT

  fetch(`http://localhost:8080/salas/${inscricao.idSala}`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Erro ao obter dados da sala: ' + response.statusText);
    }
    return response.json();
  })
  .then(sala => {
    // Cria o elemento de card
    const card = document.createElement('div');
    card.classList.add('card');

    // Cria o conteúdo do card com o nome da sala obtido
    card.innerHTML = `
      <h2 class="card-title">${sala.nome}</h2>
      <p><b>ID:</b> ${sala.id}</p>
      <p><b>Status:</b> Livre</p>
      <p><b>ID do Criador:</b> ${sala.idCriador}</p>
      <a href="sala.html">Ver mais</a>
    `;

    // Adiciona o card ao container
    salaContainer.insertBefore(card, document.querySelector('.add-card'));
  })
  .catch(error => {
    console.error('Erro ao obter dados da sala:', error);
  });
}

// Função para carregar as inscrições e exibir as salas
function carregarInscricoes() {
  const token = sessionStorage.getItem('token');

  fetch('http://localhost:8080/inscricoes', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ idSala: idSala }),
  }).then(response => response.json())
    .then(inscricoes => {
      inscricoes.forEach(inscricao => {
        criarCardSala(inscricao);
      });
    })
    .catch(error => {
      console.error('Erro ao carregar inscrições:', error);
    });
}

// Inicializa o carregamento de inscrições
document.addEventListener('DOMContentLoaded', carregarInscricoes);

// Carrega as inscrições do usuário ao abrir a página
window.addEventListener('load', fetchInscricoesUsuario);

// Configurações para o modal de criação e inscrição de sala
document.querySelector('.criar-sala-content .btn').addEventListener('click', () => {
  const nomeSala = document.getElementById('nomeSala').value;
  const token = sessionStorage.getItem('token');

  fetch('http://localhost:8080/salas', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ nome: nomeSala }),
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok ' + response.statusText);
    }
    closePopup();
    return response.json();
  })
  .then(data => {
    document.getElementById('modalId').innerText = data.id;
    document.getElementById('modalNome').innerText = data.nome;
    document.getElementById('modalIdCriador').innerText = data.idCriador;
    document.getElementById('responseModal').style.display = 'block';
        
  })
  .catch(error => {
    console.error('Error:', error);
  });
});

// Configuração para o botão de inscrição
document.querySelector('.inscrever-sala-content .btn').addEventListener('click', () => {
  const idSala = document.getElementById('idSala').value;
  const token = sessionStorage.getItem('token');

  fetch('http://localhost:8080/inscricoes', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ idSala: idSala }),
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok ' + response.statusText);
    }
    closePopup();
    return response.json();
  })
  .then(data => {
    alert('Inscrição realizada com sucesso!');
  })
  .catch(error => {
    console.error('Error:', error);
    alert('Erro ao realizar a inscrição: ' + error.message);
  });
});

// Fecha o modal ao clicar no botão "Ok" ou no "X"
document.getElementById('closeModal').addEventListener('click', () => {
  document.getElementById('responseModal').style.display = 'none';
  fetchInscricoesUsuario();
  window.location.reload();
});
