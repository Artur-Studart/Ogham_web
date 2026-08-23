/**
 * pagina-inicio.js
 * Funcionalidade dinâmica da página inicial: pesquisa e renderização da
 * tabela de documentos, chamando as funções de ogham/documentos.js.
 */
document.addEventListener("DOMContentLoaded", () => {
  const corpoTabela = document.getElementById("corpo-tabela");
  const estadoVazio = document.getElementById("estado-vazio");
  const campoBusca = document.getElementById("campo-busca");
  const botaoPesquisar = document.getElementById("botao-pesquisar");
  const botaoLimpar = document.getElementById("botao-limpar");

  function renderizar(documentos) {
    corpoTabela.innerHTML = "";
    estadoVazio.hidden = documentos.length > 0;

    documentos.forEach((doc) => {
      const periodo = Ogham.classificarPeriodo(doc.data);
      const linha = document.createElement("tr");
      linha.innerHTML = `
        <td><a href="documento.html?id=${doc.id}">${escaparHtml(doc.titulo)}</a></td>
        <td>${escaparHtml(doc.autor || "-")}</td>
        <td>${escaparHtml(doc.tipo)}</td>
        <td>${escaparHtml(doc.data || "-")}</td>
        <td><span class="selo-periodo">${periodo}</span></td>
        <td><a class="botao secundario" href="documento.html?id=${doc.id}">Ver detalhes</a></td>
      `;
      corpoTabela.appendChild(linha);
    });
  }

  function escaparHtml(texto) {
    const div = document.createElement("div");
    div.textContent = texto;
    return div.innerHTML;
  }

  function executarPesquisa() {
    const termo = campoBusca.value;
    renderizar(Ogham.pesquisar(termo));
  }

  botaoPesquisar.addEventListener("click", executarPesquisa);
  campoBusca.addEventListener("keyup", (ev) => {
    if (ev.key === "Enter") executarPesquisa();
  });
  botaoLimpar.addEventListener("click", () => {
    campoBusca.value = "";
    renderizar(Ogham.listarTodos());
  });

  // Carga inicial: mostra todos os documentos.
  renderizar(Ogham.listarTodos());
});
