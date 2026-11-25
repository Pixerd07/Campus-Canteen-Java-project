# CAMPUS CANTEEN MANAGEMENT SYSTEM
## 15-Page Project Documentation (2nd-Year Java / OOP Mini-Project)

> **Tip for Word / PDF Export:** Each section marked `## PAGE X` corresponds to one formatted report page (insert a Page Break before each `PAGE` heading when pasting into MS Word, Google Docs, or LibreOffice Writer, using 11–12 pt font and 1.15–1.5 line spacing).

---

## PAGE 1: TITLE PAGE

<div align="center">

### **A MINI-PROJECT REPORT**
### **ON**
# **CAMPUS CANTEEN MANAGEMENT SYSTEM**

Submitted in partial fulfillment of the requirements for the degree of  
**Bachelor of Technology / Bachelor of Computer Applications (2nd Year)**  
in  
**Computer Science & Engineering**

---

**Submitted By:**  
Name: `[Student Name]`  
Roll No.: `[University / College Roll No.]`  
Semester / Year: `3rd / 4th Semester (2nd Year)`  

**Under the Guidance of:**  
`[Faculty / Guide Name]`  
`[Designation, Department of Computer Science]`  

---

**Department of Computer Science & Engineering**  
`[College / University Name]`  
**Academic Year: 2026–2027**

</div>

---

### Declaration (Template)
I hereby declare that the mini-project titled **"Campus Canteen Management System"** is an authentic record of work carried out in Java using Object-Oriented Programming principles, Java Collections, and Exception Handling, submitted as part of 2nd-year academic coursework.

### Certificate (Template)
This is to certify that the project report entitled **"Campus Canteen Management System"** has been completed satisfactorily by `[Student Name]` (`[Roll No.]`) under supervision during the academic session `2026–2027`.

---

## PAGE 2: ABSTRACT & TABLE OF CONTENTS

### Abstract
College campus canteens experience heavy footfall during short break windows between lectures. Manual order taking, paper-based token slips, and unverified stock counts frequently lead to long queues, order mix-ups, and unexpected item unavailability after a student has already waited in line.

The **Campus Canteen Management System** is a console-based Java application designed to model and streamline daily canteen workflows using clean **Object-Oriented Programming (OOP)** principles. The application separates responsibilities into four distinct packages—`model`, `policy`, `service`, and `ui`—and supports three operational roles: **Student**, **Kitchen Staff**, and **Admin**.

Students can browse the live menu, place multi-item orders with real-time stock validation, cancel unstarted orders with automatic stock restoration, view order history, and track their position in the active queue using a token number. Kitchen staff can inspect all orders or active orders and advance order statuses step-by-step (`PLACED -> PREPARING -> READY -> COLLECTED`). Administrators can view the catalog, add new menu items, search items by ID, update prices, toggle availability, and manage stock inventory.

Technically, the system demonstrates core 2nd-year Java concepts including encapsulation, constructor-based dependency injection, the **Java Collections Framework** (`List`/`ArrayList` and `Map`/`HashMap`), custom and standard **Exception Handling** (`StockUnavailableException`, `NumberFormatException`, `IllegalArgumentException`, `IllegalStateException`), and transactional stock rollback on order failure.

---

### Table of Contents

| Page | Chapter / Section |
|-----:|-------------------|
| 1 | Title Page, Declaration & Certificate |
| 2 | Abstract & Table of Contents |
| 3 | Chapter 1: Introduction & Problem Statement |
| 4 | Chapter 2: System Requirements & Feasibility Study |
| 5 | Chapter 3: System Architecture & Package Design |
| 6 | Chapter 4: System Diagrams (Use Case, DFD, Order Lifecycle) |
| 7 | Chapter 5: Class Diagram & Domain Model Specifications |
| 8 | Chapter 6: Policy & Service Layer Design (Part I) |
| 9 | Chapter 6: Service Layer & Queue Tracking Design (Part II) |
| 10 | Chapter 7: User Interface & Console Input Handling |
| 11 | Chapter 8: Core Java Concepts — OOP & Collections Framework |
| 12 | Chapter 8: Core Java Concepts — Exception Handling & Rollback |
| 13 | Chapter 9: Demo Data & Sample Console Walkthrough |
| 14 | Chapter 10: System Testing & Test Case Specification |
| 15 | Chapter 11: Limitations, Future Scope, Conclusion & References |

---

## PAGE 3: CHAPTER 1 — INTRODUCTION & PROBLEM STATEMENT

### 1.1 Introduction
Campus canteens serve hundreds of students and staff within narrow break intervals. Managing menu updates, checking available stock, assigning order tokens, and coordinating food preparation manually becomes error-prone as order volume grows. Software automation—even in a lightweight console form—helps standardize how orders are validated, queued, prepared, and tracked.

The **Campus Canteen Management System** provides a modular Java solution that simulates end-to-end canteen operations inside a single command-line interface (`System.in` / `System.out`), making it easy to understand, test, and extend into a GUI, web service, or database-backed application in later semesters.

### 1.2 Problem Statement
In a traditional manual canteen workflow:
1. **No Real-Time Stock Visibility**: Students do not know whether an item is out of stock until they reach the counter.
2. **Overselling & Quantity Mismatches**: Without stock validation before order confirmation, orders may be accepted for items that no longer have sufficient quantity.
3. **Opaque Queue Status**: Students have no systematic way to know how many active orders are ahead of their token.
4. **Inconsistent Status Updates**: Orders can be skipped, forgotten, or cancelled inconsistently without restoring inventory.
5. **Unstructured Input Errors**: Simple console scripts often crash with `InputMismatchException` or `NumberFormatException` if invalid characters are typed.

### 1.3 Existing System vs. Proposed System

| Aspect | Existing Manual / Ad-Hoc Process | Proposed Java Canteen System |
|--------|----------------------------------|------------------------------|
| **Menu & Stock Check** | Verbal inquiry at counter | Instant menu listing with automatic `OUT OF STOCK` tags |
| **Order Placement** | Paper slip or verbal note | Validated `Order` with unique `orderId` and `tokenNumber` |
| **Stock Deduction** | Manual estimation | Atomic check + deduction via `InventoryService` with rollback |
| **Order Cancellation** | Informal; stock rarely adjusted | Permitted only in `PLACED` status; stock automatically restored |
| **Queue Tracking** | Manual calling of names/numbers | Calculated queue count ahead via `QueueService` |
| **Input Reliability** | Error-prone manual entry | Safe parsing via `InputHelper` with re-prompting |

### 1.4 Objectives of the Project
- To design a clean, modular Java application separated into `model`, `policy`, `service`, and `ui` packages.
- To enforce strict business rules (positive IDs/quantities, valid status transitions, cancellation rules) inside dedicated policy and model classes.
- To apply **Java Collections** (`ArrayList` and `HashMap`) appropriately for dynamic lists and fast ID-to-stock lookups.
- To implement **robust exception handling** so invalid user input, closed streams, or out-of-stock requests never crash the system.

### 1.5 Scope
The project covers in-memory simulation of Student, Kitchen, and Admin workflows during an active JVM session, preloaded with 5 demo food items and stock quantities at startup.

---

## PAGE 4: CHAPTER 2 — SYSTEM REQUIREMENTS & FEASIBILITY STUDY

### 2.1 Software Requirements

| Component | Specification |
|-----------|---------------|
| **Programming Language** | Java (Java SE — tested with OpenJDK `21.0.11` LTS) |
| **Core Libraries Used** | Standard JDK (`java.util.List`, `ArrayList`, `Map`, `HashMap`, `Scanner`, `NoSuchElementException`) |
| **Operating System** | Windows / Linux / macOS (any OS supporting a JVM) |
| **IDE / Editor (Optional)** | IntelliJ IDEA (`campus.iml` module included), VS Code, or CLI (`javac`/`java`) |
| **External Dependencies** | None (0 third-party JARs required) |

### 2.2 Hardware Requirements

| Component | Minimum Specification |
|-----------|-----------------------|
| **Processor** | Dual-core x86_64 or ARM64 CPU (1.6 GHz+) |
| **RAM** | 2 GB minimum (JVM console usage is < 50 MB) |
| **Storage** | 50 MB free disk space for JDK compilation output and source files |
| **I/O Devices** | Standard keyboard and display terminal |

### 2.3 Feasibility Study

#### 1. Technical Feasibility
The project relies exclusively on core Java SE features taught in 2nd-year coursework: classes, constructors, encapsulation, packages, `List`/`ArrayList`, `Map`/`HashMap`, and exception handling (`try-catch-finally`, custom `RuntimeException`). Because no external database server or framework setup is needed, it compiles and runs reliably on any machine with a JDK installed.

#### 2. Operational Feasibility
The console UI uses numbered menus (`1`, `2`, `3`, ...), clear prompts, confirmation steps before placing orders or changing order status, and user-friendly error messages. Any student, kitchen worker, or admin familiar with basic keyboard input can operate the panels without training.

#### 3. Economic Feasibility
The system uses 100% open-source/free tools (OpenJDK) and requires no paid licenses, cloud hosting, or specialized hardware, making development and deployment cost zero.

### 2.4 Technology Stack Summary
- **Entry Point**: `com.campus.canteen.App`
- **Collections**: `java.util.ArrayList` (ordered collections of menu items, orders, order items) and `java.util.HashMap` (O(1) average-time inventory stock lookup by item ID)
- **Exception Handling**: `StockUnavailableException` (custom), `IllegalArgumentException`, `IllegalStateException`, `NumberFormatException`, `NoSuchElementException`
- **Build & Run**: Direct `javac` compilation to bytecode and `java` execution

---

## PAGE 5: CHAPTER 3 — SYSTEM ARCHITECTURE & PACKAGE DESIGN

### 3.1 Layered Architecture
Rather than writing all logic in a single `main()` method, the application follows a **4-layer modular architecture**:

```text
+-------------------------------------------------------------------+
|                         ENTRY POINT LAYER                         |
|                    App.java (launches MainMenu)                   |
+-------------------------------------------------------------------+
                                  |
                                  v
+-------------------------------------------------------------------+
|                      USER INTERFACE LAYER (ui)                    |
|     MainMenu  |  StudentUI  |  KitchenUI  |  AdminUI  | InputHelper|
+-------------------------------------------------------------------+
          |                    |                     |
          v                    v                     v
+-------------------------------------------------------------------+
|                       SERVICE LAYER (service)                     |
|   MenuService | OrderService | QueueService | InventoryService    |
|               |              |              | StockUnavailableExc.|
+-------------------------------------------------------------------+
          |                    |                     |
          v                    v                     v
+-------------------------------------------------------------------+
|                        POLICY LAYER (policy)                      |
|      OrderPolicy      |      QueuePolicy     |   InventoryPolicy  |
+-------------------------------------------------------------------+
          |                    |                     |
          v                    v                     v
+-------------------------------------------------------------------+
|                         MODEL LAYER (model)                       |
| MenuItem | Order | OrderItem | OrderStatus | Student | Payment*   |
+-------------------------------------------------------------------+
```
*\*Note: `Student`, `Payment`, and `PaymentStatus` are defined in `model/` for domain completeness and future UI integration.*

### 3.2 Complete Directory & File Structure

```text
campus/
├── campus.iml
├── data/                                   (Reserved for future file storage)
└── src/
    └── com/campus/canteen/
        ├── App.java                        (Top-level main method & cleanup)
        ├── model/
        │   ├── MenuItem.java               (Menu item entity)
        │   ├── Order.java                  (Order entity + token counter)
        │   ├── OrderItem.java              (Line item: itemId, qty, price)
        │   ├── OrderStatus.java            (Status constants & next() transition)
        │   ├── Payment.java                (Payment entity)
        │   ├── PaymentStatus.java          (PENDING, PAID, REFUNDED constants)
        │   └── Student.java                (Student entity)
        ├── policy/
        │   ├── InventoryPolicy.java        (Positive qty & stock sufficiency)
        │   ├── OrderPolicy.java            (Modify/cancel/transition rules)
        │   └── QueuePolicy.java            (Active order status predicate)
        ├── service/
        │   ├── InventoryService.java       (HashMap stock management)
        │   ├── MenuService.java            (ArrayList menu catalog)
        │   ├── OrderService.java           (ArrayList order storage & queries)
        │   ├── QueueService.java           (Queue filtering & ahead counting)
        │   └── StockUnavailableException.java (Custom unchecked exception)
        └── ui/
            ├── AdminUI.java                (Admin console menu & actions)
            ├── InputHelper.java            (Safe Scanner wrapper)
            ├── KitchenUI.java              (Kitchen console menu & actions)
            ├── MainMenu.java               (Panel router & demo data loader)
            └── StudentUI.java              (Student console menu & order flow)
```

### 3.3 Dependency Injection in `MainMenu`
Inside `MainMenu()` constructor (`MainMenu.java`), the four services (`MenuService`, `OrderService`, `QueueService`, `InventoryService`) are instantiated **once**, seeded with demo data via `loadDemoData()`, and injected into `StudentUI`, `KitchenUI`, and `AdminUI`. This guarantees that all three panels share the **exact same live in-memory state**: when an Admin updates stock or prices, Students immediately see the change; when a Student places an order, Kitchen Staff immediately see it in active orders.

---

## PAGE 6: CHAPTER 4 — SYSTEM DIAGRAMS

### 4.1 Use Case Diagram (Role Interactions)

```text
                   +-------------------------------------------+
                   |      CAMPUS CANTEEN MANAGEMENT SYSTEM     |
                   +-------------------------------------------+
  +---------+      |                                           |
  | Student |----->|  ( Browse Menu )                          |
  +---------+      |  ( Place Order )                          |
       |           |  ( View Order History )                   |
       |           |  ( Cancel Order [if PLACED] )             |
       |           |  ( Track Queue by Token )                 |
       |           |  ( View Order Status )                    |
       |           |                                           |
  +---------+      |                                           |
  | Kitchen |----->|  ( View All Orders )                      |
  |  Staff  |      |  ( View Active Orders )                   |
  +---------+      |  ( Update Order Status )                  |
                   |                                           |
  +---------+      |  ( View Menu & Stock )                    |
  |  Admin  |----->|  ( Add Menu Item )                        |
  +---------+      |  ( Search Menu Item )                     |
                   |  ( Change Price / Availability )          |
                   |  ( Set Initial Stock / Update Stock )     |
                   +-------------------------------------------+
```

### 4.2 Level-1 Data Flow Diagram (Order Placement & Fulfillment)

```text
[Student] --(studentId, itemId, qty)--> (1.0 Validate & Build Cart)
                                                |
                                                v
                                      [InventoryService: stock Map]
                                                | (validateStock / deductStock)
                                                v
[Student] <--(Token #, Order ID)---- (2.0 Place Order) ---> [OrderService: orders List]
                                                                      |
                                                                      v
[Kitchen] --(orderId, confirm)-----> (3.0 Update Status) <--(getActiveOrders)-- [QueueService]
```

### 4.3 Order Lifecycle State Diagram (`OrderStatus` & `OrderPolicy`)

```text
                        cancelOrder() [only when PLACED]
                    +--------------------------------------+
                    |     (restores stock to Inventory)    v
  [New Order] --> ( PLACED ) ---> ( PREPARING ) ---> ( READY ) ---> ( COLLECTED )
                      ^                 ^                ^
                      |_________________|________________|
                        Active Queue States (QueuePolicy.isActive = true)
```

In `OrderStatus.next(String status)`:
- `PLACED` -> `PREPARING`
- `PREPARING` -> `READY`
- `READY` -> `COLLECTED`
- `COLLECTED` or `CANCELLED` (or unknown) -> `null` (terminal states; KitchenUI prints `"This order cannot be updated."`)

---

## PAGE 7: CHAPTER 5 — CLASS DIAGRAM & DOMAIN MODEL SPECIFICATIONS

### 5.1 Domain Model Class Overview (`com.campus.canteen.model`)

```text
+-----------------------------------+       1     *  +-----------------------------------+
|               Order               |<>------------->|             OrderItem             |
+-----------------------------------+                +-----------------------------------+
| - static tokenCounter: int = 1    |                | - itemId: long                    |
| - orderId: long                   |                | - qty: int                        |
| - studentId: long                 |                | - price: double                   |
| - tokenNumber: int                |                +-----------------------------------+
| - status: String                  |                | + OrderItem(itemId, qty, price)   |
| - items: List<OrderItem>          |                | + getItemId(), getQty(), getPrice |
+-----------------------------------+                | + setQty(int qty)                 |
| + Order(orderId, studentId)       |                +-----------------------------------+
| + addItem(OrderItem)              |
| + removeItem(OrderItem)           |                +-----------------------------------+
| + getTotalQuantity(): int         |                |             MenuItem              |
| + getTotalPrice(): double         |                +-----------------------------------+
| + setStatus(String)               |                | - id: long                        |
| + static resetTokenCounter()      |                | - name: String                    |
+-----------------------------------+                | - price: double                   |
                                                     | - available: boolean              |
+-----------------------------------+                +-----------------------------------+
|            OrderStatus            |                | + MenuItem(id, name, price, avail)|
+-----------------------------------+                | + getId(), getName(), getPrice()  |
| + PLACED = "PLACED"               |                | + isAvailable()                   |
| + PREPARING = "PREPARING"         |                | + setName(), setPrice(), setAvail |
| + READY = "READY"                 |                +-----------------------------------+
| + COLLECTED = "COLLECTED"         |
| + CANCELLED = "CANCELLED"         |                +-----------------------------------+
| + static isValid(String): boolean |                |         Student / Payment         |
| + static next(String): String     |                |   (Defined for domain completeness|
+-----------------------------------+                |    & future UI integration)       |
                                                     +-----------------------------------+
```

### 5.2 Detailed Model Specifications & Validation Guards

1. **`MenuItem` (`MenuItem.java`)**
   - Fields: `id` (`long`), `name` (`String`), `price` (`double`), `available` (`boolean`).
   - Validation: `id > 0`; `name != null && !name.trim().isEmpty()` (stored trimmed); `Double.isFinite(price) && price > 0`. Violations throw `IllegalArgumentException`.

2. **`Order` (`Order.java`)**
   - Fields: `static int tokenCounter = 1`, `orderId` (`long`), `studentId` (`long`), `tokenNumber` (`int`), `status` (`String`), `items` (`List<OrderItem>` initialized to `new ArrayList<>()`).
   - Behavior: Constructor validates `orderId > 0` and `studentId > 0`, assigns `tokenNumber = tokenCounter++`, and sets initial status to `OrderStatus.PLACED`.
   - Methods: `addItem(OrderItem)` (rejects `null`), `removeItem(OrderItem)`, `getTotalQuantity()`, `getTotalPrice()` (sums `item.getPrice() * item.getQty()`), `setStatus(String)` (validates via `OrderStatus.isValid(status)`), `resetTokenCounter()`.

3. **`OrderItem` (`OrderItem.java`)**
   - Fields: `itemId` (`long`), `qty` (`int`), `price` (`double` snapshot at order time).
   - Validation: `itemId > 0`, `qty > 0`, `Double.isFinite(price) && price >= 0`.

4. **`Student` (`Student.java`)**
   - Fields: `name` (`String`), `id` (`long`). Validates non-empty trimmed `name` and `id > 0`.

5. **`Payment` & `PaymentStatus` (`Payment.java`, `PaymentStatus.java`)**
   - `PaymentStatus` defines `PENDING`, `PAID`, `REFUNDED`.
   - `Payment` validates `paymentId > 0`, `orderId > 0`, finite `amount >= 0`, defaults `paymentMethod = "CASH"` and `paymentStatus = PaymentStatus.PENDING`, and validates status changes in `setPaymentStatus`.

---

## PAGE 8: CHAPTER 6 — POLICY & SERVICE LAYER DESIGN (PART I)

### 6.1 Policy Classes (`com.campus.canteen.policy`)
Policy classes encapsulate pure decision rules so business rules can be changed independently of storage or UI code:

1. **`InventoryPolicy` (`InventoryPolicy.java`)**
   - `isValidQuantity(int quantity)`: returns `quantity >= 1`.
   - `hasEnoughStock(int availableStock, int requestedQuantity)`: returns `requestedQuantity <= availableStock`.

2. **`OrderPolicy` (`OrderPolicy.java`)**
   - `canModify(String status)`: returns `OrderStatus.PLACED.equals(status)`.
   - `canCancel(String status)`: delegates to `canModify(status)` (only `PLACED` orders can be cancelled).
   - `canTransition(String currentStatus, String newStatus)`: checks whether `OrderStatus.next(currentStatus)` is non-null and equals `newStatus`.

3. **`QueuePolicy` (`QueuePolicy.java`)**
   - `isActive(String status)`: returns `true` for `PLACED`, `PREPARING`, or `READY`; returns `false` for `COLLECTED` or `CANCELLED`.

### 6.2 `MenuService` (`com.campus.canteen.service.MenuService`)
Manages the menu catalog using `private List<MenuItem> menu = new ArrayList<>();`:
- `addItem(MenuItem item)`: ignores `null` or duplicate IDs (`findItem(item.getId()) != null`), otherwise appends to `menu`.
- `getAllItems()`: returns `menu`.
- `findItem(long id)`: linear scan over `menu`; returns matching `MenuItem` or `null`.
- `getAvailableItems()`: builds a new `ArrayList<>` containing only items where `item.isAvailable()` is `true`.
- `changePrice(long id, double newPrice)`: verifies item exists and `Double.isFinite(newPrice) && newPrice > 0`, then updates price.
- `setAvailability(long id, boolean available)`: updates availability flag if item exists.

### 6.3 `InventoryService` (`com.campus.canteen.service.InventoryService`)
Manages item stock using `private Map<Long, Integer> stock = new HashMap<>();` and `InventoryPolicy`:
- `setInitialStock(long itemId, int quantity)`: stores stock if `itemId > 0 && quantity >= 0`.
- `getStock(long itemId)`: returns `stock.getOrDefault(itemId, 0)`.
- `hasEnoughStock(long itemId, int quantity)`: validates positive quantity and compares against `getStock(itemId)` via `InventoryPolicy`.
- `validateStock(long itemId, int requestedQuantity)`:
  - Throws `IllegalArgumentException("Quantity must be greater than zero.")` if `!inventoryPolicy.isValidQuantity(requestedQuantity)`.
  - Throws `StockUnavailableException("OUT OF STOCK!")` if `availableStock == 0`.
  - Throws `StockUnavailableException("Only " + availableStock + " left in stock. Please reduce your quantity.")` if `requestedQuantity > availableStock`.
- `deductStock(long itemId, int quantity)`: returns `false` if stock is insufficient; otherwise decrements `stock.put(itemId, getStock(itemId) - quantity)` and returns `true`.
- `restoreStock(long itemId, int quantity)`: if `quantity > 0 && stock.containsKey(itemId)`, adds `quantity` back with integer overflow protection (`if (quantity > Integer.MAX_VALUE - currentStock)` caps at `Integer.MAX_VALUE`).
- `updateStock(long itemId, int newStock)`: updates stock only if `newStock >= 0 && stock.containsKey(itemId)`.

---

## PAGE 9: CHAPTER 6 — SERVICE LAYER & QUEUE TRACKING DESIGN (PART II)

### 6.4 `OrderService` (`com.campus.canteen.service.OrderService`)
Manages all orders using `private List<Order> orders = new ArrayList<>();`, `OrderPolicy`, and `private long nextOrderId = 1;`:

1. **`placeOrder(long studentId, List<OrderItem> items)`**
   - Validates `studentId > 0`, `items != null`, `!items.isEmpty()`, and every item is non-null with `qty > 0` (returns `null` if invalid).
   - Creates `new Order(nextOrderId, studentId)`, adds each `OrderItem`, appends to `orders`, increments `nextOrderId`, and returns the created `Order`.

2. **`modifyOrder(long orderId, long studentId, List<OrderItem> newItems)`**
   - Finds order by `orderId`, verifies ownership (`order.getStudentId() == studentId`), checks `orderPolicy.canModify(order.getStatus())`, validates `newItems`, clears existing items (`order.getItems().clear()`), and adds `newItems`.

3. **`cancelOrder(long orderId, long studentId)`**
   - Finds order, checks ownership (`order.getStudentId() == studentId`) and `orderPolicy.canCancel(order.getStatus())`.
   - Sets status to `OrderStatus.CANCELLED` and returns `true` (or `false` if ineligible).

4. **Lookup & Query Methods**
   - `findOrder(long orderId)`: linear search in `orders`.
   - `findOrderByToken(int tokenNumber)`: searches backward from `orders.size() - 1` down to `0` so it finds the latest order matching `tokenNumber`.
   - `getStudentOrders(long studentId)`: returns a new `ArrayList<>` of orders belonging to `studentId`.
   - `getAllOrders()`: returns `orders`.

### 6.5 `QueueService` (`com.campus.canteen.service.QueueService`)
Computes active queue state over `List<Order>` using `QueuePolicy`:

1. **`getActiveOrders(List<Order> orders)`**
   - Null-safe; iterates over `orders` and collects non-null orders where `queuePolicy.isActive(order.getStatus())` is `true` into a new `ArrayList<>`.

2. **`getOrdersAhead(List<Order> orders, long orderId)`**
   - Counts active orders appearing before the order with `orderId`.
   - Returns `-1` if `orders` is `null`, the order is not found, or the target order is no longer active.

3. **`getOrdersAheadByToken(List<Order> orders, int tokenNumber)`**
   - Locates `targetIndex` by scanning backward from the end of `orders` for `tokenNumber > 0`.
   - Returns `-1` if not found or inactive (`!queuePolicy.isActive(...)`).
   - Otherwise counts active orders in indices `0 .. targetIndex - 1` and returns the count.

4. **`getQueueSize(List<Order> orders)`**
   - Returns `getActiveOrders(orders).size()`.

**Design Note:** `QueueService` operates over `List<Order>` instead of `java.util.Queue` so that `OrderService`'s master order history can remain intact while queue position is dynamically filtered by active status (`PLACED`, `PREPARING`, `READY`).

---

## PAGE 10: CHAPTER 7 — USER INTERFACE & CONSOLE INPUT HANDLING

### 7.1 `App.java` & `MainMenu.java`
- `App.main(String[] args)` instantiates `MainMenu` and invokes `showMenu()` inside a `try-catch-finally` block.
- `MainMenu` creates all four services, calls `loadDemoData(menuService, inventoryService)` (loading 5 items), creates `StudentUI`, `KitchenUI`, and `AdminUI`, and displays the top-level 4-option menu (`Student Panel`, `Kitchen Panel`, `Admin Panel`, `Exit`).
- `MainMenu` also includes its own `main` method with identical `try-catch-finally` handling so either `App` or `MainMenu` can launch the app.

### 7.2 `InputHelper.java`
A utility class (private constructor, static methods) wrapping a shared `private static Scanner scanner = new Scanner(System.in);`:
- `readLine(String prompt)` *(private)*: prints prompt, checks `scanner.hasNextLine()`, returns `scanner.nextLine().trim()`, or throws `IllegalStateException("Input stream closed. Exiting application.")` on EOF / `NoSuchElementException`.
- `readInt(String prompt)`: loops until `Integer.parseInt(input)` succeeds.
- `readLong(String prompt)`: loops until `Long.parseLong(input)` succeeds.
- `readDouble(String prompt)`: loops until `Double.parseDouble(input)` succeeds **and** `Double.isFinite(value)` is `true` (blocking `NaN` and `Infinity`).
- `readNonEmptyString(String prompt)`: loops until `!input.isEmpty()`.
- `close()`: closes `scanner` in `App`'s / `MainMenu`'s `finally` block.

### 7.3 `StudentUI.java`
Prompts once per entry for a positive `studentId`, then loops through options `1..6` and `0` (Back):
1. **Browse Menu (`browseMenu`)**: prints each item; appends `" - OUT OF STOCK"` if `!item.isAvailable()` or `inventoryService.getStock(item.getId()) == 0`.
2. **Place Order (`placeOrder`)**:
   - Loops reading `itemId` (`0` finishes) and `qty`.
   - If the same `itemId` is already in `items`, combines quantities with overflow guard (`qty > Integer.MAX_VALUE - existingQuantity`), calls `inventoryService.validateStock(itemId, totalQuantity)`, and updates the existing `OrderItem` quantity instead of duplicating lines.
   - Re-validates the complete cart before showing `===== ORDER SUMMARY =====` and total cost.
   - On confirmation (`1`), deducts stock item by item into `deductedItems`; if any deduction fails or an exception occurs, calls `restoreDeductedStock(deductedItems)`.
3. **View Order History (`viewHistory`)**: displays Token, Order ID, Status, Total.
4. **Cancel Order (`cancelOrder`)**: verifies `orderId` belongs to `studentId`; calls `orderService.cancelOrder`; restores stock for every `OrderItem` in `order.getItems()`.
5. **Track Queue (`trackQueue`)**: looks up order by token, verifies ownership, calls `queueService.getOrdersAheadByToken(...)`.
6. **View Order Status (`viewStatus`)**: displays current status for a student's `orderId`.

### 7.4 `KitchenUI.java` & `AdminUI.java`
- **`KitchenUI`**: 1. View All Orders, 2. View Active Orders, 3. Update Order Status (shows `Current Status` and `Next Status` via `OrderStatus.next`, asks `Confirm update? (1 = Yes, 0 = No)`), 4. Back.
- **`AdminUI`**: tracks `nextItemId` starting at `6` and provides 8 management actions (`View Menu`, `Add Menu Item`, `Search Menu Item`, `Change Item Price`, `Change Item Availability`, `Set Initial Stock`, `Update Stock`, `View Stock`) plus `9. Back`.

---

## PAGE 11: CHAPTER 8 — CORE JAVA CONCEPTS: OOP & COLLECTIONS FRAMEWORK

### 8.1 Object-Oriented Programming Principles Applied
1. **Encapsulation & Data Hiding**
   - Every domain class (`MenuItem`, `Order`, `OrderItem`, `Payment`, `Student`) declares its fields `private` and enforces invariants inside constructors and mutators (e.g., `OrderItem.setQty` rejects `<= 0`; `MenuItem.setPrice` rejects non-finite or `<= 0` prices).
   - `InputHelper` uses a `private InputHelper()` constructor so it cannot be instantiated.
2. **Separation of Concerns**
   - UI classes handle only console I/O; service classes manage state and operations; policy classes hold pure rule predicates; model classes represent domain objects.
3. **Composition & Aggregation**
   - `Order` composes `List<OrderItem>`.
   - `MainMenu` composes `StudentUI`, `KitchenUI`, and `AdminUI`.
   - `InventoryService`, `OrderService`, and `QueueService` compose `InventoryPolicy`, `OrderPolicy`, and `QueuePolicy` respectively.

### 8.2 Java Collections Framework Usage (`java.util`)
The project uses **two** concrete Collection implementations (coded to their interfaces `List` and `Map`):

#### 1. `List<T>` / `ArrayList<T>`
Used wherever ordered sequences, dynamic resizing, and iteration by insertion order are required:

| File | Field / Variable | Purpose |
|------|------------------|---------|
| `Order.java` | `private List<OrderItem> items = new ArrayList<>()` | Stores all line items inside a single order |
| `MenuService.java` | `private List<MenuItem> menu = new ArrayList<>()` | Master catalog of canteen menu items |
| `MenuService.java` | `availableItems = new ArrayList<>()` | Filtered list of currently available items |
| `OrderService.java` | `private List<Order> orders = new ArrayList<>()` | Master chronological list of all placed orders |
| `OrderService.java` | `studentOrders = new ArrayList<>()` | Filtered list of orders for a given `studentId` |
| `QueueService.java` | `activeOrders = new ArrayList<>()` | Filtered list of `PLACED`/`PREPARING`/`READY` orders |
| `StudentUI.java` | `items = new ArrayList<>()` | Temporary shopping cart during `placeOrder()` |
| `StudentUI.java` | `deductedItems = new ArrayList<>()` | Tracks deducted items for rollback if order creation fails |

*Why `ArrayList`?* Insertion order is naturally preserved (crucial for queue ordering in `QueueService`), random/reverse index access (`orders.get(i)` in `findOrderByToken` and `getOrdersAheadByToken`) runs in O(1), and memory overhead is low.

#### 2. `Map<K, V>` / `HashMap<K, V>`
Used for key-value inventory mapping:

| File | Field / Variable | Purpose |
|------|------------------|---------|
| `InventoryService.java` | `private Map<Long, Integer> stock = new HashMap<>()` | Maps `itemId` (`Long`) to available stock count (`Integer`) |

*Why `HashMap`?* Every menu validation, deduction, and restoration looks up stock by `itemId`. `HashMap` provides O(1) average-time `getOrDefault`, `containsKey`, and `put` operations without scanning a list.

---

## PAGE 12: CHAPTER 8 — CORE JAVA CONCEPTS: EXCEPTION HANDLING & ROLLBACK

### 8.1 Exception Hierarchy & Custom Exception
The project defines one custom unchecked exception:
- **`StockUnavailableException`** (`com.campus.canteen.service.StockUnavailableException`) extends `RuntimeException` with a message constructor `public StockUnavailableException(String message)`.
- Thrown by `InventoryService.validateStock(long itemId, int requestedQuantity)` in two situations:
  1. `availableStock == 0` -> `"OUT OF STOCK!"`
  2. `requestedQuantity > availableStock` -> `"Only <N> left in stock. Please reduce your quantity."`

### 8.2 Complete Inventory of Exception Handling Locations

| Layer | File | Exception(s) | Mechanism & Purpose |
|-------|------|--------------|---------------------|
| **Entry** | `App.java` | `IllegalStateException`, `Exception` | `try-catch-finally`; prints error cleanly and closes `InputHelper` in `finally` |
| **UI** | `MainMenu.java` | `IllegalStateException`, `Exception` | Rethrows `IllegalStateException` to exit app on EOF; catches `Exception` to keep menu loop alive |
| **UI** | `InputHelper.java` | `NoSuchElementException`, `IllegalStateException`, `NumberFormatException` | `readLine` converts EOF/`NoSuchElementException` into `IllegalStateException`; `readInt`/`readLong`/`readDouble` catch `NumberFormatException` and re-prompt |
| **UI** | `StudentUI.java` | `StockUnavailableException`, `IllegalArgumentException`, `IllegalStateException`, `Exception` | Multi-catch (`StockUnavailableException \| IllegalArgumentException`) during cart validation; `try-catch(Exception)` around stock deduction & order creation triggers rollback; menu loop catches `Exception` |
| **UI** | `AdminUI.java` | `IllegalStateException`, `Exception`, `IllegalArgumentException` | `addMenuItem` catches `IllegalArgumentException` from `new MenuItem(...)`; menu loop catches `Exception` |
| **UI** | `KitchenUI.java` | `IllegalStateException`, `Exception` | Menu loop rethrows `IllegalStateException` and catches `Exception` |
| **Service** | `InventoryService.java` | `IllegalArgumentException`, `StockUnavailableException` | `validateStock` throws on non-positive quantity or insufficient stock |
| **Model** | `MenuItem`, `Order`, `OrderItem`, `Payment`, `Student` | `IllegalArgumentException` | Defensive constructor/setter guards enforce domain invariants |

### 8.3 Transactional Stock Rollback Pattern (`StudentUI.placeOrder`)
When a student confirms an order containing multiple items, deducting item 1 might succeed while item 2 fails (or `placeOrder` might return `null` or throw an exception). `StudentUI` prevents partial inventory corruption using a compensating rollback list:

```java
List<OrderItem> deductedItems = new ArrayList<>();
try {
    for (OrderItem item : items) {
        boolean deducted = inventoryService.deductStock(item.getItemId(), item.getQty());
        if (!deducted) {
            restoreDeductedStock(deductedItems);
            return;
        }
        deductedItems.add(item);
    }
    Order order = orderService.placeOrder(studentId, items);
    if (order == null) {
        restoreDeductedStock(deductedItems);
    }
} catch (Exception e) {
    restoreDeductedStock(deductedItems);
    System.out.println("Failed to place order: " + e.getMessage());
}
```
This ensures all-or-nothing stock deduction consistency for every order.

---

## PAGE 13: CHAPTER 9 — DEMO DATA & SAMPLE CONSOLE WALKTHROUGH

### 9.1 Preloaded Startup Data (`MainMenu.loadDemoData`)

| Item ID | Item Name | Price (Rs.) | Available | Initial Stock |
|--------:|-----------------|------------:|:---------:|--------------:|
| 1 | Veg Burger | 60.0 | `true` | 20 |
| 2 | Cheese Sandwich | 80.0 | `true` | 15 |
| 3 | Masala Maggi | 50.0 | `true` | 25 |
| 4 | Cold Coffee | 70.0 | `true` | 30 |
| 5 | Veg Pizza | 120.0 | `true` | 10 |

### 9.2 Sample Interactive Console Session

```text
Demo data loaded successfully!

================================
 CAMPUS CANTEEN MANAGEMENT SYSTEM
================================
1. Student Panel
2. Kitchen Panel
3. Admin Panel
4. Exit
Enter your choice: 1
Enter Student ID: 101

--- STUDENT MENU ---
1. Browse Menu
2. Place Order
3. View Order History
4. Cancel Order
5. Track Queue
6. View Order Status
0. Back
Enter choice: 2

--- CANTEEN MENU ---
1. Veg Burger - Rs.60.0
2. Cheese Sandwich - Rs.80.0
3. Masala Maggi - Rs.50.0
4. Cold Coffee - Rs.70.0
5. Veg Pizza - Rs.120.0
Enter Item ID (0 to finish): 1
Enter quantity: 2
Item added to order.
Enter Item ID (0 to finish): 4
Enter quantity: 1
Item added to order.
Enter Item ID (0 to finish): 0

===== ORDER SUMMARY =====
Veg Burger x 2 = Rs. 120.0
Cold Coffee x 1 = Rs. 70.0
Total Amount: Rs. 190.0
Confirm order? (1 Yes / 0 No): 1
Order placed successfully!
Token Number: #1
Order ID: 1
Total: Rs. 190.0
```

If the user now switches to **Kitchen Panel (`2`) -> Update Order Status (`3`)** for Order ID `1`:
```text
Enter Order ID: 1
Current Status: PLACED
Next Status: PREPARING
Confirm update? (1 = Yes, 0 = No): 1
Order status updated successfully!
```
Once status becomes `PREPARING`, attempting to cancel Order `1` in Student Panel returns `"Unable to cancel order."` because `OrderPolicy.canCancel` permits cancellation only in `PLACED`.

---

## PAGE 14: CHAPTER 10 — SYSTEM TESTING & TEST CASE SPECIFICATION

### 10.1 Build / Compilation Verification
All 21 Java source files under `src/com/campus/canteen/` were compiled with `javac` (OpenJDK `21.0.11`), exiting with status code `0` and zero compilation errors or warnings.

### 10.2 Functional & Negative Test Case Specification

| TC ID | Module / Flow | Input / Precondition | Expected System Behavior |
|------:|---------------|----------------------|--------------------------|
| TC-01 | `InputHelper.readInt` | Enter `"abc"` at menu prompt | Catches `NumberFormatException`, prints `"Invalid input! Please enter a valid whole number."`, re-prompts |
| TC-02 | `InputHelper.readDouble` | Enter `"NaN"` or `"Infinity"` at price prompt | Rejects non-finite value (`!Double.isFinite`), prints `"Invalid input! Please enter a finite number."`, re-prompts |
| TC-03 | `StudentUI` entry | Enter Student ID `0` or `-5` | Prints `"Student ID must be a positive number."` and re-prompts |
| TC-04 | `StudentUI.placeOrder` | Request qty `25` for Veg Burger (stock `20`) | `InventoryService.validateStock` throws `StockUnavailableException`: `"Only 20 left in stock. Please reduce your quantity."` |
| TC-05 | `StudentUI.placeOrder` | Add item ID `1` twice (qty `10` then qty `15`) | Combines quantities (`25 > 20`) and catches `StockUnavailableException` before adding excessive total |
| TC-06 | `StudentUI.placeOrder` | Request item whose stock is `0` | `validateStock` throws `StockUnavailableException`: `"OUT OF STOCK!"` |
| TC-07 | `StudentUI.placeOrder` | Confirm valid order (2 Veg Burgers, stock `20`) | Stock becomes `18`; `Order ID: 1`, `Token Number: #1`, status `PLACED` |
| TC-08 | `StudentUI.cancelOrder` | Cancel Order `1` while status is `PLACED` | Status becomes `CANCELLED`; stock of Veg Burger restores from `18` back to `20` |
| TC-09 | `StudentUI.cancelOrder` | Student `102` tries to cancel Student `101`'s order | Prints `"Order not found."` (ownership check `order.getStudentId() != studentId`) |
| TC-10 | `KitchenUI.updateOrderStatus` | Advance Order `1` (`PLACED` -> `PREPARING` -> `READY` -> `COLLECTED`) | Each confirmed step advances status; attempting a 4th update prints `"This order cannot be updated."` |
| TC-11 | `QueueService.getOrdersAheadByToken` | 3 active orders (`#1`, `#2`, `#3`); track `#3` | Returns `2` orders ahead; if `#1` becomes `COLLECTED`, tracking `#3` returns `1` |
| TC-12 | `AdminUI.addMenuItem` | Add `"Samosa"`, price `20`, stock `40` | Creates item with `ID: 6`, adds to `MenuService`, sets stock `40` in `InventoryService` |
| TC-13 | `AdminUI.changePrice` | Set price of ID `1` to `-10` | `MenuService.changePrice` returns `false`; prints `"Unable to update price."` |
| TC-14 | `App` / EOF handling | Send EOF (`Ctrl+Z` on Windows / `Ctrl+D` on Unix) | `InputHelper.readLine` throws `IllegalStateException`; app prints exit message and closes `Scanner` in `finally` |

---

## PAGE 15: CHAPTER 11 — LIMITATIONS, FUTURE SCOPE, CONCLUSION & REFERENCES

### 11.1 Current Limitations
1. **Volatile In-Memory Storage**: Menu updates, inventory changes, and order records are lost when the application terminates (`data/` directory is currently unused).
2. **Unintegrated Domain Models**: `Student` (`Student.java`) and `Payment` / `PaymentStatus` (`Payment.java`, `PaymentStatus.java`) are implemented with validation logic in `model/`, but are not yet connected to `StudentUI`.
3. **Order Modification Not Exposed in UI**: `OrderService.modifyOrder(...)` is implemented in the service layer, but `StudentUI` does not currently include a menu option to modify a placed order.
4. **No Role Authentication**: Any user can open the Kitchen or Admin panels from `MainMenu` without credentials.
5. **Single-Session Console Interface**: Only one user can interact with the console at a time.

### 11.2 Future Scope
- **File / Database Persistence**: Serialize menu items, stock, and orders to files in `data/` or persist them in MySQL / SQLite via JDBC.
- **Payment Workflow Integration**: Integrate `Payment` and `PaymentStatus` (`PENDING`, `PAID`, `REFUNDED`) during order checkout and cancellation.
- **Student Registration & Authentication**: Use the `Student` model and add login checks for Student, Kitchen, and Admin roles.
- **Expose Order Modification**: Add a "Modify Order" option in `StudentUI` using `OrderService.modifyOrder()`.
- **Automated Unit Testing**: Add JUnit 5 test suites for `InventoryService`, `OrderService`, `QueueService`, and policy classes.
- **GUI / Web Migration**: Reuse the existing `service`, `policy`, and `model` layers behind a JavaFX desktop UI or Spring Boot REST API.

### 11.3 Conclusion
The **Campus Canteen Management System** demonstrates how a real-world campus workflow can be modeled cleanly in core Java. By separating models, policies, services, and console UI into dedicated packages, using `ArrayList` and `HashMap` effectively, and guarding every boundary with explicit validation and exception handling, the project achieves reliable behavior and provides a strong foundation for future persistence and UI upgrades.

### 11.4 References
1. Oracle Java SE Documentation — *Collections Framework (`List`, `ArrayList`, `Map`, `HashMap`)* and *Exceptions*: https://docs.oracle.com/en/java/javase/21/
2. Herbert Schildt, *Java: The Complete Reference*, McGraw-Hill Education.
3. Project Source Files:
   - `src/com/campus/canteen/App.java`
   - `src/com/campus/canteen/model/*` (`MenuItem`, `Order`, `OrderItem`, `OrderStatus`, `Payment`, `PaymentStatus`, `Student`)
   - `src/com/campus/canteen/policy/*` (`InventoryPolicy`, `OrderPolicy`, `QueuePolicy`)
   - `src/com/campus/canteen/service/*` (`InventoryService`, `MenuService`, `OrderService`, `QueueService`, `StockUnavailableException`)
   - `src/com/campus/canteen/ui/*` (`AdminUI`, `InputHelper`, `KitchenUI`, `MainMenu`, `StudentUI`)
