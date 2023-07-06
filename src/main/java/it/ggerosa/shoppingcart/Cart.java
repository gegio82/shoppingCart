package it.ggerosa.shoppingcart;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Cart {
    private Set<Item> items = new HashSet<>();
    private BigDecimal taxRate = BigDecimal.ZERO;
    private BigDecimal trasholdForDiscount = new BigDecimal("200.00");
    private BigDecimal discountRate = new BigDecimal("0.10");

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public void addItem(Product product, int quantity) {
        if (quantity == 0) {
            return;
        }
        Item currentItem = items.stream().filter(it -> it.getProduct().equals(product)).findFirst().orElse(new Item(product, 0));
        currentItem.updateQuantity(quantity);
        if (currentItem.getQuantity() > 0) {
            items.add(currentItem);
        } else {
            items.remove(currentItem);
        }
    }

    public Set<Item> getItems() {
        return items;
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(Item::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal getTaxes() {
        return getTotalPrice().multiply(taxRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getTotalPriceIncludingTaxes() {
        return getTotalPrice().add(getTaxes());
    }
}