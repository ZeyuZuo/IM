```
CREATE SCHEMA `im` ;

CREATE TABLE login ( \
  `user` VARCHAR(256) NOT NULL, \
  `pwd` VARCHAR(256) NOT NULL, \
  PRIMARY KEY (`user`));


INSERT INTO `im`.`login` (`user`, `pwd`) VALUES ('zzy', '123456');
INSERT INTO `im`.`login` (`user`, `pwd`) VALUES ('xzx', '123456');
INSERT INTO `im`.`login` (`user`, `pwd`) VALUES ('wy', '123456');


CREATE TABLE `im`.`groupUser` ( \
  `id` INT NOT NULL AUTO_INCREMENT, \
  `group` VARCHAR(256) NULL, \
  `user` VARCHAR(256) NULL, \
  PRIMARY KEY (`id`));


INSERT INTO `im`.`groupUser` (`id`, `group`, `user`) VALUES ('1', 'test', 'zzy');
INSERT INTO `im`.`groupUser` (`id`, `group`, `user`) VALUES ('2', 'test', 'wy');
INSERT INTO `im`.`groupUser` (`id`, `group`, `user`) VALUES ('3', 'test', 'xzx');
INSERT INTO `im`.`groupUser` (`id`, `group`, `user`) VALUES ('4', 'hello', 'zzy');
INSERT INTO `im`.`groupUser` (`id`, `group`, `user`) VALUES ('5', 'hello', 'xzx');