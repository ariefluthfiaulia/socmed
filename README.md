# Postr - Anonymous Social Media REST API

Postr is an anonymous social media application that allows users to post text information, engage with posts, and create
a sense of community. This repository contains the implementation of a RESTful API for the Postr application.

## Tech Stack
- Java Spring Boot
- PostgreSQL
- Spring Data JPA
- Spring Web

## Libraries Used
- Spring Boot Starter Data JPA
- Spring Boot Starter Web
- PostgreSQL Driver

## Assumptions
- Infinite scroll capabilities will be implemented using pagination in API responses.
- No user interface will be provided; this project focuses solely on building the API.
- Assumed that post and reply content can only be text-based, with a maximum length of 100 characters.
- Assumed that the frontend will handle generating unique random usernames for users.
- Assumed that the frontend will handle creating new posts and replies.

## Scope
The Postr application API will include the following endpoints:
- `[POST] /api/users`: Create a new user.
  
**Sample Request:**
```json
  {
    "username": "Joni"
  }
  ```

**Sample Response:**
```json
  {
    "body": {
      "id": "2c94a08189ff81310189ff8861430000",
      "username": "Joni"
    },
    "statusMessage": "OK",
    "statusCode": 200
  }
  ```
- `[POST] /api/posts`: Create a new post.

**Sample Request:**
```json
  {
    "username": "Joni",
    "content": "Post Test Joni"
  }
  ```
**Sample Response:**
```json
  {
    "body": {
      "id": "2c94a08189ff81310189ff8af6270001",
      "content": "Post Test Joni",
      "user": {
        "id": "2c94a08189ff81310189ff8861430000",
        "username": "Joni"
      },
      "createdDate": "2023-08-16T18:10:44.391+00:00",
      "replies": null
    },
    "statusMessage": "OK",
    "statusCode": 200
  }
  ```
- `[GET] /api/posts?user={username}&page={page}&size={size}`: Get a user's newest posts with pagination.

**Sample Response:**
```json
  {
    "body": [
      {
        "id": "2c94a08189ff81310189ff8f93130002",
        "content": "Post Test Joni 2",
        "user": {
          "id": "2c94a08189ff81310189ff8861430000",
          "username": "Joni"
        },
        "createdDate": "2023-08-16T18:15:46.706+00:00",
        "replies": []
      },
      {
        "id": "2c94a08189ff81310189ff8af6270001",
        "content": "Post Test Joni",
        "user": {
          "id": "2c94a08189ff81310189ff8861430000",
          "username": "Joni"
        },
        "createdDate": "2023-08-16T18:10:44.391+00:00",
        "replies": []
      }
    ],
    "statusMessage": "OK",
    "statusCode": 200
  }
  ```
- `[GET] /api/posts?postId={postId}`: Retrieve post and all of their replies.
 
**Sample Response:**
```json
  {
    "body": {
      "username": "Joni",
      "content": "Post Test Joni",
      "replies": [
        {
          "username": "Amran",
          "content": "Reply Post Joni"
        },
        {
          "username": "Joni",
          "content": "Reply reply-an nya pak amran"
        }
      ]
    },
    "statusMessage": "OK",
    "statusCode": 200
  }
  ```
- `[POST] /api/replies`: Reply to a post.

**Sample Request:**
```json
  {
    "postId":"2c94a08189ff81310189ff8af6270001",
    "username":"Amran",
    "content":"Reply Post Joni"
  }
  ```
**Sample Response:**
```json
  {
    "body": {
      "id": "2c94a0818a01de73018a01deb79e0000",
      "content": "Balas lagi",
      "user": {
        "id": "2c94a08189ff5c3b0189ff6550ad0000",
        "username": "Amran"
      },
      "post": {
        "id": "2c94a08189ff81310189ff8af6270001",
        "content": "Post Test Joni",
        "user": {
          "id": "2c94a08189ff81310189ff8861430000",
          "username": "Joni"
        },
        "createdDate": "2023-08-16T18:10:44.391+00:00"
      },
      "createdDate": "2023-08-17T05:01:27.836+00:00"
    },
    "statusMessage": "OK",
    "statusCode": 200
  }
  ```
  The focus of this project is to provide a functional backend API that supports the basic requirements of the Postr
  application.

## How to Run
- Clone the repository using this command `git clone https://github.com/ariefluthfiaulia/socmed.git`.
- Make sure you have installed PostgreSQL on your local computer.
- Create a database named "postr".
- Adjust the database credentials in the `application.properties` file to match your database settings.
- The default credentials for this application are:
  - Username: postgres
  - Password: postgres
- Run the application using the IDE that you like the most.