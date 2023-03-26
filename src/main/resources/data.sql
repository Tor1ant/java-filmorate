INSERT INTO FILMS(film_id, name, description, release_date, duration, RATING_ID)
VALUES (0, 'Только представь!', 'Молодые учащиеся теснятся на скамейке под ярким солнцем', '2012-12-10', 102, 1),
       (1, 'Новый порядок', 'Роскошная свадьба', '2020-01-08', 85, 0),
       (2, 'От заката до рассвета', 'Спасаясь от полиции', '1995-04-11', 90, 2);

insert INTO USERS(USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY)
VALUES (0, '312-slava@mail.ru', 'Slavick', 'Totoshka', '1998-06-01'),
       (1, '0-gfdmgnfd@mail.ru', 'jojo', 'Jonatan', '1990-05-25'),
       (2, 'sfdgd@mail.ru', 'allo', 'Suleman', '1995-03-19');

insert into FILM_GENRE(film_id, genre_id)
VALUES (0, 2),
       (0, 3),
       (2, 4),
       (1, 5),
       (2, 2),
       (1, 3);

insert into LIKES(FILM_ID, USER_ID)
values (0, 2),
       (1, 1),
       (2, 2),
       (1, 0),
       (1, 2);

insert into STATUS(STATUS, STATUS_ID)
values (true, 0),
       (false, 1);

insert into FRIENDS(FRIEND_ID, USER_ID, STATUS_ID)
values (0, 1, 1),
       (1, 2, 0),
       (0, 2, 1),
       (2, 0, 1),
       (2, 1, 0);