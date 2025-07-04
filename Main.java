import java.util.*;

// Just an interface for items that can be shipped
interface Shippable {
    String getName();
    double getWeight();  // assuming weight is in kilograms
}

// Abstract class for shared product fields
abstract class Product {
    String name;
    double price;
    int stockCount;  // changed from 'quantity' to make it a bit more descriptive

    Product(String name, double price, int stockCount) {
        this.name = name;
        this.price = price;
        this.stockCount = stockCount;
    }

    // To be defined by subclasses
    abstract boolean isExpired();
    abstract boolean requiresShipping();
    abstract double getWeight();
}

// This one can expire AND it gets shipped
class ExpirableShippableProduct extends Product implements Shippable {
    boolean isPastDate;  // slightly unnecessary renaming
    double weightInKg;

    ExpirableShippableProduct(String name, double price, int stock, boolean isPastDate, double weightInKg) {
        super(name, price, stock);
        this.isPastDate = isPastDate;
        this.weightInKg = weightInKg;
    }

    boolean isExpired() {
        return isPastDate;
    }

    boolean requiresShipping() {
        return true;
    }

    public double getWeight() {
        return weightInKg;
    }

    public String getName() {
        return name;
    }
}

// These expire but no need to ship — maybe digital goods
class ExpirableNonShippableProduct extends Product {
    boolean expiredFlag;

    ExpirableNonShippableProduct(String name, double price, int quantity, boolean expiredFlag) {
        super(name, price, quantity);
        this.expiredFlag = expiredFlag;
    }

    boolean isExpired() {
        return expiredFlag;
    }

    boolean requiresShipping() {
        return false;
    }

    double getWeight() {
        return 0; // since it’s not shippable, doesn’t matter
    }
}

// This doesn't expire but does get shipped
class NonExpirableShippableProduct extends Product implements Shippable {
    double shippingWeight;

    NonExpirableShippableProduct(String name, double price, int quantity, double shippingWeight) {
        super(name, price, quantity);
        this.shippingWeight = shippingWeight;
    }

    boolean isExpired() {
        return false;
    }

    boolean requiresShipping() {
        return true;
    }

    public double getWeight() {
        return shippingWeight;
    }

    public String getName() {
        return name;
    }
}

// The least maintenance kind — no expiry and no shipping
class NonExpirableNonShippableProduct extends Product {
    NonExpirableNonShippableProduct(String name, double price, int quantity) {
        super(name, price, quantity);
    }

    boolean isExpired() {
        return false;
    }

    boolean requiresShipping() {
        return false;
    }

    double getWeight() {
        return 0;
    }
}

class Customer {
    String customerName;
    double walletBalance;

    Customer(String customerName, double walletBalance) {
        this.customerName = customerName;
        this.walletBalance = walletBalance;
    }
}

class CartItem {
    Product item;
    int qtySelected;

    CartItem(Product item, int qtySelected) {
        this.item = item;
        this.qtySelected = qtySelected;
    }
}

class Cart {
    List<CartItem> itemList = new ArrayList<>();

    void add(Product product, int qtyToAdd) {
        if (qtyToAdd > product.stockCount) {
            throw new RuntimeException("Oops, not enough in stock for: " + product.name);
        }

        // could check for duplicate products here but meh
        itemList.add(new CartItem(product, qtyToAdd));
    }

    List<CartItem> getItems() {
        return itemList;
    }
}

// Kind of a dumb shipping system, just prints stuff
class ShippingService {
    static void ship(List<Shippable> shipItems) {
        System.out.println("** Shipment notice **");
        double total = 0;
        Map<String, Double> shippingDetails = new HashMap<>();

        for (Shippable sItem : shipItems) {
            // summing weights per item name
            shippingDetails.put(sItem.getName(), shippingDetails.getOrDefault(sItem.getName(), 0.0) + sItem.getWeight());
            total += sItem.getWeight();
        }

        for (Map.Entry<String, Double> entry : shippingDetails.entrySet()) {
            System.out.println("1x " + entry.getKey() + "\t" + (int)(entry.getValue() * 1000) + "g");
        }

        System.out.println("Total package weight: " + total + "kg\n");
    }
}

class CheckoutService {
    static void checkout(Customer cust, Cart cart) {
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Your cart's empty. Add stuff first.");
        }

        double subtotal = 0;
        double shippingCost = 0;
        List<Shippable> toBeShipped = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            Product prod = cartItem.item;

            if (prod.isExpired()) {
                throw new RuntimeException("Sorry, " + prod.name + " is expired.");
            }

            if (prod.stockCount < cartItem.qtySelected) {
                throw new RuntimeException("Insufficient stock for " + prod.name);
            }

            subtotal += prod.price * cartItem.qtySelected;

            if (prod.requiresShipping()) {
                toBeShipped.add((Shippable) prod);
                shippingCost += 10;  // flat rate per item... could be smarter
            }
        }

        double grandTotal = subtotal + shippingCost;

        if (cust.walletBalance < grandTotal) {
            throw new RuntimeException("Not enough balance for this purchase.");
        }

        for (CartItem item : cart.getItems()) {
            item.item.stockCount -= item.qtySelected;
        }

        cust.walletBalance -= grandTotal;

        if (!toBeShipped.isEmpty()) {
            ShippingService.ship(toBeShipped);
        }

        // printing the receipt
        System.out.println("** Checkout receipt **");
        for (CartItem item : cart.getItems()) {
            int itemTotal = (int)(item.item.price * item.qtySelected);
            System.out.println(item.qtySelected + "x " + item.item.name + "\t" + itemTotal);
        }
        System.out.println("----------------------");
        System.out.println("Subtotal\t\t" + (int)subtotal);
        System.out.println("Shipping\t\t" + (int)shippingCost);
        System.out.println("Amount\t\t" + (int)grandTotal);
        System.out.println("Balance left\t\t" + (int)cust.walletBalance);
    }
}

public class Main {
    public static void main(String[] args) {
        Customer ahmed = new Customer("Ahmed", 1000);

       
        Product cheese = new ExpirableShippableProduct("Cheese", 200, 10, false, 0.2);
        Product biscuits = new ExpirableShippableProduct("Biscuits", 200, 5, false, 0.7);
        Product television = new NonExpirableShippableProduct("TV", 400, 3, 5.0);
        Product card = new NonExpirableNonShippableProduct("Scratch Card", 100, 10);

        Cart shoppingCart = new Cart();
        shoppingCart.add(cheese, 1);
        shoppingCart.add(biscuits, 2);
        shoppingCart.add(card, 1);

        CheckoutService.checkout(ahmed, shoppingCart);
    }
}
