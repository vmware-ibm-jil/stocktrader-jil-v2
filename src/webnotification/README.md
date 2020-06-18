
[Tradr]: <https://github.com/vindby23/stocktrader-jil-v2/tree/master/src/tradr>
[IBM cloud]: <https://cloud.ibm.com>
[IBM cloud push]: <https://cloud.ibm.com/docs/mobilepush?topic=mobilepush-getting-started>
[SDK]: <https://github.com/ibm-bluemix-mobile-services/bms-clientsdk-javascript-webpush>
[firebase]: <https://console.firebase.google.com/>
[serviceWorker]: <https://developer.mozilla.org/en-US/docs/Web/API/ServiceWorker>
[PushManager]: <https://developer.mozilla.org/en-US/docs/Web/API/PushManager>
[push api]: <https://developer.mozilla.org/en-US/docs/Web/API/Push_API>

# IBM Cloud Push Web Notifications Node.js overview

This Node.js web application based on the Express framework using IBM cloud push SDK to send notification
to chrome and firefox.

## Prerequisites
1. openshift/Minikube or any server with kubernet and docker pre-installed.
1. New [Tradr] application needs to up and running.
1. Register to [IBM cloud] and select appropriate account type
	1. create **tag**, **service credentials** and **configure services** for firefox/chrome/safari
	1. [IBM cloud push] getting started.
	For more information configuring [SDK]
1. Open google [firebase] account for the VAPID.
	1. Go to cloud messaging settings and get server/web push key and sender ID.
1. SDK require initialization params which you can get from the IBM cloud services as follows
	1. appGUID
	`# Example: 2334544-7290-4cd9-b80a-3091b6892fe8`
	
	1. appRegion
	`# Example: imfpush.us-east.bluemix.net`
	
	1. clientSecret
	`# Example: 34cd334-6c8e-47b1-9241-fa71eb5c0fda`
	
	1. applicationServerKey (Either web push key or server key)
	`# Example: FBP6CiXS9cH2V2o12RP4_xa8Ge0yw3It04akqQIlx3mCv3JtjROK7wi460d1zylshudCrlSBsVL9B3_YJ-hiYP0yCOwMg`

## Assumptions
- Installation on https certificate enabled server. (Service worker supports https)
- [PushManager] is enabled
- [serviceWorker] is enabled
- Firefox version equal or greater than 70+
- Service workers (and Push) have been disabled in the Firefox 45, 52, 60, and 68 Extended Support Releases (ESR.) as per [push api]

## Installation
update install.sh file to configure params variables.
```sh
cd manifest
```
```sh
./install.sh
```

## Usage
Web notification to inform subscribed user about the operation in new tradr application
- open https://ip:31003
- Two button name subscribe and unsubscribe provided to web push notification.
- Click on subscribe open browser based permission pop-up. (Allow it)
- Application will retrieve all tags and register then subscribe.
- open new tradr application and perform add/delete client
- notification will be visible on bottom right, check in applicaiton main page too if not visible.

## Run the app locally

1. [Install Node.js]
2. Download and extract the starter code from the UI
3. cd into the app directory
4. Run `npm install` to install the app's dependencies
5. Run `npm start` to start the app
6. Access the running app in a browser at http://localhost:3001

[Install Node.js]: https://nodejs.org/en/download/
