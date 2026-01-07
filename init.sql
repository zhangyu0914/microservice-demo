CREATE DATABASE IF NOT EXISTS user_service;
USE user_service;

CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  `github_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `user` (username, password, role) VALUES ('user_1', 'user_1', 'USER');
INSERT INTO `user` (username, password, role) VALUES ('editor_1', 'editor_1', 'EDITOR');
INSERT INTO `user` (username, password, role) VALUES ('adm_1', 'adm_1', 'PRODUCT_ADMIN');

CREATE DATABASE IF NOT EXISTS product_service;
USE product_service;
CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

