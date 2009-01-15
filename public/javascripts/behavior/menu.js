function hasC(e,c) {
	return (e.className && e.className.indexOf(c)>-1);
};

function addC(e,c) {
	if (!hasC(e,c)){
		if (e.className) e.className += " " + c;
		else e.className = c;
	}
};

function remC(e,c) {
	if (hasC(e,c)) {
		var r = new RegExp("(^|\\s)" + c + "(\\s|$)");
		e.className = e.className.replace(r, "$2");
	}
};

function tm(el) 
{
    var p=el.parentNode;
    var d=document;
    function cl() {remC(p,'selected');d.onclick=null;}
    function sh() {d.onclick=cl;}
    if(hasC(p,'selected')) cl();
    else{
        addC(p,'selected');
        calculateWidth(p); //To have a cross browser way to fix widths of the submenu
        setTimeout(sh, 0);
        try {
            var u=(p.getElementsByTagName("UL")[1]), c=u.childNodes, w=u.clientWidth, e=u, x=0;  
        }
        catch (ig){}
    }
    return false;
}
function calculateWidth(p){
    var blockWidth = p.clientWidth;
    //get all children for this menu to get the widest
    var liArray = p.getElementsByTagName("LI")
    for(idx = 0; idx < liArray.length; idx++){
        if(liArray[idx].clientWidth > blockWidth){
            blockWidth = liArray[idx].clientWidth;
        }
    }
    if(p.clientWidth < blockWidth) p.getElementsByTagName("UL")[0].style.width = blockWidth;
    //Reset the widths to THAT!
    for(idx = 0; idx < liArray.length; idx++){
        if(liArray[idx].clientWidth < blockWidth){
            liArray[idx].style.width = blockWidth + "px";
        }
    }
}
