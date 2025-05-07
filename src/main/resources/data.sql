-- Create trigger for banking table
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

-- Insert sample customer data
INSERT INTO customer (account, balance, dob, email, first_name, last_name, last_update_date_time, mobile)
VALUES
    ('ACC001', 1000.00, '1990-01-01', 'john.doe@example.com', 'John', 'Doe', CURRENT_TIMESTAMP, '1234567890'),
    ('ACC002', 500.00, '1985-05-15', 'jane.smith@example.com', 'Jane', 'Smith', CURRENT_TIMESTAMP, '0987654321'),
    ('FIFE Account', 1000.00, '1995-03-10', 'faith.imbabazi@example.com', 'IMBABAZI', 'Faith', CURRENT_TIMESTAMP, '0781234567');