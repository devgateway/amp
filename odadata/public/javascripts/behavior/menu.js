var timeout    = 500;
var closetimer = 0;
var ddmenuitem = 0;

function mainMenu_open()
{  
	mainMenu_canceltimer();
	mainMenu_close();
	ddmenuitem = $(this).find('ul').css('visibility', 'visible');
}

function mainMenu_close()
{
	if(ddmenuitem) 
    	ddmenuitem.css('visibility', 'hidden');
}

function mainMenu_timer()
{
	closetimer = window.setTimeout(mainMenu_close, timeout);
}

function mainMenu_canceltimer()
{  
	if(closetimer)
	{
    	window.clearTimeout(closetimer);
		closetimer = null;
    }
}

$(document).ready(
function()
{
	$('#mainMenu > li').bind('mouseover', mainMenu_open)
	$('#mainMenu > li').bind('mouseout',  mainMenu_timer)
});

document.onclick = mainMenu_close;
