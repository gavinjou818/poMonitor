var Catalogstate=false;
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


