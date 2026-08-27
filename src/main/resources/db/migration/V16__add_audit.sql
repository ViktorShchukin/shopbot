CREATE TABLE user_audit_log
(
    id        UUID        NOT NULL PRIMARY KEY,
    timestamp TIMESTAMPTZ NOT NULL DEFAULT now(),
    user_id      UUID        NOT NULL REFERENCES app_user (id),
    event    TEXT        NOT NULL,
    details   TEXT
);