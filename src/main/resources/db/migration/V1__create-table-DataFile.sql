CREATE TABLE IF NOT EXISTS DataFile (
    IdFile VARCHAR(40) PRIMARY KEY,
    FileName VARCHAR(255),
    MediaType VARCHAR(100),
    Size INT,
    data BLOB,
    createdAt DATETIME
);