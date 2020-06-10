/*eslint-env node*/

// This application uses express as its web server
// for more info, see: http://expressjs.com
var express = require('express');

// cfenv provides access to your Cloud Foundry environment
// for more info, see: https://www.npmjs.com/package/cfenv
var cfenv = require('cfenv');
const https = require("https"),
    fs = require("fs");

const options = {
    key: fs.readFileSync("./keys/server.key"),
    cert: fs.readFileSync("./keys/server.crt"),
    // passphrase: 'in3126es'
};
// create a new express server
var app = express();

// serve the files out of ./public as our main files
app.use(express.static(__dirname + '/public'));
var port = 3001;
app.set('port', port);

var server = https.createServer(options, app);
// get the app environment from Cloud Foundry
// var appEnv = cfenv.getAppEnv();

// start server on the specified port and binding host
server.listen(port);
