document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Previne o envio padrão do formulário

    // Captura os valores dos campos de email e senha
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    // Realiza a requisição para o backend
    fetch('http://localhost:8080/auth/login', { // Altere a URL para a rota correta do seu backend
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
        return response.text(); // O token JWT é retornado como texto
    })
    .then(token => {
        if (token) {
            // Salva o JWT no sessionStorage
            sessionStorage.setItem('token', token);

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

