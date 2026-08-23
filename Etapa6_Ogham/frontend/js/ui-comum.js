/**
 * ui-comum.js
 * Comportamento compartilhado por todas as páginas: marcar o link ativo do
 * menu e atualizar o link "Login admin" / "Sair" conforme o estado de sessão.
 */
document.addEventListener("DOMContentLoaded", () => {
  const paginaAtual = document.body.dataset.pagina;
  document.querySelectorAll("nav.principal a[data-pagina]").forEach((link) => {
    if (link.dataset.pagina === paginaAtual) {
      link.classList.add("ativo");
    }
  });

  const linkSessao = document.getElementById("link-sessao");
  if (linkSessao) {
    if (Ogham.estaLogado()) {
      linkSessao.textContent = "Sair";
      linkSessao.href = "#";
      linkSessao.addEventListener("click", (ev) => {
        ev.preventDefault();
        Ogham.logout();
        window.location.href = "index.html";
      });
    } else {
      linkSessao.textContent = "Login admin";
      linkSessao.href = "login.html";
    }
  }
});
