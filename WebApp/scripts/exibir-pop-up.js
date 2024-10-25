const openPopupBtn = document.getElementById('openPopupBtn');
const criarSalaPopup = document.getElementById('criarSalaPopup');
const criarSalaContent = document.querySelector('.criar-sala-content');
const inscreverSalaContent = document.querySelector('.inscrever-sala-content');

openPopupBtn.addEventListener('click', () => {
  criarSalaPopup.style.display = 'block';
});

function closePopup() {
  criarSalaPopup.style.display = 'none';
}

function changeContent(contentId) {
  if (contentId === 'criar-sala-content') {
    criarSalaContent.classList.add('show');
    inscreverSalaContent.classList.remove('show');
  } else if (contentId === 'inscrever-sala-content') {
    inscreverSalaContent.classList.add('show');
    criarSalaContent.classList.remove('show');
  }
}

document.querySelector('.criar-sala-content .btn').addEventListener('click', () => {
  // Obtém o nome da sala do input
  const nomeSala = document.getElementById('nomeSala').value;

  // Obtém o token JWT do sessionStorage
  const token = sessionStorage.getItem('token');

  // Configura a requisição
  fetch('http://localhost:8080/salas', {
      method: 'POST',
      headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
      },
      body: JSON.stringify({ nome: nomeSala }), // Envia o nome da sala como JSON
  })
  .then(response => {
      if (!response.ok) {
          throw new Error('Network response was not ok ' + response.statusText);
      }
      closePopup();
      return response.json(); // Converte a resposta para JSON
  })
  .then(data => {
      //console.log('Success:', data); // Lida com a resposta de sucesso
      // Exibe o modal com os detalhes da sala
      document.getElementById('modalId').innerText = data.id;
      document.getElementById('modalNome').innerText = data.nome;
      document.getElementById('modalIdCriador').innerText = data.idCriador;

      // Mostra o modal
      document.getElementById('responseModal').style.display = 'block';
  })
  .catch(error => {
      console.error('Error:', error); // Lida com erros
  });
});

// Fecha o modal quando o botão "Ok" ou o X for clicado
document.getElementById('closeModal').addEventListener('click', () => {
  document.getElementById('responseModal').style.display = 'none';
});

// Adiciona o listener para o botão de inscrever-se
document.querySelector('.inscrever-sala-content .btn').addEventListener('click', () => {
  // Obtém o ID da sala do input
  const idSala = document.getElementById('idSala').value;

  // Obtém o token JWT do sessionStorage
  const token = sessionStorage.getItem('token');

  // Configura a requisição
  fetch('http://localhost:8080/inscricoes', {
      method: 'POST',
      headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
      },
      body: JSON.stringify({ idSala: idSala }), // Envia o ID da sala como JSON
  })
  .then(response => {
      if (!response.ok) {
          throw new Error('Network response was not ok ' + response.statusText);
      }
      closePopup(); // Fecha o popup após a inscrição
      return response.json(); // Converte a resposta para JSON
  })
  .then(data => {
      console.log('Success:', data); // Lida com a resposta de sucesso
      // Você pode adicionar lógica para exibir um modal de sucesso ou notificação
      alert('Inscrição realizada com sucesso!'); // Exemplo de notificação
  })
  .catch(error => {
      console.error('Error:', error); // Lida com erros
      alert('Erro ao realizar a inscrição: ' + error.message); // Notifica o erro
  });
});

