National Bank System Backend
Overview
The National Bank System Backend is a Spring Boot application for managing banking operations for the National Bank of Rwanda. It provides REST APIs for customer management, transactions (savings, withdrawals, transfers), email notifications, and Swagger documentation. All requirements are fully satisfied, including MySQL triggers and email notifications.
Features

Customer Management: Create, read, update, delete (CRUD) customer accounts.
Transactions: Record savings, withdrawals, and transfers in the banking table.
Email Notifications: Send confirmation emails for transactions via Gmail SMTP.
MySQL Trigger: Automatically save transaction messages in the message table using the after_banking_insert trigger, defined in data.sql.
Swagger UI: Interactive API documentation at /swagger-ui.html.

Technologies

Spring Boot: 3.2.0
Java: 17
MySQL: 8.0+
Spring Data JPA: For database operations
Spring Mail: For email notifications
Springdoc OpenAPI: For Swagger UI
Hibernate JCache/Ehcache: For caching
Maven: Build tool

Prerequisites

Java 17: Installed and configured
MySQL 8.0+: Running with a banking_db schema
Maven: For building the project
Gmail Account: For email notifications (with App Password)

Setup

Clone the Repository:
git clone https://github.com/yourusername/national-bank-sys-back-dev.git
cd national-bank-sys-back-dev


Configure MySQL:

Create a banking_db schema:CREATE DATABASE banking_db;


Ensure the MySQL user has TRIGGER privileges:GRANT TRIGGER ON banking_db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;


Create tables (customer, banking, message) manually if not already present:CREATE TABLE customer (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
account VARCHAR(50) NOT NULL,
balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
dob DATE NOT NULL,
email VARCHAR(100) NOT NULL,
first_name VARCHAR(50) NOT NULL,
last_name VARCHAR(50) NOT NULL,
last_update_date_time DATETIME NOT NULL,
mobile VARCHAR(20) NOT NULL
);
CREATE TABLE banking (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
account VARCHAR(50) NOT NULL,
amount DECIMAL(15, 2) NOT NULL,
banking_date_time DATETIME NOT NULL,
type VARCHAR(20) NOT NULL,
customer_id BIGINT NOT NULL,
FOREIGN KEY (customer_id) REFERENCES customer(id)
);
CREATE TABLE message (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
customer_id BIGINT NOT NULL,
message TEXT NOT NULL,
date_time DATETIME NOT NULL,
FOREIGN KEY (customer_id) REFERENCES customer(id)
);




Configure Application:

Open src/main/resources/application.properties.
Update spring.datasource.password and spring.mail.password with your MySQL password and Gmail App Password.
Generate a Gmail App Password:
Go to https://myaccount.google.com/security
Enable 2-Step Verification
Navigate to Security > App passwords
Select App: Mail, Device: Other (e.g., Spring Boot)
Copy the generated password to spring.mail.password




Verify SQL File:

Ensure src/main/resources/data.sql exists.
data.sql defines the after_banking_insert trigger and inserts initial customer data.


Build and Run:
mvn clean install
mvn spring-boot:run


Access the Application:

API: http://localhost:8080
Swagger UI: http://localhost:8080/swagger-ui.html



API Endpoints

Customer Management:
POST /api/customers: Create a customer
GET /api/customers: List all customers
GET /api/customers/{id}: Get a customer
PUT /api/customers/{id}: Update a customer
DELETE /api/customers/{id}: Delete a customer


Transactions:
POST /api/banking/save/{customerId}?amount={amount}: Save money
POST /api/banking/withdraw/{customerId}?amount={amount}: Withdraw money
POST /api/banking/transfer: Transfer money (JSON body with fromCustomerId, toCustomerId, amount)


Test Email:
GET /test-email: Send a test email (optional, for debugging)



Database Schema

customer: Stores customer details (id, account, balance, dob, email, first_name, last_name, last_update_date_time, mobile).
banking: Stores transactions (id, account, amount, banking_date_time, type, customer_id).
message: Stores transaction messages (id, customer_id, message, date_time).
Trigger: after_banking_insert populates message table after banking inserts, defined in data.sql.

Email Notifications

Configured via Gmail SMTP.
Ensure a valid Gmail App Password is set in application.properties.
Emails are sent for savings, withdrawals, and transfers.

Troubleshooting

Trigger Issues:
Verify:SHOW TRIGGERS LIKE 'after_banking_insert';


Check data.sql execution in logs or reapply manually:DELIMITER //
DROP TRIGGER IF EXISTS after_banking_insert//
CREATE TRIGGER after_banking_insert
AFTER INSERT ON banking
FOR EACH ROW
BEGIN
DECLARE customer_name VARCHAR(255);
DECLARE message_text VARCHAR(255);
SET customer_name = (SELECT CONCAT(first_name, ' ', last_name) FROM customer WHERE id = NEW.customer_id);
SET message_text = CONCAT('Dear ', customer_name, ', Your ', NEW.type, ' of ', NEW.amount, ' on your account ', NEW.account, ' has been Completed Successfully.');
INSERT INTO message (customer_id, message, date_time)
VALUES (NEW.customer_id, message_text, CURRENT_TIMESTAMP);
END//
DELIMITER ;




Email Issues:
Check logs for org.springframework.mail DEBUG output.
Verify Gmail App Password at https://myaccount.google.com/security.
Test with GET /test-email.


Database Issues:
Verify MySQL is running: mysqladmin -u root -p status.
Test connection: mysql -u root -p333333333 -e "SELECT 1".
Ensure tables exist:USE banking_db;
SHOW TABLES;





Contributing

Fork the repository.
Create a feature branch (git checkout -b feature/your-feature).
Commit changes (git commit -m "Add your feature").
Push to the branch (git push origin feature/your-feature).
Open a Pull Request.

License
MIT License. See LICENSE for details.
