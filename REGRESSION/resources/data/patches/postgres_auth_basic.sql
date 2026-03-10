INSERT INTO users (id, username, password, role, is_account_non_expired, is_account_non_locked,
                   is_credentials_non_expired, is_enabled)
VALUES (1, 'testlum', '$2a$10$SpJVfGc/r1P/4EvbQHgQ2.MxbhYtCZRRcPMi4LmClL8hZg3jc4TJ2', 'ADMIN', true, true, true, true),
       (2, 'admin', '$2a$10$vheiYKTWyagT1qhPEp.oB.MypeXWFsi6qCqE1oPpradIRWjJM07uG ', 'ADMIN', true, true, true, true);