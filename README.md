# National Bank System Backend 🏦

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Email Notifications](#email-notifications)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

## Overview
The National Bank System Backend is a robust Spring Boot application designed for managing banking operations for the National Bank of Rwanda. This system provides comprehensive REST APIs for customer management, financial transactions, automated email notifications, and interactive API documentation through Swagger UI.

### Key Highlights
- Complete banking operations management
- Real-time transaction processing
- Automated email notifications
- Interactive API documentation
- MySQL trigger-based message logging
- Secure authentication and authorization

## Features

### 🏦 Core Banking Features
- **Customer Management**: Full CRUD operations for customer accounts
- **Transaction Processing**: 
  - Savings deposits
  - Cash withdrawals
  - Inter-account transfers
- **Automated Notifications**: Email confirmations for all transactions
- **Message Logging**: Automatic transaction message storage via MySQL triggers
- **API Documentation**: Interactive Swagger UI interface

## Technologies

### 🛠️ Tech Stack
- **Spring Boot**: 3.2.0 - Core framework
- **Java**: 17 - Programming language
- **MySQL**: 8.0+ - Database management
- **Spring Data JPA**: Database operations
- **Spring Mail**: Email service integration
- **Springdoc OpenAPI**: API documentation
- **Hibernate JCache/Ehcache**: Performance optimization
- **Maven**: Project build and dependency management

## Prerequisites

### 🔧 Required Software
- Java 17 JDK
- MySQL 8.0 or higher
- Maven 3.6+
- Gmail account (for email notifications)

## Setup

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/national-bank-sys-back-dev.git
cd national-bank-sys-back-dev
```

### 2. Database Configuration

#### Create Database
```sql
CREATE DATABASE banking_db;
```

#### Configure User Permissions
```sql
GRANT TRIGGER ON banking_db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

#### Create Required Tables
```sql
-- Customer table
CREATE TABLE customer (
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

-- Banking transactions table
CREATE TABLE banking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account VARCHAR(50) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    banking_date_time DATETIME NOT NULL,
    type VARCHAR(20) NOT NULL,
    customer_id BIGINT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- Message logging table
CREATE TABLE message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    date_time DATETIME NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);
```

### 3. Application Configuration

1. Open `src/main/resources/application.properties`
2. Update the following properties:
   - `spring.datasource.password`: Your MySQL password
   - `spring.mail.password`: Your Gmail App Password

#### Gmail App Password Setup
1. Visit [Google Account Security](https://myaccount.google.com/security)
2. Enable 2-Step Verification
3. Navigate to Security > App passwords
4. Select App: Mail, Device: Other (Spring Boot)
5. Copy the generated password to `spring.mail.password`

### 4. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

### 5. Access Points
- API Base URL: http://localhost:8080
- Swagger UI: http://localhost:8080/api/swagger-ui/index.html

## API Endpoints

### Customer Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/customers` | Create new customer |
| GET | `/api/customers` | List all customers |
| GET | `/api/customers/{id}` | Get customer details |
| PUT | `/api/customers/{id}` | Update customer |
| DELETE | `/api/customers/{id}` | Delete customer |

### Transactions
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/banking/save/{customerId}` | Deposit money |
| POST | `/api/banking/withdraw/{customerId}` | Withdraw money |
| POST | `/api/banking/transfer` | Transfer between accounts |

### Testing
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/test-email` | Test email configuration |

## Database Schema

### Tables Overview
1. **customer**: Customer information storage
2. **banking**: Transaction records
3. **message**: Automated transaction messages

### Trigger Configuration
The `after_banking_insert` trigger automatically logs transaction messages:
```sql
DELIMITER //
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
```

## Email Notifications

### Configuration
- Uses Gmail SMTP server
- Requires valid Gmail App Password
- Sends notifications for:
  - Savings transactions
  - Withdrawal operations
  - Transfer confirmations

## Troubleshooting

### Trigger Verification
```sql
SHOW TRIGGERS LIKE 'after_banking_insert';
```

### Email Configuration
1. Check application logs for mail-related errors
2. Verify Gmail App Password configuration
3. Test email functionality using `/test-email` endpoint

### Database Connection
1. Verify MySQL service status:
```bash
mysqladmin -u root -p status
```

2. Test database connection:
```bash
mysql -u root -p333333333 -e "SELECT 1"
```

3. Verify table existence:
```sql
USE banking_db;
SHOW TABLES;
```

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/your-feature`)
3. Commit changes (`git commit -m "Add your feature"`)
4. Push to branch (`git push origin feature/your-feature`)
5. Open Pull Request

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
For support or questions, please open an issue in the repository.
