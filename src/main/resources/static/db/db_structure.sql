DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id`   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  `email`     VARCHAR(255)                      NOT NULL,
  `name`      VARCHAR(255)                      NOT NULL,
  `password`  VARCHAR(255)                      NOT NULL,
  `role_name` VARCHAR(255)                      NOT NULL
);
CREATE UNIQUE INDEX user_id_uindex
  ON user (user_id);


DROP TABLE IF EXISTS `word`;
CREATE TABLE `word` (
  `word_id` INTEGER PRIMARY KEY AUTOINCREMENT     NOT NULL,
  `value`   VARCHAR(255)                    NOT NULL
);
CREATE UNIQUE INDEX word_id_uindex
  ON word (word_id);


DROP TABLE IF EXISTS `keyword`;
CREATE TABLE `keyword` (
  `keyword_id` INTEGER PRIMARY KEY AUTOINCREMENT      NOT NULL,
  `value`      VARCHAR(255)                     NOT NULL
);
CREATE UNIQUE INDEX keyword_id_uindex
  ON keyword (keyword_id);

DROP TABLE IF EXISTS keyword_word;
CREATE TABLE `keyword_word` (
  `keyword_id` INTEGER NOT NULL,
  `word_id`    INTEGER NOT NULL,
  PRIMARY KEY (`keyword_id`, `word_id`),
  CONSTRAINT `constraint_kw_keyword_id` FOREIGN KEY (`keyword_id`) REFERENCES `keyword` (`keyword_id`),
  CONSTRAINT `constraint_kw_word_id` FOREIGN KEY (`word_id`) REFERENCES `word` (`word_id`)
);


DROP TABLE IF EXISTS `parameter`;
CREATE TABLE `parameter` (
  `parameter_id` INTEGER PRIMARY KEY AUTOINCREMENT      NOT NULL,
  `name`         VARCHAR(255)                     NOT NULL,
  `value`        VARCHAR(255)                     NOT NULL
);
CREATE UNIQUE INDEX parameter_id_uindex
  ON parameter (parameter_id);

DROP TABLE IF EXISTS parameter_keyword;
CREATE TABLE `parameter_keyword` (
  `parameter_id` INTEGER NOT NULL,
  `keyword_id`   INTEGER NOT NULL,
  PRIMARY KEY (`parameter_id`, `keyword_id`),
  CONSTRAINT `constraint_pk_parameter_id` FOREIGN KEY (`parameter_id`) REFERENCES `parameter` (`parameter_id`),
  CONSTRAINT `constraint_pk_keyword_id` FOREIGN KEY (`keyword_id`) REFERENCES `keyword` (`keyword_id`)
);


DROP TABLE IF EXISTS `subject`;
CREATE TABLE `subject` (
  `subject_id` INTEGER PRIMARY KEY AUTOINCREMENT      NOT NULL,
  `name`       VARCHAR(255)                     NOT NULL
);
CREATE UNIQUE INDEX subject_id_uindex
  ON subject (subject_id);

DROP TABLE IF EXISTS subject_keyword;
CREATE TABLE `subject_keyword` (
  `subject_id` INTEGER NOT NULL,
  `keyword_id` INTEGER NOT NULL,
  PRIMARY KEY (`subject_id`, `keyword_id`),
  CONSTRAINT `constraint_sk_subject_id` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`subject_id`),
  CONSTRAINT `constraint_sk_keyword_id` FOREIGN KEY (`keyword_id`) REFERENCES `keyword` (`keyword_id`)
);

DROP TABLE IF EXISTS subject_parameter;
CREATE TABLE `subject_parameter` (
  `subject_id`   INTEGER NOT NULL,
  `parameter_id` INTEGER NOT NULL,
  PRIMARY KEY (`subject_id`, `parameter_id`),
  CONSTRAINT `constraint_sp_subject_id` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`subject_id`),
  CONSTRAINT `constraint_sp_parameter_id` FOREIGN KEY (`parameter_id`) REFERENCES `parameter` (`parameter_id`)
);


DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer` (
  `answer_id` INTEGER PRIMARY KEY AUTOINCREMENT       NOT NULL,
  `value`     VARCHAR(1024)                     NOT NULL
);
CREATE UNIQUE INDEX answer_id_uindex
  ON answer (answer_id);


DROP TABLE IF EXISTS `answer_keyword`;
CREATE TABLE `answer_keyword` (
  `answer_id` INTEGER NOT NULL,
  `keyword_id`  INTEGER NOT NULL,
  PRIMARY KEY (`answer_id`, `keyword_id`),
  CONSTRAINT `constraint_ak_answer_id` FOREIGN KEY (`answer_id`) REFERENCES `answer` (`answer_id`),
  CONSTRAINT `constraint_ak_keyword_id` FOREIGN KEY (`keyword_id`) REFERENCES `keyword` (`keyword_id`)
);