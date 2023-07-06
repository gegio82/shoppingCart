package it.ggerosa.shoppingcart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

public class Cart {
    private final Set<Item> items = new HashSet<>();
    private final BigDecimal taxRate;
    private final BigDecimal thresholdForDiscount = new BigDecimal("400.00");
    private final BigDecimal discountRate = new BigDecimal("0.10");

    public Cart() {
        this(BigDecimal.ZERO);
    }

    public Cart(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public void addItem(Product product, int quantity) {
        if (quantity == 0) {
            return;
        }
        Item currentItem = items.stream()
                .filter(it -> it.getProduct().equals(product))
                .findFirst()
                .orElse(new Item(product, 0));
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
        BigDecimal totalPriceBeforeDiscount = items.stream().map(Item::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        if (totalPriceBeforeDiscount.compareTo(thresholdForDiscount) > 0) {
            return totalPriceBeforeDiscount.multiply(BigDecimal.ONE.subtract(discountRate));
        } else {
            return totalPriceBeforeDiscount;
        }
    }

    private BigDecimal getTaxes() {
        return getTotalPrice().multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalPriceIncludingTaxes() {
        return getTotalPrice().add(getTaxes());
    }
}
