-- Dodati Status tablicu koja ce biti vezana kao constraint na status_id stupac

CREATE TABLE IF NOT EXISTS status (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

INSERT INTO status (name) VALUES ('INACTIVE');
INSERT INTO status (name) VALUES ('ACTIVE');

ALTER TABLE users DROP COLUMN status;
ALTER TABLE users ADD COLUMN status_id INT;
ALTER TABLE users ADD CONSTRAINT fk_status FOREIGN KEY (status_id) REFERENCES status(id);
ALTER TABLE users ALTER COLUMN status_id SET NOT NULL;