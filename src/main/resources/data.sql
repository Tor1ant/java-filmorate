/*
INSERT INTO FILMS(film_id, name, description, release_date, duration, RATING_ID)
VALUES (0, 'Только представь!', 'Молодые учащиеся теснятся на скамейке под ярким солнцем', '2012-12-10', 102, 1),
       (1, 'Новый порядок', 'Роскошная свадьба', '2020-01-08', 85, 0),
       (2, 'От заката до рассвета', 'Спасаясь от полиции', '1995-04-11', 90, 2),
       (3, 'Законопослушный гражданин', 'Мужчина потерял все, что было у него', '2009-10-15', 119, 3),
       (4, 'Берегись автомобиля', 'Детектив, юмор и мелодрама', '1966-04-01', 94, 4),
       (5, 'Суспільні забави', 'Українська кримінальна комедія', '2022-03-21', 120, 3);

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
       (1, 3),
       (3, 2),
       (3, 3),
       (3, 4),
       (4, 1),
       (4, 3),
       (4, 0),
       (5, 1),
       (5, 0),
       (5, 3);

insert into LIKES(FILM_ID, USER_ID)
values (0, 2),
       (1, 1),
       (2, 2),
       (1, 0),
       (1, 2),
       (3, 2),
       (3, 1),
       (3, 0),
       (4, 1),
       (4, 2),
       (5, 0);

insert into STATUS(STATUS, STATUS_ID)
values (true, 0),
       (false, 1);

insert into FRIENDS(FRIEND_ID, USER_ID, STATUS_ID)
values (0, 1, 1),
       (1, 2, 0),
       (0, 2, 1),
       (2, 0, 1),
       (2, 1, 0);*/
