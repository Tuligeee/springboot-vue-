-- �����ˡ�֪ͨ���桱�˵���Ȩ�ޣ�ִ�к����Ա�����ɼ���
-- ���� RuoYi �˵��ṹ��Ĭ�ϸ���ɫ admin(role_id=1)

SET @parent_id := (
  SELECT menu_id FROM sys_menu
  WHERE path = 'system' AND menu_type = 'M'
  ORDER BY menu_id ASC LIMIT 1
);

-- �������� system Ŀ¼���򽵼��ҵ���Ŀ¼
SET @parent_id := IFNULL(@parent_id, 0);

INSERT INTO sys_menu
(`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`remark`)
SELECT '֪ͨ����', @parent_id, 9, 'notice', 'system/notice/index', NULL, 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', NOW(), 'ϵͳ����˵�'
FROM dual
WHERE NOT EXISTS (
  SELECT 1 FROM sys_menu WHERE path='notice' AND component='system/notice/index'
);

SET @notice_menu_id := (
  SELECT menu_id FROM sys_menu
  WHERE path='notice' AND component='system/notice/index'
  ORDER BY menu_id DESC LIMIT 1
);

INSERT INTO sys_menu
(`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`remark`)
SELECT '�����ѯ', @notice_menu_id, 1, '#', NULL, NULL, 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', NOW(), ''
FROM dual
WHERE @notice_menu_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM sys_menu WHERE parent_id=@notice_menu_id AND perms='system:notice:query');

INSERT INTO sys_menu
(`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`remark`)
SELECT '��������', @notice_menu_id, 2, '#', NULL, NULL, 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', NOW(), ''
FROM dual
WHERE @notice_menu_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM sys_menu WHERE parent_id=@notice_menu_id AND perms='system:notice:add');

INSERT INTO sys_menu
(`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`remark`)
SELECT '�����޸�', @notice_menu_id, 3, '#', NULL, NULL, 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', NOW(), ''
FROM dual
WHERE @notice_menu_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM sys_menu WHERE parent_id=@notice_menu_id AND perms='system:notice:edit');

INSERT INTO sys_menu
(`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`remark`)
SELECT '����ɾ��', @notice_menu_id, 4, '#', NULL, NULL, 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', NOW(), ''
FROM dual
WHERE @notice_menu_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM sys_menu WHERE parent_id=@notice_menu_id AND perms='system:notice:remove');

-- ��Ȩ�� admin ��ɫ��role_id=1��
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, sm.menu_id
FROM sys_menu sm
WHERE sm.menu_id = @notice_menu_id
  AND NOT EXISTS (SELECT 1 FROM sys_role_menu srm WHERE srm.role_id=1 AND srm.menu_id=sm.menu_id);

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, sm.menu_id
FROM sys_menu sm
WHERE sm.parent_id = @notice_menu_id
  AND sm.menu_type = 'F'
  AND NOT EXISTS (SELECT 1 FROM sys_role_menu srm WHERE srm.role_id=1 AND srm.menu_id=sm.menu_id);
