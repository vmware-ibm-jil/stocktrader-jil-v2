## Notification Service
Notification service for Stock Trader application will subscribe to RabbitMq for the messages sent from the Portfolio service, when user perfroms the operations like 
* Create Portfolio
* Update Portfolio (add stock)
* Delete Portfolio
Then the notification service will publish these message to IBM cloud push notification service.

## Building

`mvn clean install` To package and create application executable jar

`docker build -t stocktradersjilv2/notification .` To create the docker image from the jar file

`docker push stocktradersjilv2/notification:latest` To push the docker image to registery.


## Configuration

The application/container expects the following environment variables to be populated and carry configuration for the application.

| Env Var | Purpose |
|---------|---------|
|`MQ_USR` | The rabbitmq user name . eg. `user`|
|`MQ_PWD` | The rabbitmq password . eg. `bitnami`|
|`MQ_VIRTUAL_HOST` | The rabbitmq vertual host . eg.(default) `/`|
|`MQ_HOST_NAME` | The rabbitmq host name or ip address . eg. `http://myrabbitmq.local`|
|`MQ_PORT` | The rabbitmq port . eg. `5672`|
|`MQ_QUEUE` | The rabbitmq queue name . eg. `TESTQ`|
|`TENANT_ID` | The api guuid of the IBM cloud  push notification service . eg. `user`|
|`API_KEY_ID` | The api key from the credentials of the IBM cloud  push notification service . eg. `52XE_c9OkJJ6NfHDjTXxrYWcUUph86mwOLIZXyGlYaaa`|
|`REGION` | The region of IBM cloud push notification service . eg. `.us-east.bluemix.net`|
|`TAG_NAME` | The tag for push notifiaction message  . eg. `STOCKUPDATES`|
|`ALERT_MSG_URL` | The URL to be sent with push notifiaction alert . eg. `www.ibm.com` |


## Deployment
 - clone this repo
 - Go to directory `cd stocktrader-jil-v2\src\notification\mainifest` 
 - Create secrets for RabbitMQ and IBM Cloud Push services details:

```bash
kubectl create secret generic rbq --from-literal=user=admin --from-literal=password=secretpassword --from-literal=vhost=/ --from-literal=host=172.17.76.32 --from-literal=port=32004 --from-literal=queue=stocktrader

kubectl create secret generic ibmcloudpush --from-literal=tenenatid=77955822-7290-4cd9-b80a-3091b6892fee --from-literal=apikey=52XE_c9OkJJ6NfHDjTXxrYWcUUph86mwOLIZXyGlY2aq  --from-literal=region=.us-east.bluemix.net --from-literal=tag=STOCKTRADERS --from-literal=alertmsgurl=www.ibm.com
```
 - Apply the mainifest file `kubectl apply -f deployment.yml`
