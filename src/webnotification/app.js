/*eslint-env node*/

// This application uses express as its web server
// for more info, see: http://expressjs.com
var express = require('express');

// cfenv provides access to your Cloud Foundry environment
// for more info, see: https://www.npmjs.com/package/cfenv
var cfenv = require('cfenv');
var env = {
  "APP_GUI_ID": process.env.APP_GUI_ID || "77955822-7290-4cd9-b80a-3091b6892fe8",
  "REGION": process.env.REGION || "imfpush.us-east.bluemix.net",
  "CLIENT_SECRET": process.env.CLIENT_SECRET || "b8cce76c-6c8e-47b1-9241-fa71eb5c0fda",
  "APP_SERVER_OR_WEB_PUSH_KEY": process.env.APP_SERVER_OR_WEB_PUSH_KEY || "BP6CiXS9cH2Vo12RP4_xa8Ge0y3It0akqQIlx3mCv3JtjROK7wi460dzylhudCrlSBsVL9B3_YJ-hiYP0yCOwMg"
}
const https = require("https"),
  fs = require("fs");

const options = {
  key: fs.readFileSync("./keys/server.key"),
  cert: fs.readFileSync("./keys/server.crt"),
  // passphrase: 'in3126es'
};
// create a new express server
var app = express();

app.get('/config', function (req, res) {
  return res.json({env: env});
});
// serve the files out of ./public as our main files
app.use(express.static(__dirname + '/public'));
var port = 3001;
app.set('port', port);

var server = https.createServer(options, app);
// get the app environment from Cloud Foundry
// var appEnv = cfenv.getAppEnv();

// start server on the specified port and binding host
server.listen(port);
