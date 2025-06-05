ALTER TABLE user_telegram_info ADD COLUMN first_name TEXT;

ALTER TABLE user_telegram_info ADD COLUMN last_name TEXT;

ALTER TABLE user_telegram_info ADD COLUMN user_name TEXT;

ALTER TABLE user_telegram_info ADD COLUMN updated BOOLEAN NOT NULL DEFAULT FALSE;