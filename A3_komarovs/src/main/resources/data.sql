INSERT INTO courses (code, title, credits, complete, term, finalGrade) VALUES ('PROG10082', 'Object Oriented Programming 1', 6.0, true, 1, 94.0);
INSERT INTO courses (code, title, credits, complete, term, finalGrade) VALUES ('SYST10049', 'Web Development', 3.0, false, 1, NULL);
INSERT INTO courses (code, title, credits, complete, term, finalGrade) VALUES ('MATH18584', 'Computer Math Fundamentals', 4.0, true, 1, 90);
INSERT INTO courses (code, title, credits, complete, term, finalGrade) VALUES ('COMM13729', 'The Art of Technical Communications', 3.0, true, 1, 79);
INSERT INTO courses (code, title, credits, complete, term, finalGrade) VALUES ('PROG24178', 'Object Oriented Programming 2', 6.0, true, 2, 91);
INSERT INTO courses (code, title, credits, complete, term, finalGrade) VALUES ('SYST10199', 'Web Programming', 3.0, true, 2, 79);
INSERT INTO courses (code, title, credits, complete, term, finalGrade) VALUES ('SYST28296', 'Systems Administration Linux/UNIX', 3.0, true, 3, 86);
INSERT INTO courses (code, title, credits, complete, term, finalGrade) VALUES ('INFO24178', 'Computer and Network Security', 3.0, false, 3, NULL);


INSERT INTO evaluations (title, course, grade, max, weight, duedate) VALUES ('Assignment 1',
				'SYST10049', 21.5, 25.0, 8.0, '2021-07-24');
INSERT INTO evaluations (title, course, grade, max, weight, duedate) VALUES ('Assignment 2',
				'SYST10199', 20.5, 25.0, 6.0, '2021-07-25');
INSERT INTO evaluations (title, course, grade, max, weight, duedate) VALUES ('Exercise 1',
				'PROG10082', 20.5, 45.0, 4.0, '2021-08-08');
INSERT INTO evaluations (title, course, grade, max, weight, duedate) VALUES ('Lab 1',
				'MATH18584', 20.5, 28.0, 4.0, '2021-07-25');				
INSERT INTO evaluations (title, course, grade, max, weight, duedate) VALUES ('Exercise 3',
				'COMM13729', 10.5, 20.0, 10.0, '2021-08-15');				
INSERT INTO evaluations (title, course, grade, max, weight, duedate) VALUES ('Lab 4',
				'SYST10199', 20.0, 28.0, 5.0, '2021-06-25');	
INSERT INTO evaluations (title, course, grade, max, weight, duedate) VALUES ('Assignment 3',
				'SYST28296', 20.5, 25.0, 10.0, '2021-07-30');

INSERT INTO users (username, encryptedpassword, enabled)
VALUES ('komarovs', '$2a$10$o/j9IQweHh1m3hYeocsT9Oy0wDjpei49D04sd0R3noVSnsDBY0Zi6', TRUE);

INSERT INTO users (username, encryptedpassword, enabled)
VALUES ('jtrudeau', '$2a$10$kv9UaK/g82Jga5UveCcAOOO3XEUOiChDoWtC9TnJ0/frX56W7IvS.', TRUE);
  
INSERT INTO roles (rolename)
VALUES ('ROLE_USER');

INSERT INTO roles (rolename)
VALUES ('ROLE_ADMIN');
  
INSERT INTO user_role (userid, roleid)
VALUES (1, 1);

INSERT INTO user_role (userid, roleid)
VALUES (2, 2);  