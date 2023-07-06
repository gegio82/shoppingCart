package it.ggerosa.shoppingcart;

import java.math.BigDecimal;
import java.util.Objects;

public class Item {
    private Product product;
    private int quantity;

    public Item(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
    public Product getProduct() {
        return product;
    }

    public BigDecimal getTotalPrice() {
        return product.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public void updateQuantity(int delta) {
        quantity += delta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return product.equals(item.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }
}
