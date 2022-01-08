CREATE TABLE courses (
    code VARCHAR(9) NOT NULL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    credits DECIMAL(3,1) NOT NULL,
    complete BOOLEAN DEFAULT 0,
    term int DEFAULT 1,
    finalgrade DECIMAL(5,2)
);

CREATE TABLE evaluations (
	id INT PRIMARY KEY AUTO_INCREMENT,
	title VARCHAR(255) NOT NULL,
	course VARCHAR(9) NOT NULL,
	grade DECIMAL(5,2),
	max DECIMAL(5,2),
	weight DECIMAL(5,2),
	duedate DATE
);

CREATE TABLE users (
  userid BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(75) NOT NULL UNIQUE,
  encryptedpassword VARCHAR(128) NOT NULL,
  enabled BOOLEAN NOT NULL
);
 
CREATE TABLE roles (
  roleid BIGINT PRIMARY KEY AUTO_INCREMENT,
  rolename VARCHAR(30) NOT NULL UNIQUE
);
 
CREATE TABLE user_role (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  userid BIGINT NOT NULL,
  roleid BIGINT NOT NULL,
  UNIQUE (userid, roleid),
  FOREIGN KEY (userid) REFERENCES users(userid),
  FOREIGN KEY (roleid) REFERENCES roles(roleid)
);