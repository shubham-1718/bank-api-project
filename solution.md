# Bank Application API - Solution Overview

## Project Overview

The Bank Application API streamlines essential banking operations for users or customers, offering a secure and efficient experience. This documentation provides an overview of endpoints, requests, and responses.

## Tech Stack and Dependencies Used

The Bank Application utilizes a robust tech stack and key dependencies to deliver reliable banking services.

### Tech Stack:

- **Language:** Java Version 11
- **Build Tool:** Maven
- **Framework:** Spring Boot
- **Database:** MySQL 8.0.28

### Dependencies:

- Spring Boot Starter Web
- Spring Data JPA
- JUnit 5 (for testing)
- Mockito (for testing)

## Getting Started

To proceed with running this application, follow these steps:

1. **Pre-populated Customer Data:** The application comes with pre-populated customer data, which is provided in the data.sql file, allowing to interact with existing customer accounts.

2. **Create a Database (if not already present):** Ensure you have a database with the name "bank" set up. You can create the "bank" database by executing the following SQL query:

    ```
    CREATE DATABASE bank;
    ```

3. **Run the Application:** After creating the "bank" database, you are good to go. Run the Bank Application API, and you'll be ready to explore it.


## Endpoints and Operations

### Create a New Bank Account

**Endpoint:** `POST /accounts/create`

**Description:** This endpoint allows users to create a new bank account for a customer. It validates the request, associates the account with the customer, and stores the account in the database.

**Request Example:**

```
POST /accounts/create
Content-Type: application/json

{
"customerId": 1,
"deposit": 10000.00
}
```

**Response:**

```
{
"accountId": 101,
"deposit": 10000.00,
"customer": {
    "customerId": 1,
    "name": "Arisha Barron"
    }
}
```

### Get Account Balance

**Endpoint:** `GET /accounts/{accountId}/balance`

**Description:** This endpoint retrieves the balance of a specific account, ensuring secure access to account details.

**Request Example:**

```
GET /accounts/101/balance
```

**Response:**

200 OK with the account balance:

```
10000.00
```

### Transfer Funds

**Endpoint:** `POST /transfers`

**Description:** This endpoint allows users to transfer funds between two accounts. It checks for available funds, deducts the amount from the sender's account, adds it to the recipient's account, and logs the transaction.

**Request Example:**

```
POST /transfers
Content-Type: application/json

{
  "fromAccountId": 101,
  "toAccountId": 102,
  "amount": 500.00
}
```

**Response:**

200 OK if the transfer is successful:

```
"Transfer successful"
```

400 Bad Request in case of errors, e.g., insufficient funds:

```
"Insufficient funds"
```

### Get Transfer History

**Endpoint:** `GET /transfers/history/{accountId}`

**Description:**  This endpoint retrieves the transfer history for a specific account, providing transparency into past transactions.

**Request Example:**

```
GET /transfers/history/101
```

**Response:**

200 OK with the transfer history:
```
[
    {
        "transactionId": 1001,
        "accountId": 101,
        "narration": "Withdrawn: 100.00 to account: 102",
        "date": "2023-10-20T01:46:39",
        "withdrawalAmount": 500.00,
        "depositAmount": 0.00,
        "closingBalance": 9500.00
    },
    {
        "transactionId": 1004,
        "accountId": 101,
        "narration": "Deposited: 100.00 from account: 102",
        "date": "2023-10-20T01:47:38",
        "withdrawalAmount": 0.00,
        "depositAmount": 500.00,
        "closingBalance": 10000.00
    }
]
```

404 Not Found if the account doesn't exist.

## Testing
The application goes beyond just its implementation and includes a comprehensive suite of unit tests. These tests, crafted using the JUnit and Mockito frameworks, cover a wide range of functionalities and meticulously examine the application's behavior.

## Conclusion
The Bank Application API offers an intuitive solution for managing the finances. With these endpoints, customers can create accounts, check balances, transfer funds, and review transaction history seamlessly.