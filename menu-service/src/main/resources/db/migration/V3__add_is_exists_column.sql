ALTER TABLE pizzas
    ADD is_exists BOOLEAN;

ALTER TABLE pizzas
    ALTER COLUMN is_exists SET NOT NULL;