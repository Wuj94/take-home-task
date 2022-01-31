This is an exercise I was asked to solve for an interview. 

# Take home task

Create a Restful microservice to display details of licenses tennis matches for a customer.

A **customer** may **license** either an individual **match** or a whole tournament. Every match is part of a tournament. The service should support multiple customers with different license agreements.

The service should return the following as JSON:

- matchId: Unique ID
- startDate: Date and time the match is scheduled to start
- playerA: Name of player A
- playerB: Name of player B
- summary: An optional parameter called summaryType can be set to any of:
    - AvB: in which case returns string "<playerA> vs <playerB>"
    - AvBTime: in which case use the start time to return the string "<playerA> vs <playerB>, starts in X minutes" when the start time is in the future. And, "<playerA> vs <playerB>, started X minutes ago", when the start time is in the past.

  
# Solution

## Resource endpoints
- tournaments/${tournamentId}/matches
- customers/${customerId}/licenses

## Access Pattern

We want to retrieve all of the matches a customer has licensed

**Request**: GET customers/${customerId}/licenses?type=match

**Response**: 
- **200** *OK* if successful or
- **400** *BAD REQUEST* if any of type or customerId is not valid or
- **404** *NOT FOUND* if the customer is not found.


## Database Design 

DynamoDB (key-value NoSQL datastore) is used as DB technology,

**License table**

- PartitionKey: licenseId
- GSI1-PartitionKey: customerId
- GSI1-SortKey: type
- other attributes: startDate, playerA, playerB, summary
