CREATE TABLE basket (
    id UUID,
    user_id UUID NOT NULL,

    CONSTRAINT basket_pk PRIMARY KEY (id),
    CONSTRAINT basket_fk_user_id FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE basket_and_product (
    basket_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL,

    CONSTRAINT basket_and_product_fk_basket_id FOREIGN KEY (basket_id) REFERENCES basket (id),
    CONSTRAINT basket_and_product_basket_fk_product_id FOREIGN KEY (product_id) REFERENCES product (id),
    CONSTRAINT basket_and_product_unique_basket_id_product_id UNIQUE (basket_id, product_id)
)