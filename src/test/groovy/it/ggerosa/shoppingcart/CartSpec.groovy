package it.ggerosa.shoppingcart

import spock.lang.Specification

class CartSpec extends Specification {

    private static final String DOVE_SOAP_CODE = "DoveSoapCode"
    private static final String NIVEA_SOAP_CODE = "NiveaSoapCode"
    private static final String AXE_DEO_CODE = "AxeDeoCode"
    
    void "Total price of empty cart is zero"() {
        given: "An empty shopping cart"
            Cart cart = new Cart()
        expect: "Total price is zero"
            cart.totalPrice == BigDecimal.ZERO
    }
    
    void "When quantity is zero or less, product is not added"() {
        given: "An empty shopping cart"
            Cart cart = new Cart()
        and: "And a product, Dove Soap with a unit price of 39.99"
            Product doveSoap = new Product(DOVE_SOAP_CODE, "Dove Soap", new BigDecimal("39.99"))
        when: "The user adds Dove Soaps to the shopping cart"
           cart.addItem(doveSoap, quantity)
        then: "The product has not be added and the cart is still empty"
            cart.getItems().isEmpty()
        and: "Total price is still zero"
            cart.totalPrice == BigDecimal.ZERO
        where:
            quantity << [-10, -1, 0]
    }

    void "Add products to the shopping cart"() {
        given: "An empty shopping cart"
            Cart cart = new Cart()
        and: "And a product, Dove Soap with a unit price of 39.99"
            Product doveSoap = new Product(DOVE_SOAP_CODE, "Dove Soap", new BigDecimal("39.99"))
        when: "The user adds 5 Dove Soaps to the shopping cart"
            cart.addItem(doveSoap, 5)
        then: "The shopping cart should contain 5 Dove Soaps each with a unit price of 39.99"
            with(cart.items.find { it.product.code == DOVE_SOAP_CODE}) {
                it.quantity == 5
                it.product.unitPrice == new BigDecimal("39.99")
            }
        and: "The shopping cart’s total price should equal 199.95"
            cart.totalPrice == new BigDecimal("199.95")
    }

    void "Can add two different products"() {
        given: "An empty shopping cart"
            Cart cart = new Cart()
        and: "And two products, Dove Soap with a unit price of 39.99 and Nivea Soap with a unit price of 29.99"
            Product doveSoap = new Product(DOVE_SOAP_CODE, "Dove Soap", new BigDecimal("39.99"))
            Product niveaSoap = new Product(NIVEA_SOAP_CODE, "Nivea Soap", new BigDecimal("29.99"))
        when: "The user added 5 Dove Soaps to the shopping cart"
            cart.addItem(doveSoap, 5)
        and : "The user adds another 3 Nivea Soaps to the shopping cart"
            cart.addItem(niveaSoap, 3)
        then: "The shopping cart should contain 5 Dove Soaps each with a unit price of 39.99"
            with(cart.items.find { it.product.code == DOVE_SOAP_CODE}) {
                it.quantity == 5
                it.product.unitPrice == new BigDecimal("39.99")
            }
        and: "The shopping cart should contain 3 Dove Soaps each with a unit price of 29.99"
            with(cart.items.find { it.product.code == NIVEA_SOAP_CODE}) {
                it.quantity == 3
                it.product.unitPrice == new BigDecimal("29.99")
            }
        and: "The shopping cart’s total price should equal 289.92"
            cart.totalPrice == new BigDecimal("289.92")
    }

    void "Add additional products of the same type to the shopping cart"() {
        given: "An empty shopping cart"
            Cart cart = new Cart()
        and: "And a product, Dove Soap with a unit price of 39.99"
            Product doveSoap = new Product(DOVE_SOAP_CODE, "Dove Soap", new BigDecimal("39.99"))
        and: "The user added 5 Dove Soaps to the shopping cart"
            cart.addItem(doveSoap, 5)
        when: "The user adds another 3 Dove Soaps to the shopping cart"
            cart.addItem(doveSoap, 3)
        then: "The shopping cart should contain 8 Dove Soaps each with a unit price of 39.99"
            with(cart.items.find { it.product.code == DOVE_SOAP_CODE}) {
                it.quantity == 8
                it.product.unitPrice == new BigDecimal("39.99")
            }
        and: "The shopping cart’s total price should equal 319.92"
            cart.totalPrice == new BigDecimal("319.92")
    }

    void "Remove products of the same type to the shopping cart"() {
        given: "An empty shopping cart"
            Cart cart = new Cart()
        and: "And a product, Dove Soap with a unit price of 39.99"
            Product doveSoap = new Product(DOVE_SOAP_CODE, "Dove Soap", new BigDecimal("39.99"))
        and: "The user added 5 Dove Soaps to the shopping cart"
            cart.addItem(doveSoap, 5)
        when: "The user removes  3 Dove Soaps to the shopping cart"
            cart.addItem(doveSoap, -3)
        then: "The shopping cart should contain 2 Dove Soaps each with a unit price of 39.99"
            with(cart.items.find { it.product.code == DOVE_SOAP_CODE}) {
                it.quantity == 2
                it.product.unitPrice == new BigDecimal("39.99")
            }
        and: "The shopping cart’s total price should equal 79.98"
            cart.totalPrice == new BigDecimal("79.98")
    }

    void "When the user removes the same amount of items or more, the product will be removed from cart"() {
        given: "An empty shopping cart"
            Cart cart = new Cart()
        and: "And a product, Dove Soap with a unit price of 39.99"
            Product doveSoap = new Product(DOVE_SOAP_CODE, "Dove Soap", new BigDecimal("39.99"))
        and: "The user added 3 Dove Soaps to the shopping cart"
            cart.addItem(doveSoap, 3)
        expect: "The shopping cart’s total price should equal 119.97"
            cart.totalPrice == new BigDecimal("119.97")
        when: "The user adds remove #quantityToRemove Dove Soaps to the shopping cart"
            cart.addItem(doveSoap, quantityToRemove)
        then: "The product is removed from the cart"
            !cart.items.find { it.product.code == DOVE_SOAP_CODE}
        and: "Total price is back to zero"
            cart.totalPrice == BigDecimal.ZERO
        where:
            quantityToRemove << [-5, -3]
    }

    void "Calculate the tax rate of the shopping cart with multiple items"() {
        given: "An empty shopping cart"
            Cart cart = new Cart()
        and: "A sales tax rate of 12.5%"
            cart.setTaxRate(new BigDecimal(0.125))
        and: "And a product, Dove Soap with a unit price of 39.99"
            Product doveSoap = new Product(DOVE_SOAP_CODE, "Dove Soap", new BigDecimal("39.99"))
        and: "And another product, Axe Deo with a unit price of 99.99"
            Product axeDeo = new Product(AXE_DEO_CODE, "Axe Deo", new BigDecimal("99.99"))
        when: "The user added 2 Dove Soaps to the shopping cart"
            cart.addItem(doveSoap, 2)
        and : "The user adds another 2 Axe Deos to the shopping cart"
            cart.addItem(axeDeo, 2)
        then: "The shopping cart should contain 2 Dove Soaps each with a unit price of 39.99"
            with(cart.items.find { it.product.code == DOVE_SOAP_CODE}) {
                it.quantity == 2
                it.product.unitPrice == new BigDecimal("39.99")
            }
        and: "The shopping cart should contain 2 Axe Deos each with a unit price of 99.99"
            with(cart.items.find { it.product.code == AXE_DEO_CODE}) {
                it.quantity == 2
                it.product.unitPrice == new BigDecimal("99.99")
            }
        and: "The total sales tax amount for the shopping cart should equal 35.00"
            cart.taxes == new BigDecimal("35.00")
        and: "The shopping cart’s total price should equal 314.96"
            cart.totalPriceIncludingTaxes == new BigDecimal("314.96")
    }
}
