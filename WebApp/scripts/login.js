document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Previne o envio padrão do formulário

    // Captura os valores dos campos de email e senha
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    console.log("login efetuado com sucesso!");
    window.location.href = 'listagem-salas.html';

    /*
    // Realiza a requisição para o backend
    fetch('https://seu-backend.com/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email, password: password })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Redireciona para a página de listagem de salas
            window.location.href = 'listagem-salas.html';
        } else {
            alert('Login inválido, tente novamente.');
        }
    })
    .catch(error => {
        console.error('Erro ao fazer login:', error);
    });*/
});
