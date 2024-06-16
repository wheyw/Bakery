CREATE TABLE IF NOT EXISTS favorite_item (
    id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    bakery_id INT NOT NULL
);