// Função para validar o formato do email usando regex
function validarEmail(email) {
    const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return regex.test(email);
}

document.getElementById('cadastroForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Previne o envio tradicional do formulário

    // Captura os valores do formulário
    const nome = document.getElementById('nome').value;
    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;
    const confirmaSenha = document.getElementById('confirmaSenha').value;

    // Validação dos campos
    if (!validarEmail(email)) {
        alert('Email inválido. Por favor, insira um email válido.');
        return; // Cancela o envio se o email for inválido
    }

    if (senha !== confirmaSenha) {
        alert('As senhas não coincidem. Tente novamente.');
        return; // Cancela o envio se as senhas não conferem
    }

    // Monta o objeto com os dados
    const dados = {
        nome: nome,
        email: email,
        senha: senha
    };

    window.location.href = 'login.html';

    /*// Envia os dados ao servidor usando fetch
    fetch('https://seu-backend.com/api/cadastro', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dados)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Redireciona para a página de login se o cadastro foi bem-sucedido
            window.location.href = 'login.html';
        } else {
            alert('Erro ao cadastrar: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Erro ao cadastrar:', error);
        alert('Erro ao se comunicar com o servidor. Tente novamente mais tarde.');
    });*/
});
