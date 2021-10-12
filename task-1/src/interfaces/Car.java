package interfaces;

import java.io.Serializable;


public class Car implements Serializable {
    private static final long serialVersionUID = 38074790563195965L;
    final String serialNumber, brand, model, color;
    final int year;
    final float price, weight;

    public Car(String serialNumber, String brand, String model, String color, int year, float price, float weight) {
        this.serialNumber = serialNumber;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.year = year;
        this.price = price;
        this.weight = weight;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public int getYear() {
        return year;
    }

    public float getPrice() {
        return price;
    }

    public float getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Car{" +
                "serialNumber='" + serialNumber + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", weight=" + weight +
                '}';
    }
}
