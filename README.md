# TelcoService - JavaEE Web Application - Project
Project developed for the examination of the Data Bases 2 course at Politecnico di Milano (A.Y. 2021/2022).

## Specifications
The complete list of specifications can be found [here](./Specifications.pdf).

The aim of this project is to create a telco website, where customers can buy telco products/subscriptions, and registered employees can create new packages to sell to customers, with different optional products and services included. Employees can also see lots of statistics on selling performances.

The **front-end** has been developed with HTML, CSS, and JS, with the use of Bootstrap and Jquery. It communicates with the backend through AJAX requests and JSON responses.

The Apache TommEE **back-end** integrates the Jakarta (Java) Persistence API, which is connected to a  MySQL database and calls an external service to process the payments.

There are two different frontends for the two different types of users, while the backend shares the same database.

## Group members
- **Gabriele Marra** ([@gabrielemarra](https://github.com/gabrielemarra)) <br>
- **Destiny Mora**([@dmora4](https://github.com/dmora4)) <br>

## Screenshots
### Login
![Login](./final_result-screenshots/01-Login.png)
### Home Page - Logged and unlogged
![Home Page - Logged user](./final_result-screenshots/03-HomePage-LoggedUser.png)
![Home Page - Unlogged user](./final_result-screenshots/02-HomePaeg-UnloggedUser.png)
### Buy Service Page
![Buy Service Page ](./final_result-screenshots/04-ChoosingProduct.png)
### Order Confirmation - Failing and successful
![Order Failing](./final_result-screenshots/05-OrderFailing.png)
![Order Successful](./final_result-screenshots/06-OrderPlaced.png)
---
### Employee Home Page
![Employee Home Page](./final_result-screenshots/07-EmployeeHomePage.png)
### Service Creation
![Service Creation](./final_result-screenshots/08-ServiceCreation.png)
### Statistics
![Statistics 1](./final_result-screenshots/09-Statistics1.png)
![Statistics 2](./final_result-screenshots/10-Statistics2.png)
