CREATE TABLE IF NOT EXISTS Depoimentos (
    Id BIGINT PRIMARY KEY auto_increment,
    IdFile VARCHAR(40) NULL,
    Depoimento VARCHAR(1000) NOT NULL,
    NomePessoa VARCHAR(100) NOT NULL,
    DataCriacao DATETIME,
    DataAtualizacao DATETIME
);

ALTER TABLE Depoimentos
ADD CONSTRAINT Depoimentos_DataFile_FK
FOREIGN KEY (IdFile)
REFERENCES DataFile(IdFile);