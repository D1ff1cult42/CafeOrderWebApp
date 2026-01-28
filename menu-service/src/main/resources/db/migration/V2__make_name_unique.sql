ALTER TABLE pizzas
    ADD CONSTRAINT uc_pizzas_name UNIQUE (name);