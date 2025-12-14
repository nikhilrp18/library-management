-- Sample data for SpringBooks Library Management System
-- This data will be loaded automatically when the application starts

-- Insert sample books
INSERT INTO books (title, author, isbn, is_borrowed) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', false),
('To Kill a Mockingbird', 'Harper Lee', '9780061120084', false),
('1984', 'George Orwell', '9780451524935', true),
('Pride and Prejudice', 'Jane Austen', '9780141439518', false),
('The Catcher in the Rye', 'J.D. Salinger', '9780316769488', false),
('Lord of the Flies', 'William Golding', '9780571056866', true),
('Animal Farm', 'George Orwell', '9780451526342', false),
('Brave New World', 'Aldous Huxley', '9780060850524', false);

-- Insert sample members
INSERT INTO members (name, email) VALUES
('John Doe', 'john.doe@example.com'),
('Jane Smith', 'jane.smith@example.com'),
('Bob Johnson', 'bob.johnson@example.com'),
('Alice Brown', 'alice.brown@example.com'),
('Charlie Wilson', 'charlie.wilson@example.com');
