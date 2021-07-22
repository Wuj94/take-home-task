#IMG Arena Interview

##Resources

tournaments
tournaments/${tournamentId}/matches
customers
customers/${customerId}/licenses

#Access Pattern

We want to retrieve all of the matches a customer has licensed

Request: GET customers/${customerId}/licenses?type=match
Response: **200** *OK* 

or **400** *BAD REQUEST* if any of type or customerId is not valid 

or **404** *NOT FOUND* if the customer is not found.

###Representation of the response: 
JSON array where top-level attributes of the elements in the array are:
- matchId
- startDate 
- playerA 
- playerB 
- summary


#Database Design 

DynamoDB (key-value NoSQL datastore) is used as DB technology,

**License table**

- PartitionKey: licenseId
- GSI1-PartitionKey: customerId
- GSI1-SortKey: type
- other attributes: startDate, playerA, playerB, summary

the attribute *summary* is computed on-the-fly and given to the user.
