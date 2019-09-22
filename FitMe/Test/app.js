const express = require('express');
const auth = require('./Auth.js');
const path = require('path');
const bodyParser = require('body-parser');
const app = express();

app.use(bodyParser.urlencoded({extended:false}));
app.use(bodyParser.json());



// Controlling Authenticate

app.post('/login', auth);




app.use(express.static(__dirname + '/Public'));

app.get('/',function(req,res){
res.sendFile(path.join(__dirname+'/Public/index.html')); 
});

app.listen(8080, () => console.log("listening on port 3000..."));