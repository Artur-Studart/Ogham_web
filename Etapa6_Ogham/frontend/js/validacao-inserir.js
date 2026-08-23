/**
 * validacao-inserir.js
 * Protege a página (exige login) e valida o formulário no cliente antes de
 * "salvar" (aqui, via Ogham.inserir em documentos.js). As mensagens e regras
 * usadas são as mesmas de ogham.service.DocumentValidator no back-end Java,
 * propositalmente, para facilitar a Etapa 9.
 */
document.addEventListener("DOMContentLoaded", () => {
  const cartaoAcessoNegado = document.getElementById("cartao-acesso-negado");
  const cartaoFormulario = document.getElementById("cartao-formulario");

  if (!Ogham.estaLogado()) {
    cartaoAcessoNegado.hidden = false;
    return;
  }
  cartaoFormulario.hidden = false;

  const form = document.getElementById("form-inserir");
  const campoTitulo = document.getElementById("titulo");
  const campoTipo = document.getElementById("tipo");
  const campoArquivo = document.getElementById("arquivo");
  const campoData = document.getElementById("data");
  const campoAutor = document.getElementById("autor");
  const campoTags = document.getElementById("tags");
  const alertaSucesso = document.getElementById("alerta-sucesso");

  const erroTitulo = document.getElementById("erro-titulo");
  const erroTipo = document.getElementById("erro-tipo");
  const erroArquivo = document.getElementById("erro-arquivo");

  function limparErros() {
    [campoTitulo, campoTipo, campoArquivo].forEach((c) => c.classList.remove("campo-invalido"));
    erroTitulo.textContent = "";
    erroTipo.textContent = "";
    erroArquivo.textContent = "";
  }

  function exibirErros(mensagens) {
    mensagens.forEach((msg) => {
      if (msg.startsWith("Título")) {
        erroTitulo.textContent = msg;
        campoTitulo.classList.add("campo-invalido");
      } else if (msg.startsWith("Tipo")) {
        erroTipo.textContent = msg;
        campoTipo.classList.add("campo-invalido");
      } else if (msg.startsWith("Arquivo")) {
        erroArquivo.textContent = msg;
        campoArquivo.classList.add("campo-invalido");
      }
    });
  }

  form.addEventListener("submit", (ev) => {
    ev.preventDefault();
    limparErros();
    alertaSucesso.hidden = true;

    const documento = {
      titulo: campoTitulo.value.trim(),
      autor: campoAutor.value.trim(),
      tipo: campoTipo.value,
      data: campoData.value,
      arquivo: campoArquivo.files.length > 0 ? campoArquivo.files[0].name : "",
      tags: campoTags.value.trim(),
    };

    try {
      const salvo = Ogham.inserir(documento);
      alertaSucesso.textContent = `Documento "${salvo.titulo}" salvo com sucesso (ID ${salvo.id}). Confira na página inicial.`;
      alertaSucesso.hidden = false;
      form.reset();
    } catch (erro) {
      exibirErros(erro.erros || ["Não foi possível salvar o documento."]);
    }
  });

  [campoTitulo, campoTipo, campoArquivo].forEach((campo) => {
    campo.addEventListener("input", () => campo.classList.remove("campo-invalido"));
    campo.addEventListener("change", () => campo.classList.remove("campo-invalido"));
  });
});
