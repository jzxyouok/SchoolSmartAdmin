package bean;

import java.io.Serializable;

/**
 * 商品类.
 */
public class Good implements Serializable{
    private int id;//同key
    private String name;
    private double price;
    private String img;
    private int num;
    private int type;//1代表超市，2代表外卖，3代表早餐，4代表团购

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
