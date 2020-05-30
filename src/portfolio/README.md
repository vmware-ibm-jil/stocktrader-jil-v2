<!--
       Copyright 2017 IBM Corp All Rights Reserved

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->

This service manages a *stock portfolio*.  The data is backed by two **DB2** tables, communicated with
via *JDBC*.  The following operations are available:

`GET /` - gets summary data for all portfolios.

`POST /{owner}` - creates a new portfolio for the specified owner.

`GET /{owner}` - gets details for the specified owner.

`PUT /{owner}` - updates the portfolio for the specified owner (by adding a stock).

`DELETE /{owner}` - removes the portfolio for the specified owner.

`GET /{owner}/returns` - gets the return on investment for this portfolio.

`POST /{owner}/feedback` - submits feedback (to the Watson Tone Analyzer)

All operations return *JSON*.  A *portfolio* object contains fields named *owner*, *total*, *loyalty*, *balance*,
*commissions*, *free*, *sentiment*, and *nextCommission*, plus an array of *stocks*.  A *stock* object contains
fields named *symbol*, *shares*, *commission*, *price*, *total*, and *date*.  The only operation that takes any
query params is the `PUT` operation, which expects params named *symbol* and *shares*.  Also, the `feedback`
operation takes a JSON object in the http body, with a single field named *text*.

For example, doing a `PUT http://localhost:9080/portfolio/John?symbol=IBM&shares=123` (against a freshly
created portfolio for *John*) would return *JSON* like `{"owner": "John", "total": 19120.35, "loyalty": "Bronze",
"balance": 40.01, "commissions": 9.99, "free": 0, "sentiment": "Unknown", "nextCommission": 8.99, "stocks":
[{"symbol": "IBM", "shares": 123, "commission": 9.99, "price": 155.45, "total": 19120.35, "date": "2017-06-26"}]}`.

The above REST call would also add a row to the Stocks table via a SQL statement like `INSERT INTO Stock
(owner, symbol, shares, price, total, dateQuoted) VALUES ('John', 'IBM', 123, 155.45, 19120.35, '2017-06-26')`,
and would update the corresponding row in the Portfolio table via a SQL statement like
`UPDATE Portfolio SET total = 19120.35, loyalty = 'Bronze' WHERE owner = 'John'`.

The code should work with any *JDBC* provider.  It has been tested with **DB2** and with **Derby**.  Changing
providers simply means updating the *Dockerfile* to copy the *JDBC* jar file into the Docker image, and updating
the *server.xml* to reference it and specify any database-specific settings.  No *Java* code changes are necessary
when changing *JDBC* providers.  The database can either be another pod in the same *Kubernetes* environment, or
it can be running on "bare metal" in a traditional on-premises environment.
 
 
 ### Build
To build `portfolio` clone this repo and run:
```bash
cd stocktrader-jil-v2/src/portfolio/
mvn package
docker build -t portfolio:latest -t stocktraders/portfolio:latest .
docker tag portfolio:latest stocktraders/portfolio:latest
docker push stocktraders/portfolio:latest
```

### Include below enviroment variables for configuration
| Env Var | Purpose |
|---------|---------|
|`LOYALTY_URL` | The url of the loyalty service, eg. `http://192.168.18.100:31422/DecisionService/rest/v1/ICP_Trader_Dev_1/determineLoyalty` |
|`LOYALTY_ID`| The user id for the loyalty service|
|`LOYALTY_PWD`| The password for the loyalty service|
|`JDBC_HOST` | The host for the jdbc connection |
|`JDBC_PORT` | The port for the jdbc connection | 
|`JDBC_ID` | The userid for the jdbc connection |
|`JDBC_PASSwORD` | The password for the jdbc connection |
|`JDBC_DB` | The database to user with the jdbc connection | 
|`WATSON_APIKEY` (formerly `WATSON_ID` and `WATSON_PWD`)| The api key used to access Watson Tone Analyzer service |
|`WATSON_URL` | Url for Watson Tone Analyzer service |
|`STOCK_QUOTE_URL` | Url for Stock Quote service | 
|`JWT_AUDIENCE` | The expected audience for jwt authentication |
|`JWT_ISSUER` | The expected isser for jwt authentication | 
|`JWT_KEY` (note : *temporary*) | shared key for jwt authentication |
|`MQ_USR` | The rabbitmq user name . eg. `user`|
|`MQ_PWD` | The rabbitmq password . eg. `bitnami`|
|`MQ_VIRTUAL_HOST` | The rabbitmq vertual host . eg.(default) `/`|
|`MQ_HOST_NAME` | The rabbitmq host name or ip address . eg. `http://myrabbitmq.local`|
|`MQ_PORT` | The rabbitmq port . eg. `5672`|
|`MQ_QUEUE` | The rabbitmq queue name . eg. `TESTQ`|


