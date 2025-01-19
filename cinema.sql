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


create or replace view view_all_movies as
select
m.movie_id,
m.title,
string_agg(DISTINCT g.genre, ' | ') genres
from movie_genres
natural join movies m
natural join genres g
group by m.movie_id;