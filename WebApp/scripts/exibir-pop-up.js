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