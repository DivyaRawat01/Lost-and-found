# Lost and Found Portal

A backend-focused web application built with **Java Servlets, JSP and JDBC** (MySQL) where students can report lost or found items, search the reports, and an admin can manage them.

Mini project for MCA, University Institute of Computing, Chandigarh University.

---

## Features

**Student**
- Register and log in (session based, using `HttpSession`)
- Report a **lost** or **found** item (name, description, location, date)
- View all reports and search by keyword (item name, description, location) and filter by type (Lost / Found)
- Log out

**Admin**
- Separate admin login
- View all reports with search and filter
- Edit a report, mark it as **Resolved**, or delete it

**Security and good practice**
- `PreparedStatement` for every SQL query (prevents SQL injection)
- HTML escaping of all stored text shown in JSP pages (prevents script injection)
- Session and role check on every protected servlet and page
- Post-Redirect-Get pattern after every state-changing action

---

## Tech Stack

| Layer | Technology |
|---|---|
| View | JSP, HTML5, CSS3 |
| Controller | Java 17+, Jakarta Servlet 6.1 (`@WebServlet`) |
| Server | Apache Tomcat 11 |
| Database | MySQL 8 |
| DB access | JDBC with MySQL Connector/J 8.x |
| Session | `HttpSession` |
| IDE | Eclipse IDE for Enterprise Java and Web Developers |

> The project uses **JDBC** for persistence. Hibernate is not used in this version.

---

## Architecture

```
Browser (HTML/CSS)  <-->  Servlets (Controller) + JSP (View)  <-->  JDBC  <-->  MySQL
        client tier               Apache Tomcat 11                         data tier
```

MVC mapping:
- **Model:** `Item` bean and the `users` / `items` tables
- **View:** JSP pages
- **Controller:** servlets

---

## Project Structure

```
LostAndFound
├── database
│   └── schema.sql
├── src/main/java/com/lostfound
│   ├── DBConnection.java        # JDBC connection utility
│   ├── Item.java                # bean for one row of items (+ HTML escape helper)
│   ├── RegisterServlet.java     # POST /register
│   ├── LoginServlet.java        # POST /login (student and admin)
│   ├── ReportItemServlet.java   # POST /reportItem
│   ├── ViewItemsServlet.java    # GET  /viewItems (list and search)
│   ├── UpdateItemServlet.java   # POST /updateItem (admin: edit, resolve)
│   ├── DeleteItemServlet.java   # POST /deleteItem (admin)
│   └── LogoutServlet.java       # GET  /logout
├── src/main/webapp
│   ├── css/style.css
│   ├── index.jsp
│   ├── login.jsp
│   ├── register.jsp
│   ├── dashboard.jsp
│   ├── reportItem.jsp
│   ├── items.jsp
│   ├── adminLogin.jsp
│   └── adminDashboard.jsp
├── pom.xml                      # optional (Maven build)
└── README.md
```

---

## Database

Database name: `lostfound`

**users**

| Column | Type | Notes |
|---|---|---|
| id | INT | Primary key, auto increment |
| name | VARCHAR(100) | |
| email | VARCHAR(100) | |
| username | VARCHAR(50) | Unique |
| password | VARCHAR(100) | |

**items**

| Column | Type | Notes |
|---|---|---|
| id | INT | Primary key, auto increment |
| user_id | INT | Foreign key to `users(id)` |
| type | VARCHAR(10) | `Lost` or `Found` |
| item_name | VARCHAR(100) | |
| description | VARCHAR(500) | |
| location | VARCHAR(100) | |
| date | DATE | |
| status | VARCHAR(10) | `Open` (default) or `Resolved` |

The full script is in [`database/schema.sql`](database/schema.sql).

---

## Getting Started

### Prerequisites
- JDK 17 or later
- Apache Tomcat 11
- MySQL Server 8
- MySQL Connector/J 8.x jar
- Eclipse IDE for Enterprise Java and Web Developers (or any IDE with Tomcat support)

### 1. Create the database
Run the script in MySQL Workbench or the MySQL command line:

```sql
SOURCE database/schema.sql;
```

### 2. Configure the database connection
Open `src/main/java/com/lostfound/DBConnection.java` and set your MySQL password:

```java
private static final String URL = "jdbc:mysql://localhost:3306/lostfound";
private static final String USER = "root";
private static final String PASSWORD = "your_password";
```

> Do not commit your real password to GitHub.

### 3. Run in Eclipse
1. **File → New → Dynamic Web Project**, name it `LostAndFound`, target runtime **Apache Tomcat v11.0**, content directory `src/main/webapp`.
2. Copy the Java files into `src/main/java` (package `com.lostfound`) and the JSP files and `css` folder into `src/main/webapp`.
3. Put the MySQL Connector/J jar in `src/main/webapp/WEB-INF/lib`.
4. Right-click the project → **Run As → Run on Server** → Tomcat v11.0.
5. Open <http://localhost:8080/LostAndFound/>

### Run with Maven (optional)
```bash
mvn clean package
```
Copy `target/LostAndFound.war` to Tomcat's `webapps` folder and start Tomcat.

### Using Tomcat 9 or older
Tomcat 9 and older use the `javax.servlet` packages. Replace `jakarta.servlet` with `javax.servlet` in all servlet imports (and in `pom.xml`).

---

## Default Admin Login

| Username | Password |
|---|---|
| `admin` | `admin123` |

The admin account is fixed in `LoginServlet.java` (`ADMIN_USERNAME`, `ADMIN_PASSWORD`). Change it before using the project anywhere real.

---

## URL Map

| URL | Method | Access | Purpose |
|---|---|---|---|
| `/register` | POST | Public | Register a student |
| `/login` | POST | Public | Student or admin login |
| `/reportItem` | POST | Student | Save a lost or found report |
| `/viewItems` | GET | Logged in | List and search reports |
| `/updateItem` | POST | Admin | Edit or resolve a report |
| `/deleteItem` | POST | Admin | Delete a report |
| `/logout` | GET | Logged in | End the session |

---

## How a Request Works (example: report an item)

1. The student submits the form on `reportItem.jsp`; the browser sends `POST /reportItem`.
2. `ReportItemServlet.doPost()` checks the session and validates the input.
3. `DBConnection.getConnection()` opens a JDBC connection.
4. A `PreparedStatement` runs `INSERT INTO items ...` in MySQL.
5. The servlet redirects to `/viewItems`, which runs a `SELECT`, stores the result list in the request and forwards to `items.jsp`.
6. The JSP renders the list as HTML and the browser displays it.

---

---

## Known Limitations

- Passwords are stored as plain text to keep the project simple.
- The admin account is hard-coded.
- A new JDBC connection is opened for each request (no connection pool).

## Future Improvements

- Hash passwords (BCrypt) and add an authentication filter
- DAO layer and Hibernate ORM instead of direct JDBC, with connection pooling
- Claim requests with admin approval and e-mail notifications
- Photo upload and automatic matching of lost and found reports
- Pagination and a responsive layout

---

## Author
Divya 



**Your Name** - MCA, University Institute of Computing, Chandigarh University
GitHub: [@your-username](https://github.com/your-use
