# spring-boot-ATS
Microservice Explanation:

API Gateway:

All the request lands on the api gateway which validates the JWT bearer token and then forwards the request if its valid. The JWT validation is done for all apis but login.

Login Service:

Login service is responsible for validation of the user when the user tries to log into the system. It generates a short lived JWT token. If the user is valid it returns the JWT token with roles in it, in the response body. This JWT token is required to be in the authorization header as Bearer token to access other apis.

Monitor Service:

Monitor service runs in a scheduled time and gets the delivery details, processes the delivery details, creates the tickets based on the strategies and saves them in the database for ticket service.

Ticket Service:

Ticket service retrieves the tickets from the database when the user with the allowed role makes a request. 

