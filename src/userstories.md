

## As a user, I should be able to create an account so that I can have access to the service.

Registration should be a POST at account/registration.

- The request body should contain username and password. 
- Username must be unique and between 4 and 20 characters.
- The password should be at between 8 - 16 characters.
- If successful, response status should be 200 and the response body should contain the newly created account.
- If unsuccessful, response status should be 400 and the response body should be empty


## As a registered user, I should be able to login so that I can use the service.

Logging in should be a POST at account/login.

- The request body should contain username and password
- Log in will be successful if and only if username and password provided exists in the database. 
- If successful, response status should be 200 and the response body should contain the newly created account.
- If unsuccessful, response status should be 401 and the response body should be empty


## As a logged-in user, I should be able to create new posts.

Creation should be a POST at /posts

- The request body should contain the post content and an account_id 
- Post creation will be successful if and only if the post content is between 3 and 140 characters long, and the account_id refers to an existing account.
- If successful, response status should be 200 and the response body should contain the newly created post.
- If unsuccessful, response status should be 400 and the response body should be empty


## As a user, I should be able to read all posts

Retrieving all posts should be a GET at /posts

- Should return 200 regardless of success or failure and the response body should contain a JSON representation of a list containing all posts


## As a user, I should be able to visit a link with a post id so that I can read that post specifically along with it's details

Retrieving a posts by id should be a GET at /posts/{post_id}

- If successful, response status should be 200 and the response body should contain the post identifed by post_id
- If unsuccessful (post doesn't exist), response status should stil be 200 but the response body will be empty.


## As a user, I should be able to delete a post I made

Deleting a post should be a DELETE at /posts/{post_id}

- If successful, response status should be 200 and the response body should contain the now-deleted post.
- If unsuccessful (post doesn't exist), response status should still be 200 but the response body will be empty


## As a user, I should be able to update a post I made

Updating posts should be a PATCH at /posts/{post_id}

- The request body should contain the new post content
- Updating post content is successful if and only if the new post content is between 4 and 140 characters, the post_id references an existing post
- If successful, response status should be 200 and the response body should contain the now-updated post.
- If unsuccessful (post doesn't exist, char count unacceptable), response status should still be 400 but the response body should be empty.


## As a user, I should be able to retrieve all posts made by a particular user

Retrieving all posts made by user should be a GET at /accounts/{account_id}/messages

- Should return 200 regardless of success or failure and the response body should contain a JSON representation of a list containing all posts made by user identified by account_id, empty if the account_id was not found or the account did not make any posts