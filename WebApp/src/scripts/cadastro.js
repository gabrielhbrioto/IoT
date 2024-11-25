import '../../css/cadastro.css';

/**
 * Valida se o email fornecido está no formato correto.
 *
 * @param {string} email - O email a ser validado.
 * @returns {boolean} Retorna true se o email for válido, caso contrário, false.
 */
function validarEmail(email) {
    const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return regex.test(email);
}

document.getElementById('cadastroForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const nome = document.getElementById('nome').value;
    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;

    const dados = {
        nome: nome,
        email: email,
        senha: senha
    };

    fetch('http://localhost:8080/usuarios', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(dados)
    })    
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        console.log('Usuário cadastrado:', data);
        window.location.href = '/';
    })
    .catch(error => {
        console.error('Erro ao cadastrar o usuário:', error);
    });
});
