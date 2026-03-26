ALTER TABLE user_telegram_info ADD COLUMN created_at TIMESTAMPTZ NOT NULL DEFAULT now();
ALTER TABLE user_telegram_info ADD COLUMN updated_at TIMESTAMPTZ NOT NULL DEFAULT now();