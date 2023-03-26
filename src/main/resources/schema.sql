create table if not exists FILMS
(
    FILM_ID      INTEGER           not null,
    NAME         CHARACTER VARYING not null,
    DESCRIPTION  CHARACTER VARYING(200),
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    constraint "FILMS_pk"
        primary key (FILM_ID)
);

create table if not exists GENRE
(
    GENRE_ID INTEGER auto_increment primary key,
    NAME     CHARACTER VARYING not null
);

create table if not exists FILM_GENRE
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    constraint "film_genre_FILMS_film_id_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "film_genre_GENRE_GENRE_ID_fk"
        foreign key (GENRE_ID) references GENRE
);

create table if not exists RATINGS
(
    RATING_ID INTEGER     not null,
    NAME      VARCHAR(20) not null,
    constraint "RATINGS_pk"
        primary key (RATING_ID)
);

create table if not exists RATING_FILM
(
    RATING_ID INTEGER not null,
    FILM_ID   INTEGER not null,
    constraint "rating_film_FILMS_film_id_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "rating_film_RATINGS_RATING_ID_fk"
        foreign key (RATING_ID) references RATINGS
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
    STATUS_ID INTEGER,
    constraint "FRIENDS_USERS_FRIEND_ID_USER_ID"
        foreign key (FRIEND_ID) references USERS (USER_ID),
    constraint "FRIENDS_USERS_USER_ID_USER_ID"
        foreign key (USER_ID) references USERS (USER_ID),
    constraint "FRIENDS_STATUS_STATUS_ID_STATUS_ID"
        FOREIGN KEY (STATUS_ID) references STATUS (STATUS_ID),
    constraint unique_friend_status UNIQUE (USER_ID, FRIEND_ID, STATUS_ID)
);