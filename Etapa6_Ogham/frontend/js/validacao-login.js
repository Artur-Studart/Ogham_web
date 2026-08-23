/**
 * validacao-login.js
 * Validação client-side do formulário de login: campos obrigatórios, mais a
 * tentativa de autenticação (mock, ver Ogham.login em documentos.js).
 */
document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("form-login");
  const campoUsuario = document.getElementById("usuario");
  const campoSenha = document.getElementById("senha");
  const erroUsuario = document.getElementById("erro-usuario");
  const erroSenha = document.getElementById("erro-senha");
  const alertaErro = document.getElementById("alerta-erro");

  function limparErros() {
    [campoUsuario, campoSenha].forEach((c) => c.classList.remove("campo-invalido"));
    erroUsuario.textContent = "";
    erroSenha.textContent = "";
    alertaErro.hidden = true;
  }

  function validarCampos() {
    let valido = true;
    if (!campoUsuario.value.trim()) {
      erroUsuario.textContent = "Informe o usuário.";
      campoUsuario.classList.add("campo-invalido");
      valido = false;
    }
    if (!campoSenha.value.trim()) {
      erroSenha.textContent = "Informe a senha.";
      campoSenha.classList.add("campo-invalido");
      valido = false;
    }
    return valido;
  }

  form.addEventListener("submit", (ev) => {
    ev.preventDefault();
    limparErros();

    if (!validarCampos()) {
      return;
    }

    const autenticado = Ogham.login(campoUsuario.value.trim(), campoSenha.value);
    if (autenticado) {
      window.location.href = "inserir.html";
    } else {
      alertaErro.textContent = "Usuário ou senha inválidos.";
      alertaErro.hidden = false;
    }
  });

  // Limpa a mensagem de erro assim que o usuário volta a digitar.
  [campoUsuario, campoSenha].forEach((campo) => {
    campo.addEventListener("input", () => {
      campo.classList.remove("campo-invalido");
    });
  });
});
