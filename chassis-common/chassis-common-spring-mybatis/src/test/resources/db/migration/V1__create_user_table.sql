CREATE TABLE IF NOT EXISTS `user`
(
    `id`         BIGINT       NOT NULL PRIMARY KEY
        COMMENT '主键id',
    `create_at`  TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
        COMMENT '记录创建时间',
    `update_at`  TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3)
        COMMENT '记录更新时间',
    `is_deleted` BOOLEAN      NOT NULL DEFAULT FALSE
        COMMENT '标识记录是否删除',
    `name`       VARCHAR(32)  NULL
        COMMENT '姓名'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
