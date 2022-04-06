CREATE TABLE `heartbeat_logs`
(
    `id`                  varchar(100) NOT NULL,
    `applianceId`         varchar(100) NOT NULL,
    `heartbeatReceivedAt` timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`Id`),
    CONSTRAINT `appliances_id_FK` FOREIGN KEY (`applianceId`) REFERENCES `appliances` (`applianceId`),
    KEY `customers_address_IDX` (`heartbeatReceivedAt`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;