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

The **trader** microservice provides the UI for the *Stock Trader* sample.  It calls the **portfolio** microservice,
which then calls various other services as needed.  It uses the *mpRestClient* to make the call, and passes a JWT on the
request, which **portfolio** checks for via *mpJwt*.

The main entry point is the **summary** servlet, which lets you choose an operation and a portfolio to act upon.  It
transfers control to other servlets, such as **addPortfolio**, **viewPortfolio**, and **addStock**, each of which
transfers control back to **summary** when done.  The **viewPortfolio** and **addStock** servlets expect a query param
named *owner*.

Each page has a header and footer image, and there's an index.html that redirects to the **summary** servlet.

The servlets just concern themselves with constructing the right **HTML** to return.  The UI is very basic; there
is no use of **JavaScript** or anything fancy.  All of the real logic is in the PortfolioServices.java, which
contains all of the REST calls to the Portfolio microservice, and appropriate JSON wrangling.

You can hit the main entry point by entering a URL such as `http://localhost:9080/trader/summary` in your
browser's address bar.  Or in a Kubernetes environment, you'd replace `localhost` with your proxy node address, and
`9080` with your node port or ingress port.  You also need to use `https` if using the IBMid version.

This is version 1 of the *Stock Trader* UI, implemented in **Java**, and is deliberately simplistic.  See the
**tradr** sibling repository for an alternate, more professional-looking version, implemented in **JavaScript** and **Vue**.
 
 ### Build
To build `trader` clone this repo and run:
```bash
cd stocktrader-jil-v2/src/trader/
mvn package
docker build -t trader:latest -t stocktraders/trader:latest .
docker tag trader:latest stocktraders/trader:latest
docker push stocktraders/trader:latest
```