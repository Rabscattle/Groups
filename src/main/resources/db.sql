CREATE TABLE `users`
(
    `uuid`          VARCHAR(36) NOT NULL,
    `username`      VARCHAR(16) NOT NULL,
    `primary_group` VARCHAR(36) NOT NULL,
    PRIMARY KEY (`uuid`)
);

CREATE TABLE `user_permissions`
(
    `id`         INT AUTO_INCREMENT NOT NULL,
    `uuid`       VARCHAR(36)  NOT NULL,
    `permission` VARCHAR(200) NOT NULL,
    `value`      BOOL         NOT NULL,
    `expiring`   BIGINT       NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE INDEX `user_permissions_uuid` ON `user_permissions` (`uuid`);

CREATE TABLE `groups`
(
    `name`   VARCHAR(36) NOT NULL,
    `weight` INT DEFAULT 0,
    PRIMARY KEY (`name`)
);

CREATE TABLE `group_permissions`
(
    `id`         INT AUTO_INCREMENT NOT NULL,
    `group`      VARCHAR(36)  NOT NULL,
    `permission` VARCHAR(200) NOT NULL,
    `value`      BOOL         NOT NULL,
    `expiring`   BIGINT       NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE INDEX `group_permissions_group` ON `group_permissions` (`group`);
