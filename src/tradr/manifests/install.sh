AUDIENCE=stock-trader
ISSUER=http://stock-trader.ibm.com
TRADER_HOST=https://172.17.76.29:9443
PORTFOLIO_HOST=https://172.17.76.29:9442
STATEMENT_HOST=https://172.17.76.32:31010
INGRESS_HOST=https://172.17.76.32:3000   # tradr application host or localhost

# add new secrets
kubectl create secret generic trader-host --from-literal=host=$TRADER_HOST
kubectl create secret generic portfolio-host --from-literal=host=$PORTFOLIO_HOST
kubectl create secret generic ingress-host --from-literal=host=$INGRESS_HOST
kubectl create secret generic jwt --from-literal=audience=$AUDIENCE --from-literal=issuer=$ISSUER
# Deploy the mainifests

kubectl apply -f deployment.yml
kubectl apply -f autoscaler.yml
kubectl apply -f service.yml
kubectl apply -f Ingress.yml
