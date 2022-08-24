package com.wjy.demo.disruptor.multi;

/**
 * @author weijiayu
 * @date 2022/8/24 14:58
 */
public class Order {

    private String id;
    private String name;
    private Double price;

    public Order() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
