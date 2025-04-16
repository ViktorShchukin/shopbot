CREATE TABLE app_user (
    id UUID,
    login TEXT,
    last_state TEXT NOT NULL,

    CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE user_telegram_info (
    telegram_id BIGINT,
    user_id UUID NOT NULL,

    CONSTRAINT user_telegram_info_pk PRIMARY KEY (telegram_id),
    CONSTRAINT user_telegram_info_user_fk FOREIGN KEY (user_id) REFERENCES app_user (id)
);