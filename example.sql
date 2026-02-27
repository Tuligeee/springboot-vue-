/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50717 (5.7.17-log)
 Source Host           : localhost:3306
 Source Schema         : example

 Target Server Type    : MySQL
 Target Server Version : 50717 (5.7.17-log)
 File Encoding         : 65001

 Date: 04/08/2024 18:24:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ce_aspiration
-- ----------------------------
DROP TABLE IF EXISTS `ce_aspiration`;
CREATE TABLE `ce_aspiration`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `student_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '学生学号',
  `entrance_year` int(11) NOT NULL COMMENT '填报年份',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_user` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_user` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '志愿表单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ce_aspiration
-- ----------------------------
INSERT INTO `ce_aspiration` VALUES (21, 'A2022001', 2023, '2025-01-30 16:12:55', 'wangwu', '2025-01-30 16:12:55', NULL);
INSERT INTO `ce_aspiration` VALUES (29, 'A2019001', 2023, '2025-01-30 20:28:24', 'lisi', '2025-01-30 20:28:24', NULL);
INSERT INTO `ce_aspiration` VALUES (31, 'A2020001', 2024, '2024-08-04 18:22:25', 'zhangsan', '2024-08-04 18:22:25', NULL);

-- ----------------------------
-- Table structure for ce_aspiration_detail
-- ----------------------------
DROP TABLE IF EXISTS `ce_aspiration_detail`;
CREATE TABLE `ce_aspiration_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `student_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '学生学号',
  `college_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '院校代码',
  `college_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '院校名称',
  `profession_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '专业代码',
  `profession_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '专业名称',
  `aspiration_batch` int(11) NOT NULL COMMENT '志愿批次',
  `profession_sort` int(11) NOT NULL DEFAULT 0 COMMENT '志愿专业顺序',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_user` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_user` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '志愿明细' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ce_aspiration_detail
-- ----------------------------
INSERT INTO `ce_aspiration_detail` VALUES (26, 'A2022001', 'C003', '上海交通大学', 'P010', '信息管理与信息系统', 1, 1, '2025-01-30 16:12:55', 'wangwu', '2025-01-30 16:12:55', NULL);
INSERT INTO `ce_aspiration_detail` VALUES (27, 'A2022001', 'C005', '武汉大学', 'P012', '法学', 1, 2, '2025-01-30 16:12:55', 'wangwu', '2025-01-30 16:12:55', NULL);
INSERT INTO `ce_aspiration_detail` VALUES (28, 'A2022001', 'C004', '复旦大学', 'P011', '会计学', 1, 3, '2025-01-30 16:12:55', 'wangwu', '2025-01-30 16:12:55', NULL);
INSERT INTO `ce_aspiration_detail` VALUES (44, 'A2019001', 'C002', '北京大学', 'P007', '马克思主义理论', 1, 1, '2025-01-30 20:28:24', 'lisi', '2025-01-30 20:28:24', NULL);
INSERT INTO `ce_aspiration_detail` VALUES (45, 'A2019001', 'C002', '北京大学', 'P006', '汉语言文学', 1, 2, '2025-01-30 20:28:24', 'lisi', '2025-01-30 20:28:24', NULL);
INSERT INTO `ce_aspiration_detail` VALUES (46, 'A2019001', 'C001', '清华大学', 'P001', '机械工程', 1, 3, '2025-01-30 20:28:24', 'lisi', '2025-01-30 20:28:24', NULL);
INSERT INTO `ce_aspiration_detail` VALUES (50, 'A2020001', 'C004', '复旦大学', 'P011', '会计学', 1, 1, '2024-08-04 18:22:25', 'zhangsan', '2024-08-04 18:22:25', NULL);
INSERT INTO `ce_aspiration_detail` VALUES (51, 'A2020001', 'C003', '上海交通大学', 'P010', '信息管理与信息系统', 1, 2, '2024-08-04 18:22:25', 'zhangsan', '2024-08-04 18:22:25', NULL);
INSERT INTO `ce_aspiration_detail` VALUES (52, 'A2020001', 'C002', '北京大学', 'P006', '汉语言文学', 1, 3, '2024-08-04 18:22:25', 'zhangsan', '2024-08-04 18:22:25', NULL);

-- ----------------------------
-- Table structure for ce_college
-- ----------------------------
DROP TABLE IF EXISTS `ce_college`;
CREATE TABLE `ce_college`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `college_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '院校代码',
  `college_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '院校名称',
  `city` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '城市',
  `ranking` int(11) NOT NULL DEFAULT 0 COMMENT '排名',
  `person_count` int(11) NOT NULL DEFAULT 0 COMMENT '人数',
  `detail_info` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '详细信息',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_user` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_user` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '院校表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ce_college
-- ----------------------------
INSERT INTO `ce_college` VALUES (1, 'C001', '清华大学', '北京', 1, 47282, '清华大学（Tsinghua University），简称“清华”，位于北京市海淀区，是中华人民共和国教育部直属的全国重点大学，位列国家“双一流”、 [75]  “985工程”、“211工程”，入选“2011计划”、“珠峰计划”、“强基计划”、“111计划”，为九校联盟（C9） [86]  、松联盟、中国大学校长联谊会、亚洲大学联盟、环太平洋大学联盟、中俄综合性大学联盟 [68]  、清华—剑桥—MIT低碳大学联盟成员、中国高层次人才培养和科学技术研究的基地，被誉为“红色工程师的摇篮”。 [1-3]   \n学校前身清华学堂始建于1911年，校名“清华”源于校址“清华园”地名，是晚清政府设立的留美预备学校，其建校的资金源于1908年美国退还的部分庚子赔款。1912年更名为清华学校。1928年更名为国立清华大学。1937年抗日战争全面爆发后，南迁长沙，与国立北京大学、私立南开大学组建国立长沙临时大学，1938年迁至昆明改名为国立西南联合大学。1946年迁回清华园。1949年中华人民共和国成立，清华大学进入新的发展阶段。中国高等院校1952年院系调整后成为多科性工业大学。1978年以来逐步恢复和发展为综合性的研究型大学。 [4] \n截至2022年8月，学校校园面积460.19公顷，建筑面积303.32万平方米，馆藏总量565.87万册；设有21个学院、59个教学系，开设88个本科专业；有博士后科研流动站50个，一级学科国家重点学科22个，一级学科博士、硕士学位授权点63个；有教职工16270人，在校生59270人，其中本科生16320人，硕士生22423，博士生20527，港澳台侨学生1001人。', '2025-01-01 17:25:17', '', '2025-01-02 17:12:55', 'admin');
INSERT INTO `ce_college` VALUES (2, 'C002', '北京大学', '北京', 2, 35288, '北京大学（Peking University），简称“北大”，位于北京市海淀区，是中华人民共和国教育部直属的全国重点大学，位列“双一流”、“211工程”、“985工程”，入选“学位授权自主审核单位”、“基础学科拔尖学生培养试验计划”、“基础学科招生改革试点”、“高等学校创新能力提升计划”、“高等学校学科创新引智计划”，为九校联盟 [105]  、松联盟、中国大学校长联谊会、京港大学联盟、全球大学高研院联盟、亚洲大学联盟、亚洲大学生集体行动交流计划、东亚研究型大学协会、国际研究型大学联盟、环太平洋大学联盟、全球大学校长论坛、21世纪学术联盟、东亚四大学论坛、国际公立大学论坛、中俄综合性大学联盟成员。 [90]  [92] \n北京大学创立于1898年维新变法之际，初名京师大学堂，创办之初也是国家教育行政机关。1912年改为国立北京大学。1937年迁至长沙，与国立清华大学和私立南开大学组成国立长沙临时大学，1938年迁至昆明，更名为国立西南联合大学。1946年返回北平。1952年成为以文理基础学科为主的综合性大学，并自北京沙滩等地迁至现址。2000年与原北京医科大学合并，组建为新的北京大学。 [1] \n北京大学是新文化运动的中心和五四运动的策源地，最早在中国传播马克思主义和科学、民主思想，是创建中国共产党的重要基地之一。长期以来，北京大学始终与中国和中国人民共命运，与时代和社会同前进。', '2025-01-01 19:49:45', '', '2025-01-02 17:12:59', 'admin');
INSERT INTO `ce_college` VALUES (3, 'C003', '上海交通大学', '深圳', 3, 48999, '上海交通大学（Shanghai Jiao Tong University），简称“上海交大”，位于上海，是教育部直属、上海市共建、中央直管的全国重点大学 [137]  ，位列国家“双一流”“985工程”“211工程”， [135]  为九校联盟 [139]  、环太平洋大学联盟、21世纪学术联盟、中国大学校长联谊会、Universitas 21、国际应用科技开发协作网、新工科教育国际联盟、亚洲校园 [159]  成员，入选“珠峰计划”、“强基计划”、“111计划”、“2011计划”、卓越医生教育培养计划、卓越法律人才教育培养计划、卓越工程师教育培养计划、卓越农林人才教育培养计划、国家建设高水平大学公派研究生项目、中国政府奖学金来华留学生接收院校、学位授权自主审核单位。 [1-5]     \n学校创建于1896年，原名南洋公学，1911年更名为南洋大学堂，1929年更名为国立交通大学，1949年更名为交通大学；1957年经历西迁与分设，分为交通大学上海部分和西安部分；1959年，交通大学上海部分启用“上海交通大学”校名；1999年，原上海农学院并入；2005年，与原上海第二医科大学合并。 [2]  [6-8]   \n截至2023年4月，学校有徐汇、闵行、黄浦、长宁、浦东等校区，总占地面积300余万平方米；设有34个学院/直属系，开设75个本科专业；拥有博士后流动站38个、一级学科博士学位授权点52个、博士专业学位类别9个、一级学科硕士学位授权点58个、硕士专业学位类别32个；有专任教师3700名，全日制本科生（国内）17606人、全日制研究生26944人、学位留学生2096人。', '2025-01-01 19:50:08', '', '2025-01-02 17:13:03', 'admin');
INSERT INTO `ce_college` VALUES (5, 'C004', '复旦大学', '上海', 4, 56332, '复旦大学，简称“复旦”，位于直辖市上海，是中华人民共和国教育部直属的全国重点大学，中央直管高校，综合性研究型大学，由教育部与上海市重点共建，位列国家“双一流”、“985工程”、“211工程”建设高校，入选珠峰计划、强基计划、111计划、2011计划、卓越医生教育培养计划、卓越法律人才教育培养计划、国家建设高水平大学公派研究生项目、新工科研究与实践项目、中国政府奖学金来华留学生接收院校、深化创新创业教育改革示范高校、首批学位授权自主审核单位，环太平洋大学联盟、九校联盟、全球大学高研院联盟、亚洲校园、中国大学校长联谊会、东亚研究型大学协会、新工科教育国际联盟、医学“双一流”建设联盟、长三角研究型大学联盟、长三角高校智库联盟、中俄综合性大学联盟成员。 [2]  [4]  [114]  [134]  [137]  [139-141]    [158] \n学校前身是1905年创办的复旦公学，是中国最早由民间创办的高等学校之一。2000年，复旦大学与上海医科大学合并，组建新的复旦大学。2017年，学校入选“双一流”建设高校A类名单。 [145] \n截至2022年10月，学校有邯郸、枫林、江湾、张江四个校区；设有直属院（系）35个，附属医院18家（其中2家筹建），开设本科专业80个；有在校普通本科生15164人，研究生34618人（含全日制和非全日制的大陆港澳台生），学历留学生2535人；在校教学科研人员3602人。 [5]  [161] ', '2025-01-02 17:03:50', 'admin', '2025-01-02 17:13:06', 'admin');
INSERT INTO `ce_college` VALUES (6, 'C005', '武汉大学', '武汉', 5, 32113, '武汉大学（Wuhan University），简称武大，位于武汉市，是中华人民共和国教育部直属的综合性全国重点大学；位列国家“双一流” [152]  “985工程”“211工程”；入选学位授权自主审核单位、“珠峰计划”“强基计划”“2011计划”“111计划”、卓越工程师教育培养计划、卓越法律人才教育培养计划、卓越医生教育培养计划、国家建设高水平大学公派研究生项目、国家级新工科研究与实践项目、一流网络安全学院建设示范项目高校、中国政府奖学金来华留学生接收院校、全国深化创新创业教育改革示范高校、国家大学生文化素质教育基地、大众创业万众创新示范基地、基础学科拔尖学生培养计划2.0基地，为欧亚-太平洋大学联盟、大学通识教育联盟、中国高校行星科学联盟、法学教育创新联盟、医学“双一流”建设联盟 [174]  成员。 [1-2]  \n学校溯源于1893年清末湖广总督张之洞奏请清政府创办的自强学堂，历经传承演变，1928年定名为国立武汉大学，是近代中国第一批国立大学。2000年，武汉大学与武汉水利电力大学、武汉测绘科技大学、湖北医科大学合并组建新的武汉大学。 [2-3]  \n截至2022年12月，学校占地面积5195亩，建筑面积2960805平方米，馆藏图书722.13万册；设有六大学部34个学院、3所三级甲等附属医院，开设133个本科专业；拥有46个博士后科研流动站、53个博士学位授权一级学科、61个硕士学位授权一级学科；有教职工7468人，其中专任教师3875人；有普通本科生29944人，硕士研究生20298人，博士研究生9036人，另有外国留学生1461人。', '2025-01-02 17:04:12', 'admin', '2025-01-02 17:13:10', 'admin');
INSERT INTO `ce_college` VALUES (7, 'C006', '浙江大学', '杭州', 6, 65332, '浙江大学（Zhejiang University），简称“浙大”，位于浙江省杭州市，是中华人民共和国教育部直属的综合性全国重点大学，位列国家“双一流”、 [105]  “211工程”、“985工程”，是九校联盟（C9） [112]  、中国大学校长联谊会、环太平洋大学联盟、世界大学联盟、全球大学校长论坛、全球高校人工智能学术联盟、国际应用科技开发协作网、新工科教育国际联盟、全球能源互联网大学联盟、CDIO工程教育联盟、医学“双一流”建设联盟成员，入选“珠峰计划”、“强基计划”、“2011计划”、“111计划”、卓越工程师教育培养计划、卓越医生教育培养计划、卓越法律人才教育培养计划、卓越农林人才教育培养计划、全国首批深化创新创业教育改革示范高校、学位授权自主审核单位。曾培养出厉绥之、束星北、李政道等杰出校友。 [1-2]   [80]  [82]  [110] \n学校前身是创立于1897年的求是书院，1914年停办。1928年于求是书院旧址新建国立浙江大学。1937年举校西迁，在遵义、湄潭等地办学，1946年秋回迁杭州。1952年浙江大学部分系科转入中国科学院和其他高校，主体部分在杭州重组为若干所院校，后分别发展为原浙江大学、杭州大学、浙江农业大学和浙江医科大学。1998年，同根同源的四校实现合并，组建了新的浙江大学。\n根据2022年11月学校官网信息显示，学校设有紫金港、玉泉、西溪、华家池、之江、舟山、海宁等7个校区；设有7个学部、37个专业学院（系）、1个工程师学院、2个中外合作办学学院、7家附属医院；总占地面积6890108平方米。', '2025-01-02 17:04:42', 'admin', '2025-01-02 17:13:13', 'admin');
INSERT INTO `ce_college` VALUES (8, 'C007', '中国人民大学', '北京', 7, 23111, '中国人民大学（Renmin University of China），简称“人大”，位于北京，是教育部直属高校，教育部与北京市共建，位列国家“双一流”、 [117]  “211工程”、“985工程”，为世界大学联盟、国际应用科技开发协作网、亚太国际教育协会、亚洲校园 [148]  、京港大学联盟成员，入选国家“强基计划”、“111计划”、“2011计划”、卓越法律人才教育培养计划、卓越农林人才教育培养计划、国家建设高水平大学公派研究生项目、新工科研究与实践项目、中国政府奖学金来华留学生接收院校，是一所以人文社会科学为主的综合性研究型全国重点大学。 [1-7]       \n学校前身是1937年成立的陕北公学，以及华北联合大学和北方大学、华北大学。 [3]  1949年12月16日，中央人民政府政务院通过了《关于成立中国人民大学的决定》。1950年10月3日，以华北大学为基础合并组建的中国人民大学正式开学，成为新中国创办的第一所新型正规大学。1954年，被确定为以社会科学为主的综合大学和首批全国重点大学；1960年，被确定为综合性全国重点大学；2017年入选国家“双一流”建设名单。 [8] \n截至2021年11月，学校设有39个学院，另设有体育部、继续教育学院、深圳研究院等；开设本科专业83个；有博士后流动站21个，一级学科博士学位授权点22个，一级学科硕士学位授权点37个；拥有8个国家重点一级学科，8个国家重点二级学科；图书馆收藏纸质图书436.7余万册；专任教师1951人；全日制在校生28501人，其中本科生11354人，硕士生11149人，博士生4792人，留学生1206人。', '2025-01-02 17:05:01', 'admin', '2025-01-02 17:13:16', 'admin');
INSERT INTO `ce_college` VALUES (9, 'C008', '南京大学', '南京', 8, 54232, '南京大学（Nanjing University），简称“南大”，位于江苏省南京市，是中华人民共和国教育部直属、中央直管副部级建制的全国重点大学， [85]  位列国家“双一流”、“985工程”、“211工程”重点建设高校，入选“珠峰计划”、“强基计划”、“111计划”、“2011计划”，为九校联盟（C9）、中国大学校长联谊会、环太平洋大学联盟、21世纪学术联盟、全球大学高研院联盟、国际应用科技开发协作网、东亚研究型大学协会、亚洲校园 [134]  、新工科教育国际联盟、中国高校行星科学联盟、长三角研究型大学联盟、学位授权自主审核单位成员，首批国家级双创示范基地。 [111] \n学校肇始于1902年创建的三江师范学堂，此后历经两江师范学堂、南京高等师范学校、国立东南大学、国立中央大学等时期，1950年更名为南京大学。1952年，调整出部分院系后与创办于1888年的金陵大学文、理学院等合并，仍名南京大学。校址从四牌楼迁至鼓楼金陵大学原址。2006年，教育部和江苏省签订重点共建南京大学的协议。2011年，教育部和江苏省签署协议继续重点共建南京大学。\n截至2022年11月，学校有仙林、鼓楼、浦口、苏州四个校区；设有33个院系，本科专业91个；有博士后科研流动站38个，博士学位授权一级学科点44个，博士学位授权二级学科点（不含一级学科覆盖点）1个，专业博士学位授权点5个，硕士学位授权一级学科点4个，专业硕士学位授权点28个；有本科生13934人、硕士研究生18158人、博士研究生8948人、留学生1691人。', '2025-01-02 17:05:24', 'admin', '2025-01-02 17:13:21', 'admin');
INSERT INTO `ce_college` VALUES (10, 'C009', '吉林大学', '吉林', 9, 23110, '吉林大学（Jilin University），简称“吉大”，位于吉林省长春市，是中华人民共和国教育部直属全国重点大学，中央直管副部级建制，位列国家“双一流”、“211工程”、“985工程”，入选珠峰计划、2011计划、111计划、卓越法律人才教育培养计划、卓越工程师教育培养计划、卓越医生教育培养计划、卓越农林人才教育培养计划、国家建设高水平大学公派研究生项目、国家大学生创新性实验计划、新工科研究与实践项目、国家级大学生创新创业训练计划、国家创新人才培养示范基地、全国深化创新创业教育改革示范高校、中国政府奖学金来华留学生接收院校，首批建立研究生院的22所大学之一，21世纪学术联盟、亚太国际教育协会、中俄交通大学联盟、粤港澳大湾区物流与供应链创新联盟、医学“双一流”建设联盟 [138]  、亚洲校园 [137]  成员。\n学校始建于1946年；1952年经院系调整成为建国后中国共产党亲手创建的第一所综合性大学；1960年被列为全国重点大学；2000年，原吉林大学、吉林工业大学、白求恩医科大学、长春科技大学、长春邮电学院合并组建新吉林大学。2004年，中国人民解放军军需大学并入。\n截至2022年11月，学校有6个校区、7个校园，校园占地面积733.51万平方米，校舍建筑面积286.09万平方米；有52个教学单位，140个本科专业；有博士后科研流动站44个、一级学科博士学位授权点49个、交叉学科博士学位授权点3个、博士专业学位授权点8个、一级学科硕士学位授权点62个、硕士专业学位授权点33个；在籍学生74707人。', '2025-01-02 17:05:53', 'admin', '2025-01-02 17:13:25', 'admin');
INSERT INTO `ce_college` VALUES (11, 'C010', '中山大学', '深圳', 10, 56000, '中山大学（Sun Yat-sen University），简称“中大”，位于广东省，由中华人民共和国教育部直属，是教育部、国家国防科技工业局和广东省共建的综合性全国重点大学，位列国家“双一流”、“985工程”、“211工程”，入选国家“珠峰计划”、“111计划”、“2011计划”、卓越法律人才教育培养计划、卓越医生教育培养计划、国家大学生创新性实验计划、国家级大学生创新创业训练计划、国家建设高水平大学公派研究生项目、新工科研究与实践项目、全国深化创新创业教育改革示范高校、国家大学生文化素质教育基地、国家创新人才培养示范基地、国家国际科技合作基地、首批高等学校科技成果转化和技术转移基地、学位授权自主审核单位等，是环太平洋大学联盟、中国高校行星科学联盟、中国人工智能教育联席会、中国自由贸易试验区研究院联盟、大学通识教育联盟、粤港澳高校联盟、粤港澳大湾区物流与供应链创新联盟成员。 [44]  [47] \n1924年，孙中山亲手将广州地区多所高校整合创立国立广东大学。1926年定名为国立中山大学。如今该校由1952年院系调整后分设的中山大学和中山医科大学于2001年10月合并而成。\n截至2022年9月，学校由广州校区、珠海校区、深圳校区三个校区、五个校园及十家附属医院组成；开设141个本科专业；有博士后科研流动站44个，一级学科博士点57个，一级学科硕士点64个，专业学位类别43种；有在校学生67135人，有普通本科生33224人，硕士23125人，博士10163人，留学生623人；有专任教师4771人。', '2025-01-02 17:06:17', 'admin', '2025-01-02 17:13:28', 'admin');
INSERT INTO `ce_college` VALUES (12, 'C011', '北京师范大学', '北京', 11, 32100, '北京师范大学（Beijing Normal University）是中华人民共和国教育部直属、教育部与北京市共建的全国重点大学，位列国家“双一流”、“985工程”、“211工程”，国家“七五”、“八五”首批重点建设十所大学之一；为中国高校行星科学联盟、京港大学联盟、粤港澳大湾区物流与供应链创新联盟成员；入选“珠峰计划”、“强基计划”、“2011计划”、“111计划”、“国培计划”、卓越法律人才教育培养计划、卓越教师培养计划、国家大学生创新性实验计划、国家级大学生创新创业训练计划、国家建设高水平大学公派研究生项目、亚洲校园。 [1-3]    [52]  [74] \n学校的前身是1902年创立的京师大学堂师范馆，1908年改称京师优级师范学堂，独立设校，1912年改名为北京高等师范学校。1923年学校更名为北京师范大学，成为中国历史上第一所师范大学。1931年、1952年北平女子师范大学、辅仁大学先后并入北京师范大学。1959年，被中央确定为首批全国重点大学。2002年，成为首批拥有自主设置本科专业审批权的6所高校之一。2017年，学校进入国家“世界一流大学”建设A类名单。 [4]  2018年，成为首批可开展学位授权自主审核单位的20所高校之一。\n截至2021年5月，北京师范大学北京校区占地面积1191亩（海淀校园、西城校园、昌平校园），另建有北京师范大学珠海校区；全日制在校生24000余人；下设3个学部、27个学院、2个系、11个研究院（所）、4个书院；有博士后科研流动站28个，一级学科博士点32个，一级学科硕士点38个；开设本科专业77个。', '2025-01-02 17:06:36', 'admin', '2025-01-02 17:13:33', 'admin');
INSERT INTO `ce_college` VALUES (13, 'C012', '华中科技大学', '上海', 12, 53313, '华中科技大学（Huazhong University of Science and Technology），简称华中大、华科大 [139]  ，位于湖北省武汉市，是中华人民共和国教育部直属的综合性研究型全国重点大学、位列国家“双一流” [118]  “985工程”“211工程”、入选“强基计划”“111计划”、卓越工程师教育培养计划、卓越医生教育培养计划、国家大学生创新性实验计划、国家级大学生创新创业训练计划、国家建设高水平大学公派研究生项目、国家级新工科研究与实践项目、基础学科拔尖学生培养计划2.0，是学位授权自主审核单位、全国深化创新创业教育改革示范高校、一流网络安全学院建设示范项目高校、中国政府奖学金来华留学生接收院校、教育部来华留学示范基地，为中欧工程教育平台成员和医学“双一流”建设联盟 [154]  、国际应用科技开发协作网 [141]  、全球能源互联网大学联盟 [142]  成员。 [1-7]        [9] \n学校由原华中理工大学、同济医科大学、武汉城市建设学院于2000年合并组建。原华中理工大学前身为1952年国家筹办、1953年10月开学的华中工学院。同济医科大学前身为1907年德国埃里希宝隆创办的德文医学堂。武汉城市建设学院前身为1952年12月创建的中南建筑工程学校。 [155] \n截至2022年9月，学校校园占地7000余亩，绿化覆盖率72%；设有51个院系，开设112个本科专业；拥有39个博士后科研流动站，46个博士学位授权一级学科，48个硕士学位授权一级学科；有一级学科国家重点学科7个，二级学科国家重点学科15个，国家重点（培育）学科7个；有专任教师3700余人。', '2025-01-02 17:07:04', 'admin', '2025-01-02 17:13:36', 'admin');

-- ----------------------------
-- Table structure for ce_profession
-- ----------------------------
DROP TABLE IF EXISTS `ce_profession`;
CREATE TABLE `ce_profession`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `profession_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '专业代码',
  `profession_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '专业名称',
  `college_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属院校代码',
  `study_year` int(11) NULL DEFAULT NULL COMMENT '修业年限',
  `detail_info` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '详情',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_user` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_user` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '专业表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ce_profession
-- ----------------------------
INSERT INTO `ce_profession` VALUES (7, 'P001', '机械工程', 'C001', 4, '机械工程是一门利用物理定律为机械系统作分析、设计、制造及维修的工程学科。机械工程是以有关的自然科学和技术科学为理论基础，结合生产实践中的技术经验，研究和解决在开发、设计、制造、安装、运用和维修各种机械中的全部理论和实际问题的应用学科。机械工程是工学研究生教育一级学科，工程研究生教育一个领域。', '2025-01-02 17:27:07', 'admin', '2025-01-02 18:18:47', 'admin');
INSERT INTO `ce_profession` VALUES (8, 'P002', '车辆工程', 'C001', 4, '车辆工程专业培养掌握机械、电子、计算机等方面工程技术基础理论和汽车设计、制造、试验等方面专业知识与技能，了解并重视与汽车技术发展有关的人文社会知识，能在企业、科研院（所）等部门，从事与车辆工程有关的产品设计开发、生产制造、试验检测、应用研究、技术服务、经营销售和管理等方面的工作，具有较强实践能力和创新精神的高级专门人才', '2025-01-02 17:27:55', 'admin', '2025-01-02 18:19:13', 'admin');
INSERT INTO `ce_profession` VALUES (9, 'P003', '微电子科学与工程', 'C001', 4, '该专业是理工兼容、互补的专业，是在物理学、电子学、材料科学、计算机科学、集成电路设计制造等多学科和超净、超纯、超精细加工技术基础上发展起来的一门新兴学科，主要研究半导体器件物理、功能电子材料、固体电子器件，超大规模集成电路（ULSI）的设计与制造技术、微机械电子系统以及计算机辅助设计制造技术等。 ', '2025-01-02 17:28:43', 'admin', '2025-01-02 18:19:29', 'admin');
INSERT INTO `ce_profession` VALUES (10, 'P004', '信息与计算科学', 'C001', 4, '信息与计算科学是一门普通高等学校本科专业，属数学类专业，基本修业年限为四年，授予理学学士学位。 [1] \n本专业培养具有良好的数学素养，掌握信息科学和计算科学的基本理论和方法，受到科学研究的初步训练，能运用所学知识和熟练的计算机技能解决实际问题，能在科技、教育和经济部门从事研究、教学和应用开发和管理工作的高级专门人才。本专业学生主要学习信息科学和计算科学的基本理论、基本知识和基本方法，打好数学基础，受到较扎实的计算机训练，初步具备在信息科学与计算科学领域从事科学研究、解决实际问题及设计开发有关软件的能力。', '2025-01-02 17:31:30', 'admin', '2025-01-02 18:19:49', 'admin');
INSERT INTO `ce_profession` VALUES (11, 'P005', '金融学', 'C001', 4, '金融学是研究价值判断和价值规律的学科。本专业培养具有金融学理论知识及专业技能的专门人才。', '2025-01-02 17:32:32', 'admin', '2025-01-02 18:20:03', 'admin');
INSERT INTO `ce_profession` VALUES (12, 'P006', '汉语言文学', 'C002', 4, '该专业学生主要学习汉语和中国文学方面的基本知识，受到有关理论、发展历史、研究现状等方面的系统教育和业务能力的基本训练，培养具备一定的文艺理论素养和系统的汉语言文学知识，能在新闻文艺出版部门、高校、科研机构和机关企事业单位从事文学评论、汉语言文学教学与研究工作，以及文化、宣传方面的实际工作的汉语言文学高级专门人才。', '2025-01-02 17:35:34', 'admin', '2025-01-02 18:20:19', 'admin');
INSERT INTO `ce_profession` VALUES (13, 'P007', '马克思主义理论', 'C002', 4, '马克思主义哲学是关于自然、社会和思维发展一般规律的科学，是唯物论和辩证法的统一、唯物论自然观和历史观的统一。马克思主义哲学专业学科是哲学的二级学科，主要研究辩证唯物主义和历史唯物主义。本学科包括马克思主义哲学的理论、历史和应用三个方面。', '2025-01-02 17:36:14', 'admin', '2025-01-02 18:20:35', 'admin');
INSERT INTO `ce_profession` VALUES (14, 'P008', '临床医学（八年制）', 'C002', 8, '临床医学是研究疾病的病因、诊断、治疗和预后，提高临床治疗水平，促进人体健康的科学。\n临床即“亲临病床”之意，它根据病人的临床表现，从整体出发结合研究疾病的病因、发病机理和病理过程，进而确定诊断，通过预防和治疗以最大程度上减弱疾病、减轻病人痛苦、恢复病人健康、保护劳动力。临床医学是直接面对疾病、病人，对病人直接实施治疗的科学。\n16世纪文艺复兴时期，医学陈规被打破，产生了人体解剖学。17世纪，生理学建立。18世纪，病理解剖学建立。19世纪，细胞学、细菌学获得长足发展。基础医学和临床医学逐渐成为两个独立学科，数学、生物学、物理学、化学等方面的巨大进步为现代临床医学的产生奠定了坚实基础。', '2025-01-02 17:36:58', 'admin', '2025-01-02 18:20:58', 'admin');
INSERT INTO `ce_profession` VALUES (15, 'P009', '生物科学', 'C002', 4, '生物科学是一门普通高等学校本科专业，属生物科学类专业，基本修业年限为四年，授予理学学位。生物科学是自然科学的重要分支，是人们观察和揭示生命现象、探讨生命本质和发现生命内在规律的科学 [2]  。\n生物科学专业培养具备生物科学的基本理论、基本知识和较强的实验技能，能在科研院所、高等学校及企事业单位等从事科学研究、教学工作及管理工作的生物科学高级专业人才', '2025-01-02 17:37:44', 'admin', '2025-01-02 18:21:12', 'admin');
INSERT INTO `ce_profession` VALUES (16, 'P010', '信息管理与信息系统', 'C003', 4, '信息管理与信息系统是一门普通高等学校本科专业，属管理科学与工程类专业，基本修业年限为四年，授予管理学或工学学士学位，管理学侧重于信息系统管理，工学侧重于计算机科学与技术。\n该专业学生主要学习经济、管理、数量分析方法、信息资源管理、计算机及信息系统方面的基本理论和基本知识，接受系统和设计方法以及信息管理方法的基本训练，具备综合运用所学知识分析和解决问题的基本能力，培养能在国家各级管理部门、工商企业、金融机构、科研单位等部门从事信息管理以及信息系统分析、设计、实施管理和评价等方面的高级专门人才', '2025-01-02 17:38:42', 'admin', '2025-01-02 18:21:29', 'admin');
INSERT INTO `ce_profession` VALUES (17, 'P011', '会计学', 'C004', 4, '该专业培养适应社会主义市场经济要求，培养德智体美全面发展，具有经济学、管理学的基础理论，具备财务管理、会计、审计、资产管理等方面专业知识和基本技能，能够在综合经济管理部门、各类企事业单位及会计师事务所、资产评估事务所等中介机构从事管理、咨询等工作的高级专门人才；学生要求系统地握会计学的基本理论、知识和技能，能熟练地运用计算机处理财务、会计、审计等方面的业务，较熟练地掌握一门外语。深入理解中国和国际会计准则，精通现代会计业务，掌握国际通行的会计核算与公司理财技能，能够独立从事财务管理、会计核算、审计等方面的工作', '2025-01-02 17:40:26', 'admin', '2025-01-02 18:18:31', 'admin');
INSERT INTO `ce_profession` VALUES (18, 'P012', '法学', 'C005', 4, '法学，是关于法律的科学。是以法律、法律现象以及其规律性为研究内容的科学。法律作为社会的强制性规范，其直接目的在于维持社会秩序，并通过秩序的构建与维护，实现社会公正。作为以法律为研究对象的法学，其核心就在对于秩序与公正的研究，是秩序与公正之学。 [1] \n在中国，法学思想最早源于春秋战国时期的法家哲学思想，法学在中国先秦时代被称为“刑名之学”，从汉代开始有“律学”的名称。在西方，古罗马法学家乌尔比安（Ulpianus）对“法学”（古代拉丁语中的Jurisprudentia）一词的定义是：人和神的事务的概念，正义和非正义之学。\n截至2020年，中国大陆拥有法学一级博士点授予权的院校为52所，是国内法学人才培养的教育高地。', '2025-01-02 17:41:26', 'admin', '2025-01-02 18:21:44', 'admin');
INSERT INTO `ce_profession` VALUES (19, 'P013', '新闻学', 'C007', 4, '新闻学是研究新闻事业和新闻工作规律的科学。新闻学是以人类社会客观存在的新闻现象作为自己的研究对象，研究的重点是新闻事业和人类社会的关系，探索新闻事业的产生、发展的特殊规律和新闻工作的基本要求的一门科学。它研究的内容是新闻理论、新闻史和新闻业务。新闻学的中心议题是：客观社会的诸条件对人类新闻活动的决定、支配作用以及新闻活动对社会的反作用。新闻学有广义和狭义之分。', '2025-01-02 18:10:33', 'admin', '2025-01-02 18:22:00', 'admin');
INSERT INTO `ce_profession` VALUES (20, 'P013', '艺术与科技', 'C011', 4, '本专业培养能够系统掌握艺术与科技专业的基本理论知识和技能，契合国家文化发展战略，熟悉展示设计的理论与方法、艺术与科技相融合的表现手段，掌握展示文案策划、空间造型艺术、空间视觉表达、数字媒体技术应用、工程技术管理等相关知识，培养具有国际视野、交叉学科基础、创新创意能力的艺术与科技领域的应用型高级专业人才。', '2025-01-02 18:11:14', 'admin', '2025-01-02 18:22:14', 'admin');
INSERT INTO `ce_profession` VALUES (21, 'P014', '服装与服饰设计', 'C008', 4, '服装与服饰设计是中国普通高等学校专科专业。', '2025-01-02 18:11:50', 'admin', '2025-01-02 18:22:29', 'admin');
INSERT INTO `ce_profession` VALUES (22, 'P015', '数字媒体艺术', 'C009', 4, '数字媒体艺术是一门普通高等学校本科专业，属于设计类专业，基本修业年限为四年，授予艺术学学士学位；2012年9月，教育部将新的数字媒体艺术专业取代旧的数字媒体艺术和数字游戏设计两个专业。 [1-2]  \n数字媒体艺术专业主要研究利用信息技术手段进行艺术处理和创作的方法和技巧。通过理论学习、专业技能培训等途径，学生可以掌握数字媒体软件的使用技术，具备一定的使用数字技术手段对各种类型的作品进行艺术加工的能力。', '2025-01-02 18:12:25', 'admin', '2025-01-02 18:22:45', 'admin');
INSERT INTO `ce_profession` VALUES (23, 'P016', '工业设计', 'C009', 4, '工业设计（industrial design），简称ID。指以工学、美学、经济学为基础对工业产品进行设计。\n工业设计分为产品设计、环境设计、传播设计、设计管理4类；包括造型设计、机械设计、服装设计、环境规划、室内设计、UI设计、平面设计、包装设计、广告设计、展示设计、网站设计等。工业设计又称工业产品设计学，工业设计涉及到心理学，社会学，美学，人机工程学，机械构造，摄影，色彩学等。工业发展和劳动分工所带来的工业设计，与其它艺术、生产活动、工艺制作等都有明显不同，它是各种学科、技术和审美观念的交叉产物。', '2025-01-02 18:13:02', 'admin', '2025-01-02 18:23:08', 'admin');

-- ----------------------------
-- Table structure for ce_score_line
-- ----------------------------
DROP TABLE IF EXISTS `ce_score_line`;
CREATE TABLE `ce_score_line`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `profession_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '专业代码',
  `college_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `score` int(11) NULL DEFAULT NULL COMMENT '分数线',
  `year` int(11) NULL DEFAULT NULL COMMENT '年份',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_user` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_user` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 125 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分数线表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ce_score_line
-- ----------------------------
INSERT INTO `ce_score_line` VALUES (5, 'A001', 'C001', 679, 2020, '2025-01-02 17:27:07', 'admin', '2025-01-02 17:27:07', NULL);
INSERT INTO `ce_score_line` VALUES (6, 'A001', 'C001', 700, 2021, '2025-01-02 17:27:07', 'admin', '2025-01-02 17:27:07', NULL);
INSERT INTO `ce_score_line` VALUES (7, 'A001', 'C001', 657, 2022, '2025-01-02 17:27:07', 'admin', '2025-01-02 17:27:07', NULL);
INSERT INTO `ce_score_line` VALUES (26, 'P005', 'C002', 700, 2022, '2025-01-02 17:35:34', 'admin', '2025-01-02 17:35:34', NULL);
INSERT INTO `ce_score_line` VALUES (27, 'P005', 'C002', 677, 2020, '2025-01-02 17:35:34', 'admin', '2025-01-02 17:35:34', NULL);
INSERT INTO `ce_score_line` VALUES (28, 'P005', 'C002', 690, 2021, '2025-01-02 17:35:34', 'admin', '2025-01-02 17:35:34', NULL);
INSERT INTO `ce_score_line` VALUES (68, 'P-16', 'C009', 401, 2022, '2025-01-02 18:13:02', 'admin', '2025-01-02 18:13:02', NULL);
INSERT INTO `ce_score_line` VALUES (69, 'P-16', 'C009', 400, 2021, '2025-01-02 18:13:02', 'admin', '2025-01-02 18:13:02', NULL);
INSERT INTO `ce_score_line` VALUES (70, 'P-16', 'C009', 410, 2020, '2025-01-02 18:13:02', 'admin', '2025-01-02 18:13:02', NULL);
INSERT INTO `ce_score_line` VALUES (71, 'P011', 'C004', 580, 2021, '2025-01-02 18:18:31', 'admin', '2025-01-02 18:18:31', NULL);
INSERT INTO `ce_score_line` VALUES (72, 'P011', 'C004', 550, 2022, '2025-01-02 18:18:31', 'admin', '2025-01-02 18:18:31', NULL);
INSERT INTO `ce_score_line` VALUES (73, 'P011', 'C004', 590, 2020, '2025-01-02 18:18:31', 'admin', '2025-01-02 18:18:31', NULL);
INSERT INTO `ce_score_line` VALUES (74, 'P001', 'C001', 700, 2021, '2025-01-02 18:18:47', 'admin', '2025-01-02 18:18:47', NULL);
INSERT INTO `ce_score_line` VALUES (75, 'P001', 'C001', 657, 2022, '2025-01-02 18:18:47', 'admin', '2025-01-02 18:18:47', NULL);
INSERT INTO `ce_score_line` VALUES (76, 'P001', 'C001', 679, 2020, '2025-01-02 18:18:47', 'admin', '2025-01-02 18:18:47', NULL);
INSERT INTO `ce_score_line` VALUES (77, 'P002', 'C001', 666, 2020, '2025-01-02 18:19:13', 'admin', '2025-01-02 18:19:13', NULL);
INSERT INTO `ce_score_line` VALUES (78, 'P002', 'C001', 710, 2021, '2025-01-02 18:19:13', 'admin', '2025-01-02 18:19:13', NULL);
INSERT INTO `ce_score_line` VALUES (79, 'P002', 'C001', 699, 2022, '2025-01-02 18:19:13', 'admin', '2025-01-02 18:19:13', NULL);
INSERT INTO `ce_score_line` VALUES (80, 'P003', 'C001', 701, 2021, '2025-01-02 18:19:29', 'admin', '2025-01-02 18:19:29', NULL);
INSERT INTO `ce_score_line` VALUES (81, 'P003', 'C001', 687, 2022, '2025-01-02 18:19:29', 'admin', '2025-01-02 18:19:29', NULL);
INSERT INTO `ce_score_line` VALUES (82, 'P003', 'C001', 710, 2020, '2025-01-02 18:19:29', 'admin', '2025-01-02 18:19:29', NULL);
INSERT INTO `ce_score_line` VALUES (83, 'P004', 'C001', 699, 2020, '2025-01-02 18:19:49', 'admin', '2025-01-02 18:19:49', NULL);
INSERT INTO `ce_score_line` VALUES (84, 'P004', 'C001', 680, 2021, '2025-01-02 18:19:49', 'admin', '2025-01-02 18:19:49', NULL);
INSERT INTO `ce_score_line` VALUES (85, 'P004', 'C001', 691, 2022, '2025-01-02 18:19:49', 'admin', '2025-01-02 18:19:49', NULL);
INSERT INTO `ce_score_line` VALUES (86, 'P005', 'C001', 700, 2020, '2025-01-02 18:20:03', 'admin', '2025-01-02 18:20:03', NULL);
INSERT INTO `ce_score_line` VALUES (87, 'P005', 'C001', 711, 2022, '2025-01-02 18:20:03', 'admin', '2025-01-02 18:20:03', NULL);
INSERT INTO `ce_score_line` VALUES (88, 'P005', 'C001', 720, 2021, '2025-01-02 18:20:03', 'admin', '2025-01-02 18:20:03', NULL);
INSERT INTO `ce_score_line` VALUES (89, 'P006', 'C002', 677, 2020, '2025-01-02 18:20:19', 'admin', '2025-01-02 18:20:19', NULL);
INSERT INTO `ce_score_line` VALUES (90, 'P006', 'C002', 690, 2021, '2025-01-02 18:20:19', 'admin', '2025-01-02 18:20:19', NULL);
INSERT INTO `ce_score_line` VALUES (91, 'P006', 'C002', 700, 2022, '2025-01-02 18:20:19', 'admin', '2025-01-02 18:20:19', NULL);
INSERT INTO `ce_score_line` VALUES (92, 'P007', 'C002', 670, 2020, '2025-01-02 18:20:35', 'admin', '2025-01-02 18:20:35', NULL);
INSERT INTO `ce_score_line` VALUES (93, 'P007', 'C002', 691, 2022, '2025-01-02 18:20:35', 'admin', '2025-01-02 18:20:35', NULL);
INSERT INTO `ce_score_line` VALUES (94, 'P007', 'C002', 688, 2021, '2025-01-02 18:20:35', 'admin', '2025-01-02 18:20:35', NULL);
INSERT INTO `ce_score_line` VALUES (95, 'P008', 'C002', 710, 2021, '2025-01-02 18:20:58', 'admin', '2025-01-02 18:20:58', NULL);
INSERT INTO `ce_score_line` VALUES (96, 'P008', 'C002', 711, 2022, '2025-01-02 18:20:58', 'admin', '2025-01-02 18:20:58', NULL);
INSERT INTO `ce_score_line` VALUES (97, 'P008', 'C002', 699, 2020, '2025-01-02 18:20:58', 'admin', '2025-01-02 18:20:58', NULL);
INSERT INTO `ce_score_line` VALUES (98, 'P009', 'C002', 681, 2020, '2025-01-02 18:21:12', 'admin', '2025-01-02 18:21:12', NULL);
INSERT INTO `ce_score_line` VALUES (99, 'P009', 'C002', 688, 2021, '2025-01-02 18:21:12', 'admin', '2025-01-02 18:21:12', NULL);
INSERT INTO `ce_score_line` VALUES (100, 'P009', 'C002', 690, 2022, '2025-01-02 18:21:12', 'admin', '2025-01-02 18:21:12', NULL);
INSERT INTO `ce_score_line` VALUES (101, 'P010', 'C003', 620, 2021, '2025-01-02 18:21:29', 'admin', '2025-01-02 18:21:29', NULL);
INSERT INTO `ce_score_line` VALUES (102, 'P010', 'C003', 630, 2022, '2025-01-02 18:21:29', 'admin', '2025-01-02 18:21:29', NULL);
INSERT INTO `ce_score_line` VALUES (103, 'P010', 'C003', 610, 2020, '2025-01-02 18:21:29', 'admin', '2025-01-02 18:21:29', NULL);
INSERT INTO `ce_score_line` VALUES (104, 'P012', 'C005', 500, 2020, '2025-01-02 18:21:44', 'admin', '2025-01-02 18:21:44', NULL);
INSERT INTO `ce_score_line` VALUES (105, 'P012', 'C005', 510, 2021, '2025-01-02 18:21:44', 'admin', '2025-01-02 18:21:44', NULL);
INSERT INTO `ce_score_line` VALUES (106, 'P012', 'C005', 520, 2022, '2025-01-02 18:21:44', 'admin', '2025-01-02 18:21:44', NULL);
INSERT INTO `ce_score_line` VALUES (107, 'P013', 'C007', 477, 2021, '2025-01-02 18:22:00', 'admin', '2025-01-02 18:22:00', NULL);
INSERT INTO `ce_score_line` VALUES (108, 'P013', 'C007', 490, 2022, '2025-01-02 18:22:00', 'admin', '2025-01-02 18:22:00', NULL);
INSERT INTO `ce_score_line` VALUES (109, 'P013', 'C007', 499, 2020, '2025-01-02 18:22:00', 'admin', '2025-01-02 18:22:00', NULL);
INSERT INTO `ce_score_line` VALUES (110, 'P013', 'C011', 499, 2022, '2025-01-02 18:22:14', 'admin', '2025-01-02 18:22:14', NULL);
INSERT INTO `ce_score_line` VALUES (111, 'P013', 'C011', 500, 2021, '2025-01-02 18:22:14', 'admin', '2025-01-02 18:22:14', NULL);
INSERT INTO `ce_score_line` VALUES (112, 'P013', 'C011', 460, 2020, '2025-01-02 18:22:14', 'admin', '2025-01-02 18:22:14', NULL);
INSERT INTO `ce_score_line` VALUES (113, 'P014', 'C008', 500, 2022, '2025-01-02 18:22:29', 'admin', '2025-01-02 18:22:29', NULL);
INSERT INTO `ce_score_line` VALUES (114, 'P014', 'C008', 502, 2021, '2025-01-02 18:22:29', 'admin', '2025-01-02 18:22:29', NULL);
INSERT INTO `ce_score_line` VALUES (115, 'P014', 'C008', 501, 2020, '2025-01-02 18:22:29', 'admin', '2025-01-02 18:22:29', NULL);
INSERT INTO `ce_score_line` VALUES (116, 'P015', 'C009', 450, 2022, '2025-01-02 18:22:45', 'admin', '2025-01-02 18:22:45', NULL);
INSERT INTO `ce_score_line` VALUES (117, 'P015', 'C009', 410, 2020, '2025-01-02 18:22:45', 'admin', '2025-01-02 18:22:45', NULL);
INSERT INTO `ce_score_line` VALUES (118, 'P015', 'C009', 420, 2021, '2025-01-02 18:22:45', 'admin', '2025-01-02 18:22:45', NULL);
INSERT INTO `ce_score_line` VALUES (122, 'P016', 'C009', 400, 2021, '2025-01-02 18:23:08', 'admin', '2025-01-02 18:23:08', NULL);
INSERT INTO `ce_score_line` VALUES (123, 'P016', 'C009', 401, 2022, '2025-01-02 18:23:08', 'admin', '2025-01-02 18:23:08', NULL);
INSERT INTO `ce_score_line` VALUES (124, 'P016', 'C009', 410, 2020, '2025-01-02 18:23:08', 'admin', '2025-01-02 18:23:08', NULL);

-- ----------------------------
-- Table structure for ce_student
-- ----------------------------
DROP TABLE IF EXISTS `ce_student`;
CREATE TABLE `ce_student`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '绑定用户id',
  `student_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '学号',
  `student_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '学生姓名',
  `enrollment_year` int(11) NULL DEFAULT NULL COMMENT '入学年份',
  `graduate_year` int(11) NULL DEFAULT NULL COMMENT '毕业年份',
  `sex` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别 MAN-男 WOMEN-女',
  `achievement` int(11) NULL DEFAULT NULL COMMENT '成绩分数',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_user` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_user` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '学生信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ce_student
-- ----------------------------
INSERT INTO `ce_student` VALUES (19, 42, 'A2020001', '张三', 2020, 2022, 'WOMEN', 510, '2025-01-02 15:46:33', 'admin', '2025-01-30 16:11:27', 'zhangsan');
INSERT INTO `ce_student` VALUES (20, 43, 'A2019001', '李四', 2019, 2022, 'WOMEN', 699, '2025-01-02 15:47:01', 'admin', '2025-01-02 20:33:58', 'admin');
INSERT INTO `ce_student` VALUES (21, 45, 'A2022001', '王五', 2022, 2025, 'MAN', 401, '2025-01-02 15:47:25', 'admin', '2025-01-05 16:05:17', 'admin');
INSERT INTO `ce_student` VALUES (22, 46, 'A2018003', '赵六', 2018, 2023, 'WOMEN', 559, '2025-01-02 15:48:40', 'admin', '2025-01-05 16:05:31', 'admin');
INSERT INTO `ce_student` VALUES (23, 47, 'A2019002', '杨七', 2019, 2023, 'WOMEN', 799, '2025-01-02 15:49:14', 'admin', '2025-01-05 16:07:04', 'admin');
INSERT INTO `ce_student` VALUES (24, 48, 'A2019003', '王二', 2019, 2023, 'WOMEN', 402, '2025-01-02 15:51:02', 'admin', '2025-01-05 16:08:42', 'admin');
INSERT INTO `ce_student` VALUES (25, 49, 'A2019004', '王刚', 2019, 2023, 'MAN', 578, '2025-01-02 15:51:53', 'admin', '2025-01-05 16:08:49', 'admin');
INSERT INTO `ce_student` VALUES (26, NULL, 'B2019001', '张一', 2019, 2023, 'MAN', 677, '2025-01-02 15:52:24', 'admin', '2025-01-02 16:29:15', NULL);
INSERT INTO `ce_student` VALUES (27, NULL, 'B2019004', '杨过', 2019, 2023, 'MAN', 591, '2025-01-02 15:52:58', 'admin', '2025-01-02 16:29:16', NULL);
INSERT INTO `ce_student` VALUES (28, NULL, 'C2019001', '张安丰', 2019, 201, 'WOMEN', 399, '2025-01-02 15:53:26', 'admin', '2025-01-02 16:29:13', NULL);
INSERT INTO `ce_student` VALUES (29, NULL, 'C2019002', '宋八', 2019, 2023, 'MAN', 334, '2025-01-02 15:53:54', 'admin', '2025-01-02 16:29:14', NULL);
INSERT INTO `ce_student` VALUES (30, NULL, 'D2019001', '赵一二', 2019, 2023, 'WOMEN', 555, '2025-01-02 15:54:27', 'admin', '2025-01-02 16:29:12', NULL);
INSERT INTO `ce_student` VALUES (31, NULL, 'C2018002', '田七', 2018, 2022, 'WOMEN', 557, '2025-01-02 15:54:56', 'admin', '2025-01-02 16:29:10', NULL);
INSERT INTO `ce_student` VALUES (32, NULL, 'M2018003', '田二', 2018, 2022, 'WOMEN', 531, '2025-01-02 16:01:34', 'admin', '2025-01-02 16:01:51', 'admin');

-- ----------------------------
-- Table structure for ce_tag_rel
-- ----------------------------
DROP TABLE IF EXISTS `ce_tag_rel`;
CREATE TABLE `ce_tag_rel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tag_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '标签名称',
  `tag_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '标签类型：STUDENT-学生 PROFESSION-专业',
  `rel_id` int(11) NOT NULL COMMENT '关联id。ce_student.id，ce_college.id，ce_profession.id 。',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_user` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_user` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 164 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '标签关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ce_tag_rel
-- ----------------------------
INSERT INTO `ce_tag_rel` VALUES (162, '上海', 'STUDENT', 19, '2025-01-09 20:01:31', 'admin', '2025-01-09 20:01:31', NULL);
INSERT INTO `ce_tag_rel` VALUES (163, '北京', 'STUDENT', 19, '2025-01-09 20:01:31', 'admin', '2025-01-09 20:01:31', NULL);

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-green', 'Y', 'admin', '2022-03-21 21:33:13', 'admin', '2023-03-14 09:16:01', '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2022-03-21 21:33:13', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-light', 'Y', 'admin', '2022-03-21 21:33:13', 'admin', '2023-03-12 07:39:43', '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaOnOff', 'true', 'Y', 'admin', '2022-03-21 21:33:13', '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2022-03-21 21:33:13', 'admin', '2023-03-18 09:07:50', '是否开启注册用户功能（true开启，false关闭）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 113 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '高中学校', 0, 'liefox', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-03-21 21:33:12', 'admin', '2025-01-02 03:32:17');
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '杭州一中', 1, 'liefox', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-03-21 21:33:12', 'admin', '2025-01-02 03:32:31');
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '杭州十六中', 2, 'liefox', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-03-21 21:33:12', 'admin', '2025-01-02 03:33:45');
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '高一<1>班', 1, 'liefox', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-03-21 21:33:12', 'admin', '2025-01-02 03:33:05');
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '高二<1>班', 2, 'liefox', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-03-21 21:33:12', 'admin', '2025-01-02 03:33:14');
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '高三<1>班', 3, 'liefox', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-03-21 21:33:12', 'admin', '2025-01-02 03:33:27');
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '高一<2>班', 1, 'liefox', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-03-21 21:33:12', 'admin', '2025-01-02 03:34:01');
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '高三<2>班', 2, 'liefox', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-03-21 21:33:12', 'admin', '2025-01-02 03:34:16');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(11) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 115 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (19, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (20, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (21, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (22, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (23, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (24, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (25, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (26, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (27, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (28, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (100, 0, 'Java', 'Java', 'book_type', NULL, 'success', 'N', '0', 'admin', '2022-03-22 09:13:22', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (101, 0, 'Vue', 'Vue', 'book_type', NULL, 'success', 'N', '0', 'admin', '2022-03-22 09:13:39', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (102, 0, '生活', '生活', 'book_type', NULL, 'success', 'N', '0', 'admin', '2022-03-22 09:13:55', 'admin', '2022-03-22 09:14:01', NULL);
INSERT INTO `sys_dict_data` VALUES (103, 0, '待审核', '0', 'book_audit', NULL, 'warning', 'N', '0', 'admin', '2022-05-02 17:03:53', 'admin', '2022-05-02 18:30:21', NULL);
INSERT INTO `sys_dict_data` VALUES (105, 0, '审核失败', '2', 'book_audit', NULL, 'danger', 'N', '0', 'admin', '2022-05-02 17:04:13', 'admin', '2022-05-02 18:30:35', NULL);
INSERT INTO `sys_dict_data` VALUES (107, 0, '文章', '2', 'ppt_type', NULL, 'default', 'N', '0', 'admin', '2022-07-24 18:23:37', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (108, 0, '第三方', '3', 'ppt_type', NULL, 'default', 'N', '0', 'admin', '2022-07-24 18:23:45', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 109 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2022-03-21 21:33:13', 'admin', '2023-03-24 07:54:16', '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2022-03-21 21:33:13', '', NULL, '登录状态列表');
INSERT INTO `sys_dict_type` VALUES (100, '书籍类型', 'book_type', '0', 'admin', '2022-03-22 09:12:42', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (101, '书籍审核状态', 'book_audit', '0', 'admin', '2022-05-02 17:03:26', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (102, '幻灯片类型', 'ppt_type', '0', 'admin', '2022-07-24 18:22:44', 'admin', '2022-07-24 18:22:56', NULL);
INSERT INTO `sys_dict_type` VALUES (107, '232', '23', '0', 'admin', '2023-03-24 08:15:56', '', NULL, '23');
INSERT INTO `sys_dict_type` VALUES (108, '232', '2323', '0', 'admin', '2023-03-24 08:16:01', '', NULL, '2323');

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 622 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO `sys_logininfor` VALUES (391, '', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '1', '登录用户名：NONE_PROVIDED 不存在', NULL);
INSERT INTO `sys_logininfor` VALUES (392, '', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '1', '登录用户名：NONE_PROVIDED 不存在', NULL);
INSERT INTO `sys_logininfor` VALUES (393, '', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '1', '登录用户名：NONE_PROVIDED 不存在', NULL);
INSERT INTO `sys_logininfor` VALUES (394, '', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '1', '登录用户名：NONE_PROVIDED 不存在', NULL);
INSERT INTO `sys_logininfor` VALUES (395, '', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '1', '登录用户名：NONE_PROVIDED 不存在', NULL);
INSERT INTO `sys_logininfor` VALUES (396, '', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '1', '登录用户名：NONE_PROVIDED 不存在', NULL);
INSERT INTO `sys_logininfor` VALUES (397, 'admin', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (398, 'admin', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (399, 'admin', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (400, 'admin', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (401, 'admin', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (402, 'admin', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (403, 'admin', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (404, 'admin', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (405, 'admin', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (406, 'admin', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (407, 'admin', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (408, 'admin', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (409, 'admin', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (410, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (411, 'admin', '192.168.31.121', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (412, 'admin', '192.168.31.121', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (413, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (414, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (415, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (416, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (417, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (418, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (419, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (420, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (421, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (422, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (423, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (424, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (425, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (426, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (427, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (428, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (429, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (430, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (431, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (432, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (433, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (434, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (435, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (436, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (437, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (438, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (439, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (440, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (441, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (442, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (443, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (444, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (445, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (446, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (447, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (448, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (449, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (450, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (451, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (452, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (453, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (454, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (455, 'admin', '127.0.0.1', '内网IP', 'Unknown', 'Unknown', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (456, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (457, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (458, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (459, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (460, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (461, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (462, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (463, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (464, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (465, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (466, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (467, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (468, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (469, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (470, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (471, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (472, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (473, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (474, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (475, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (476, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (477, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (478, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (479, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (480, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (481, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (482, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (483, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (484, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (485, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (486, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (487, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (488, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (489, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (490, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (491, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (492, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (493, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (494, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (495, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (496, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (497, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (498, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (499, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (500, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (501, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (502, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (503, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (504, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (505, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (506, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (507, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (508, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (509, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (510, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (511, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (512, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (513, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (514, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (515, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (516, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (517, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (518, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (519, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (520, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (521, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (522, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (523, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (524, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (525, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (526, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (527, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (528, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (529, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (530, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (531, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (532, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (533, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (534, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (535, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (536, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (537, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (538, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (539, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (540, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (541, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (542, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (543, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (544, 'bianjiawang001', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '1', '密码错误', NULL);
INSERT INTO `sys_logininfor` VALUES (545, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (546, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (547, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (548, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (549, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (550, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (551, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (552, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (553, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (554, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (555, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (556, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (557, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (558, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (559, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (560, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (561, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (562, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (563, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (564, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (565, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (566, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (567, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (568, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (569, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (570, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (571, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (572, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (573, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (574, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (575, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (576, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (577, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (578, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (579, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (580, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (581, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (582, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (583, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (584, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (585, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (586, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (587, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (588, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (589, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (590, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (591, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (592, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (593, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (594, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (595, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (596, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (597, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (598, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (599, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (600, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (601, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (602, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (603, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (604, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (605, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (606, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (607, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (608, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (609, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (610, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (611, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (612, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (613, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (614, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (615, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (616, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (617, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (618, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (619, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);
INSERT INTO `sys_logininfor` VALUES (620, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '退出成功', NULL);
INSERT INTO `sys_logininfor` VALUES (621, 'admin', '127.0.0.1', '内网IP', 'Chrome 11', 'Mac OS X', '0', '用户登录成功', NULL);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(11) NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int(11) NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2065 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2022-03-21 21:33:12', 'admin', '2023-03-09 07:50:27', '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2022-03-21 21:33:12', '', NULL, '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2022-03-21 21:33:12', '', NULL, '系统工具目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2022-03-21 21:33:12', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2022-03-21 21:33:12', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2022-03-21 21:33:12', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '高中管理', 1, 4, 'dept', 'system/dept/index', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2022-03-21 21:33:12', 'admin', '2025-01-02 03:34:39', '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', 1, 0, 'C', '1', '0', 'system:post:list', 'post', 'admin', '2022-03-21 21:33:12', 'admin', '2025-01-02 03:30:59', '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', 1, 1, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2022-03-21 21:33:12', 'admin', '2023-03-09 07:45:01', '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2022-03-21 21:33:12', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2022-03-21 21:33:12', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (114, '表单构建', 3, 1, 'build', 'tool/build/index', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2022-03-21 21:33:12', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu` VALUES (116, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2022-03-21 21:33:12', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (1001, '用户查询', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户新增', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户修改', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户删除', 100, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导出', 100, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '用户导入', 100, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '重置密码', 100, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色查询', 101, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色新增', 101, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色修改', 101, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色删除', 101, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '角色导出', 101, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单新增', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单修改', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '菜单删除', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门查询', 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门新增', 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门修改', 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '部门删除', 103, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位查询', 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位新增', 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位修改', 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位删除', 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '岗位导出', 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典查询', 105, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典新增', 105, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典修改', 105, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典删除', 105, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '字典导出', 105, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数查询', 106, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数新增', 106, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数修改', 106, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数删除', 106, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '参数导出', 106, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2052, '学生管理', 0, 4, 'student', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'peoples', 'admin', '2023-03-31 07:32:09', 'admin', '2025-01-04 20:17:41', '');
INSERT INTO `sys_menu` VALUES (2053, '学生信息', 2052, 1, 'student', 'entrance/student/index', NULL, 1, 0, 'C', '0', '0', 'entrance:student:list', 'people', 'admin', '2023-03-31 07:32:58', 'admin', '2023-03-31 07:33:52', '');
INSERT INTO `sys_menu` VALUES (2054, '院校管理', 0, 5, 'college', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'education', 'admin', '2025-01-01 01:12:06', 'admin', '2025-01-04 20:17:45', '');
INSERT INTO `sys_menu` VALUES (2055, '院校查询', 2054, 1, 'college', 'entrance/college/index', NULL, 1, 0, 'C', '0', '0', 'entrance:college:index', 'search', 'admin', '2025-01-01 01:13:09', 'admin', '2025-01-01 01:13:28', '');
INSERT INTO `sys_menu` VALUES (2056, '专业查询', 2054, 2, 'profession', 'entrance/profession/index', NULL, 1, 0, 'C', '0', '0', 'entrance:profession:index', 'guide', 'admin', '2025-01-01 07:33:13', 'admin', '2025-01-01 07:33:23', '');
INSERT INTO `sys_menu` VALUES (2057, '学生添加', 2052, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'entrance:student:add', '#', 'admin', '2025-01-02 03:38:11', 'admin', '2025-01-02 03:38:27', '');
INSERT INTO `sys_menu` VALUES (2058, '学生编辑', 2052, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'entrance:student:edit', '#', 'admin', '2025-01-02 03:38:50', 'admin', '2025-01-02 03:39:05', '');
INSERT INTO `sys_menu` VALUES (2059, '学生删除', 2052, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'entrance:student:remove', '#', 'admin', '2025-01-02 03:39:25', 'admin', '2025-01-04 06:45:48', '');
INSERT INTO `sys_menu` VALUES (2060, '学生标签', 2052, 5, '', NULL, NULL, 1, 0, 'F', '0', '0', 'entrance:student:tag', '#', 'admin', '2025-01-04 06:45:39', 'admin', '2025-01-04 06:45:58', '');
INSERT INTO `sys_menu` VALUES (2061, '填报管理', 0, 6, 'aspiration', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'list', 'admin', '2025-01-04 19:00:03', 'admin', '2025-01-04 20:17:55', '');
INSERT INTO `sys_menu` VALUES (2062, '测评分析', 2061, 1, 'evaluate', 'entrance/aspiration/evaluate', NULL, 1, 0, 'C', '0', '0', 'entrance:aspiration:evaluate', 'cascader', 'admin', '2025-01-04 20:14:59', 'admin', '2025-01-05 04:35:54', '');
INSERT INTO `sys_menu` VALUES (2063, '志愿管理', 2061, 2, 'aspiration', 'entrance/aspiration/index', NULL, 1, 0, 'C', '0', '0', 'entrance:aspiration:index', 'logininfor', 'admin', '2025-01-05 04:34:31', 'admin', '2025-01-29 20:49:11', '');
INSERT INTO `sys_menu` VALUES (2064, '志愿填报', 2061, 3, 'form', 'entrance/aspiration/form', NULL, 1, 0, 'C', '0', '0', 'entrance:aspiration:form', 'edit', 'admin', '2025-01-29 21:01:01', 'admin', '2025-01-29 21:03:17', '');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(11) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int(11) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int(11) NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 501 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int(11) NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2022-03-21 21:33:12', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2022-03-21 21:33:12', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(11) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2022-03-21 21:33:12', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2022-03-21 21:33:12', 'admin', '2023-03-31 21:50:03', '普通角色');
INSERT INTO `sys_role` VALUES (11, '学生角色', 'student', 30, '1', 1, 1, '0', '0', 'admin', '2025-01-02 03:40:36', 'admin', '2025-01-30 03:03:57', NULL);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 2052);
INSERT INTO `sys_role_menu` VALUES (2, 2053);
INSERT INTO `sys_role_menu` VALUES (11, 2052);
INSERT INTO `sys_role_menu` VALUES (11, 2053);
INSERT INTO `sys_role_menu` VALUES (11, 2054);
INSERT INTO `sys_role_menu` VALUES (11, 2055);
INSERT INTO `sys_role_menu` VALUES (11, 2056);
INSERT INTO `sys_role_menu` VALUES (11, 2058);
INSERT INTO `sys_role_menu` VALUES (11, 2060);
INSERT INTO `sys_role_menu` VALUES (11, 2061);
INSERT INTO `sys_role_menu` VALUES (11, 2062);
INSERT INTO `sys_role_menu` VALUES (11, 2064);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 103, 'admin', 'mock', '00', 'mock@163.com', '15888888823', '0', '/profile/avatar/2024/08/04/c32c6b5d-af41-4af9-a0a3-5260b7cb3f1a.jpeg', '$2a$10$k0RU5nwQgbjsPyCP/z3Jp.u5DD3p6PjmrpwDFw4VF0bZToH8XqDAy', '0', '0', '127.0.0.1', '2023-03-12 05:23:08', 'admin', '2022-03-21 21:33:12', 'admin', '2024-08-04 10:57:54', '管理员');
INSERT INTO `sys_user` VALUES (42, 105, 'zhangsan', '张三', '00', '1@qq.com', '18766662222', '0', '', '$2a$10$neIrZOHcPnm7H2dx3K0D9u.T/tlqByqQJ4xkFI0Z1vkjhADTcJ2su', '0', '0', '', NULL, 'admin', '2025-01-02 03:02:32', 'admin', '2025-01-02 03:48:27', NULL);
INSERT INTO `sys_user` VALUES (43, 109, 'lisi', '李四', '00', '', '', '1', '', '$2a$10$Fo9nPSQMYSFiDF5.vm5Hr.nArt2KE/4VvQruAwYr9u91urqK/kMti', '0', '0', '', NULL, 'admin', '2025-01-02 05:36:31', '', NULL, NULL);
INSERT INTO `sys_user` VALUES (45, 109, 'wangwu', '王五', '00', '', '', '0', '', '$2a$10$KgqKW6TtEExltYFRapU1OuAWgXsxiaXBcy8xiDmBSVltRLhDh4skC', '0', '0', '', NULL, 'admin', '2025-01-05 03:02:43', 'admin', '2025-01-05 03:05:06', NULL);
INSERT INTO `sys_user` VALUES (46, 109, 'zhaoliu', '赵六', '00', '', '18727272111', '0', '', '$2a$10$1VeRIFXixkuYSQM36MYy/OPOiU9x9EKkVhELu4FJV22FzVDBzmTi.', '0', '0', '', NULL, 'admin', '2025-01-05 03:04:51', '', NULL, NULL);
INSERT INTO `sys_user` VALUES (47, 108, 'yangqi', '杨七', '00', '', '', '1', '', '$2a$10$NhuZH532E/.RY7bTzogYeOP3n3eQPgY2eGdrDqzjd2ypiUOlY.GOK', '0', '0', '', NULL, 'admin', '2025-01-05 03:06:48', '', NULL, NULL);
INSERT INTO `sys_user` VALUES (48, 109, 'wanger', '王二', '00', '', '', '0', '', '$2a$10$TpMog3ez6ZWbXwPu2n/J7.NXxxrNJ0F3YGEMLhdcbVZotUO/LWIzm', '0', '0', '', NULL, 'admin', '2025-01-05 03:08:07', '', NULL, NULL);
INSERT INTO `sys_user` VALUES (49, 108, 'wanggang', '王刚', '00', '', '18711112222', '1', '', '$2a$10$7cNs3Z8LRIrZesrNpDeuB.odlYmXillCVbsdpe/C4JdMJwncdkXrm', '0', '0', '', NULL, 'admin', '2025-01-05 03:08:32', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);
INSERT INTO `sys_user_post` VALUES (6, 4);
INSERT INTO `sys_user_post` VALUES (42, 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);
INSERT INTO `sys_user_role` VALUES (6, 2);
INSERT INTO `sys_user_role` VALUES (42, 11);
INSERT INTO `sys_user_role` VALUES (43, 11);
INSERT INTO `sys_user_role` VALUES (45, 11);
INSERT INTO `sys_user_role` VALUES (46, 11);
INSERT INTO `sys_user_role` VALUES (47, 11);
INSERT INTO `sys_user_role` VALUES (48, 11);
INSERT INTO `sys_user_role` VALUES (49, 11);

SET FOREIGN_KEY_CHECKS = 1;
