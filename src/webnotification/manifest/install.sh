APPGUIID=34098203948-7290-4cd9-b80a-3091b6892fe8
CLIENTSECRET=565gth8ju_c9OkJJ6NfHDjTXxrYWcUUph86mwOLIZXyGlY2aA
REGION=imfpush.us-east.bluemix.net
TAG=NOTIFICAtions-WB
SERVERWEBKEY=FBP6CiXS9cH2V2o12RP4_xa8Ge0yw3It04akqQIlx3mCv3JtjROK7wi460d1zylshudCrlSBsVL9B3_YJ-hiYP0yCOwMg

# Create IBM push secret
kubectl create secret generic ibmpushnotification --from-literal=appguiid=$APPGUIID --from-literal=clientsecret=$CLIENTSECRET --from-literal=region=$REGION --from-literal=tag= --from-literal=serverwebkey=$SERVERWEBKEY

# Apply Manifest Configurations 
kubectl apply -f deployment.yml
kubectl apply -f service.yml
kubectl apply -f Ingress.yml
