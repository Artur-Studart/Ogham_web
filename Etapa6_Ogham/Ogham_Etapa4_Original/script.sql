CREATE DATABASE IF NOT EXISTS oghamdb CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE oghamdb;

CREATE TABLE IF NOT EXISTS documentos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(100),
    descricao TEXT,
    tipo ENUM('PDF','JPEG') NOT NULL,
    data DATE,
    arquivo_path VARCHAR(255) NOT NULL,
    tags VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS administradores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    chave_acesso VARCHAR(50) NOT NULL
);

INSERT IGNORE INTO administradores (id, chave_acesso) VALUES (1, 'oghamadmin');

INSERT IGNORE INTO documentos (id, titulo, autor, descricao, tipo, data, arquivo_path, tags)
VALUES
(1, 'Carta de 1820', 'João Almeida', 'Carta antiga sobre colonização', 'PDF', '1820-05-02', 'data/docs/carta_1820.pdf', 'história,colonialismo'),
(2, 'Fotografia de 1905', 'Museu Nacional', 'Imagem da exposição de 1905', 'JPEG', '1905-07-12', 'data/docs/foto_1905.jpg', 'museu,fotografia');
