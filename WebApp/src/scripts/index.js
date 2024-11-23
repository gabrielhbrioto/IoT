import '../../css/login.css';    

import loginImage from '../../img/login.png';

document.getElementById('login-image').src = loginImage;


document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Previne o envio padrão do formulário

    // Captura os valores dos campos de email e senha
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    // Realiza a requisição para o backend
    fetch(`http://localhost:8080/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email,
            senha: password // Note que o nome do campo deve corresponder ao esperado pelo backend
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao realizar login: ' + response.statusText);
        }
        return response.json(); // O token e o ID do usuário são retornados como JSON
    })
    .then(data => {
        if (data.token) {
            // Salva o JWT e o ID no sessionStorage
            sessionStorage.setItem('token', data.token);
            sessionStorage.setItem('ID', data.userId); // Salva o ID do usuário

            // Redireciona para a página de listagem de salas
            window.location.href = 'listagem-salas.html';
        } else {
            alert('Login inválido, tente novamente.');
        }
    })
    .catch(error => {
        console.error('Erro ao fazer login:', error);
        alert('Erro ao realizar login. Por favor, tente novamente.');
    });
});
