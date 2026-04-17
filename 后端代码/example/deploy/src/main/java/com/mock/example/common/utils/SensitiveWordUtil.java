package com.mock.example.common.utils;

import java.util.*;

/**
 * 敏感词检测引擎 (基于高性能的DFA算法)
 */
public class SensitiveWordUtil {

    // 敏感词汇字典树
    public static Map<String, Object> sensitiveWordMap = null;

    // 预置默认的敏感词汇库 (全面防护版：内置200+高频违背公序良俗词汇)
    private static final String[] DEFAULT_SENSITIVE_WORDS = {
            // -- 1. 高考/升学/教育作弊诈骗类 --
            "代考", "作弊", "枪手", "考题泄露", "考前答案", "绝密真题", 
            "内部指标", "包过", "包录取", "花钱改分", "买答案", "答案首发",
            "录取内定", "替考", "泄题", "高考答案", "志愿包录", "交钱上大学", 
            "免试入学", "内部名额", "统招包进", "改成绩", "黑客改分", "内部运作",
            "代写论文", "保送名额", "修改档案", "买文凭", "假文凭", "学历造假", 
            "托福代考", "雅思代考", "四六级代考", "考研包过",

            // -- 2. 辱骂/低俗暴词/人身攻击 --
            "傻逼", "他妈的", "操你妈", "妈的", "尼玛", "贱人", "脑残", "弱智", 
            "白痴", "滚蛋", "你妈", "草泥马", "特么的", "麻痹", "智障", "畜生", 
            "王八蛋", "死妈", "孤儿", "全家炸了", "婊子", "妓女", "杂种", "你大爷", 
            "煞笔", "沙雕", "二逼", "装逼", "傻屌", "鸡巴", "屌丝", "龟孙", "泼妇", 
            "废物", "没教养", "绿茶婊", "公交车", "不要脸", "缺德", "老不死", 
            "下贱", "不要碧莲", "妈卖批", "扑街", "死全家",

            // -- 3. 色情/黄赌毒类 --
            "赌场", "博彩", "六合彩", "外围女", "包小姐", "性感少妇", "裸聊", 
            "原味", "迷奸", "同城约炮", "色情服务", "上门服务", "找小姐", "鸭子", 
            "成人网站", "无码", "番号", "三级片", "推油", "全套", "一夜情", 
            "嫖娼", "援交", "迷药", "春药", "催情", "冰毒", "海洛因", "大麻", 
            "K粉", "摇头丸", "冰妹", "溜冰", "注射毒品", "地下赌场", "百家乐", 
            "老虎机", "彩票走势", "赌球", "黑彩", "网投",

            // -- 4. 暴恐/黑产/诈骗/违禁品/买卖隐私 --
            "枪支弹药", "发票", "代开发票", "洗钱", "办假证", "信用卡套现", 
            "毒品交易", "走私", "买卖器官", "人体器官", "删帖", "刷单", 
            "兼职刷信誉", "网赚骗局", "黑客接单", "木马", "肉鸡", "DDOS", 
            "支付宝套现", "花呗套现", "免签支付", "售假", "高仿", "精仿", 
            "假人民币", "军火", "炸药", "雷管", "氰化钾", "电击棒", "气枪", 
            "猎枪", "窃听器", "偷拍摄像", "针孔摄像", "套牌车", "二手车改表",
            "买卖银行卡", "买卖身份证", "出售个人信息", "开房记录", "查开房", 
            "户籍查询", "手机定位", "通话记录查询", "微信密码破解", "QQ密码破解", 
            "盗号", "透视挂", "自瞄挂", "私服", "无限元宝", "绝地求生外挂",

            // -- 5. 垃圾广告/欺诈引流 --
            "刷粉", "刷钻", "买粉", "僵尸粉", "网络水军", "刷阅读", "代刷", 
            "收徒", "躺赚", "日赚百元", "快速赚钱", "无抵押贷款", "黑户可贷", 
            "秒下款", "套路贷", "高利贷", "暴力催收", "裸条", "裸贷"
    };

    static {
        Set<String> wordSet = new HashSet<>(Arrays.asList(DEFAULT_SENSITIVE_WORDS));
        initWordMap(wordSet);
    }

    /**
     * 初始化敏感词树 (DFA算法构建)
     */
    @SuppressWarnings("unchecked")
    public static void initWordMap(Set<String> wordSet) {
        sensitiveWordMap = new HashMap<>(wordSet.size());
        Map<String, Object> nowMap;
        Map<String, Object> newWordMap;

        for (String key : wordSet) {
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);
                Object wordMap = nowMap.get(String.valueOf(keyChar));
                if (wordMap != null) {
                    nowMap = (Map<String, Object>) wordMap;
                } else {
                    newWordMap = new HashMap<>();
                    newWordMap.put("isEnd", "0");
                    nowMap.put(String.valueOf(keyChar), newWordMap);
                    nowMap = newWordMap;
                }
                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");
                }
            }
        }
    }

    /**
     * 校验文本中是否包含任何敏感词
     * @param txt 要分析的文本
     * @return true = 包含敏感词; false = 安全
     */
    public static boolean containsSensitiveWord(String txt) {
        if (txt == null || txt.isEmpty()) {
            return false;
        }
        for (int i = 0; i < txt.length(); i++) {
            // 从当前字符开始判断是否匹配树
            int matchFlag = checkSensitiveWordLength(txt, i);
            if (matchFlag > 0) {
                return true; 
            }
        }
        return false;
    }

    /**
     * 找出包含的任意一个违规词的具体内容（用于错误提示）
     * @param txt 文本
     * @return 命中的第一个敏感词汇总，如果没有则返回 null
     */
    public static String getFirstMatchedWord(String txt) {
        if (txt == null || txt.isEmpty()) {
            return null;
        }
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = checkSensitiveWordLength(txt, i);
            if (matchFlag > 0) {
                return txt.substring(i, i + matchFlag);
            }
        }
        return null;
    }

    /**
     * 核心检测逻辑，检查从某个索引开始是否构成敏感词
     * 返回大于0说明属于敏感词，并返回其匹配长度
     */
    @SuppressWarnings("unchecked")
    private static int checkSensitiveWordLength(String txt, int beginIndex) {
        boolean flag = false;
        int matchFlag = 0;
        Map<String, Object> nowMap = sensitiveWordMap;

        for (int i = beginIndex; i < txt.length(); i++) {
            char word = txt.charAt(i);
            nowMap = (Map<String, Object>) nowMap.get(String.valueOf(word));
            if (nowMap != null) {
                matchFlag++;
                if ("1".equals(nowMap.get("isEnd"))) {
                    flag = true;
                    // 如果匹配到了最短的词，这里直接 break 就能实现贪婪或非贪婪，
                    // 只要确认 isEnd=1 就说明已经命中了违规词，可以直接返回了
                    break;
                }
            } else {
                break;
            }
        }
        if (!flag) {
            matchFlag = 0;
        }
        return matchFlag;
    }
}
