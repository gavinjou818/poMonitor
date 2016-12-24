$(document).ready(function(){
            $("#flip1").click(function(){
                $("#Menu_panel1").slideToggle("slow");
            });

             $("#flip2").click(function(){
                $("#Menu_panel2").slideToggle("slow");
            });
        });

        var state=true;

        function changeTIME()
        {
            if(state==true)
            {
                $("#cansetTime").animate({left:"0px"},500);
                state=false;
            }
            else
            {
                $("#cansetTime").animate({left:"250px"},500);
                state=true;
            }
 }