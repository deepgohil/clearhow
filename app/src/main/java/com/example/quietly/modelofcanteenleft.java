package com.example.quietly;

public class modelofcanteenleft {
String image;
String productnameone;
String productpriceone;

    @Override
    public String toString() {
        return "modelofcanteenleft{" +
                "image='" + image + '\'' +
                ", productnameone='" + productnameone + '\'' +
                ", productpriceone='" + productpriceone + '\'' +
                '}';
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductnameone() {
        return productnameone;
    }

    public void setProductnameone(String productnameone) {
        this.productnameone = productnameone;
    }

    public String getProductpriceone() {
        return productpriceone;
    }

    public void setProductpriceone(String productpriceone) {
        this.productpriceone = productpriceone;
    }
}
