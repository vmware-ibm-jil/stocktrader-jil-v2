<!--
       Copyright 2018 IBM Corp All Rights Reserved

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

[Portfolio]: <https://github.com/vindby23/stocktrader-jil-v2/tree/master/src/portfolio>
[traditional Trader]: <https://github.com/vindby23/stocktrader-jil-v2/tree/master/src/trader>

# tradr

This is a new Node.js-hosted and Vue.js-based user interface for StockTrader. It calls the default `portfolio` 
JAX-RS web services when interacting with stock portfolios. As such `portfolio` must be exposed via ingress for `tradr`
to work properly.
 
**Note:** There is a bug where the UI does not function if the portfolio database is empty.
 
## Prerequisites
- openshift/Minikube or any server with kubernet and docker pre-installed.
- [Portfolio] and [traditional Trader] application need to be up and running
- ready **TRADER_HOST**, **PORTFOLIO_HOST** and **INGRESS_HOST** variable.

## Installation
setup TRADER_HOST, PORTFOLIO_HOST, INGRESS_HOST in manifests/install.sh file.
```sh
cd manifests
```
```sh
./install.sh
```
## Usage
- open https://ip:31002

## Run the app locally
1. [Install Node.js][]
2. Download and extract the starter code from the UI
3. cd into the app directory
4. Run `npm install` to install the app's dependencies
5. update devServer in vue.config.js by uncomment proxy settings.
5. Run `npm start` to start the app
6. Access the running app in a browser at http://localhost:3000

[Install Node.js]: https://nodejs.org/en/download/
