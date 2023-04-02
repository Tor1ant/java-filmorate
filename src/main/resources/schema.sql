create table if not exists RATINGS
(
    RATING_ID INT  PRIMARY KEY,
    NAME      VARCHAR(20) not null
);

MERGE INTO RATINGS (RATING_ID,NAME)
    VALUES (1,'G'),
           (2,'PG'),
           (3,'PG-13'),
           (4,'R'),
           (5,'NC-17');


create table if not exists FILMS
(
    FILM_ID      INTEGER auto_increment,
    NAME         CHARACTER VARYING      not null,
    DESCRIPTION  CHARACTER VARYING(200),
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    RATING_ID    INTEGER,
    constraint "FILMS_pk"
        primary key (FILM_ID),
    constraint "FILMS_RATINGS_RATING_ID_RATING_ID"
        foreign key (RATING_ID) references RATINGS
);

create table if not exists GENRES
(
    GENRE_ID INTEGER  primary key,
    NAME     VARCHAR not null
);

MERGE INTO GENRES (GENRE_ID,NAME)
    VALUES (1,'Комедия'),
           (2,'Драма'),
           (3,'Мультфильм'),
           (4,'Триллер'),
           (5,'Документальный'),
           (6,'Боевик');

create table if not exists FILM_GENRE
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    constraint "film_genre_FILMS_film_id_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "film_genre_GENRE_GENRE_ID_fk"
        foreign key (GENRE_ID) references GENRES
);

create table if not exists USERS
(
    USER_ID  INTEGER primary key auto_increment not null,
    EMAIL    VARCHAR                            not null,
    LOGIN    VARCHAR                            not null,
    NAME     VARCHAR,
    BIRTHDAY date                               not null,
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

MERGE INTO STATUS (STATUS_ID)
    VALUES (1),
           (2);

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

