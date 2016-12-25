$(document).ready(function(){
            $("#flip1").click(function(){
                $("#Menu_panel1").slideToggle("slow");
            });

             $("#flip2").click(function(){
                $("#Menu_panel2").slideToggle("slow");
            });
        });






        function changeSon(parent)
        {

            if($("#"+parent).attr("value") == 1 )
            {
                $("#"+parent).animate({top:"100px"},500);
                $("#"+parent).attr("value",0);
            }
            else
            {
                $("#"+parent).animate({top:"680px"},500);
                $("#"+parent).attr("value",1);
            }

        }

        var barstate=false;
        var state=false;
        var Catalogstate=false;

       function changeTIME()
       {
            if(state==true)
            {
                $("#cansetTime").animate({left:"-220px"},500);
                state=false;
            }
            else
            {
                 if(barstate==true)
                 {
                     $("#cansetTime").animate({left: "250px"}, 500);
                     state = true;
                 }
                 else
                 {
                     $("#cansetTime").animate({left: "30px"}, 500);
                     state = true;
                 }
            }
       }

        function changeBar()
        {

             if(barstate==true)
             {
                 $("#leftbar").animate({left:"-220px"},500);
                 barstate=false;



             }
             else
             {
                   $("#leftbar").animate({left:"0px"},500);
                 barstate=true;
             }

             $("#cansetTime").animate({left:"-220px"},500);
             state=false;
        }


function changeCatalog()
{
    if(Catalogstate==true)
    {
        $("#Catalog").animate({right:"-200px"},500);
        Catalogstate=false;
    }
    else
    {
        $("#Catalog").animate({right:"0px"},500);
        Catalogstate=true;
    }
}


