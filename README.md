# Postr - Anonymous Social Media REST API
Postr is an anonymous social media application that allows users to post text information, engage with posts, and create a sense of community. This repository contains the implementation of a RESTful API for the Postr application.

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
- `[GET] /api/users/{username}`: Get user details by username.
- `[POST] /api/posts`: Create a new post.
- `[GET] /api/posts?user={username}&page={page}&size={size}`: Get a user's newest posts with pagination.
- `[POST] /api/replies`: Reply to a post.
- `[GET] /api/replies?post={postId}&page={page}&size={size}`: Get replies to a post with pagination.
The focus of this project is to provide a functional backend API that supports the basic requirements of the Postr application.
