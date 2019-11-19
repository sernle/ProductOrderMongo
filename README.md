# ProductOrderMongo
Simple rest API to demo product order end to end


# Requirements
API supports basic products CRUD:
● Create a new product
● Get a list of all products
● Update a product
The API should also support:
● Placing an order
● Retrieving all orders within a given time period
A product should have a name and some representation of its price.

# Persistence consideration
Due to the requirement, I think we could use a simple key-value database here. 
There is not any require for complex transaction here. Most of the key-value/document database in the market 
could fit the job.

I choice MongoDb, because it is quite easy to set up with a basic docker container locally. 
If there is no requirement on local deployment, I would rather go with Google Cloud Datastore or AWS DynamoDb, 
because it is easy to work with. And also, there are a lot of potential to scale later on.

## Set up MongoDB
You would need to have docker in your local machine first. 

```bash
docker pull mongo:4.2
# docker rm mongodb
docker run -d -p 27017-27019:27017-27019 --name mongodb mongo:4.2
```

Create new user

```bash
docker exec -it mongodb bash
mongo
use db_test
db.createUser( { user: "samle",
                 pwd: "pa55w0rd",
                 roles: [ { role: "readWrite", db: "db_test" }]
                   } );
```


# Rest Doc API
I use swagger so, you could follow the link to access to the rest api doc of the application when the application is running
http://localhost:8080/v2/api-docs
http://localhost:8080/swagger-ui.html


# Service redundancy
