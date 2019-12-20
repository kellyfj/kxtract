CREATE TABLE episodes (
    id INT NOT NULL AUTO_INCREMENT,
    podcast_id INT NOT NULL,
    episode_id INT, 
    episode_name VARCHAR(128) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (podcast_id) REFERENCES podcast(id)
);

