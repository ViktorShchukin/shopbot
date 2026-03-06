CREATE TABLE pool_info(
    id UUID,
    user_id UUID NOT NULL,
    pool_type text NOT NULL,
    pool_depth double precision NOT NULL DEFAULT 0,
    pool_length double precision NOT NULL DEFAULT 0,
    pool_width double precision NOT NULL DEFAULT 0,
    pool_diameter double precision NOT NULL DEFAULT 0,
    pool_volume double precision NOT NULL DEFAULT 0,

    CONSTRAINT pk_pool_info PRIMARY KEY (id),
    CONSTRAINT fk_pool_info_app_user FOREIGN KEY (user_id) REFERENCES app_user (id),
    CONSTRAINT uniq_pool_info UNIQUE (user_id)
);