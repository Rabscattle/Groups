CREATE DATABASE IF NOT EXISTS `test`;

CREATE USER 'test'@'%' IDENTIFIED BY 'BNMCSBH2432IUHOI32GJ';
GRANT ALL ON *.* TO 'test'@'%';
FLUSH PRIVILEGES;