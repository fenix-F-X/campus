-- =============================================
-- 校园π (Campus Pi) 数据库初始化脚本
-- 执行前提：若依框架数据库已初始化完毕
-- 执行方式：mysql -u root -p ry-vue < campus_init.sql
-- =============================================

-- -----------------------------------------------
-- 第一部分：业务表创建
-- -----------------------------------------------

-- 任务表
CREATE TABLE IF NOT EXISTS campus_task (
                                           task_id       BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '任务ID',
                                           title         VARCHAR(100) NOT NULL COMMENT '任务标题',
                                           content       TEXT COMMENT '任务描述',
                                           publisher_id  BIGINT NOT NULL COMMENT '发布人ID',
                                           dept_id       BIGINT COMMENT '所属部门/社团',
                                           status        CHAR(1) DEFAULT '0' COMMENT '0待接取 1进行中 2待审核 3已完成 4已拒绝',
                                           reward_points INT DEFAULT 0 COMMENT '积分奖励',
                                           deadline      DATETIME COMMENT '截止时间',
                                           create_time   DATETIME DEFAULT NOW(),
                                           update_time   DATETIME DEFAULT NOW() ON UPDATE NOW(),
                                           del_flag      CHAR(1) DEFAULT '0'
) ENGINE=InnoDB COMMENT='校园任务表';

-- 任务接取记录表
CREATE TABLE IF NOT EXISTS campus_task_record (
                                                  record_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                  task_id     BIGINT NOT NULL COMMENT '任务ID',
                                                  taker_id    BIGINT NOT NULL COMMENT '接取人ID',
                                                  status      CHAR(1) DEFAULT '1' COMMENT '1进行中 2提交审核 3通过 4拒绝',
                                                  submit_desc TEXT COMMENT '提交说明',
                                                  submit_time DATETIME COMMENT '提交时间',
                                                  audit_time  DATETIME COMMENT '审核时间',
                                                  auditor_id  BIGINT COMMENT '审核人ID',
                                                  audit_remark VARCHAR(500) COMMENT '审核备注',
                                                  create_time DATETIME DEFAULT NOW()
) ENGINE=InnoDB COMMENT='任务接取记录';

-- 校园公告表
CREATE TABLE IF NOT EXISTS campus_notice (
                                             notice_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
                                             title       VARCHAR(200) NOT NULL,
                                             content     TEXT,
                                             type        CHAR(1) COMMENT '1通知 2活动 3紧急',
                                             publisher_id BIGINT NOT NULL,
                                             view_count  INT DEFAULT 0 COMMENT '浏览量',
                                             is_top      CHAR(1) DEFAULT '0' COMMENT '是否置顶',
                                             status      CHAR(1) DEFAULT '1' COMMENT '0草稿 1发布',
                                             create_time DATETIME DEFAULT NOW(),
                                             update_time DATETIME DEFAULT NOW() ON UPDATE NOW(),
                                             del_flag    CHAR(1) DEFAULT '0'
) ENGINE=InnoDB COMMENT='校园公告表';

-- 信息交流帖子表
CREATE TABLE IF NOT EXISTS campus_post (
                                           post_id     BIGINT PRIMARY KEY AUTO_INCREMENT,
                                           title       VARCHAR(200) NOT NULL,
                                           content     TEXT,
                                           author_id   BIGINT NOT NULL,
                                           category    VARCHAR(50) COMMENT '分类：失物招领/求助/闲置等',
                                           view_count  INT DEFAULT 0,
                                           like_count  INT DEFAULT 0,
                                           status      CHAR(1) DEFAULT '1',
                                           create_time DATETIME DEFAULT NOW(),
                                           update_time DATETIME DEFAULT NOW() ON UPDATE NOW(),
                                           del_flag    CHAR(1) DEFAULT '0'
) ENGINE=InnoDB COMMENT='信息交流帖子';

-- 帖子评论表
CREATE TABLE IF NOT EXISTS campus_post_comment (
                                                   comment_id  BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                   post_id     BIGINT NOT NULL,
                                                   author_id   BIGINT NOT NULL,
                                                   content     VARCHAR(1000) NOT NULL,
                                                   parent_id   BIGINT DEFAULT 0 COMMENT '父评论ID，0为顶级',
                                                   create_time DATETIME DEFAULT NOW(),
                                                   del_flag    CHAR(1) DEFAULT '0'
) ENGINE=InnoDB COMMENT='帖子评论';

-- -----------------------------------------------
-- 第二部分：菜单权限配置
-- -----------------------------------------------

-- 一级菜单：校园π
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_time)
VALUES (2000, '校园π', 0, 5, 'campus', NULL, 'M', '0', '0', '', 'education', NOW());

-- 任务管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_time)
VALUES (2001, '任务管理', 2000, 1, 'task', 'campus/task/index', 'C', '0', '0', 'campus:task:list', 'list', NOW());

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_time) VALUES
                                                                                                                                           (2011, '任务查询', 2001, 1, '', NULL, 'F', '0', '0', 'campus:task:list',   '#', NOW()),
                                                                                                                                           (2012, '任务新增', 2001, 2, '', NULL, 'F', '0', '0', 'campus:task:add',    '#', NOW()),
                                                                                                                                           (2013, '任务修改', 2001, 3, '', NULL, 'F', '0', '0', 'campus:task:edit',   '#', NOW()),
                                                                                                                                           (2014, '任务删除', 2001, 4, '', NULL, 'F', '0', '0', 'campus:task:remove', '#', NOW());

-- 公告管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_time)
VALUES (2002, '公告管理', 2000, 2, 'notice', 'campus/notice/index', 'C', '0', '0', 'campus:notice:list', 'message', NOW());

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_time) VALUES
                                                                                                                                           (2021, '公告查询', 2002, 1, '', NULL, 'F', '0', '0', 'campus:notice:list',   '#', NOW()),
                                                                                                                                           (2022, '公告新增', 2002, 2, '', NULL, 'F', '0', '0', 'campus:notice:add',    '#', NOW()),
                                                                                                                                           (2023, '公告修改', 2002, 3, '', NULL, 'F', '0', '0', 'campus:notice:edit',   '#', NOW()),
                                                                                                                                           (2024, '公告删除', 2002, 4, '', NULL, 'F', '0', '0', 'campus:notice:remove', '#', NOW());

-- 信息交流
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_time)
VALUES (2003, '信息交流', 2000, 3, 'post', 'campus/post/index', 'C', '0', '0', 'campus:post:list', 'chat', NOW());

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_time) VALUES
                                                                                                                                           (2031, '帖子查询', 2003, 1, '', NULL, 'F', '0', '0', 'campus:post:list',   '#', NOW()),
                                                                                                                                           (2032, '帖子新增', 2003, 2, '', NULL, 'F', '0', '0', 'campus:post:add',    '#', NOW()),
                                                                                                                                           (2033, '帖子修改', 2003, 3, '', NULL, 'F', '0', '0', 'campus:post:edit',   '#', NOW()),
                                                                                                                                           (2034, '帖子删除', 2003, 4, '', NULL, 'F', '0', '0', 'campus:post:remove', '#', NOW());

-- -----------------------------------------------
-- 第三部分：角色与权限分配
-- -----------------------------------------------

-- 创建辅导员、学生角色
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, status, del_flag, create_time, remark)
VALUES
    (100, '辅导员', 'counselor', 2, '1', '0', '0', NOW(), '辅导员角色'),
    (101, '学生',   'student',   3, '1', '0', '0', NOW(), '学生角色');

-- 辅导员权限：任务全部 + 公告全部 + 帖子管理
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
                                                 (100, 2000),(100, 2001),(100, 2011),(100, 2012),(100, 2013),(100, 2014),
                                                 (100, 2002),(100, 2021),(100, 2022),(100, 2023),(100, 2024),
                                                 (100, 2003),(100, 2031),(100, 2033),(100, 2034);

-- 学生权限：任务查询 + 帖子查询/新增
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
                                                 (101, 2000),(101, 2001),(101, 2011),
                                                 (101, 2003),(101, 2031),(101, 2032);

-- -----------------------------------------------
-- 第四部分：测试数据（可选）
-- -----------------------------------------------

-- 插入测试任务
INSERT INTO campus_task (title, content, publisher_id, status, reward_points, deadline) VALUES
                                                                                            ('图书馆志愿者招募', '需要5名志愿者协助整理图书', 1, '0', 10, DATE_ADD(NOW(), INTERVAL 7 DAY)),
                                                                                            ('校园环保活动', '周末校园清洁活动，欢迎参与', 1, '0', 15, DATE_ADD(NOW(), INTERVAL 3 DAY));

-- 插入测试公告
INSERT INTO campus_notice (title, content, type, publisher_id, is_top, view_count) VALUES
                                                                                       ('校园网络维护通知', '本周六晚22:00-24:00进行网络升级', '1', 1, '1', 0),
                                                                                       ('社团招新开始啦', '各大社团开始招新，欢迎新生加入', '2', 1, '1', 0);

-- 插入测试帖子
INSERT INTO campus_post (title, content, author_id, category, view_count, like_count) VALUES
                                                                                          ('失物招领：黑色钱包', '在图书馆三楼拾到黑色钱包一个', 1, '失物招领', 0, 0),
                                                                                          ('求助：高数作业讨论', '第三章习题有人一起讨论吗', 1, '求助', 0, 0);