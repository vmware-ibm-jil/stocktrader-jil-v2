# StockTraderJilv2 Installation:

### Deployment of IBM DB2 :

- Provision one ubuntu-16.04 DB2 VM2 
- Install Docker on Ubuntu OS [link](https://docs.docker.com/engine/install/ubuntu/)
- Run the following command to setup the containerized DB2:
    ```bash
    $ sudo docker pull stocktradersjilv2/st-db2
    $ sudo docker run -itd --name mydb2 --privileged=true -p 50000:50000 -e LICENSE=accept -e DB2INST1_PASSWORD=db2inst1 -e DBNAME=STOCKTRD -v /data:/database --restart unless-stopped stocktraders/st-db2
    $ sudo docker exec -ti mydb2 bash -c "su db2inst1"
    ```
- Verify the Image:
    ```bash
    
    $ sudo docker exec -i -t mydb2 /bin/bash
    $ su db2inst1
    $ db2 connect to STOCKTRD user db2inst1 using db2inst1
    ```
    -   Check the DB2 access from VM1 - Telnet port 50000
    -   Also can check the JDBC connection through eclipse plugin or Java program

### Deployment of Stock Trader application components:

- Prerequisite: DB2 VM is up and accessible
- Provision Ubuntu 16.04 VM
- Install Docker Engine [link](https://docs.docker.com/engine/install/ubuntu/)
- Install Docker compose [link](https://docs.docker.com/compose/install/)
    ```bash
    sudo curl -L "https://github.com/docker/compose/releases/download/1.26.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
    #verify using the below command
    docker-compose --version
    ```
- Clone the repo [https://github.com/vmware-ibm-jil/stocktrader-jil-v2](https://github.com/vmware-ibm-jil/stocktrader-jil-v2)
- Go to directory cd stocktrader-jil-v2\ installation
- Execute the below Command to pull the required images and then start service deployments
    ```bash
    COMPOSE_HTTP_TIMEOUT=300 docker-compose up -d
    ```
- You can check the logs of the services with below command using service name for(odm/trader/redis/stock-quote/portfolio)
    ```bash
    docker-compose logs | grep –I ‘<Service-Name>’
    ```
- Check the docker container status with “docker ps”
- Now once all the services are up and running –Import and deploy  stocktrader-jil-v2/src/portfolio/stock-trader-loyalty-decision-service.zip on ODM `http://<Ubuntu-VM-IP>:9060` (odmAdmin/odmAdmin)
- Now you can login to Trader UI with link `https://<Ubuntu-VM-IP>:9443/trader/login` (admin/admin)

### Deploy notification service in OCP:
- Clone the repo [https://github.com/vmware-ibm-jil/stocktrader-jil-v2](https://github.com/vmware-ibm-jil/stocktrader-jil-v2)
- Go to directory cd stocktrader-jil-v2\src\notification\manifest
- Create secrets for RabbitMQ and IBM Cloud Push services details:
    ```bash
    kubectl create secret generic rbq --from-literal=user=admin --from-literal=password=secretpassword --from-literal=vhost=/ --from-literal=host=172.17.76.32 --from-literal=port=32004 --from-literal=queue=stocktrader 

    kubectl create secret generic ibmcloudpush --from-literal=tenenatid=77955822-7290-4cd9-b80a-3091b6892fee --from-literal=apikey=52XE_c9OkJJ6NfHDjTXxrYWcUUph86mwOLIZXyGlY2aq  --from-literal=region=.us-east.bluemix.net --from-literal=tag=STOCKTRADERS --from-literal=alertmsgurl=www.ibm.com
    ```
- Create or use the  project exits - oc new-project stocktrader or oc project stocktrader
- Apply the manifest file kubectl apply -f deployment.yml

### Steps to install RabbitMQ using helm:
- Use curl command as mentioned below to create a get_helm.sh file to install helm.
    ```bash
    curl https://raw.githubusercontent.com/kubernetes/helm/master/scripts/get-helm-3 > get_helm.sh"statement
    ```
- Get read, write and execute permission for the file "get_helm.sh" using the following command
    ```bash
    chmod 700 get_helm.sh
    ```  
- Execute the get_helm.sh file to install helm "./get_helm.sh"
- Once helm is installed, add bitnami repo to your setup using the following command
    ```bash
    helm repo add bitnami https://charts.bitnami.com/bitnami
    ``` 
- Now use the following command to install rabbitMQ using helm.
    ```bash
    helm install my-release --set rabbitmq.username=admin,rabbitmq.password=secretpassword,persistence.enabled=false,service.nodePort=32004,service.nodeTlsPort=32005,service.type=NodePort bitnami/rabbitmq
    ``` 
    or  (If the statefulset-controller gives error, we can disable it)
    ```bash
    helm install my-release --set rabbitmq.username=admin,rabbitmq.password=secretpassword,persistence.enabled=false,service.nodePort=32004,service.nodeTlsPort=32005,service.type=NodePort,securityContext.enabled=false bitnami/rabbitmq --namespace stocktrader
    ```
##### Below are some important commands:
- To obtain the NodePort IP and Ports:
    ```bash
    A. export NODE_IP=$(kubectl get nodes --namespace default -o jsonpath="{.items[0].status.addresses[0].address}") 
    B. export NODE_PORT_AMQP=$(kubectl get --namespace default -o jsonpath="{.spec.ports[1].nodePort}" services my-release-rabbitmq)
    C. export NODE_PORT_STATS=$(kubectl get --namespace default -o jsonpath="{.spec.ports[3].nodePort}" services my-release-rabbitmq)
    ```
- To Access the RabbitMQ AMQP port:
    ```bash
    echo "URL : amqp://$NODE_IP:$NODE_PORT_AMQP/"
    ```
- To Access the RabbitMQ Management interface:
    ```bash
    echo "URL : http://$NODE_IP:$NODE_PORT_STATS/"
    ```

### Steps to install Install MongoDB using helm:
##### If helm is not yet installed, use the following steps to install helm:
- Use curl command as mentioned below to create a get_helm.sh file to install helm.
    ```bash
    curl https://raw.githubusercontent.com/kubernetes/helm/master/scripts/get-helm-3 > get_helm.sh"statement
    ```
- Get read, write and execute permission for the file "get_helm.sh" using the following command
    ```bash
    chmod 700 get_helm.sh
    ```  
- Execute the get_helm.sh file to install helm "./get_helm.sh"
- Once helm is installed, add bitnami repo to your setup using the following command
    ```bash
    helm repo add bitnami https://charts.bitnami.com/bitnami
    ``` 
##### If helm is installed then run the following command to install mongoDB:

```bash
helm install my-mongodb --set mongodbRootPassword=secretpassword,mongodbUsername=my-user,mongodbPassword=my-password,mongodbDatabase=my-database,service.nodePort=32008,service.type=NodePort bitnami/mongodb
``` 

### Deploy Statement service in OCP
- Clone the repo [https://github.com/vmware-ibm-jil/stocktrader-jil-v2](https://github.com/vmware-ibm-jil/stocktrader-jil-v2)
- Go to directory cd stocktrader-jil-v2\src\statement\manifest
- Create secrets for MongoDB and Host details:
    ```bash
    kubectl create secret generic mongodb --from-literal=user=admin --from-literal=password=secretpassword	--from-literal=host=172.17.76.32 --from-literal=port=32008 --from-literal=database=stocktrader --from-		literal=authenticationdb=admin
    ``` 
- Apply the manifest files :
    ```bash
    kubectl apply -f deployment.yml
    kubectl apply -f service.yml
    kubectl apply -f ingress.yml
    ``` 

### Deploy Web notification in ocp:
- Clone the repo [https://github.com/vmware-ibm-jil/stocktrader-jil-v2](https://github.com/vmware-ibm-jil/stocktrader-jil-v2)
- Go to directory cd stocktrader-jil-v2\src\webnotification\manifest
- Get the value of APPGUIID, CLIENTSECRET, REGION, TAG, SERVERWEBKEY
- Create secret using following command
    ```bash
    kubectl create secret generic ibmpushnotification --from-literal=appguiid=$APPGUIID --from-literal=clientsecret=$CLIENTSECRET --from-literal=region=$REGION --from-literal=tag= --from-literal=serverwebkey=$SERVERWEBKEY
    ``` 
- Apply the manifest files:
    ```bash
    kubectl apply -f deployment.yml
    kubectl apply -f service.yml
    kubectl apply -f Ingress.yml
    ``` 
### Deploy new Tradr in ocp:
- Clone the repo [https://github.com/vmware-ibm-jil/stocktrader-jil-v2](https://github.com/vmware-ibm-jil/stocktrader-jil-v2)
- Go to directory cd stocktrader-jil-v2\src\tradr\manifests
- Get the value of AUDIENCE, ISSUER, TRADER_HOST, PORTFOLIO_HOST, STATEMENT_HOST, INGRESS_HOST.
- Create secret using following commands
    ```bash
    # add new secrets
    kubectl create secret generic trader-host --from-literal=host=$TRADER_HOST
    kubectl create secret generic portfolio-host --from-literal=host=$PORTFOLIO_HOST
    kubectl create secret generic ingress-host --from-literal=host=$INGRESS_HOST
    kubectl create secret generic statement-host --from-literal=host=$STATEMENT_HOST
    kubectl create secret generic jwt --from-literal=audience=$AUDIENCE --from-literal=issuer=$ISSUER
    ``` 
- Apply the manifest files:
    ```bash
    # Deploy the mainifests
    kubectl apply -f deployment.yml
    kubectl apply -f autoscaler.yml
    kubectl apply -f service.yml
    kubectl apply -f Ingress.yml
    ```
### Migration guidelines to cloud

[above]: <https://github.com/vmware-ibm-jil/stocktrader-jil-v2#deployment-of-stock-trader-application-components>

1. Static ip changes (if you does not see new ip for the VM)
2. need to update docker-compose for configuration related to new IP
3. go to installation directory.
```sh
docker-compose down
```
```sh
docker-compose up -d 
```
4. wait until all container start, follow logs for any exceptions. docker-compose logs -f
5. Import and deploy stocktrader-jil-v2/src/portfolio/stock-trader-loyalty-decision-service.zip on ODM (follow step [above])
6. enter https://<Ubuntu-VM-IP>:9443/trader/login (follow step [above])
