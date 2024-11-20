import '../../css/listagem-salas.css';

import additionSymbol from '../../img/simb_mais.png';

document.getElementById('addition-symbol').src = additionSymbol;

const openPopupBtn = document.getElementById('openPopupBtn');
const criarSalaPopup = document.getElementById('criarSalaPopup');
const criarSalaContent = document.querySelector('.criar-sala-content');
const inscreverSalaContent = document.querySelector('.inscrever-sala-content');
const salaContainer = document.getElementById('salaContainer');

const criarSalaRadio = document.getElementById('criarSalaRadio');
const inscreverSalaRadio = document.getElementById('inscreverSalaRadio');
const closeResponseModalBtn = document.getElementById('closeResponseModalBtn');
const closePopupBtn = document.getElementById('closePopupBtn');

closeResponseModalBtn.addEventListener('click', closeResponseModal);
closePopupBtn.addEventListener('click', closePopup);
criarSalaRadio.addEventListener('change', () => changeContent('criar-sala-content'));
inscreverSalaRadio.addEventListener('change', () => changeContent('inscrever-sala-content'));


document.addEventListener('DOMContentLoaded', () => {
  // Adicione o event listener após a garantia de que o DOM está carregado
  document.querySelector('.inscrever-sala-content .btn')?.addEventListener('click', inscreverSala);
  document.querySelector('.criar-sala-content .btn')?.addEventListener('click', criarSala);

  // Fecha o modal ao clicar no botão "Ok" ou no "X"
  document.getElementById('closeModal')?.addEventListener('click', closeResponseModal);
  document.getElementById('closeBtn').addEventListener('click', showResponseModal); // Alterar para showResponseModal
});

// Mostra o modal de resposta e recarrega a página
function showResponseModal() {
  console.log("aaa");
  document.getElementById('responseModal').style.display = 'none';
  //fetchInscricoesUsuario();
  window.location.reload(); // Recarrega a página
}

// Função para fechar o modal de resposta
function closeResponseModal() {
  document.getElementById('responseModal').style.display = 'none';
}

// Evento para fechar o modal ao clicar no botão "Ok"
document.getElementById('closeBtn').addEventListener('click', closeResponseModal);

// Evento para fechar o modal ao clicar no "X"
document.querySelector('.modal .close-btn').addEventListener('click', closeResponseModal);

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

  fetch('http://backend:8080/inscricoes/usuario', {
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

  fetch(`http://backend:8080/salas/${inscricao.idSala}`, {
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
      <!--<p><b>Status:</b> Livre</p>-->
      <p><b>ID do Criador:</b> ${sala.idCriador}</p>
      <a href="sala.html?id=${sala.id}">Ver mais</a>
    `;


    // Adiciona o card ao container
    salaContainer.insertBefore(card, document.querySelector('.add-card'));
  })
  .catch(error => {
    console.error('Erro ao obter dados da sala:', error);
  });
}

// Carrega as inscrições do usuário ao abrir a página
window.addEventListener('load', fetchInscricoesUsuario);

// Configurações para o modal de criação e inscrição de sala
function criarSala() {
  const nomeSala = document.getElementById('nomeSala').value;
  const token = sessionStorage.getItem('token');

  fetch('http://backend:8080/salas', {
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

    // Exibe o modal com os detalhes da sala
    document.getElementById('modalId').innerText = data.sala.id;
    document.getElementById('modalNome').innerText = data.sala.nome;
    document.getElementById('modalIdCriador').innerText = data.sala.idCriador;

    // Itera sobre os sensores e exibe o tipo e o ID de cada um
    const sensoresInfo = data.sensores.map(sensor => {
        return `<b>ID Sensor de ${sensor.tipo.charAt(0).toUpperCase() + sensor.tipo.slice(1).toLowerCase()}:</b> ${sensor.id}`;
    }).join('<br>'); // Junta as informações em linhas separadas

    // Atualiza o elemento do modal com as informações dos sensores
    document.getElementById('modalSensorInfo').innerHTML = sensoresInfo;

    // Mostra o modal
    document.getElementById('responseModal').style.display = 'block';
  })
  .catch(error => {
    console.error('Error:', error);
  });
}

// Configuração para o botão de inscrição
function inscreverSala() {
  const idSala = document.getElementById('idSala').value;
  const token = sessionStorage.getItem('token');

  fetch('http://backend:8080/inscricoes', {
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
    // Exibe uma mensagem de sucesso
    alert('Inscrição realizada com sucesso!');
    
    //fetchInscricoesUsuario();
    window.location.reload(); 
  })
  .catch(error => {
    console.error('Error:', error);
    alert('Erro ao realizar a inscrição: ' + error.message);
  });
}

document.getElementById('logoutBtn').addEventListener('click', () => {
  sessionStorage.clear(); // Limpa todo o conteúdo do sessionStorage
  window.location.href = '/'; // Redireciona para a página de login
});

