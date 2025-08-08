# 🍔 Food Ordering Management System

> A simple **Java + MySQL** console-based application to manage food orders for a restaurant.  
> Supports both **Admin** and **User** functionalities with real-time database updates.

---

## 📸 Sample Demonstration

### 🔑 Login Screen
```
--- Login ---
Enter Username: admin
Enter Password: ****
✅ Login Successful (Role: Admin)
```

---

### 👨‍💼 Admin Menu
```
--- Admin Menu ---
1. View Food Items
2. Add Food Item
3. Update Food Item
4. Delete Food Item
5. View All Orders
6. Update/Delete Order
7. Logout
```

---

#### 📜 Viewing Food Items
```
+----+-----------+--------+
| ID | Name      | Price  |
+----+-----------+--------+
| 1  | Burger    | 120.00 |
| 2  | Pizza     | 250.00 |
+----+-----------+--------+
```

---

#### 🆕 Adding Food Item
```
Food Name: Pasta
Price: 180
✅ Food item added.
```

---

#### 📦 Viewing All Orders
```
Order ID: 1 | User ID: 2 | Food ID: 1 | Qty: 2 | Total: ₹240.00 | Date: 2025-08-08 | Status: Pending
Order ID: 2 | User ID: 3 | Food ID: 2 | Qty: 1 | Total: ₹250.00 | Date: 2025-08-08 | Status: Completed
```

---

### 👤 User Menu
```
--- User Menu ---
1. View Menu
2. Place Order
3. View My Orders
4. Cancel My Pending Order
5. Logout
```

---

#### 📜 Viewing Menu
```
+----+-----------+--------+
| ID | Name      | Price  |
+----+-----------+--------+
| 1  | Burger    | 120.00 |
| 2  | Pizza     | 250.00 |
| 3  | Pasta     | 180.00 |
+----+-----------+--------+
```

---

#### 🛒 Placing Order
```
Food ID: 1
Quantity: 2
✅ Order placed! Total: ₹240.00
```

---

#### 📦 Viewing My Orders
```
Order ID: 1 | Food ID: 1 | Qty: 2 | Total: ₹240.00 | Date: 2025-08-08 | Status: Pending
```

---

## 🛠 How It Works
1. **Login** as Admin or User
2. **Admin** manages food menu & updates orders
3. **User** views menu, places orders, and tracks them
4. All changes are **stored in MySQL** database in real-time

---

## 📂 Tech Stack
- Java (Core Java + JDBC)
- MySQL
- Maven

---

## 📌 Setup
1. Clone repository  
   ```bash
   git clone https://github.com/your-username/food-ordering-demo.git
   ```
2. Create MySQL database & tables (use provided `.sql` file)  
3. Configure `DBUtil.java` with your MySQL credentials  
4. Run project with Maven  
   ```bash
   mvn clean install
   java -cp target/food-ordering-system-1.0.jar com.aurionpro.test.MainApp
   ```

---

## 🎯 Demo Users
```
Admin Login → Username: admin | Password: admin123
User Login → Username: john  | Password: pass123
```

---

## 👨‍💻 Author
Made with ❤️ in Java  
[GitHub](https://github.com/your-username)
