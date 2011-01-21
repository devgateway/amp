function ActionsMenu(objectId,nameprefix,isTeamMenu){
	this.objectId		= objectId;
	this.nameprefix		= nameprefix;
	
	this.overlay		= null;
	this.visible		= false;
	this.divEl		= null;
	this.teamView=null;
	
	if ( isTeamMenu ) 
		this.ownerType	= "team";
	else
		this.ownerType	= "private";
}

ActionsMenu.prototype.render=function (){
	this.overlay 	= new YAHOO.widget.Overlay(this.nameprefix, { context:[this.objectId,"tl","bl"],
		  visible:false,
		  width:"150px" } );
	var bigDiv		= document.createElement("div");
	var divEl	= document.createElement("div");
	var brEl		= document.createElement("br");
	
	this.divEl	= divEl;
		var divInnerHTML='';
		var retArray	= new Array();
		//div's content
		var ownType=this.ownerType;
		var teamView=this.teamView;
		// add resources link
		var addResLinkEl	= "<a  style=\"cursor:pointer; color: black; font-size: 11px;text-decoration:none;background: none\" >";
		addResLinkEl+=uploadDoc; // we use var instead of digi:trn because ie adds <?xml:namespace prefix=digi/> :(
		addResLinkEl+="</a>";		
		
		var addResdivEl = document.createElement("div");		
		var clickActionsForAddDoc	= function (e, addResdivEl) {
			if(teamView!=null && ! teamView){
				menuPanelForUser.toggleUserView();
			}else{
				menuPanelForTeam.toggleTeamView();
			}
			setType(ownType);
			configPanel(0,'','','', false);
			showMyPanel(0, 'addDocumentDiv');
		};
		createActionDiv(addResdivEl,addResLinkEl,clickActionsForAddDoc);
		divEl.appendChild(addResdivEl);
		
		
		//add web url link
		var addUrlLinkEl	= "<a style=\"cursor:pointer; color: black; font-size: 11px;text-decoration:none;background: none\" >";		
		addUrlLinkEl+=addWebLink;
		addUrlLinkEl+="</a>";
		
		var addURLdivEl = document.createElement("div");
		var clickActionsForAddLink = function (e, addURLdivEl) {
			if(teamView!=null && ! teamView){
				menuPanelForUser.toggleUserView();
			}else{
				menuPanelForTeam.toggleTeamView();
			}
			setType(ownType);
			configPanel(0,'','','', '',true);
			showMyPanel(0, 'addDocumentDiv');
		};
		createActionDiv(addURLdivEl,addUrlLinkEl,clickActionsForAddLink);
		divEl.appendChild(addURLdivEl);
		
		
		//if(this.teamView!=null && ! this.teamView){
			//create from template link
			var createFromTemplateLinkEl="<a style=\"cursor:pointer; color: black; font-size: 11px;text-decoration:none;background: none\">";
			createFromTemplateLinkEl+=createFromTemplate;
			createFromTemplateLinkEl+="</a>";
			var createFromTempldivEl	= document.createElement("div");
			var clickActionsForTemplate = function (e, createFromTempldivEl) {
				if(teamView!=null && ! teamView){
					menuPanelForUser.toggleUserView();
				}else{
					menuPanelForTeam.toggleTeamView();
				}
				addFromTemplate(ownType);
			};
			createActionDiv(createFromTempldivEl,createFromTemplateLinkEl,clickActionsForTemplate);
			divEl.appendChild(createFromTempldivEl);
		//}
	
	divEl.style.border	= "1px solid gray";
	divEl.style.backgroundColor="white";
	
	
	
	//bigDiv.style.border	= "1px solid gray";
	bigDiv.appendChild(divEl);
	bigDiv.appendChild(brEl);
	
	this.overlay.setBody(bigDiv);
	this.overlay.render(document.body);
}

ActionsMenu.prototype.toggleUserView	= function() {	
	this.teamView=false;
	this.showOrHideMenu();
}

ActionsMenu.prototype.toggleTeamView	= function() {
	this.teamView=true;	
	this.showOrHideMenu();
}

ActionsMenu.prototype.hide	= function () {
	this.overlay.hide();
	this.divEl.innerHTML	= "";
}

ActionsMenu.prototype.show	= function () {	
	this.render();
	this.overlay.show();
	this.divEl.focus();
}

ActionsMenu.prototype.showOrHideMenu = function (){
	if ( this.visible ) {
		this.hide();
		this.visible	= false;
	}
	else {
		this.show();
		this.visible	= true;
	}
	return;
}

function createActionDiv (divEl,divInnerHTML,clickActionsForDiv){
	
	divEl.innerHTML=divInnerHTML;
	
	var mouseoverCallbackObj	= function (e, divEl) {divEl.style.backgroundColor="#ECF3FD";};
	var mouseoutCallbackObj	= function (e, divEl) {divEl.style.backgroundColor="white";};	
	
	
	YAHOO.util.Event.addListener(divEl, "mouseover", mouseoverCallbackObj, divEl, false);
	YAHOO.util.Event.addListener(divEl, "mouseout", mouseoutCallbackObj, divEl, false);
	YAHOO.util.Event.addListener(divEl, "click", clickActionsForDiv, divEl, false);
	return divEl;
}