package com.hebut.earbook.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class StringUtil {
    private static HashSet<Character> PunctuationSet = new HashSet<Character>() {
        {
            add(',');
            add('.');
            add('"');
            add('!');
            add('?');
        }
    };

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString 原始字符串
     * @param length      指定长度
     * @return
     */
    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString 原始字符串
     * @param length      指定长度
     * @param size        指定列表大小
     * @return
     */
    public static List<String> getStrList(String inputString, int length,
                                          int size) {
        List<String> list = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    /**
     * 分割字符串，如果开始位置大于字符串长度，返回空
     *
     * @param str 原始字符串
     * @param f   开始位置
     * @param t   结束位置
     * @return
     */
    public static String substring(String str, int f, int t) {
        // 如果起点已经超过字符串的长度，直接返回空
        if (f > str.length()) {
            return null;
        }

        // 将当前截取字符串的起点移动到单词前
        while (f > 0 && Character.isLetterOrDigit(str.charAt(f))) {
            f--;
        }

        // 如果终点已经超过字符串的长度，则从起点截到终点
        if (t > str.length()) {
            return str.substring(f);
        }
        // 将当前截取字符串的终点移动到单词前
        while (t > 0 && Character.isLetterOrDigit(str.charAt(t))) {
            t--;
        }

        return str.substring(f, t);

    }

    public static boolean isPunctuation(char ch) {
        return PunctuationSet.contains(ch);
    }
}
