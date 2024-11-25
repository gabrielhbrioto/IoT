import '../../css/listagem-salas.css';
import additionSymbol from '../../img/simb_mais.png';

// Define a imagem do símbolo de adição
document.getElementById('addition-symbol').src = additionSymbol;

// Seleciona elementos do DOM
const openPopupBtn = document.getElementById('openPopupBtn');
const criarSalaPopup = document.getElementById('criarSalaPopup');
const criarSalaContent = document.querySelector('.criar-sala-content');
const inscreverSalaContent = document.querySelector('.inscrever-sala-content');
const salaContainer = document.getElementById('salaContainer');

const criarSalaRadio = document.getElementById('criarSalaRadio');
const inscreverSalaRadio = document.getElementById('inscreverSalaRadio');
const closeResponseModalBtn = document.getElementById('closeResponseModalBtn');
const closePopupBtn = document.getElementById('closePopupBtn');

// Adiciona eventos aos botões
closeResponseModalBtn.addEventListener('click', closeResponseModal);
closePopupBtn.addEventListener('click', closePopup);
criarSalaRadio.addEventListener('change', () => changeContent('criar-sala-content'));
inscreverSalaRadio.addEventListener('change', () => changeContent('inscrever-sala-content'));

// Adiciona eventos após o DOM ser carregado
document.addEventListener('DOMContentLoaded', () => {
  document.querySelector('.inscrever-sala-content .btn')?.addEventListener('click', inscreverSala);
  document.querySelector('.criar-sala-content .btn')?.addEventListener('click', criarSala);
  document.getElementById('closeModal')?.addEventListener('click', closeResponseModal);
  document.getElementById('closeBtn').addEventListener('click', showResponseModal);
});

// Função para mostrar o modal de resposta
function showResponseModal() {
  console.log("aaa");
  document.getElementById('responseModal').style.display = 'none';
  window.location.reload();
}

// Função para fechar o modal de resposta
function closeResponseModal() {
  document.getElementById('responseModal').style.display = 'none';
}

// Adiciona eventos para fechar o modal
document.getElementById('closeBtn').addEventListener('click', closeResponseModal);
document.querySelector('.modal .close-btn').addEventListener('click', closeResponseModal);

// Abre o popup de criar sala
openPopupBtn.addEventListener('click', () => {
  criarSalaPopup.style.display = 'block';
});

// Função para fechar o popup
function closePopup() {
  criarSalaPopup.style.display = 'none';
}

// Função para trocar o conteúdo exibido
function changeContent(contentId) {
  if (contentId === 'criar-sala-content') {
    criarSalaContent.classList.add('show');
    inscreverSalaContent.classList.remove('show');
  } else if (contentId === 'inscrever-sala-content') {
    inscreverSalaContent.classList.add('show');
    criarSalaContent.classList.remove('show');
  }
}

// Função para buscar inscrições do usuário
function fetchInscricoesUsuario() {
  const token = sessionStorage.getItem('token');
  const addCardButton = document.querySelector('.add-card');
  const criarSalaPopupDiv = document.querySelector('.popup');
  const responseModalDiv = document.querySelector('.modal');

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
    return response.json();
  })
  .then(data => {
    data.forEach(inscricao => {
      criarCardSala(inscricao);
    });

    salaContainer.appendChild(addCardButton);
    salaContainer.appendChild(criarSalaPopupDiv);
    salaContainer.appendChild(responseModalDiv);

  })
  .catch(error => {
    console.error('Error ao buscar inscrições:', error);
  });
}

// Função para criar um card de sala
function criarCardSala(inscricao) {
  const token = sessionStorage.getItem('token');

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
    const card = document.createElement('div');
    card.classList.add('card');

    card.innerHTML = `
      <h2 class="card-title">${sala.nome}</h2>
      <p><b>ID:</b> ${sala.id}</p>
      <p><b>ID do Criador:</b> ${sala.idCriador}</p>
      <a href="sala.html?id=${sala.id}">Ver mais</a>
    `;

    salaContainer.insertBefore(card, document.querySelector('.add-card'));
  })
  .catch(error => {
    console.error('Erro ao obter dados da sala:', error);
  });
}

// Carrega as inscrições do usuário ao carregar a página
window.addEventListener('load', fetchInscricoesUsuario);

// Função para criar uma nova sala
function criarSala() {
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
    document.getElementById('modalId').innerText = data.sala.id;
    document.getElementById('modalNome').innerText = data.sala.nome;
    document.getElementById('modalIdCriador').innerText = data.sala.idCriador;

    const sensoresInfo = data.sensores.map(sensor => {
        return `<b>ID Sensor de ${sensor.tipo.charAt(0).toUpperCase() + sensor.tipo.slice(1).toLowerCase()}:</b> ${sensor.id}`;
    }).join('<br>');

    document.getElementById('modalSensorInfo').innerHTML = sensoresInfo;
    document.getElementById('responseModal').style.display = 'block';
  })
  .catch(error => {
    console.error('Error:', error);
  });
}

// Função para inscrever-se em uma sala
function inscreverSala() {
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
    return response.json();
  })
  .then(data => {
    alert('Inscrição realizada com sucesso!');
    window.location.reload(); 
  })
  .catch(error => {
    console.error('Error:', error);
    alert('Erro ao realizar a inscrição: ' + error.message);
  });
}

// Adiciona evento para logout
document.getElementById('logoutBtn').addEventListener('click', () => {
  sessionStorage.clear();
  window.location.href = '/';
});
