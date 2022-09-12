CREATE TABLE users(
    id SERIAL NOT NULL PRIMARY KEY,
    username VARCHAR NOT NULL UNIQUE,
    email VARCHAR NOT NULL UNIQUE,
    phone VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL
);

CREATE TABLE genre(
    id SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE TABLE movie(
    id SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    premiere_date TIMESTAMP NOT NULL,
    UNIQUE (name, premiere_date)
);

CREATE TABLE movie_genre(
    movie_id INT REFERENCES movie(id),
    genre_id INT REFERENCES genre(id)
);

CREATE TABLE cinema_hall(
    id SERIAL NOT NULL PRIMARY KEY,
    name varchar NOT NULL UNIQUE,
    rows_number INT NOT NULL CHECK (rows_number > 0),
    seats_per_row INT NOT NULL CHECK (seats_per_row > 0)
);

CREATE TABLE sessions(
    id SERIAL NOT NULL PRIMARY KEY,
    movie_id INT REFERENCES movie(id),
    cinema_hall INT REFERENCES cinema_hall(id),
    date TIMESTAMP NOT NULL
);

CREATE TABLE ticket(
    id SERIAL NOT NULL PRIMARY KEY,
    session_id INT REFERENCES sessions(id),
    user_id INT REFERENCES users(id),
    row INT NOT NULL,
    seat INT NOT NULL
);

