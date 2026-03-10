INSERT INTO users (id, username, password, role, is_account_non_expired, is_account_non_locked,
                   is_credentials_non_expired, is_enabled)
VALUES (1, 'testlum', 'testlum123', 'ADMIN', true, true, true, true),
       (2, 'admin', 'admin123', 'ADMIN', true, true, true, true);