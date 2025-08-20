package io.dapr.quarkus.examples.producer;

import java.util.UUID;

public class Order {

    private String id;
    private String item;
    private Integer amount;

    public Order() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Order{" + "id='" + id + '\'' + ", item='" + item + '\'' + ", amount=" + amount + '}';
    }
}