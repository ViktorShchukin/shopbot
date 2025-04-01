CREATE TABLE product (
    id UUID NOT NULL,
    name TEXT NOT NULL,
    cost BIGINT NOT NULL,
    description TEXT,

    CONSTRAINT product_pk PRIMARY KEY (id),
    CONSTRAINT product_uc UNIQUE (name)
)