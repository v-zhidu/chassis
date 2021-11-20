CREATE TABLE IF NOT EXISTS `leaf_alloc`
(
    `biz_tag`     varchar(128) NOT NULL DEFAULT '',
    `max_id`      bigint(20)   NOT NULL DEFAULT '1',
    `step`        int(11)      NOT NULL,
    `description` varchar(256) NOT NULL DEFAULT '',
    `update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`biz_tag`)
) ENGINE = InnoDB;

INSERT INTO `leaf_alloc` (biz_tag, max_id, step, description)
VALUES ('leaf-segment-test', 1, 2000, 'Test leaf Segment ModeGet Id');