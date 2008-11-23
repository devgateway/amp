var ddmenuitem = 0;
var ddmenuimage = 0;

function mainMenu_open()
{  
	if($(this).get(0) != ddmenuimage)
	{
		mainMenu_close();
		ddmenuitem = $(this).parent().find('ul').css('visibility', 'visible');
		ddmenuimage = $(this).get(0);
	}
	else
	{
		mainMenu_close();
	}
	return false;
}

function mainMenu_close()
{
	if(ddmenuimage)
		ddmenuimage = 0;
	if(ddmenuitem) 
    	ddmenuitem.css('visibility', 'hidden');
}

$(document).ready(
function()
{
	$('#mainMenu > li img').bind('click', mainMenu_open)
//	$('#mainMenu > li ul').
});

document.onclick = mainMenu_close;
