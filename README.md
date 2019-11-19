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

I choose MongoDb, because it is quite easy to set up with a basic docker container locally. 
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


# Testing 

The application can only run after a MongoDB install have been set up. You could look at the guide above for more information

Default properties in application.properties 
spring.data.mongodb.uri=mongodb://samle:pa55w0rd@localhost:27017/db_test
api.search.product.uri=http://localhost:8080/api/products/search


```bash
mvn clean install
java -jar target/demo.service-0.1.0.jar

```
To override any of the default properties

```bash

java -jar target/demo.service-0.1.0.jar --api.search.product.uri=http://localhost:8080/api/products/search


```
# Security 
Between service and service, I think we could pass a simple JWT around in general case. It is simple and effective enough.
Otherwise, set up an public/private key pair, may be more secure option. However, I think it is a bit over kill.
For client authentication, I think OAuth could be a nice choice as it would free the application from being 
authentication provider, which mostly, does not a part of the business.  

# Service redundancy
To bring down the service, at the moment, you need to get access to the machine the service running on. 
You can force kill the service but, I think firstly, I would stop anyone call the API by block the 8080 port.

Before taking action to redundant the service, you would need to think about
     
   - whether you have any replacement service. If yes, then we need to have a redirect strategy/ testing new service in place.
   - how we deal with outstanding order
   - how we deal with historical data (depend on regulation some data may need to be kept for long time)
 