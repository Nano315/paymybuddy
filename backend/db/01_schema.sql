CREATE TABLE `users` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(50) UNIQUE NOT NULL,
  `email` VARCHAR(255) UNIQUE NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `user_connections` (
  `user_id` INT NOT NULL,
  `connection_id` INT NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP),
  PRIMARY KEY (`user_id`, `connection_id`)
);

CREATE TABLE `transactions` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `sender_id` INT NOT NULL,
  `receiver_id` INT NOT NULL,
  `description` VARCHAR(255),
  `amount` DECIMAL(12,2) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

CREATE INDEX `idx_tx_sender` ON `transactions` (`sender_id`);

CREATE INDEX `idx_tx_receiver` ON `transactions` (`receiver_id`);

ALTER TABLE `user_connections` ADD CONSTRAINT `fk_uc_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

ALTER TABLE `user_connections` ADD CONSTRAINT `fk_uc_connection` FOREIGN KEY (`connection_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

ALTER TABLE `transactions` ADD CONSTRAINT `fk_tx_sender` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`);

ALTER TABLE `transactions` ADD CONSTRAINT `fk_tx_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`);
