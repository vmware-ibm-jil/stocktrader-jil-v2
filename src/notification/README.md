## Notification Service
Notification service for Stock Trader application will subscribe to RabbitMq for the messages sent from the Portfolio service, when user perfroms the operations like 
* Create Portfolio
* Update Portfolio (add stock)
* Delete Portfolio
Then the notification service will publish these message to IBM cloud push notification service.

## Building

`mvn clean install` To package and create application executable jar

`docker build -t stocktrader/notifications .` To create the docker image from the jar file

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

