# Fawry Rise Journey - Full Stack Development Internship Challenge

## 💼 E-Commerce System

This project is a solution to the Fawry Quantum Internship Challenge 3. It implements a basic e-commerce system with support for product definitions, cart operations, shipping handling, and checkout functionality.

---

## 🚀 Features

- Define products with:
  - Name
  - Price
  - Quantity
  - Expiry behavior (optional)
  - Shipping requirement (optional, with weight)

- Customer Cart:
  - Add items to cart without exceeding available quantity
  - Prevent adding expired items
  - Prevent checkout if balance is insufficient

- Checkout Summary:
  - Subtotal
  - Shipping fees (flat rate: 10 per shippable item)
  - Total amount
  - Remaining customer balance

- Shipping Service:
  - Outputs list of shipped items with total package weight

---

## ✅ Example Console Output

** Shipment notice **
1x Cheese 200g
1x Biscuits 700g
Total package weight 0.9kg

** Checkout receipt **
1x Cheese	200
2x Biscuits	400
1x Scratch Card	100
----------------------
Subtotal		700
Shipping		20
Amount		720
Balance left		280

yaml
Copy
Edit

---

## 🧪 How to Run

### Requirements:
- Java 8+

### Steps:
1. Compile the Java file:
   ```bash
   javac Main.java
Run the application:

bash
Copy
Edit
java Main
🧠 Assumptions
Flat shipping fee: 10 per product requiring shipment.

Weight displayed in grams for each product in shipping notice.

Expired or insufficient stock products cause checkout failure.

Cart cannot be empty on checkout.
