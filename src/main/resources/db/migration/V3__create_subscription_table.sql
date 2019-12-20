CREATE TABLE subscription (
    id INT NOT NULL AUTO_INCREMENT,
    user_id VARCHAR(64) NOT NULL,
    podcast_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (podcast_id) REFERENCES podcast(id)
);
