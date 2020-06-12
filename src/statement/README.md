## Statement Service
Statement service for Stock Trader application connects to MongoDB database to store and retrieve the statements for Stock Trader Clients. 

## Building

`mvn clean install` To package and create application executable jar

`docker build -t stocktradersjilv2/statement .` To create the docker image from the jar file

`docker push stocktradersjilv2/statement:latest` To push the docker image to the registry.


## Configuration

The application/container expects the following environment variables to be populated and carry configuration for the application.

| Env Var | Purpose |
|---------|---------|
|`MONGODB_HOST` | The MongoDB host  |
|`MONGODB_PORT` | The MongoDB port eg. `31008`|
|`MONGODB_DATABASE_NAME` | The MongoDB database name . eg.`stocktrader`|
|`MONGODB_USER` | MongoDB username eg. `root`|
|`MONGODB_PWD` | MongoDB password eg. `secretpassword`|
|`MONGODB_AUTH_DB` | The MongoDB authentication database eg. `admin`|
 

## Deployment
 - Make sure you have MongoDB instance running and accessible
 - Clone this repo
 - Go to directory `cd stocktrader-jil-v2\src\statement\manifest` 
 - Create secret for MongoDB credentials and host details
```bash
kubectl create secret generic mongodb --from-literal=user=admin --from-literal=password=secretpassword --from-literal=host=172.17.76.32 --from-literal=port=32008 --from-literal=database=stocktrader --from-literal=authenticationdb=admin
```
 - Apply the manifest file 
 ```bash
kubectl apply -f deployment.yml
kubectl apply -f service.yml
kubectl apply -f ingress.yml
```


## Download Statements
We can access the statement service API with below URL
`http://{OCP-HOST}:31010/trader/statement/{CLIENT}?startDate={DD-MM-YYYY}&endDate={DD-MM-YYYY}`

#### Note:
 _The service stores dummy default statement in MongoDB for all clients - If needed we can upload the statements for a specific user with below curl_
```bash
 curl --location --request POST 'http://{OCP-HOST}:31010/trader/statement/add/{CLIENT}?title=AccountStatemet' --header 'Content-Type: multipart/form-data' --form 'file=@/root/OwnerStatement.pdf'
```
