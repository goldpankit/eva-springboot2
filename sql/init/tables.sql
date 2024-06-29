/*
 数据库类型    : MySQL
 数据库版本    : 8.0.32
*/
-- 创建数据库表
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- 系统用户
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user`
(
    `id`            int        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`      varchar(50)                             DEFAULT NULL COMMENT '用户名',
    `real_name`     varchar(50)                             DEFAULT NULL COMMENT '真实姓名',
    `emp_no`        varchar(50)                             DEFAULT NULL COMMENT '工号',
    `birthday`      date                                    DEFAULT NULL COMMENT '生日',
    `gender`        varchar(20)                             DEFAULT NULL COMMENT '性别',
    `email`         varchar(500)                            DEFAULT NULL COMMENT '邮箱',
    `email_digest`  varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱摘要',
    `mobile`        varchar(200)                            DEFAULT NULL COMMENT '手机号码',
    `mobile_digest` varchar(200)                            DEFAULT NULL COMMENT '手机号码摘要',
    `avatar`        varchar(500)                            DEFAULT NULL COMMENT '头像',
    `password`      varchar(64)                             DEFAULT NULL COMMENT '密码',
    `salt`          varchar(20)                             DEFAULT NULL COMMENT '密码盐',
    `fixed`         tinyint(1) NOT NULL                     DEFAULT '0' COMMENT '是否为固定用户',
    `created_by`    int                                     DEFAULT NULL COMMENT '创建人',
    `created_at`    datetime   NOT NULL COMMENT '创建时间',
    `updated_by`    int                                     DEFAULT NULL COMMENT '更新人',
    `updated_at`    datetime                                DEFAULT NULL COMMENT '更新时间',
    `deleted`       tinyint(1) NOT NULL                     DEFAULT '0' COMMENT '是否已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  AUTO_INCREMENT = 1000 COMMENT ='系统用户';

-- ----------------------------
-- 系统角色
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role`
(
    `id`         int         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code`       varchar(50) NOT NULL COMMENT '角色CODE',
    `name`       varchar(50) NOT NULL COMMENT '角色名称',
    `remark`     varchar(500)         DEFAULT NULL COMMENT '角色备注',
    `fixed`      tinyint(1)  NOT NULL DEFAULT '0' COMMENT '是否为固定角色',
    `created_by` int         NOT NULL COMMENT '创建人',
    `created_at` datetime    NOT NULL COMMENT '创建时间',
    `updated_by` int                  DEFAULT NULL COMMENT '更新人',
    `updated_at` datetime             DEFAULT NULL COMMENT '更新时间',
    `deleted`    tinyint(1)  NOT NULL DEFAULT '0' COMMENT '是否已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  AUTO_INCREMENT = 1000 COMMENT ='系统角色';

-- ----------------------------
-- 系统菜单
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu`
(
    `id`            int         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parent_id`     int                  DEFAULT NULL COMMENT '上一级菜单',
    `permission_id` int                  DEFAULT NULL COMMENT '权限ID',
    `name`          varchar(50) NOT NULL COMMENT '菜单名称',
    `type`          varchar(20)          DEFAULT NULL COMMENT '菜单类型',
    `uri`           varchar(200)         DEFAULT NULL COMMENT '菜单访问路径',
    `remark`        varchar(500)         DEFAULT NULL COMMENT '备注',
    `icon_id`       int                  DEFAULT NULL COMMENT '图标ID',
    `disabled`      tinyint(1)  NOT NULL DEFAULT '0' COMMENT '是否禁用',
    `sort`          int                  DEFAULT NULL COMMENT '排序',
    `created_by`    int         NOT NULL COMMENT '创建人',
    `created_at`    datetime    NOT NULL COMMENT '创建时间',
    `updated_by`    int                  DEFAULT NULL COMMENT '更新人',
    `updated_at`    datetime             DEFAULT NULL COMMENT '更新时间',
    `deleted`       tinyint(1)  NOT NULL DEFAULT '0' COMMENT '是否已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  AUTO_INCREMENT = 1000 COMMENT ='系统菜单';

-- ----------------------------
-- 系统菜单功能
-- ----------------------------
DROP TABLE IF EXISTS `system_menu_func`;
CREATE TABLE `system_menu_func`
(
    `id`            int         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `menu_id`       int         NOT NULL COMMENT '菜单ID',
    `permission_id` int                  DEFAULT NULL COMMENT '权限ID',
    `name`          varchar(50) NOT NULL COMMENT '功能名称',
    `remark`        varchar(500)         DEFAULT NULL COMMENT '功能备注',
    `created_by`    int         NOT NULL COMMENT '创建人',
    `created_at`    datetime    NOT NULL COMMENT '创建时间',
    `updated_by`    int                  DEFAULT NULL COMMENT '更新人',
    `updated_at`    datetime             DEFAULT NULL COMMENT '更新时间',
    `deleted`       tinyint(1)  NOT NULL DEFAULT '0' COMMENT '是否已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  AUTO_INCREMENT = 1000 COMMENT ='系统菜单功能';

-- ----------------------------
-- 系统菜单图标
-- ----------------------------
DROP TABLE IF EXISTS `system_menu_icon`;
CREATE TABLE `system_menu_icon`
(
    `id`          int         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        varchar(20) NOT NULL COMMENT '图标名称',
    `class_name`  varchar(100)         DEFAULT NULL COMMENT '图标CLASS',
    `uri`         varchar(500)         DEFAULT NULL COMMENT '网络路径',
    `access_type` varchar(10)          DEFAULT NULL COMMENT '访问类型，CLASS=样式;FILE_PATH=文件;URI=网络',
    `created_by`  int         NOT NULL COMMENT '创建人',
    `created_at`  datetime    NOT NULL COMMENT '创建时间',
    `updated_by`  int                  DEFAULT NULL COMMENT '更新人',
    `updated_at`  datetime             DEFAULT NULL COMMENT '更新时间',
    `deleted`     tinyint(1)  NOT NULL DEFAULT '0' COMMENT '是否已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  AUTO_INCREMENT = 1000 COMMENT ='系统菜单图标';

-- ----------------------------
-- 系统用户与角色的关联
-- ----------------------------
DROP TABLE IF EXISTS `system_user_role`;
CREATE TABLE `system_user_role`
(
    `id`         int        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`    int        NOT NULL COMMENT '用户ID',
    `role_id`    int        NOT NULL COMMENT '角色ID',
    `created_by` int        NOT NULL COMMENT '创建人',
    `created_at` datetime   NOT NULL COMMENT '创建时间',
    `updated_by` int                 DEFAULT NULL COMMENT '更新人',
    `updated_at` datetime            DEFAULT NULL COMMENT '更新时间',
    `deleted`    tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  AUTO_INCREMENT = 1000 COMMENT ='用户角色关联';

-- ----------------------------
-- 系统权限
-- ----------------------------
DROP TABLE IF EXISTS `system_permission`;
CREATE TABLE `system_permission`
(
    `id`         int                                     NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code`       varchar(50)                             NOT NULL COMMENT '权限CODE',
    `name`       varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称',
    `remark`     varchar(500)                                     DEFAULT NULL COMMENT '权限备注',
    `created_by` int                                     NOT NULL COMMENT '创建人',
    `created_at` datetime                                NOT NULL COMMENT '创建时间',
    `updated_by` int                                              DEFAULT NULL COMMENT '更新人',
    `updated_at` datetime                                         DEFAULT NULL COMMENT '更新时间',
    `deleted`    tinyint(1)                              NOT NULL DEFAULT '0' COMMENT '是否已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  AUTO_INCREMENT = 1000 COMMENT ='系统权限';

-- ----------------------------
-- 系统角色与权限的关联
-- ----------------------------
DROP TABLE IF EXISTS `system_role_permission`;
CREATE TABLE `system_role_permission`
(
    `id`            int        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id`       int        NOT NULL COMMENT '角色ID',
    `permission_id` int        NOT NULL COMMENT '权限ID',
    `created_by`    int        NOT NULL COMMENT '创建人',
    `created_at`    datetime   NOT NULL COMMENT '创建时间',
    `updated_by`    int                 DEFAULT NULL COMMENT '更新人',
    `updated_at`    datetime            DEFAULT NULL COMMENT '更新时间',
    `deleted`       tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  AUTO_INCREMENT = 1000 COMMENT ='角色权限关联';

-- ----------------------------
-- 系统配置
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config`
(
    `id`            int         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `permission_id` int                  DEFAULT NULL COMMENT '权限ID',
    `code`          varchar(50) NOT NULL COMMENT '配置编码',
    `name`          varchar(50) NOT NULL COMMENT '配置名称',
    `value`         longtext COMMENT '配置值',
    `value_type`    varchar(20) NOT NULL COMMENT '值类型',
    `scope`         varchar(50) NOT NULL COMMENT '作用域',
    `remark`        varchar(500)         DEFAULT NULL COMMENT '备注',
    `created_by`    int         NOT NULL COMMENT '创建人',
    `created_at`    datetime    NOT NULL COMMENT '创建时间',
    `updated_by`    int                  DEFAULT NULL COMMENT '更新人',
    `updated_at`    datetime             DEFAULT NULL COMMENT '更新时间',
    `deleted`       tinyint(1)  NOT NULL DEFAULT '0' COMMENT '是否已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  AUTO_INCREMENT = 1000 COMMENT ='系统配置';

-- ----------------------------
-- 系统字典
-- ----------------------------
DROP TABLE IF EXISTS `system_dict`;
CREATE TABLE `system_dict`
(
    `id`         int         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code`       varchar(50) NOT NULL COMMENT '字典编码',
    `name`       varchar(50) NOT NULL COMMENT '字典名称',
    `scope`      varchar(50) NOT NULL DEFAULT '' COMMENT '作用域',
    `remark`     varchar(500)         DEFAULT NULL COMMENT '备注',
    `created_by` int         NOT NULL COMMENT '创建人',
    `created_at` datetime    NOT NULL COMMENT '创建时间',
    `updated_by` int                  DEFAULT NULL COMMENT '更新人',
    `updated_at` datetime             DEFAULT NULL COMMENT '更新时间',
    `deleted`    tinyint(1)  NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  AUTO_INCREMENT = 1000 COMMENT ='系统字典';

-- ----------------------------
-- 系统字典数据
-- ----------------------------
DROP TABLE IF EXISTS `system_dict_data`;
CREATE TABLE `system_dict_data`
(
    `id`         int         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `dict_id`    int         NOT NULL COMMENT '所属字典',
    `value`      varchar(50) NOT NULL COMMENT '数据值',
    `label`      varchar(50) NOT NULL COMMENT '数据标签',
    `sort`       int         NOT NULL DEFAULT '0' COMMENT '排序',
    `config`     longtext COMMENT '其它配置',
    `disabled`   tinyint(1)  NOT NULL DEFAULT '0' COMMENT '是否禁用',
    `remark`     varchar(200)         DEFAULT NULL COMMENT '备注',
    `created_by` int         NOT NULL COMMENT '创建人',
    `created_at` datetime    NOT NULL COMMENT '创建时间',
    `updated_by` int                  DEFAULT NULL COMMENT '更新人',
    `updated_at` datetime             DEFAULT NULL COMMENT '更新时间',
    `deleted`    tinyint(1)  NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  AUTO_INCREMENT = 1000 COMMENT ='系统字典数据';

-- ----------------------------
-- 系统登录日志
-- ----------------------------
DROP TABLE IF EXISTS `system_login_log`;
CREATE TABLE `system_login_log`
(
    `id`             int         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`        int          DEFAULT NULL COMMENT '登录用户ID',
    `login_username` varchar(50) NOT NULL COMMENT '登录用户名',
    `ip`             varchar(20)  DEFAULT NULL COMMENT '登录IP',
    `location`       varchar(500) DEFAULT NULL COMMENT '登录地区',
    `client_info`    varchar(500) DEFAULT NULL COMMENT '客户端',
    `os_info`        varchar(500) DEFAULT NULL COMMENT '操作系统',
    `platform`       varchar(20)  DEFAULT NULL COMMENT '登录平台',
    `system_version` varchar(20)  DEFAULT NULL COMMENT '系统版本',
    `server_ip`      varchar(20)  DEFAULT NULL COMMENT '服务器IP',
    `success`        tinyint(1)   DEFAULT NULL COMMENT '是否登录成功',
    `reason`         varchar(200) DEFAULT NULL COMMENT '失败原因',
    `login_time`     datetime    NOT NULL COMMENT '登录时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='登录日志';

-- ----------------------------
-- 操作日志
-- ----------------------------
DROP TABLE IF EXISTS `system_trace_log`;
CREATE TABLE `system_trace_log`
(
    `id`               int          NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`          int                   DEFAULT NULL COMMENT '用户',
    `username`         varchar(50)           DEFAULT NULL COMMENT '固化用户名',
    `user_real_name`   varchar(50)           DEFAULT NULL COMMENT '固化用户姓名',
    `user_roles`       varchar(2000)         DEFAULT NULL COMMENT '固化用户角色',
    `user_permissions` varchar(2000)         DEFAULT NULL COMMENT '固化用户权限',
    `opera_module`     varchar(50)  NOT NULL COMMENT '操作模块',
    `opera_type`       varchar(50)  NOT NULL COMMENT '操作类型',
    `opera_remark`     varchar(50)  NOT NULL COMMENT '操作备注',
    `opera_time`       datetime     NOT NULL COMMENT '操作时间',
    `opera_duration`   int                   DEFAULT NULL COMMENT '耗时',
    `request_method`   varchar(10)  NOT NULL COMMENT '请求方式',
    `request_uri`      varchar(500) NOT NULL COMMENT '请求地址',
    `request_params`   varchar(2000)         DEFAULT NULL COMMENT '请求参数',
    `request_result`   varchar(2000)         DEFAULT NULL COMMENT '请求结果',
    `status`           tinyint      NOT NULL DEFAULT '-1' COMMENT '状态（0操作失败，1操作成功，-1未得到处理）',
    `exception_level`  tinyint               DEFAULT NULL COMMENT '异常等级',
    `exception_stack`  varchar(5000)         DEFAULT NULL COMMENT '异常信息',
    `server_ip`        varchar(20)  NOT NULL COMMENT '服务器IP',
    `system_version`   varchar(20)  NOT NULL COMMENT '接口版本',
    `platform`         varchar(20)  NOT NULL COMMENT '操作平台',
    `ip`               varchar(20)           DEFAULT NULL COMMENT '用户IP',
    `client_info`      varchar(500)          DEFAULT NULL COMMENT '客户端信息',
    `os_info`          varchar(500)          DEFAULT NULL COMMENT '系统信息',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统操作日志';

SET FOREIGN_KEY_CHECKS = 1;
