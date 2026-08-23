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
 tags VARCHAR(255),
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS administradores (
 id INT AUTO_INCREMENT PRIMARY KEY,
 usuario VARCHAR(50) NOT NULL UNIQUE,
 senha_hash CHAR(64) NOT NULL
);

INSERT INTO administradores (usuario, senha_hash) VALUES ('admin','b6ba11750c8fcf190407211b3e3122c6e6c9ad12ecee87baeb873b66dbd2520d')
ON DUPLICATE KEY UPDATE usuario=VALUES(usuario);

INSERT INTO documentos (id,titulo,autor,descricao,tipo,data,arquivo_path,tags) VALUES
(1,'Carta pessoal de 1820','Autor desconhecido','Carta histórica digitalizada','PDF','1820-05-10','carta_1820.pdf','carta,correspondencia,seculo XIX'),
(2,'Fotografia de família','Estúdio fotográfico local','Registro fotográfico histórico','JPEG','1905-03-22','foto_1905.jpg','fotografia,familia,retrato'),
(3,'Ata de fundação da associação','Secretaria da associação','Documento institucional','PDF','1998-11-02','ata_1998.pdf','ata,associacao,seculo XX')
ON DUPLICATE KEY UPDATE titulo=VALUES(titulo);
