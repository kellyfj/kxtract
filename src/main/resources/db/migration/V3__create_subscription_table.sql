CREATE TABLE subscription (
    id INT PRIMARY KEY,
    user_id VARCHAR(64) NOT NULL,
    podcast_id INT NOT NULL,
    FOREIGN KEY (podcast_id) REFERENCES podcast(id)
);
