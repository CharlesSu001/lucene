/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: ProductUtil
 * Author:   苏晨宇
 * Date:     2020/12/2 10:53
 * Description: 把文件转换为泛型是product的集合
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.how2java;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈把文件转换为泛型是product的集合〉
 *
 * @author 苏晨宇
 * @create 2020/12/2
 * @since 1.0.0
 */
public class ProductUtil {
    public static void main(String[] args) throws IOException {
        String fileName = "140k_products.txt";
        List<Product> products = file2list(fileName);
        System.out.println(products.size());
    }

    public static List<Product> file2list(String fileName) throws IOException {
        File f = new File(fileName);
        List<String> lines = FileUtils.readLines(f, "UTF-8");
        List<Product> products = new ArrayList<>();
        for (String line : lines) {
            Product p = line2product(line);
            products.add(p);
        }
        return products;
    }

    private static Product line2product(String line) {
        Product p = new Product();
        String[] fields = line.split(",");
        p.setId(Integer.parseInt(fields[0]));
        p.setName(fields[1]);
        p.setCategory(fields[2]);
        p.setPrice(Float.parseFloat(fields[3]));
        p.setPlace(fields[4]);
        p.setCode(fields[5]);
        return p;
    }
}
 
