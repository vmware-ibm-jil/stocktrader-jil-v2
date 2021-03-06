# Create mongodb  secret
kubectl create secret generic mongodb --from-literal=user=root --from-literal=password=secretpassword --from-literal=host=172.17.76.32 --from-literal=port=32008 --from-literal=database=stocktrader --from-literal=authenticationdb=admin

kubectl delete -f deployment.yml
kubectl delete -f service.yml
kubectl delete -f ingress.yml

# Apply Manifest Configurations 
kubectl apply -f deployment.yml
kubectl apply -f service.yml
kubectl apply -f ingress.yml
