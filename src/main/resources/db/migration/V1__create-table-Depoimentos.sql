CREATE TABLE IF NOT EXISTS Depoimentos (
    Id BIGINT PRIMARY KEY auto_increment,
    Foto VARBINARY(200),
    Depoimento VARCHAR(1000) NOT NULL,
    NomePessoa VARCHAR(100) NOT NULL,
    DataCriacao DATETIME,
    DataAtualizacao DATETIME
);
