CREATE TABLE episodes (
    id INT PRIMARY KEY,
    podcast_id INT NOT NULL,
    episode_id INT, 
    episode_name VARCHAR(128) NOT NULL,
    FOREIGN KEY (podcast_id) REFERENCES podcast(id)
);

