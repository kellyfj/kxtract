CREATE TABLE transcription (
    id INT NOT NULL AUTO_INCREMENT,
    episode_id INT NOT NULL,
    s3_transcription_download_location VARCHAR(4096), 
    raw_transcription MEDIUMTEXT,
    formatted_transcription MEDIUMTEXT,
    PRIMARY KEY (id),
    FOREIGN KEY (episode_id) REFERENCES episode(id),
    CONSTRAINT unique_episode_id UNIQUE (episode_id)
);

