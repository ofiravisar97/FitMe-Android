var sql = require('mysql');
// SQL CONNECTION

var con = sql.createConnection({
host: 'localhost',
user: 'root',
password: '12345678',
database: 'FitMe'
});

con.connect(function(err){
if(err) throw err;
console.log('Connected to sql');
});


// Creating Database
    con.query("CREATE DATABASE IF NOT EXISTS FitMe",function(err, result){
if(err)throw err.message;
console.log('Database FitMe Created');
});

//CREATING USERS TABLE
con.query("CREATE TABLE IF NOT EXISTS users(id INT auto_increment primary key,username VARCHAR(256) not null,pass VARCHAR(256) not null,email VARCHAR(256) not null);",function(err,result){
    if(err) throw err.message;
    console.log('Table Created');
});

con.query("SELECT * FROM users",function(err,results){
if(err) throw err;
console.log(results);
});

module.exports = con;