CREATE TABLE `appliances`
(
    `customerId`               int(11) NOT NULL,
    `applianceId`              varchar(100) NOT NULL,
    `factoryNr`                varchar(100) NOT NULL,
    `lastHeartBeatReceiveTime` timestamp NULL DEFAULT NULL,
    UNIQUE KEY `appliances_UN` (`applianceId`),
    KEY                        `appliances_FK` (`customerId`),
    CONSTRAINT `appliances_FK` FOREIGN KEY (`customerId`) REFERENCES `customers` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;