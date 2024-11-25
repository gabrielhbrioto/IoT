import '../../css/login.css';    

import loginImage from '../../img/login.png';

document.getElementById('login-image').src = loginImage;

document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    /**
     * Obtém o valor do campo de entrada com o ID 'password'.
     * 
     * @constant {string} password - O valor do campo de entrada de senha.
     */
    const password = document.getElementById('password').value;

    fetch(`http://localhost:8080/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email,
            senha: password
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao realizar login: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        if (data.token) {
            sessionStorage.setItem('token', data.token);
            sessionStorage.setItem('ID', data.userId);
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
