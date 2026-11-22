# Campus Canteen Pre-Order and Queue Management System

## Current Progress

Completed the basic **Model Layer** using Java and OOP.

### Classes Created
- **Student.java** – Stores student details.
- **MenuItem.java** – Stores food item details, price and availability.
- **OrderItem.java** – Stores item ID, quantity and price.
- **Order.java** – Manages order details and calculates total quantity and price.
- **OrderStatus.java** – Defines order statuses.
- **Payment.java** – Stores payment details and checks payment status.
- **PaymentStatus.java** – Defines payment statuses.

### Completed Model Responsibilities
- Store student and menu information.
- Manage orders and their items.
- Calculate total order quantity and price.
- Track order progress and cancellation status.
- Store payment details linked to orders.
- Track pending, paid and refunded payments.


### Reason behind using Repository Layer
- We require our order history's information in memory
- If we store it in Orders.java, closing App will result in loss in data
- To save it we will store that all in external files (Repository files)
- For that we need CSV files.

## what is Repository 
- It is a class responsible for saving , retrieving , uodating and deleting data.
- 

### Next Step
Build the **Repository Layer** to save and retrieve data using CSV files.
