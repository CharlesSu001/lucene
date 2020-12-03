/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: Product
 * Author:   苏晨宇
 * Date:     2020/12/2 10:51
 * Description: 实体类存放产品信息
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.how2java;

/**
 * 〈一句话功能简述〉<br>
 * 〈实体类存放产品信息〉
 *
 * @author 苏晨宇
 * @create 2020/12/2
 * @since 1.0.0
 */
public class Product {
    int id;
    String name;
    String category;
    float price;
    String place;
    String code;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", place='" + place + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
 
