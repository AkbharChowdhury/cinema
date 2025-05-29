CREATE TABLE genres(
    genre VARCHAR(50) NOT NULL,
    genre_id SERIAL PRIMARY KEY
);

CREATE TABLE movies(
    movie_id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL
);

CREATE TABLE movie_genres(
    movie_id INTEGER REFERENCES movies(movie_id) ON DELETE CASCADE,
	genre_id INTEGER REFERENCES genres(genre_id) ON DELETE CASCADE,
    PRIMARY KEY(movie_id, genre_id)
);
INSERT INTO genres(genre) VALUES
('Drama'),
('Adventure'),
('Animation'),
('Action'),
('Documentary'),
('Comedy'),
('Crime'),
('Sci-Fi'),
('Romance'),
('Children'),
('Horror'),
('Mystery'),
('Fantasy'),
('Musical');


-- unique genres
CREATE EXTENSION IF NOT EXISTS citext;
ALTER TABLE genres ALTER COLUMN genre TYPE citext;

-- or
CREATE UNIQUE INDEX genre_unique_idx on genres (LOWER(genre));


-- SELECT m.movie_id,
--    m.title,
--    string_agg(DISTINCT g.genre::text, ' | '::text) AS genres
--   FROM movie_genres
--     JOIN movies m USING (movie_id)
--     JOIN genres g USING (genre_id)
--  GROUP BY m.movie_id;


create or replace view view_all_movies as
select
m.movie_id,
m.title,
string_agg(DISTINCT g.genre, ' | ') genres
from movie_genres
natural join movies m
natural join genres g
group by m.movie_id;



CREATE OR REPLACE FUNCTION fn_get_movies(term VARCHAR, myGenre VARCHAR)
   RETURNS TABLE (
        movie_id INTEGER,
        title VARCHAR,
        genres VARCHAR
) 
AS 
$$
SELECT m.movie_id,
       m.title,
       string_agg(DISTINCT g.genre, ' | '
                  ORDER BY genre) genre_list
FROM movie_genres
NATURAL JOIN movies m
NATURAL JOIN genres g
WHERE title ILIKE term
GROUP BY m.movie_id
HAVING string_agg(DISTINCT g.genre, ' | ') ILIKE myGenre
$$
LANGUAGE sql;
SELECT * FROM fn_get_movies('%%','%%');








CREATE OR REPLACE VIEW available_movie_genres AS
    SELECT DISTINCT genre, mg.genre_id
    FROM movie_genres mg
    NATURAL JOIN genres g
    ORDER BY genre;
SELECT * FROM available_movie_genres;