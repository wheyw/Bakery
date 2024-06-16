CREATE TABLE IF NOT EXISTS bakery (
    id SERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    manufacturer VARCHAR(120) NOT NULL,
    imageURL TEXT NOT NULL DEFAULT 'https://via.placeholder.com/170',
    description TEXT,
    price NUMERIC(10, 2) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    discount_percent INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT false
);
