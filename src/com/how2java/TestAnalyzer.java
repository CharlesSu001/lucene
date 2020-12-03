/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: TestAnalyzer
 * Author:   苏晨宇
 * Date:     2020/12/2 10:19
 * Description: 分词器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.how2java;

import org.apache.lucene.analysis.TokenStream;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;

/**
 * 〈一句话功能简述〉<br>
 * 〈分词器〉
 *
 * @author 苏晨宇
 * @create 2020/12/2
 * @since 1.0.0
 */
public class TestAnalyzer {
    public static void main(String[] args) throws IOException {
        IKAnalyzer analyzer = new IKAnalyzer();
        TokenStream ts = analyzer.tokenStream("name", "亚索杀了你的马");
        ts.reset();
        while (ts.incrementToken()) {
            System.out.println(ts.reflectAsString(false));
        }
    }
}
 
