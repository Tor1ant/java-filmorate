drop table LIKES;
drop table FILM_GENRE;
drop table FILMS;
drop table RATINGS;
drop table GENRE;
drop table FRIENDS;
drop table USERS;
drop table STATUS;

create table if not exists RATINGS
(
    RATING_ID INTEGER     not null,
    NAME      VARCHAR(20) not null,
    constraint "RATINGS_pk"
        primary key (RATING_ID)
);

INSERT INTO RATINGS(RATING_ID, NAME)
VALUES (0, 'G'),
       (1, 'PG'),
       (2, 'PG13'),
       (3, 'R'),
       (4, 'NC17');

create table if not exists FILMS
(
    FILM_ID      INTEGER           not null,
    NAME         CHARACTER VARYING not null,
    DESCRIPTION  CHARACTER VARYING(200),
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    RATING_ID    INTEGER,
    constraint "FILMS_pk"
        primary key (FILM_ID),
    constraint "FILMS_RATINGS_RATING_ID_RATING_ID"
        foreign key (RATING_ID) references RATINGS
);

create table if not exists GENRE
(
    GENRE_ID INTEGER auto_increment primary key,
    NAME     CHARACTER VARYING not null
);

INSERT INTO GENRE(GENRE_ID, NAME)
VALUES (0, 'боевик'),
       (1, 'вестерн'),
       (2, 'детектив'),
       (3, 'исторический'),
       (4, 'драма'),
       (5, 'комедия'),
       (6, 'мелодрама'),
       (7, 'приключения'),
       (8, 'триллеры'),
       (9, 'ужасы'),
       (10, 'фантастика');


create table if not exists FILM_GENRE
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    constraint "film_genre_FILMS_film_id_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "film_genre_GENRE_GENRE_ID_fk"
        foreign key (GENRE_ID) references GENRE
);

create table if not exists USERS
(
    USER_ID  INTEGER not null,
    EMAIL    VARCHAR not null,
    LOGIN    VARCHAR not null,
    NAME     VARCHAR,
    BIRTHDAY date    not null,
    constraint "USERS_pk"
        primary key (USER_ID)
);

create table if not exists LIKES
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    constraint "LIKES_FILMS_film_id_fk"
        foreign key (FILM_ID) REFERENCES FILMS,
    constraint "LIKES_USERS_user_id_fk"
        foreign key (USER_ID) REFERENCES USERS
);

create table if not exists STATUS
(
    STATUS_ID INTEGER,
    STATUS    BOOLEAN,
    constraint "STATUS_pk"
        primary key (STATUS_ID),
    constraint unique_status UNIQUE (STATUS)
);

create table if not exists FRIENDS
(
    FRIEND_ID INTEGER,
    USER_ID   INTEGER,
    STATUS_ID INTEGER DEFAULT 1,
    constraint "FRIENDS_USERS_FRIEND_ID_USER_ID"
        foreign key (FRIEND_ID) references USERS (USER_ID),
    constraint "FRIENDS_USERS_USER_ID_USER_ID"
        foreign key (USER_ID) references USERS (USER_ID),
    constraint "FRIENDS_STATUS_STATUS_ID_STATUS_ID"
        FOREIGN KEY (STATUS_ID) references STATUS (STATUS_ID),
    constraint unique_friend_status UNIQUE (USER_ID, FRIEND_ID, STATUS_ID)
);