-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: 2018-06-18 17:15:39
-- 服务器版本： 8.0.11
-- PHP Version: 7.2.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Database: `atm`
--

-- --------------------------------------------------------

--
-- 表的结构 `cards`
--

CREATE TABLE `cards` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `available_balance` decimal(19,2) DEFAULT NULL,
  `card_number` varchar(20) DEFAULT NULL,
  `frozen_balance` decimal(19,2) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `total_balance` decimal(19,2) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `cards`
--

INSERT INTO `cards` (`id`, `created_at`, `updated_at`, `available_balance`, `card_number`, `frozen_balance`, `status`, `total_balance`, `type`, `user_id`) VALUES
  (1, '2018-06-18 16:42:48', '2018-06-18 16:42:48', '0.00', '0000000000001', '0.00', 1, '0.00', 2, 1),
  (2, '2018-06-18 16:59:51', '2018-06-18 16:59:51', '0.00', '0000000000002', '0.00', 1, '0.00', 3, 2),
  (3, '2018-06-18 17:00:02', '2018-06-18 17:00:02', '0.00', '4222927815478', '0.00', 1, '0.00', 0, 3),
  (5, '2018-06-18 17:13:16', '2018-06-18 17:13:16', '10000.00', '4151902185620', '100.00', 0, '10100.00', 0, 104),
  (6, '2018-06-18 17:13:53', '2018-06-18 17:13:53', '10000.00', '4357189044712', '100.00', 0, '10100.00', 0, 1000),
  (7, '2018-06-18 17:14:31', '2018-06-18 17:14:31', '10000.00', '4138360002079', '100.00', 0, '10100.00', 0, 1001);

-- --------------------------------------------------------

--
-- 表的结构 `currencies`
--

CREATE TABLE `currencies` (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `exchange_rate` decimal(19,2) DEFAULT NULL,
  `symbol` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `currencies`
--

INSERT INTO `currencies` (`id`, `code`, `exchange_rate`, `symbol`) VALUES
  (1, 'CNY', '1.00', '￥');

-- --------------------------------------------------------

--
-- 表的结构 `recharges`
--

CREATE TABLE `recharges` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `actual_amount` decimal(19,2) DEFAULT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `card_id` bigint(20) NOT NULL,
  `currency_id` bigint(20) NOT NULL,
  `transaction_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 表的结构 `roles`
--

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL,
  `name` varchar(60) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
  (2, 'ROLE_ADMIN'),
  (3, 'ROLE_SYSTEM'),
  (1, 'ROLE_USER');

-- --------------------------------------------------------

--
-- 表的结构 `transactions`
--

CREATE TABLE `transactions` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `actual_amount` decimal(19,2) DEFAULT NULL,
  `additional` json DEFAULT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `from_card_after_available_balance` decimal(19,2) DEFAULT NULL,
  `from_card_before_available_balance` decimal(19,2) DEFAULT NULL,
  `remark` longtext,
  `tax` decimal(19,2) DEFAULT NULL,
  `to_card_after_available_balance` decimal(19,2) DEFAULT NULL,
  `to_card_before_available_balance` decimal(19,2) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `zero` decimal(19,2) DEFAULT NULL,
  `currency_id` bigint(20) NOT NULL,
  `from_card_id` bigint(20) NOT NULL,
  `recharge_id` bigint(20) DEFAULT NULL,
  `to_card_id` bigint(20) NOT NULL,
  `withdraw_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 表的结构 `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `email` varchar(40) DEFAULT NULL,
  `name` varchar(40) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `username` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;

--
-- 转存表中的数据 `users`
--

INSERT INTO `users` (`id`, `created_at`, `updated_at`, `email`, `name`, `password`, `status`, `username`) VALUES
  (1, '2018-06-18 16:42:48', '2018-06-18 16:42:48', 'recharge@recharge', 'recharge', '', 2, 'recharge'),
  (2, '2018-06-18 16:59:51', '2018-06-18 16:59:51', 'withdraw@withdraw', 'withdraw', '', 2, 'withdraw'),
  (3, '2018-06-18 17:00:02', '2018-06-18 17:00:02', 'exchange@exchange', 'exchange', '', 2, 'exchange'),
  (104, '2018-06-18 17:13:16', '2018-06-18 17:13:16', 'admin@admin.admin', 'admin', '$2a$10$8G06XDB80GGkBI5vK.ZdceHIO.PDlPVrCLS6ndhlX7c1TBQBS.61C', 0, 'admin'),
  (1000, '2018-06-18 17:13:53', '2018-06-18 17:13:53', 'test@test.com', 'test', '$2a$10$PMouqrlhjw9bEjYO6zfhIeszUcF3UQVbtYvFRKKyeCMWXnLXOU4hC', 0, 'test'),
  (1001, '2018-06-18 17:14:31', '2018-06-18 17:14:31', 'test2@test2.com', 'test2', '$2a$10$7Cr4r1zzxBrQ6HDS.9p93.rkU59sjjBIdEVVhz5oOtmR6kSFFRCGG', 0, 'test2');

-- --------------------------------------------------------

--
-- 表的结构 `user_roles`
--

CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `user_roles`
--

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
  (104, 1),
  (1000, 1),
  (1001, 1),
  (104, 2),
  (1, 3),
  (2, 3),
  (3, 3);

-- --------------------------------------------------------

--
-- 表的结构 `withdraws`
--

CREATE TABLE `withdraws` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `actual_amount` decimal(19,2) DEFAULT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `card_id` bigint(20) NOT NULL,
  `currency_id` bigint(20) NOT NULL,
  `transaction_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cards`
--
ALTER TABLE `cards`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKcmanafgwbibfijy2o5isfk3d5` (`user_id`);

--
-- Indexes for table `currencies`
--
ALTER TABLE `currencies`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `recharges`
--
ALTER TABLE `recharges`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5a9srrp1gtrhrmq4agyujhmp` (`card_id`),
  ADD KEY `FK6njb7ujlurld737jfp30kagea` (`currency_id`),
  ADD KEY `FKstecyptha1lyaj2pbkuwh4que` (`transaction_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_nb4h0p6txrmfc0xbrd1kglp9t` (`name`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK812edr8o27pte306gvbmypytx` (`currency_id`),
  ADD KEY `FK2rjf7q3aokek0bc3817l80p77` (`from_card_id`),
  ADD KEY `FK33mv5qah87sngcyiha5js3dcb` (`recharge_id`),
  ADD KEY `FKrjipfg1tkqdhun7vvoe5m6rca` (`to_card_id`),
  ADD KEY `FKlhpkyi2twy0vffh2cggeeycjx` (`withdraw_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`);

--
-- Indexes for table `user_roles`
--
ALTER TABLE `user_roles`
  ADD PRIMARY KEY (`user_id`,`role_id`),
  ADD KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`);

--
-- Indexes for table `withdraws`
--
ALTER TABLE `withdraws`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK4uivss42sa29jw4j75lgqokgx` (`card_id`),
  ADD KEY `FKqoj9im5ywbv5fxm1xxmyu0132` (`currency_id`),
  ADD KEY `FKaeyl41g5nkx5f9spobemceakk` (`transaction_id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `cards`
--
ALTER TABLE `cards`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- 使用表AUTO_INCREMENT `currencies`
--
ALTER TABLE `currencies`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用表AUTO_INCREMENT `recharges`
--
ALTER TABLE `recharges`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- 使用表AUTO_INCREMENT `transactions`
--
ALTER TABLE `transactions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1002;

--
-- 使用表AUTO_INCREMENT `withdraws`
--
ALTER TABLE `withdraws`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- 限制导出的表
--

--
-- 限制表 `cards`
--
ALTER TABLE `cards`
  ADD CONSTRAINT `FKcmanafgwbibfijy2o5isfk3d5` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- 限制表 `recharges`
--
ALTER TABLE `recharges`
  ADD CONSTRAINT `FK5a9srrp1gtrhrmq4agyujhmp` FOREIGN KEY (`card_id`) REFERENCES `cards` (`id`),
  ADD CONSTRAINT `FK6njb7ujlurld737jfp30kagea` FOREIGN KEY (`currency_id`) REFERENCES `currencies` (`id`),
  ADD CONSTRAINT `FKstecyptha1lyaj2pbkuwh4que` FOREIGN KEY (`transaction_id`) REFERENCES `transactions` (`id`);

--
-- 限制表 `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `FK2rjf7q3aokek0bc3817l80p77` FOREIGN KEY (`from_card_id`) REFERENCES `cards` (`id`),
  ADD CONSTRAINT `FK33mv5qah87sngcyiha5js3dcb` FOREIGN KEY (`recharge_id`) REFERENCES `recharges` (`id`),
  ADD CONSTRAINT `FK812edr8o27pte306gvbmypytx` FOREIGN KEY (`currency_id`) REFERENCES `currencies` (`id`),
  ADD CONSTRAINT `FKlhpkyi2twy0vffh2cggeeycjx` FOREIGN KEY (`withdraw_id`) REFERENCES `withdraws` (`id`),
  ADD CONSTRAINT `FKrjipfg1tkqdhun7vvoe5m6rca` FOREIGN KEY (`to_card_id`) REFERENCES `cards` (`id`);

--
-- 限制表 `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  ADD CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- 限制表 `withdraws`
--
ALTER TABLE `withdraws`
  ADD CONSTRAINT `FK4uivss42sa29jw4j75lgqokgx` FOREIGN KEY (`card_id`) REFERENCES `cards` (`id`),
  ADD CONSTRAINT `FKaeyl41g5nkx5f9spobemceakk` FOREIGN KEY (`transaction_id`) REFERENCES `transactions` (`id`),
  ADD CONSTRAINT `FKqoj9im5ywbv5fxm1xxmyu0132` FOREIGN KEY (`currency_id`) REFERENCES `currencies` (`id`);
COMMIT;
