package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import ru.vzotov.accounting.interfaces.common.dto.MoneyDTO;

import java.io.Serializable;

public class ItemDTO implements Serializable {
    private String name;
    private MoneyDTO price;
    private double quantity;
    private MoneyDTO sum;
    private Integer index;
    private String category;

    public ItemDTO() {
    }

    public ItemDTO(String name, MoneyDTO price, double quantity, MoneyDTO sum, Integer index, String category) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.sum = sum;
        this.index = index;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public MoneyDTO getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public MoneyDTO getSum() {
        return sum;
    }

    public String getCategory() {
        return category;
    }

    public Integer getIndex() {
        return index;
    }
}
