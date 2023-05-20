CREATE TABLE `order_info` (
	`order_id` INT(11) NOT NULL AUTO_INCREMENT,
	`user_id` INT(11) NOT NULL,
	`trade_type` CHAR(2) NOT NULL,
	`trade_status` CHAR(2) NOT NULL,
	`create_time` DATETIME NOT NULL,
	PRIMARY KEY (`order_id`),
)
COMMENT='订单表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;
