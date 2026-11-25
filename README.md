# Campus Canteen Management System

A console-based Java application for managing a college canteen. Built as a 2nd-year OOP / Java project using core Java, layered packages (`model`, `policy`, `service`, `ui`), the Java Collections Framework, and exception handling.

---

## 1. Project Overview

Students often face long waiting lines and stock uncertainty at campus canteens. This system simulates a digital canteen workflow where:
- **Students** can browse the menu, place orders, cancel orders (before preparation starts), view order history, and track their queue position using a token number.
- **Kitchen Staff** can view all or active orders and move orders through status stages (`PLACED` -> `PREPARING` -> `READY` -> `COLLECTED`).
- **Admins** can manage menu items, update prices and availability, and manage item stock.

All data is stored **in memory** during the program's execution, and demo menu items/stock are automatically loaded when the application starts.

---

## 2. Features

### Student Panel (`StudentUI`)
1. **Browse Menu** — Lists all menu items with prices and marks items as `OUT OF STOCK` if unavailable or stock is `0`.
2. **Place Order** — Lets a student select multiple items and quantities, validates stock in real time, shows an order summary with total cost, deducts stock upon confirmation, and generates an order ID and token number. Rolls back stock if order placement fails.
3. **View Order History** — Displays all orders placed by the student ID.
4. **Cancel Order** — Allows cancelling an order (only while its status is `PLACED`) and restores the deducted stock.
5. **Track Queue** — Shows how many active orders are ahead of the student's token number.
6. **View Order Status** — Checks the current status of a specific order ID.

### Kitchen Panel (`KitchenUI`)
1. **View All Orders** — Lists every order (including `COLLECTED` and `CANCELLED`).
2. **View Active Orders** — Lists only orders currently in `PLACED`, `PREPARING`, or `READY` status.
3. **Update Order Status** — Advances an order to its next valid status (`PLACED` -> `PREPARING` -> `READY` -> `COLLECTED`).

### Admin Panel (`AdminUI`)
1. **View Menu** — Displays all items along with current stock.
2. **Add Menu Item** — Adds a new menu item with price and initial stock.
3. **Search Menu Item** — Finds an item by its ID.
4. **Change Item Price** — Updates the price of an existing item.
5. **Change Item Availability** — Marks an item as available or unavailable.
6. **Set Initial Stock** — Sets stock for an existing menu item.
7. **Update Stock** — Updates the current stock quantity for an item in inventory.
8. **View Stock** — Checks available stock for a specific item ID.

---

## 3. Project Structure

```text
campus/
├── campus.iml
├── data/
└── src/
    └── com/
        └── campus/
            └── canteen/
                ├── App.java
                ├── model/
                │   ├── MenuItem.java
                │   ├── Order.java
                │   ├── OrderItem.java
                │   ├── OrderStatus.java
                │   ├── Payment.java
                │   ├── PaymentStatus.java
                │   └── Student.java
                ├── policy/
                │   ├── InventoryPolicy.java
                │   ├── OrderPolicy.java
                │   └── QueuePolicy.java
                ├── service/
                │   ├── InventoryService.java
                │   ├── MenuService.java
                │   ├── OrderService.java
                │   ├── QueueService.java
                │   └── StockUnavailableException.java
                └── ui/
                    ├── AdminUI.java
                    ├── InputHelper.java
                    ├── KitchenUI.java
                    ├── MainMenu.java
                    └── StudentUI.java
```

### Package Responsibilities
- **`com.campus.canteen`** (`App.java`) — Program entry point that launches `MainMenu` and handles top-level cleanup.
- **`model`** — Core data classes (`MenuItem`, `Order`, `OrderItem`, `Student`, `Payment`) and status constants (`OrderStatus`, `PaymentStatus`).
- **`policy`** — Business rules kept separate from services:
  - `InventoryPolicy`: validates positive quantities and checks if available stock is enough.
  - `OrderPolicy`: checks whether an order can be modified/cancelled (`PLACED` only) or transitioned to the next status.
  - `QueuePolicy`: defines active queue statuses (`PLACED`, `PREPARING`, `READY`).
- **`service`** — In-memory data management and business operations (`MenuService`, `InventoryService`, `OrderService`, `QueueService`, `StockUnavailableException`).
- **`ui`** — Console-based menus for Student, Kitchen, and Admin roles, plus `InputHelper` for safe console reading.

---

## 4. Order Lifecycle

```text
PLACED ──> PREPARING ──> READY ──> COLLECTED
  │
  └──> CANCELLED  (allowed only while PLACED; restores stock)
```

---

## 5. Core Java Concepts Used

### Object-Oriented Programming (OOP)
- **Encapsulation**: Model fields (`MenuItem`, `Order`, `OrderItem`, `Payment`, `Student`) are `private` with validation in constructors and setters.
- **Separation of Concerns**: UI, Services, Policies, and Models are separated into distinct packages instead of putting everything into `main`.
- **Constructor Injection**: `MainMenu` creates the service objects once and passes them to `StudentUI`, `KitchenUI`, and `AdminUI`.

### Collections Framework (`java.util`)
- **`List` / `ArrayList`**:
  - `MenuService`: stores all `MenuItem` objects (`List<MenuItem> menu = new ArrayList<>()`).
  - `OrderService`: stores all `Order` objects (`List<Order> orders = new ArrayList<>()`).
  - `Order`: stores items inside an order (`List<OrderItem> items = new ArrayList<>()`).
  - `QueueService` & UI classes: filter and display orders/items using `ArrayList`.
- **`Map` / `HashMap`**:
  - `InventoryService`: maps `itemId` (`Long`) to available quantity (`Integer`) using `Map<Long, Integer> stock = new HashMap<>()` for quick stock lookups and updates.

### Exception Handling
- **Custom Exception**: `StockUnavailableException` (extends `RuntimeException`) is thrown by `InventoryService.validateStock()` when an item is out of stock or requested quantity exceeds stock.
- **Input Validation Exceptions**:
  - `InputHelper` catches `NumberFormatException` when parsing `int`, `long`, and `double` inputs so invalid typing does not crash the app.
  - `InputHelper.readLine()` catches `NoSuchElementException` / EOF and throws `IllegalStateException` to exit cleanly.
- **Model & Service Guards**: Model constructors/setters and `InventoryService` throw `IllegalArgumentException` for invalid IDs, empty names, non-positive prices/quantities, or invalid statuses.
- **Try-Catch-Finally & Rollback**:
  - `StudentUI.placeOrder()` catches `StockUnavailableException | IllegalArgumentException` during item selection and catches `Exception` during order confirmation to roll back already-deducted stock.
  - `MainMenu`, `StudentUI`, `KitchenUI`, `AdminUI`, and `App` wrap menu actions in `try-catch`, and `App` closes the `Scanner` in `finally`.

---

## 6. Default Demo Data

On startup, `MainMenu.loadDemoData()` loads 5 menu items with initial stock:

| Item ID | Name            | Price (Rs.) | Initial Stock |
|--------:|-----------------|------------:|--------------:|
| 1       | Veg Burger      | 60.0        | 20            |
| 2       | Cheese Sandwich | 80.0        | 15            |
| 3       | Masala Maggi    | 50.0        | 25            |
| 4       | Cold Coffee     | 70.0        | 30            |
| 5       | Veg Pizza       | 120.0       | 10            |

---

## 7. How to Compile and Run

**Requirements:** Java JDK (tested with OpenJDK 21).

From the project root folder (`campus/`):

```powershell
# Compile all Java files into an out/ directory
New-Item -ItemType Directory -Force out | Out-Null
Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName } | Set-Content sources.txt
javac -d out "@sources.txt"
Remove-Item sources.txt

# Run the application
java -cp out com.campus.canteen.App
```

Or on Linux/macOS:

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out com.campus.canteen.App
```

---

## 8. Limitations & Future Scope

- **No Persistent Storage**: Orders, menu changes, and stock updates live only in memory and reset when the program exits (the `data/` folder is currently unused).
- **Unused Domain Models**: `Student` and `Payment` / `PaymentStatus` models are implemented in `model/`, but `StudentUI` currently identifies students only by `studentId` and does not yet include a payment step.
- **Future Improvements**:
  - Save and load menu, stock, and order history using files (`data/`) or a database (JDBC/SQLite).
  - Integrate `Student` registration/login and `Payment` processing into the order flow.
  - Add JUnit tests for services and policy classes.
