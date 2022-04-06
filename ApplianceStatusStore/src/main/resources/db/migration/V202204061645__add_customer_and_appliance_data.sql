# inserting customers
INSERT INTO customers(name, address)
VALUES ('Kalles Grustransporter AB', 'Cementvägen 8, 111 11 Södertälje');
SELECT @kallesGrustransporter := LAST_INSERT_ID();

INSERT INTO customers(name, address)
VALUES ('Johans Bulk AB', 'Bulkvägen 12, 222 22 Stockholm');
SELECT @johansBulk := LAST_INSERT_ID();

INSERT INTO customers(name, address)
VALUES ('Haralds Värdetransporter AB', 'Budgetvägen 1, 333 33 Uppsala');
SELECT @heraldsVardtransporter := LAST_INSERT_ID();

## inserting user's appliances
INSERT INTO appliances(customerId, applianceId, factoryNr)
VALUES
       (@kallesGrustransporter, 'YS2R4X20005399401', 'ABC123'),
       (@kallesGrustransporter, 'VLUR4X20009093588', 'DEF456'),
       (@kallesGrustransporter, 'VLUR4X20009048066', 'ABC123');

INSERT INTO appliances(customerId, applianceId, factoryNr)
VALUES
    (@johansBulk, 'YS2R4X20005388011', 'JKL012'),
    (@johansBulk, 'YS2R4X20005387949', 'MN0345');

INSERT INTO appliances(customerId, applianceId, factoryNr)
VALUES
    (@heraldsVardtransporter, 'YS2R4X20009048066', 'PQR678'),
    (@heraldsVardtransporter, 'VLUR4X20005387055', 'STU901');



