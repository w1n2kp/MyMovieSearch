DROP PROCEDURE IF EXISTS MovieSearch.sp_create_movie;
SET NAMES UTF8;

CREATE DEFINER=`sa`@`%` PROCEDURE `sp_create_movie`(
  title varchar(255), original_title VARCHAR(255), year INT, plot TEXT, rating FLOAT, filename VARCHAR(255), actors VARCHAR(255), genres VARCHAR(255), directors VARCHAR(255), countries VARCHAR(255)
)
BEGIN 
  
    INSERT INTO Movies (title, year, plot, original_title, rating, filename) VALUES (title, year, plot, original_title, rating, filename);
    SET @movie_id = LAST_INSERT_ID();
    
    SET @genre = '';
    SET @genres = genres;
  
    
    SET @c = 0;
    WHILE @c < func_get_split_string_total(genres, ',')
      DO 
        SET @c = @c + 1;
        SET @genre = func_get_split_string(genres, ',', @c);
        SET @id = (SELECT id FROM Genre WHERE genre = @genre);
      
        IF NOT @id  IS NULL THEN 
            INSERT INTO movie_genre (movie_id, genre_id) VALUES (@movie_id, @id);
        ELSE
          INSERT INTO Genre (genre) VALUES (@genre);
          INSERT INTO movie_genre (movie_id, genre_id) VALUES (@movie_id, last_insert_id());
        END IF;
      END WHILE;


    SET @actor = '';
    SET @actors = Actors;
  
  
    SET @c = 0;
    WHILE @c < func_get_split_string_total(@actors, ',')
    DO
      SET @c = @c + 1;
      SET @actor = func_get_split_string(@actors, ',', @c);
      SET @id = (SELECT id FROM Actor WHERE name = @actor);
  
      IF NOT @id  IS NULL THEN
        INSERT INTO movie_actor (movie_id, actor_id) VALUES (@movie_id, @id);
      ELSE
        INSERT INTO Actor (name) VALUES (@actor);
        INSERT INTO movie_actor (movie_id, actor_id) VALUES (@movie_id, last_insert_id());
      END IF;
    END WHILE;

    SET @country = '';
    SET @countrys = Countrys;
  
  
    SET @c = 0;
    WHILE @c < func_get_split_string_total(@countrys, ',')
    DO
      SET @c = @c + 1;
      SET @country = func_get_split_string(@countrys, ',', @c);
      SET @id = (SELECT id FROM Country WHERE name = @country);
  
      IF NOT @id  IS NULL THEN
        INSERT INTO movie_country (movie_id, country_id) VALUES (@movie_id, @id);
      ELSE
        INSERT INTO Country (name) VALUES (@country);
        INSERT INTO movie_country (movie_id, country_id) VALUES (@movie_id, last_insert_id());
      END IF;
    END WHILE;

    SET @director = '';
    SET @directors = Directors;
  
  
    SET @c = 0;
    WHILE @c < func_get_split_string_total(@directors, ',')
    DO
      SET @c = @c + 1;
      SET @director = func_get_split_string(@directors, ',', @c);
      SET @id = (SELECT id FROM Director WHERE name = @director);
  
      IF NOT @id  IS NULL THEN
        INSERT INTO movie_director (movie_id, director_id) VALUES (@movie_id, @id);
      ELSE
        INSERT INTO Director (name) VALUES (@director);
        INSERT INTO movie_director (movie_id, director_id) VALUES (@movie_id, last_insert_id());
      END IF;
    END WHILE;
  END;