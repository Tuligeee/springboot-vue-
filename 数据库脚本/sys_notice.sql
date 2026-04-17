-- ϵͳ�����������Ŀ�����ݿ�ִ�У�
CREATE TABLE IF NOT EXISTS `sys_notice` (
  `notice_id` bigint NOT NULL AUTO_INCREMENT COMMENT '����ID',
  `notice_title` varchar(100) NOT NULL COMMENT '�������',
  `notice_type` char(1) NOT NULL DEFAULT '2' COMMENT '�������ͣ�1֪ͨ 2���棩',
  `notice_content` longtext COMMENT '��������',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '����״̬��0���� 1�رգ�',
  `create_by` varchar(64) DEFAULT '' COMMENT '������',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
  `update_by` varchar(64) DEFAULT '' COMMENT '������',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '����ʱ��',
  `remark` varchar(255) DEFAULT NULL COMMENT '��ע',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ϵͳ�����';
