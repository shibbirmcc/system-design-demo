CREATE TABLE `customers`
(
    `Id`      int(11)      NOT NULL AUTO_INCREMENT,
    `name`    varchar(100) NOT NULL,
    `address` varchar(100) NOT NULL,
    PRIMARY KEY (`Id`),
    KEY `customers_name_IDX` (`name`) USING BTREE,
    KEY `customers_address_IDX` (`address`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;