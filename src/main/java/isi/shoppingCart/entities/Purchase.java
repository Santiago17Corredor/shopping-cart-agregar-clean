package isi.shoppingCart.entities;

import java.time.LocalDateTime;
import java.util.List;

public class Purchase {
    private int id;
    private List<CartItem> items;
    private double total;
    private LocalDateTime date;

    public Purchase(List<CartItem> items, double total) {
        this.items = items;
        this.total = total;
        this.date = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public LocalDateTime getDate() {
        return date;
    }
}

