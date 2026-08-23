# Ogham Web — Front-end (Etapa 8)

Protótipo de front-end do sistema Ogham, sem back-end (conforme pedido pela
atividade). Todas as páginas funcionam abrindo os arquivos `.html`
diretamente no navegador — não precisa de servidor, banco de dados ou Java
para rodar esta parte.

## Como abrir

Basta abrir `index.html` no navegador (duplo clique, ou clique direito >
Abrir com > seu navegador). A navegação entre páginas funciona por links
normais.

Se preferir rodar por um servidor local (opcional, evita alguns bloqueios de
segurança do navegador para `file://`), qualquer servidor estático serve, por
exemplo, com Python instalado:

```
cd frontend
python -m http.server 8000
```

E acesse `http://localhost:8000`.

## Estrutura

```
frontend/
 ├── index.html         → página inicial: listagem + busca de documentos
 ├── login.html          → login do administrador
 ├── inserir.html         → formulário de inserção de documento (exige login)
 ├── documento.html       → detalhe de um documento (usa ?id=N na URL)
 ├── css/
 │    └── estilo.css      → todo o CSS do sistema, em um único arquivo
 └── js/
      ├── documentos.js    → "camada de dados e regras de negócio" do front-end
      ├── ui-comum.js       → comportamento compartilhado (menu ativo, sessão)
      ├── pagina-inicio.js   → busca e renderização da tabela
      ├── validacao-login.js → validação e fluxo de login
      ├── validacao-inserir.js → validação do formulário de inserção
      └── pagina-detalhe.js   → carrega e exibe o documento pelo ID da URL

wireframes/
 ├── wireframe_home.png
 ├── wireframe_login.png
 ├── wireframe_inserir.png
 └── wireframe_detalhe.png
```

## Sem back-end — como isso foi resolvido

Como a atividade pede para **não** implementar conexão com banco de dados
nesta etapa, a "camada de dados" (`js/documentos.js`) simula o back-end:

- Começa com 3 documentos de exemplo (os mesmos temas dos arquivos de
  exemplo do projeto desktop: `carta_1820.pdf`, `foto_1905.jpg`).
- Documentos inseridos pelo formulário são salvos no `localStorage` do
  navegador, então persistem entre páginas e recarregamentos (mas só nesse
  navegador/computador — é um recurso temporário desta etapa).
- O login usa uma credencial fixa (`admin` / `ogham123`), guardada em
  `sessionStorage`, só para demonstrar o fluxo de "área restrita".

**Isso foi proposital**: as funções em `documentos.js` têm os mesmos nomes e
a mesma forma de uso das classes já existentes no back-end Java
(`DocumentService.listarTodos()`, `.pesquisar()`, `.inserir()`,
`DocumentValidator.validar()`, `HistoricalPeriodCalculator.classificarPeriodo()`).
Na Etapa 9, quando o back-end for conectado, a ideia é que baste trocar o
conteúdo dessas funções por chamadas `fetch()` a uma API — as páginas HTML e
os outros arquivos JS não deveriam precisar mudar.

## Validações implementadas em JavaScript

- **Login**: usuário e senha obrigatórios; mensagem de erro para credenciais inválidas.
- **Inserir documento**: título obrigatório, tipo obrigatório (PDF ou JPEG),
  arquivo obrigatório — as mesmas três regras de
  `ogham.service.DocumentValidator` no back-end desktop, com as mesmas
  mensagens de erro, mostradas ao lado de cada campo.
- **Proteção de rota**: a página de inserção verifica se há uma sessão de
  administrador ativa antes de mostrar o formulário; caso contrário, mostra
  uma tela de "acesso restrito" com link para o login.

## Wireframes

A pasta `wireframes/` contém esboços de baixa fidelidade (blocos cinza, sem
estilo) feitos antes da implementação, para planejar a estrutura de cada
página: página inicial, login, inserir documento e detalhe do documento. As
páginas HTML finais (o protótipo de alta fidelidade) seguem a mesma
disposição geral desses esboços, já com o CSS aplicado.
