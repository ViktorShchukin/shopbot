CREATE TABLE user_order (
    id UUID,
    user_id UUID NOT NULL,

    CONSTRAINT user_order_pk PRIMARY KEY (id),
    CONSTRAINT user_order_fk_user_id FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE order_and_product (
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL,

    CONSTRAINT order_and_product_fk_order_id FOREIGN KEY (order_id) REFERENCES user_order (id),
    CONSTRAINT order_and_product_basket_fk_product_id FOREIGN KEY (product_id) REFERENCES product (id),
    CONSTRAINT order_and_product_unique_order_id_product_id UNIQUE (order_id, product_id)
)