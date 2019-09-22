var con = require('./Connection');
var path = require('path');


var login = (function(req, res){
    
var username = req.body.username;
var pass = req.body.password;



con.query("SELECT * FROM users WHERE username=?", username,function(err,results,fields){
    if(err) throw err;
    
    var user = results[0];
    
    if(user.pass == pass){
 res.sendFile(path.join(__dirname+'/Public/main.html')); 
    }
})});



module.exports = login;