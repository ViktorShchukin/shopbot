ALTER TABLE user_order ADD COLUMN distribution_mode TEXT;
ALTER TABLE user_order ADD COLUMN phone_number TEXT;
ALTER TABLE user_order ADD COLUMN address TEXT;

ALTER TABLE user_telegram_info ADD COLUMN last_message_id BIGINT;