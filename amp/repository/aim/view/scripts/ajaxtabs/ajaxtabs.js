var bustcachevar = 1; //bust potential caching of external pages after initial request? (1=yes, 0=no)
var loadstatustext = "<div align='center' style='font-size: 11px;margin-top:260px;'><img src='/TEMPLATE/ampTemplate/img_2/ajax-loader.gif'/><p>Requesting content...</p></div>";
var loadedobjects="";
var defaultcontentarray=new Object();
var bustcacheparameter="";
var tab_loading = false;


function ajaxpage(url, containerid, targetobj)
{
	//console.log('ajaxpage called for id ' + containerid + ', url:' + url);
	var page_request = false;
	if (window.XMLHttpRequest) // if Mozilla, Safari etc
		page_request = new XMLHttpRequest();
	else if (window.ActiveXObject){ // if IE
			try {
				page_request = new ActiveXObject("Msxml2.XMLHTTP");
				} 
			catch (e)
			{
				try{
					page_request = new ActiveXObject("Microsoft.XMLHTTP");
				}
				catch (e){}
			}
		}
	else
		return false;
	
	var ullist=targetobj.parentNode.parentNode.getElementsByTagName("li");
	for (var i=0; i<ullist.length; i++)
		ullist[i].className = "";  //deselect all tabs
	
	targetobj.parentNode.className = "selected";  //highlight currently clicked on tab
	if (url.indexOf("#default")!=-1){ //if simply show default content within container (verus fetch it via ajax)
		document.getElementById(containerid).innerHTML=defaultcontentarray[containerid];
		return
	};
	document.getElementById(containerid).innerHTML = loadstatustext;
	//('#' + containerid).html(loadstatustext);
	page_request.onreadystatechange=function()
	{
		loadpage(page_request, containerid);
	};
	if (bustcachevar) //if bust caching of external page
		bustcacheparameter = (url.indexOf("?")!=-1)? "&"+new Date().getTime() : "?"+new Date().getTime();
	page_request.open('GET', url+bustcacheparameter, true);
	page_request.send(null);
}

function loadpage(page_request, containerid)
{
	//console.log('loadpage called for id:' + containerid + ", readyState = " + page_request.readyState);
	if (page_request.readyState == 4)
		tab_loading = false;
	if (page_request.readyState == 4 && (page_request.status==200 || window.location.href.indexOf("http")==-1))
	{
		//console.log('entered the IF');
		//$('#' + containerid).html(page_request.responseText);
		document.getElementById(containerid).innerHTML = page_request.responseText;
		/* CONSTANTIN: the reason why I put back raw javascript instead of jquery(foo).html() is that the jQuery text is:
		1. barely working
		2. slower
		3. makes popins NOT WORK
		
		Please refer to https://jira.dgfoundation.org/browse/AMP-17685 and https://jira.dgfoundation.org/browse/AMP-17683 for further reference
		*/
		try
		{	

//	 		TODO-Constantin: investigate ways of making it faster on huge tables
	  		var reporTable=new scrollableTable("reportTable",null);
			reporTable.debug=false;
			reporTable.usePercentage=false;
			reporTable.maxRowDepth=4;
			reporTable.useFixForDisplayNoneRows=true;
			reporTable.scroll();

			continueExecution = false;
		}catch(e)
		{
			//alert(e);
		}
		//	Remove yellow shadow from trails cells


		/**
		 	code below is roughly equivalent with:
		 	=====
		    $("#reportTable .row_data_header td :first-child").each(function(){
				$(this).removeClass("desktop_project_count_sel");
				$(this).addClass("desktop_project_count");			
			});
			=====
			Had to resort to manual iteration because I am not aware of any way to say "td which is second or later child"
		*/

		var firstContentCellIndex = 2;
		$("#reportTable .row_data_header").each(function()
		{
			for(var x=firstContentCellIndex ; x < this.children.length; x++)
			{
				var j = this.children[x].children[0];
				$(j).removeClass("desktop_project_count_sel");
				$(j).addClass("desktop_project_count");
			}
		});
		
 		tb = document.getElementById('reportTable'); // DO NOT COMMENT THIS OUT, AS IT IS A GLOBAL VAR USED IN OTHER PARTS (stupid design btw) 		
	}
}


function loadobjs(revattribute)
{
	if (revattribute!=null && revattribute!="")
	{ //if "rev" attribute is defined (load external .js or .css files)
		var objectlist = revattribute.split(/\s*,\s*/); // split the files and store as array
		for (var i = 0; i < objectlist.length; i++)
		{
			var file = objectlist[i];
			var fileref = "";
			if (loadedobjects.indexOf(file)==-1){ //Check to see if this object has not already been added to page before proceeding
				if (file.indexOf(".js")!=-1){ //If object is a js file
					fileref=document.createElement('script');
					fileref.setAttribute("type","text/javascript");
					fileref.setAttribute("src", file);
				}
				else if (file.indexOf(".css")!=-1){ //If object is a css file
					fileref=document.createElement("link");
					fileref.setAttribute("rel", "stylesheet");
					fileref.setAttribute("type", "text/css");
					fileref.setAttribute("href", file);
				}
			}
			if (fileref != "")
			{
				document.getElementsByTagName("head").item(0).appendChild(fileref);
				loadedobjects += file + " "; //Remember this object as being already added to page
			}
		}
	}
}


//added by SRK@DGF
function changeTabUrl(tabcontentid,tabid,url) {
	//debugger;
	var thetab=document.getElementById(tabid);
	if(thetab)
		{
	thetab.href=url;
	if (thetab.getAttribute("rel")){
		ajaxpage(thetab.getAttribute("href"), thetab.getAttribute("rel"), thetab);
		loadobjs(thetab.getAttribute("rev"));
	}
	}
}

function reloadTab(tabcontentid,tabid) {
var thetab=document.getElementById(tabid);
if (thetab!=null)
	if (thetab.getAttribute("rel")){
		ajaxpage(thetab.getAttribute("href"), thetab.getAttribute("rel"), thetab);
		tab_loading = true;
		loadobjs(thetab.getAttribute("rev"));
	}
}

function expandtab(tabcontentid, tabnumber) //interface for selecting a tab (plus expand corresponding content)
{ 
	var thetab=document.getElementById(tabcontentid).getElementsByTagName("a")[tabnumber];
	if (thetab.getAttribute("rel")){
		ajaxpage(thetab.getAttribute("href"), thetab.getAttribute("rel"), thetab);
		loadobjs(thetab.getAttribute("rev"));
	}
}

function savedefaultcontent(contentid) // save default ajax tab content
{
	if (typeof defaultcontentarray[contentid]=="undefined") //if default content hasn't already been saved
		defaultcontentarray[contentid]=document.getElementById(contentid).innerHTML;
}

function startajaxtabs()
{
	for (var i=0; i<arguments.length; i++){ //loop through passed UL ids
		var ulobj=document.getElementById(arguments[i]);
		var ulist=ulobj.getElementsByTagName("li"); //array containing the LI elements within UL
		for (var x=0; x<ulist.length; x++){ //loop through each LI element
			var ulistlink = ulist[x].getElementsByTagName("a")[0];
			var modifiedurl;
			if (ulistlink.getAttribute("rel")){
				if(window.location.port=="80"||window.location.port=="")
					modifiedurl=ulistlink.getAttribute("href").replace(/^http:\/\/[^\/]+\//i, "http://"+window.location.hostname+"/");
				else
					modifiedurl=ulistlink.getAttribute("href").replace(/^http:\/\/[^\/]+\//i, "http://"+window.location.hostname+":"+window.location.port+"/");
				ulistlink.setAttribute("href", modifiedurl); //replace URL's root domain with dynamic root domain, for ajax security sake
				savedefaultcontent(ulistlink.getAttribute("rel")); //save default ajax tab content
				ulistlink.onclick=function(){
					startProgressCheck();
					ajaxpage(this.getAttribute("href"), this.getAttribute("rel"), this);
					loadobjs(this.getAttribute("rev"));
					return false;
				};
				if (ulist[x].className=="selected"){
					startProgressCheck();
					ajaxpage(ulistlink.getAttribute("href"), ulistlink.getAttribute("rel"), ulistlink); //auto load currenly selected tab content
					loadobjs(ulistlink.getAttribute("rev")); //auto load any accompanying .js and .css files
				}
			}
		}
	}
}

function startProgressCheck(){
	try
	{
		continueExecution = true;
		checkstatus();
	}
	catch(e)
	{
		//Fail silently
	}
}

