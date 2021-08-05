package com.dumpit.ffff;

// 마켓아이템 데이터 담기
public class MarketItem {
    String name;
    int price;
    int resId;

    // Generate > Constructor
    public MarketItem(String name, int price, int resId) {
        this.name = name;
        this.price = price;
        this.resId = resId;
    }

    // Generate > Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    // Generate > toString() : 아이템을 문자열로 출력

    @Override
    public String toString() {
        return "MarketItem{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}