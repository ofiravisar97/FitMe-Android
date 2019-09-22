// Some Vars

var menuState = false;

// ----- Opening menu

$("#menu-btn").on('click',function(){
    
menuState = !menuState;

if(menuState){
    
    //MENU CONT
    
    $("#menu-cont").css({display: 'block'});
    $("#menu-cont").animate({width: '15%'}, "slow");
    $("#home-img").animate({width: '5rem'}, "slow");
    $('.menu-item').css({padding : '45px'});

}
else {
    
$("#menu-cont").animate({width: '0px'}, "slow");
$("#home-img").animate({width: '0'}, "slow");

setTimeout(terminateMenu,500);
  }

});


function terminateMenu(){
        $("#menu-cont").css({display: 'none'});   
}

