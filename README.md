# Grocery Store API

Java API using Spring Boot, consisting in of a payment system API consuming a given wiremock with a product list and dealing with promotions.

## Table of Contents

- [Introduction](#introduction)
- [Installation](#installation)
- [Usage](#usage)
- [Future Improvements](#future-improvements)
- [Follow Up Questions](#follow-up-questions)
- [Extra Question](#extra-question)

## Introduction

The goal is to create a job that queries a weather API at a configurable scheduled interval and records updated data for one specific city, in a PostgreSQL database. In addition, an endpoint was created to query the temperature records, allowing the user to specify a date range for the query.

## Installation

1. Clone the project;
2. Check prerequisites: Java 17, Apache Maven 3.X and an IDE (IntelliJ IDEA, Eclipse, VSCode). The system uses an in memory H2 database;
3. Import as a Maven project;
4. Build the project: Once the project is imported, build it using Maven:
<!-- -->
    mvn clean install

5. Run the project via IDE or Command Line:
<!-- -->
    mvn spring-boot:run

6. Run the wiremock server following the instructions in its README.md

## Usage

```/api/carts``` - **GET** - list of all carts\
```/api/carts/{id}``` - **GET** - get a cart by id<br>
```/api/carts/create``` - **POST** - create a new cart (required a form as body)\
```/api/carts/add/{id}``` - **PATCH** - add items into an already existent cart (required a form as body)\
```/api/carts/remove/{id}``` - **PATCH** - remove items from an already existent cart (required a form as body)\
```/api/carts/checkout/{id}``` - **PATCH** - change basket status to CHECKOUT\
```/api/carts/completed/{id}``` - **PATCH** - change basket status to COMPLETED

```/api/products``` - **GET** - list all products\
```/api/products/{productId}``` - **GET** - get product using id<br>

### Example of form to create a new cart, add or remove items

```json
{
  "productBaskets": [
    {
      "productId": "Dwt5F7KAhi",
      "quantity": 3
    },
    {
      "productId": "PWWe3w1SDU",
      "quantity": 1
    },
    {
      "productId": "C8GDyLrHJb",
      "quantity": 2
    }
  ]
} 
```

### Example of api response

<!-- -->
    GET /api/carts

    RESPONSE: 200 OK
    [
      {
        "id": 1,
        "status": "ACTIVE",
        "items": [
            {
                "id": 1,
                "productInfo": {
                    "productId": "Dwt5F7KAhi",
                    "name": "Amazing Pizza!"
                },
                "quantity": 2
            },
            {
                "id": 2,
                "productInfo": {
                    "productId": "PWWe3w1SDU",
                    "name": "Amazing Burger!"
                },
                "quantity": 3
            }
        ],
        "grosslValue": 51.95,
        "totalSavings": 13.98,
        "netValue": 37.97
      },
      ...
    ]

## Future Improvements
Logging, CI/CD pipeline, Testing, Containerization. 

## Follow-up questions
### How long did you spend on the test? What would you add if you had more time?
I've spend around 12 hours doing this test. If I had more time I would refine code, put logs, configure a container for the application, create tests, add a CI/CD pipeline in github.

### What was the most useful feature that was added to the latest version of your chosen language? Please include a snippet of code that shows how you've used it.
As I am using Java 17, I am including the Records to deal with Promotion and Product data. It simplifies the declarations, generating by default some methods that are frequently used in these cases (equals(), hashCode(), toString(), getter).

### What did you find most difficult?
I've found difficult in pay attention to all the exceptions that the API could throw and treat them to give meaningful returns to the client.

### What mechanism did you put in place to track down issues in production on this code? If you didn’t put anything, write down what you could do.
I didn't put anything in place, but as I described in the first question one of the features that I would add was logging to track the issues in production.

###  The Wiremock represents one source of information. We should be prepared to integrate with more sources. List the steps that we would need to take to add more sources of items with diferent formats and promotions.
First of all, it is necessary to define the models for Product and Promotion, considering particularly that the model for Product can accomodate a list of different Promotion. Then, it is necessary to create interfaces to to indicate what kind of operations can be made to interact with different sources (e.g. getAll, getById), including then methods to manage the products and promotions (create or delete). To make it easier to run the wiremock in different contexts, it can be "Dockerized" ir order to let it easier to run and intergrate. 

## Extra Question

### Share a recent judgment call you've made within the past 12 months that couldn't be thoroughly analyzed beforehand. Focus on a business-related issue. Whether it was a significant decision or a smaller one, describe the situation, the alternatives you considered, and your decision-making process. Explain why you chose the judgment call you did over the alternatives.
I was working on a project to maintain a platform for the Ministry of Education, and we were facing a problem that was affecting the functioning of the platform, but the team had only been taking temporary solutions. It happened that the user entered financial information to generate annual reports and, even though there was an error when sending the report, the report was always generated, requiring us to send migrations to the DBAs to adjust the database. I made the decision on my own to understand the situation better and find a solution outside of business hours. I realized that the transaction to the database in which the method for sending the report was called was poorly organized, because there were many dependencies and it was not enough to just use the framework's annotations. So, I worked for about 10 days to solve the problem and managed to solve it, eliminating a problem that was responsible for about 30% of the platform's support calls. The alternatives were just keeping the system as it was (and it was fine because the team was operating like that for about 8 years) or searching for the solution, which would led to the result I've described before, and giving us the opportunity to earn the right to maintain more platforms for the client, increasing the financial results.
