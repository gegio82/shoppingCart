package it.ggerosa.shoppingcart;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private String code;
    private String name;
    private BigDecimal unitPrice;

    public Product(String code, String name, BigDecimal unitPrice) {
        this.code = code;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return code.equals(product.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
