CREATE TABLE episode (
    id INT NOT NULL AUTO_INCREMENT,
    podcast_id INT NOT NULL,
    episode_id INT, 
    episode_name VARCHAR(128) NOT NULL,
    filesize_kb INT,
    origin_url VARCHAR(256),
    PRIMARY KEY (id),
    FOREIGN KEY (podcast_id) REFERENCES podcast(id),
    CONSTRAINT episode_name_unique UNIQUE(podcast_id, episode_name)
);

