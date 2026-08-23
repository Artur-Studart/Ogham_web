/**
 * documentos.js
 *
 * Camada de "dados e regras de negócio" do front-end. Nesta etapa (8) o
 * sistema não tem back-end, então os documentos ficam em memória (um array
 * inicial + o que for salvo em localStorage pelo formulário de inserção).
 *
 * Propositalmente, os nomes das funções espelham as classes já existentes no
 * back-end Java (ogham.service.DocumentValidator e
 * ogham.service.HistoricalPeriodCalculator), para facilitar a futura
 * integração na Etapa 9: quando o back-end existir, esta camada poderá ser
 * substituída por chamadas fetch() a uma API, sem que as páginas HTML
 * precisem mudar a forma como chamam listarTodos()/pesquisar()/inserir().
 */

const Ogham = (() => {
  const CHAVE_ARMAZENAMENTO = "ogham_documentos_extra";

  // Conjunto inicial de documentos (equivalente aos dados de exemplo do
  // projeto desktop: data/docs/carta_1820.pdf e data/docs/foto_1905.jpg).
  const DOCUMENTOS_BASE = [
    {
      id: 1,
      titulo: "Carta pessoal de 1820",
      autor: "Autor desconhecido",
      tipo: "PDF",
      data: "1820-05-10",
      tags: "carta, correspondencia, seculo XIX",
      arquivo: "carta_1820.pdf",
    },
    {
      id: 2,
      titulo: "Fotografia de família",
      autor: "Estúdio fotográfico local",
      tipo: "JPEG",
      data: "1905-03-22",
      tags: "fotografia, familia, retrato",
      arquivo: "foto_1905.jpg",
    },
    {
      id: 3,
      titulo: "Ata de fundação da associação",
      autor: "Secretaria da associação",
      tipo: "PDF",
      data: "1998-11-02",
      tags: "ata, associacao, seculo XX",
      arquivo: "ata_1998.pdf",
    },
  ];

  function carregarExtras() {
    try {
      const bruto = localStorage.getItem(CHAVE_ARMAZENAMENTO);
      return bruto ? JSON.parse(bruto) : [];
    } catch (e) {
      console.warn("Não foi possível ler documentos salvos localmente:", e);
      return [];
    }
  }

  function salvarExtras(lista) {
    localStorage.setItem(CHAVE_ARMAZENAMENTO, JSON.stringify(lista));
  }

  function proximoId() {
    const todos = listarTodos();
    return todos.reduce((max, d) => Math.max(max, d.id), 0) + 1;
  }

  /**
   * Equivalente a DocumentService.listarTodos() / repository.listarTodos().
   */
  function listarTodos() {
    return [...DOCUMENTOS_BASE, ...carregarExtras()];
  }

  /**
   * Equivalente a DocumentService.pesquisar(termo): termo vazio retorna
   * tudo; caso contrário filtra por título, autor ou tags.
   */
  function pesquisar(termo) {
    const alvo = (termo || "").trim().toLowerCase();
    if (!alvo) {
      return listarTodos();
    }
    return listarTodos().filter((d) =>
      [d.titulo, d.autor, d.tags].some((campo) =>
        (campo || "").toLowerCase().includes(alvo)
      )
    );
  }

  function buscarPorId(id) {
    return listarTodos().find((d) => d.id === Number(id)) || null;
  }

  /**
   * Equivalente a DocumentValidator.validar(Document): retorna uma lista de
   * mensagens de erro (vazia = válido). Mesmas três regras do back-end:
   * título obrigatório, tipo deve ser PDF/JPEG, arquivo obrigatório.
   */
  function validar(documento) {
    const erros = [];
    if (!documento.titulo || !documento.titulo.trim()) {
      erros.push("Título é obrigatório.");
    }
    if (!documento.tipo || !["PDF", "JPEG"].includes(documento.tipo)) {
      erros.push("Tipo deve ser 'PDF' ou 'JPEG'.");
    }
    if (!documento.arquivo || !documento.arquivo.trim()) {
      erros.push("Arquivo é obrigatório.");
    }
    return erros;
  }

  /**
   * Equivalente a DocumentService.inserir(Document): valida e, se estiver
   * tudo certo, persiste (aqui, em localStorage) e devolve o documento com
   * ID gerado. Lança um erro com a lista de mensagens se inválido.
   */
  function inserir(documento) {
    const erros = validar(documento);
    if (erros.length > 0) {
      const erro = new Error("Documento inválido");
      erro.erros = erros;
      throw erro;
    }
    const novo = { ...documento, id: proximoId() };
    const extras = carregarExtras();
    extras.push(novo);
    salvarExtras(extras);
    return novo;
  }

  /**
   * Equivalente a HistoricalPeriodCalculator.classificarPeriodo(String).
   */
  function classificarPeriodo(dataIso) {
    if (!dataIso || dataIso.length < 4) {
      return "Data inválida";
    }
    const ano = parseInt(dataIso.substring(0, 4), 10);
    if (Number.isNaN(ano)) {
      return "Data inválida";
    }
    if (ano <= 1900) return "Século XIX ou anterior";
    if (ano <= 2000) return "Século XX";
    return "Século XXI";
  }

  /** Sessão simples de "admin logado", usada para proteger a página de inserção. */
  function estaLogado() {
    return sessionStorage.getItem("ogham_admin_logado") === "sim";
  }

  function login(usuario, senha) {
    // Credenciais fixas apenas para demonstração do fluxo nesta etapa
    // (front-end sem back-end). Na Etapa 9, isso deve ser substituído por
    // uma chamada real de autenticação no servidor.
    if (usuario === "admin" && senha === "ogham123") {
      sessionStorage.setItem("ogham_admin_logado", "sim");
      return true;
    }
    return false;
  }

  function logout() {
    sessionStorage.removeItem("ogham_admin_logado");
  }

  return {
    listarTodos,
    pesquisar,
    buscarPorId,
    validar,
    inserir,
    classificarPeriodo,
    estaLogado,
    login,
    logout,
  };
})();
