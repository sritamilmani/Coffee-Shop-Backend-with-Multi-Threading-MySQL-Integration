# Coffee Shop Backend – Producer-Consumer Simulation

## Overview

This project simulates a coffee shop backend using the **Producer-Consumer** pattern in **Java**. The system models multiple **baristas** (producers) preparing coffee orders and multiple **customers** (consumers) picking them up. The orders are stored and managed using **MySQL** through **JDBC** for database interaction.

## Features

- **Multithreading**: Implements Java concurrency to handle multiple baristas and customers.
- **Database Integration**: Stores and updates order details in a MySQL database.
- **Controlled Order Processing**: Limits the total number of orders to prevent infinite execution.
- **Status Management**: Ensures an order is only picked up by one customer.

## Technologies Used

- **Java (JDK 11 or later)**
- **MySQL** (Database)
- **JDBC** (Database Connectivity)
- **Multithreading** (Java `Thread` and `BlockingQueue`)

## Installation & Setup

### Prerequisites

- Install **Java** (JDK 11+)
- Install **MySQL** and create a database named `coffeeshop`
- Add the **MySQL JDBC Driver** to your project

### Database Setup

Run the following SQL commands to create the required table:

```sql
CREATE DATABASE coffeeshop;
USE coffeeshop;

CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    coffee_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Expected Output

for MAX\_ORDERS = 5 (Barista.java)

**Java Console Output:**
```
‍🍳 Barista-1 prepared: Espresso
👨‍🍳 Barista-3 prepared: Espresso
👨‍🍳 Barista-2 prepared: Americano
👨‍🍳 Barista-1 prepared: Cappuccino
👨‍🍳 Barista-3 prepared: Latte
🛑 Barista-2 has finished making orders.
☕ Customer-1 picked up: Order #2 - Espresso
☕ Customer-2 picked up: Order #1 - Espresso
☕ Customer-3 picked up: Order #3 - Americano
☕ Customer-4 picked up: Order #4 - Cappuccino
☕ Customer-5 picked up: Order #5 - Latte
🛑 Barista-1 has finished making orders.
🛑 Barista-3 has finished making orders.
✅ Customer-3 has finished. No more orders left.
✅ Customer-2 has finished. No more orders left.
✅ Customer-1 has finished. No more orders left.
✅ Customer-4 has finished. No more orders left.
✅ Customer-5 has finished. No more orders left.

🚀 Coffee shop is closed. All orders completed!
```
**Database Output:**
```
order_id  coffee_type   status          created_at
1	  Espresso	Completed	2025-04-03 13:21:59
2	  Espresso	Completed	2025-04-03 13:21:59
3	  Americano	Completed	2025-04-03 13:21:59
4	  Cappuccino	Completed	2025-04-03 13:22:01
5	  Latte	        Completed	2025-04-03 13:22:01
			

```
## License

This project is licensed under the MIT License.

