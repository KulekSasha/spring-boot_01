INSERT INTO PERSON_ROLE
(ROLE_ID, NAME)
VALUES
  (1, 'ADMIN'),
  (2, 'USER');

INSERT INTO PERSON
(PERSON_ID, ROLE_ID, LOGIN, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME, BIRTHDAY)
VALUES
  (1, 1, 'testUser_1', 'testUser_1', 'testUser_1@gmail.com', 'Ivan', 'Ivanov', '1986-01-01'),
  (2, 1, 'testUser_2', 'testUser_2', 'testUser_2@gmail.com', 'Petr', 'Petrov', '1985-02-02'),
  (3, 2, 'testUser_3', 'testUser_3', 'testUser_3@gmail.com', 'Dmitrii', 'Dmitriev', '1984-03-03'),
  (4, 2, 'testUser_4', 'testUser_4', 'testUser_4@gmail.com', 'Stas', 'Mikhailov', '1990-04-04'),
  (5, 2, 'testUser_5', 'testUser_5', 'testUser_5@gmail.com', 'Oleg', 'Gazmanov', '1980-05-05');