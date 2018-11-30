CREATE TABLE users (
  id                  INT           NOT NULL AUTO_INCREMENT,

  name                VARCHAR(256)  NOT NULL,

  created_at          DATETIME      NOT NULL,
  updated_at          DATETIME      NOT NULL,

  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE github_users (
  id                  INT           NOT NULL AUTO_INCREMENT,
  user_id             INT           NOT NULL,

  gh_user_id          BIGINT        NOT NULL,
  gh_node_id          VARCHAR(256)  NOT NULL,
  gh_login            VARCHAR(256)  NOT NULL,
  gh_name             VARCHAR(256)  NOT NULL,
  gh_email            VARCHAR(256)  NOT NULL,
  gh_created_at       DATETIME      NOT NULL,
  gh_updated_at       DATETIME      NOT NULL,

  created_at          DATETIME      NOT NULL,
  updated_at          DATETIME      NOT NULL,

  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  UNIQUE KEY (gh_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
