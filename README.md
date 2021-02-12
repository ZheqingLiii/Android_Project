# Rent My Car

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/69d3b12e64494f62a742b050558322d2)](https://app.codacy.com/app/ZheqingLiii/RentMyCar?utm_source=github.com&utm_medium=referral&utm_content=ZheqingLiii/RentMyCar&utm_campaign=Badge_Grade_Dashboard)

## Introduction
Rent My Car is an Android application that allows you to search for cars in your area that are available for rent, The application connects you to the owner if the car and allows you to contact them in order to meet their lease terms.

## Installation
- Use the apk file to install and run the app
- The Backend NodeJS is only used to demonstrate the architecture of the backend. The server is current live on AWS and can be used anytime using the .apk

## User Manual
- Signup
  - Enter email,name and password
- Signin
  - Use registered email and password
- Search
  - Enter start date, end date and city location to search for available vehicles within 25 miles
  - The results will be displayed with model, photo, price in a list
  - Click on a vehicle of your preference from the list to see more details about the vehicle 
- Book
  - Click “checkout” at the button of the vehicle detail page
  - Send email to the owner (the owner email will be displayed) 
- Post
  - If the user wants to add a car he/she owned,select “add” tab
  - Enter all the required information in that page
  - Click “submit”, the successfully message should popup
- Update
  - User can see all the vehicle showed in alist
  - Click a vehicle to see details of that vehicle
  - In vehicle detail page, users can upload an image of that vehicle
  - Click “Save” to update all the information of that vehicle
- Signout
  - Click profile tab, click “signout” at the top right
  - User will jump back to sign in page
