CREATE TABLE genres(
    genre VARCHAR(50) NOT NULL,
    genre_id SERIAL PRIMARY KEY
);

CREATE TABLE movies(
    movie_id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL
);

CREATE TABLE movie_genres(
    movie_id INTEGER REFERENCES movies(movie_id),
	genre_id INTEGER REFERENCES genres(genre_id),
    PRIMARY KEY(movie_id, genre_id)
);
