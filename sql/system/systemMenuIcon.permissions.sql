-- 创建并分配菜单

INSERT INTO SYSTEM_MENU (NAME, PATH, REMARK, ICON, DISABLED, SORT, FIXED, CREATE_USER, CREATE_TIME, UPDATE_USER, UPDATE_TIME, DELETED) VALUES ('系统图标', '/system/system-icon', '', '', 0, 1, 0, 1, current_timestamp, null, null, 0);
INSERT INTO SYSTEM_ROLE_MENU (ROLE_ID, MENU_ID, CREATE_USER, CREATE_TIME, UPDATE_USER, UPDATE_TIME, DELETED) VALUES (1, (SELECT MAX(LAST_INSERT_ID()) FROM SYSTEM_MENU), 1, current_timestamp, null, null, 0);

-- 创建并分配权限

INSERT INTO `SYSTEM_PERMISSION`(`CODE`, `NAME`, `MODULE`, `REMARK`, `FIXED`, `CREATE_USER`, `CREATE_TIME`, `UPDATE_USER`, `UPDATE_TIME`, `DELETED`) VALUES ('system:icon:create', '新建', '系统图标', '', 0, 1, CURRENT_TIMESTAMP, NULL, NULL, 0);
INSERT INTO SYSTEM_ROLE_PERMISSION (ROLE_ID, PERMISSION_ID, CREATE_USER, CREATE_TIME, UPDATE_USER, UPDATE_TIME, DELETED) VALUES (1, (SELECT MAX(LAST_INSERT_ID()) FROM SYSTEM_PERMISSION), 1, current_timestamp, null, null, 0);

INSERT INTO `SYSTEM_PERMISSION`(`CODE`, `NAME`, `MODULE`, `REMARK`, `FIXED`, `CREATE_USER`, `CREATE_TIME`, `UPDATE_USER`, `UPDATE_TIME`, `DELETED`) VALUES ('system:icon:delete', '删除', '系统图标', '', 0, 1, CURRENT_TIMESTAMP, NULL, NULL, 0);
INSERT INTO SYSTEM_ROLE_PERMISSION (ROLE_ID, PERMISSION_ID, CREATE_USER, CREATE_TIME, UPDATE_USER, UPDATE_TIME, DELETED) VALUES (1, (SELECT MAX(LAST_INSERT_ID()) FROM SYSTEM_PERMISSION), 1, current_timestamp, null, null, 0);

INSERT INTO `SYSTEM_PERMISSION`(`CODE`, `NAME`, `MODULE`, `REMARK`, `FIXED`, `CREATE_USER`, `CREATE_TIME`, `UPDATE_USER`, `UPDATE_TIME`, `DELETED`) VALUES ('system:icon:update', '修改', '系统图标', '', 0, 1, CURRENT_TIMESTAMP, NULL, NULL, 0);
INSERT INTO SYSTEM_ROLE_PERMISSION (ROLE_ID, PERMISSION_ID, CREATE_USER, CREATE_TIME, UPDATE_USER, UPDATE_TIME, DELETED) VALUES (1, (SELECT MAX(LAST_INSERT_ID()) FROM SYSTEM_PERMISSION), 1, current_timestamp, null, null, 0);

INSERT INTO `SYSTEM_PERMISSION`(`CODE`, `NAME`, `MODULE`, `REMARK`, `FIXED`, `CREATE_USER`, `CREATE_TIME`, `UPDATE_USER`, `UPDATE_TIME`, `DELETED`) VALUES ('system:icon:query', '查询', '系统图标', '', 0, 1, CURRENT_TIMESTAMP, NULL, NULL, 0);
INSERT INTO SYSTEM_ROLE_PERMISSION (ROLE_ID, PERMISSION_ID, CREATE_USER, CREATE_TIME, UPDATE_USER, UPDATE_TIME, DELETED) VALUES (1, (SELECT MAX(LAST_INSERT_ID()) FROM SYSTEM_PERMISSION), 1, current_timestamp, null, null, 0);
