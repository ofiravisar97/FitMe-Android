
function bmrWomen(weight,height,age){
    var bmr = 655 + (9.6 * weight) + (1.8 * height) - (4.7 * age); 
    return bmr;
}

function bmrMan(weight,height,age){
    var bmr = 66 + (13.7 * weight) + (5 * height) - (6.8 * age);
    return bmr
}

function login(){
    
var user = document.getElementById("user").value;

var pass = document.getElementById('pass').value;

var data = JSON.stringify({username: user,
password: pass});

console.log(data);
var xhr = new XMLHttpRequest();

xhr.open("POST", "/login", true);
    
xhr.setRequestHeader("Content-type","application/json");

xhr.send(data);
if(xhr.status == 200){
console.log("Hello");
}
}