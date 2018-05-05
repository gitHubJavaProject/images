CREATE TABLE `images`.`role`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255),
  `description` VARCHAR(255),
  `create_date` DATE,
  `update_date` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `images`.`privilege` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource` varchar(255) DEFAULT NULL,
  `action` varchar(255) DEFAULT NULL,
  `create_date` date DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `images`.`user`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(255),
  `pwd` VARCHAR(255),
  `create_date` DATE,
  `update_date` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `images`.`user_role`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20),
  `role_id` BIGINT(20),
  `create_date` DATE,
  `update_date` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `images`.`role_privilege`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `role_id` BIGINT(20),
  `privilege_id` BIGINT(20),
  `create_date` DATE,
  `update_date` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_general_ci;

insert into `images`.role (id,name,description,create_date,update_date)
values
(1,'后台','登陆后台用户',now(),now()),
(2,'前台','登陆前台用户',now(),now());

ALTER TABLE `images`.`user`
  ADD COLUMN `name` VARCHAR(255) NULL AFTER `id`;

ALTER TABLE `images`.`user`
  ADD COLUMN `status` INT(11) NULL AFTER `update_date`;

ALTER TABLE `images`.`privilege`
  ADD COLUMN `parent_id` BIGINT(20) NULL AFTER `action`;

ALTER TABLE `images`.`privilege`
  ADD COLUMN `name` VARCHAR(255) NULL AFTER `id`;

  CREATE TABLE `images`.`select_key`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `create_date` DATE,
  `update_date` DATETIME,
  `key` VARCHAR(255),
  `select_number` BIGINT(20),
  PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `images`.`category`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `create_date` DATE,
  `update_date` DATETIME,
  `name` VARCHAR(255),
  `description` VARCHAR(255),
  `parent` BIGINT(20),
  `level` INT(11),
  PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `images`.`image`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `create_date` DATE,
  `update_date` DATETIME,
  `name` VARCHAR(255),
  `description` VARCHAR(255),
  `show_image` VARCHAR(255),
  `file_url` VARCHAR(255),
  `type` BIGINT(20),
  `type_str` VARCHAR(255),
  `tag` INT(11),
  `user_id` BIGINT(20),
  `download_number` BIGINT(20),
  `collection_number` BIGINT(20),
  PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_general_ci;

ALTER TABLE `images`.`image`
  ADD COLUMN `keys` VARCHAR(255) NULL AFTER `collection_number`,
  ADD COLUMN `size` VARCHAR(255) NULL AFTER `keys`,
  ADD COLUMN `width` INT(11) NULL AFTER `size`,
  ADD COLUMN `height` INT(11) NULL AFTER `width`;

  CREATE TABLE `images`.`image_favorite`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `create_date` DATE,
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP,
  `favorite_id` BIGINT(20),
  `image_id` BIGINT(20),
  PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `images`.`favorite`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `create_date` DATE,
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP,
  `name` VARCHAR(255),
  `description` VARCHAR(255),
  `url` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_general_ci;

ALTER TABLE `images`.`favorite`
  ADD COLUMN `user_id` BIGINT(20) NULL AFTER `url`;

  ALTER TABLE `images`.`user`
  ADD COLUMN `header` VARCHAR(255) NULL AFTER `status`;

  ALTER TABLE `images`.`image`
  ADD COLUMN `status` INT(11) NULL AFTER `height`;


