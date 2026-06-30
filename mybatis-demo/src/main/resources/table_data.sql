
create database student_db;

use student_db;
-- 创建班级表
CREATE TABLE `clazz` (
                         `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '班级ID',
                         `name` VARCHAR(100) NOT NULL COMMENT '班级名称',
                         `status` INT NOT NULL DEFAULT 0 COMMENT '状态：0正在上学，1已毕业',
                         `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         PRIMARY KEY (`id`),
                         INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班级表';

-- 创建学生表
CREATE TABLE `student` (
                           `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '学生ID',
                           `name` VARCHAR(50) NOT NULL COMMENT '学生姓名',
                           `birthday` DATE COMMENT '出生日期',
                           `clazz_id` BIGINT COMMENT '班级ID，关联clazz表',
                           `status` INT NOT NULL DEFAULT 0 COMMENT '状态：0正在上学，1已经毕业，2休学肄业',
                           `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           PRIMARY KEY (`id`),
                           INDEX `idx_clazz_id` (`clazz_id`),
                           INDEX `idx_status` (`status`),
                           CONSTRAINT `fk_student_clazz` FOREIGN KEY (`clazz_id`) REFERENCES `clazz` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生表';

-- 插入班级数据
INSERT INTO `clazz` (`id`, `name`, `status`, `create_time`) VALUES
                                                                (1, '计算机科学与技术1班', 0, '2021-09-01 08:00:00'),
                                                                (2, '软件工程2班', 0, '2021-09-01 08:00:00'),
                                                                (3, '数据科学与大数据3班', 1, '2018-09-01 08:00:00'),
                                                                (4, '人工智能4班', 0, '2022-09-01 08:00:00'),
                                                                (5, '网络工程5班', 1, '2019-09-01 08:00:00');

-- 插入学生数据
INSERT INTO `student` (`id`, `name`, `birthday`, `clazz_id`, `status`, `create_time`) VALUES
                                                                                          (1, '张三', '2000-05-15', 1, 0, '2021-09-01 10:00:00'),
                                                                                          (2, '李四', '2000-08-22', 1, 0, '2021-09-01 10:00:00'),
                                                                                          (3, '王五', '1999-12-10', 2, 0, '2021-09-01 10:00:00'),
                                                                                          (4, '赵六', '2000-03-18', 2, 1, '2018-09-01 10:00:00'),
                                                                                          (5, '小明', '2001-01-25', 3, 1, '2018-09-01 10:00:00'),
                                                                                          (6, '小红', '2000-07-30', 3, 2, '2018-09-01 10:00:00'),
                                                                                          (7, '小刚', '2001-04-12', 4, 0, '2022-09-01 10:00:00'),
                                                                                          (8, '小丽', '2001-09-08', 4, 0, '2022-09-01 10:00:00'),
                                                                                          (9, '小强', '2000-11-20', 5, 1, '2019-09-01 10:00:00'),
                                                                                          (10, '小美', '2001-02-28', 5, 1, '2019-09-01 10:00:00');