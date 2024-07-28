# Stella | A Shopping Website ( Backend )

## Project Overview
This project is a secure e-commerce shopping website backend developed using Java Spring Boot. It includes user authentication, product management, order management, and payment gateway integration to provide a complete online shopping experience.

## Features
- **User Authentication:** Secure user registration and login using Spring Security and JWT Authentication.
- **Product Management:** RESTful APIs to manage product listings, including adding, updating, and deleting products.
- **Order Management:** RESTful APIs to handle orders, including creating, viewing, and managing orders.
- **Payment Gateway Integration:** Seamless and secure payment processing for orders.

## Technologies Used
- **Backend Framework:** Spring Boot
- **Security:** Spring Security, JWT
- **Database:** MySQL/MariaDB
- **Payment Gateway:** Integration with a popular payment gateway (e.g., Stripe, PayPal)
- **Tools:** Visual Studio Code, Eclipse, Git, GitHub
- **Languages:** Java, SQL

## Getting Started

### Prerequisites
- Java 8 or higher
- Maven
- MySQL or MariaDB
- IDE (e.g., Visual Studio Code, Eclipse)

### Installation

1. **Clone the repository:**
    ```sh
    git clone https://github.com/Adikumaw/ecommerce-shopping-website.git
    cd ecommerce-shopping-website
    ```

2. **Set up the database:**
    - Create a database named `ecommerce_db` in MySQL/MariaDB.
    - Update the database configuration in `src/main/resources/application.properties` with your database credentials.

3. **Build the project:**
    ```sh
    mvn clean install
    ```

4. **Run the application:**
    ```sh
    mvn spring-boot:run
    ```

### API Endpoints

#### Authentication
- **Register User:** `POST /api/auth/register`
- **Login User:** `POST /api/auth/login`

#### Product Management
- **Get All Products:** `GET /api/products`
- **Get Product by ID:** `GET /api/products/{id}`
- **Add New Product:** `POST /api/products`
- **Update Product:** `PUT /api/products/{id}`
- **Delete Product:** `DELETE /api/products/{id}`

#### Order Management
- **Create Order:** `POST /api/orders`
- **Get User Orders:** `GET /api/orders/user/{userId}`
- **Get Order by ID:** `GET /api/orders/{id}`

#### Payment Processing
- **Process Payment:** `POST /api/payments`

## Usage
- **Swagger UI:** Access the API documentation at `http://localhost:8080/swagger-ui.html` after running the application.
- **Admin Panel:** A simple admin panel for managing products can be added for convenience.

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request for any feature additions, bug fixes, or improvements.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact
**Aditya Kumawat**  
Email: [kumawataditya105@gmail.com](mailto:kumawataditya105@gmail.com)  
LinkedIn: [Aditya Kumawat](http://www.linkedin.com/in/adityakumawat105)  
GitHub: [Adikumaw](http://github.com/Adikumaw)
