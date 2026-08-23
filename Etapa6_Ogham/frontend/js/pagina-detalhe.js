/**
 * pagina-detalhe.js
 * Lê o parâmetro ?id= da URL, busca o documento (Ogham.buscarPorId) e
 * preenche a página. O botão de download é um placeholder: nesta etapa não
 * há back-end nem arquivos reais servidos, então apenas explica o que vai
 * acontecer quando a Etapa 9 conectar isso a um servidor de verdade.
 */
document.addEventListener("DOMContentLoaded", () => {
  const parametros = new URLSearchParams(window.location.search);
  const id = parametros.get("id");
  const documento = id ? Ogham.buscarPorId(id) : null;

  const cartaoNaoEncontrado = document.getElementById("cartao-nao-encontrado");
  const cartaoDetalhe = document.getElementById("cartao-detalhe");

  if (!documento) {
    cartaoNaoEncontrado.hidden = false;
    return;
  }
  cartaoDetalhe.hidden = false;

  document.getElementById("titulo-documento").textContent = documento.titulo;
  document.getElementById("subtitulo-documento").textContent = `Documento #${documento.id}`;
  document.getElementById("valor-autor").textContent = documento.autor || "Não informado";
  document.getElementById("valor-tipo").textContent = documento.tipo;
  document.getElementById("valor-data").textContent = documento.data || "Não informada";
  document.getElementById("valor-periodo").textContent = Ogham.classificarPeriodo(documento.data);
  document.getElementById("valor-tags").textContent = documento.tags || "Nenhuma";
  document.getElementById("valor-arquivo").textContent = documento.arquivo || "Não informado";

  document.getElementById("botao-download").addEventListener("click", () => {
    alert(
      "Download disponível quando o back-end for integrado (Etapa 9). " +
      "Por enquanto, este é apenas um protótipo de front-end."
    );
  });
});
