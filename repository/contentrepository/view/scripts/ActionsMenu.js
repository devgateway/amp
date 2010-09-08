function ActionsMenu(objectId,nameprefix){
	this.objectId		= objectId;
	this.nameprefix		= nameprefix;
	
	this.overlay		= null;
	this.visible		= false;
	this.divEl		= null;
	this.teamView=null;
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
		// add resources link
		var addResLinkEl	= "<a href=\"javascript:setType('private');javascript:configPanel(0,'','','', '',false); javascript:showMyPanel(0, 'addDocumentDiv');\" style=\"cursor:pointer; color: black; font-size: 11px;text-decoration:none;background: none\" ";
		if(this.teamView!=null && ! this.teamView){
			addResLinkEl+="onclick=\"menuPanelForUser.toggleUserView(); \">";
		}else{
			addResLinkEl+="onclick=\"menuPanelForTeam.toggleTeamView(); \">";
		}
		addResLinkEl+="<digi:trn jsFriendly='true'>Upload doc</digi:trn>";
		addResLinkEl+="</a>";	
		var addResdivEl		= createActionDiv(addResLinkEl);
		divEl.appendChild(addResdivEl);
		//add web url link
		var addUrlLinkEl	= "<a href=\"javascript:setType('private');javascript:configPanel(0,'','','', '',true); javascript:showMyPanel(0, 'addDocumentDiv');\" style=\"cursor:pointer; color: black; font-size: 11px;text-decoration:none;background: none\" ";
		if(this.teamView!=null && ! this.teamView){
			addUrlLinkEl+="onclick=\"menuPanelForUser.toggleUserView(); \">";
		}else{
			addUrlLinkEl+="onclick=\"menuPanelForTeam.toggleTeamView(); \">";
		}
		addUrlLinkEl+="<digi:trn jsFriendly='true'>Add Web Link</digi:trn>";
		addUrlLinkEl+="</a>";	
		var addURLdivEl		= createActionDiv(addUrlLinkEl);
		divEl.appendChild(addURLdivEl);
		
		
		if(this.teamView!=null && ! this.teamView){
			var hasCreateDocFromTemplateRights = document.getElementById("hasCreateDocFromTemplateRights");
			if(hasCreateDocFromTemplateRights!=null && hasCreateDocFromTemplateRights.value=='true'){
				//create from template link
				var createFromTemplateLinkEl="<a href=\"javascript:addFromTemplate()\" style=\"cursor:pointer; color: black; font-size: 11px;text-decoration:none;background: none\" onclick=\"menuPanelForUser.toggleUserView();\">";
				createFromTemplateLinkEl+="<digi:trn jsFriendly='true'>Create From Template</digi:trn>";
				createFromTemplateLinkEl+="</a>";
				var createFromTempldivEl	= createActionDiv(createFromTemplateLinkEl);
				//divEl.appendChild(createFromTempldivEl);
			}			
		}
	
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

function createActionDiv (divInnerHTML){
	var divEl = document.createElement("div");
	var addResinputEl	= "<a href=\"javascript:setType('private');javascript:configPanel(0,'','','', false); javascript:showMyPanel(0, 'addDocumentDiv');\" style=\"cursor:pointer; color: black; font-size: 11px;text-decoration:none;background: none\">";
	addResinputEl+="<digi:trn jsFriendly='true'>Upload doc/Add Url</digi:trn>";
	addResinputEl+="</a>";
	
	divEl.innerHTML=divInnerHTML;
	var mouseoverCallbackObj	= function (e, divEl) {divEl.style.backgroundColor="#ECF3FD";};
	var mouseoutCallbackObj	= function (e, divEl) {divEl.style.backgroundColor="white";};
	
	YAHOO.util.Event.addListener(divEl, "mouseover", mouseoverCallbackObj, divEl, false);
	YAHOO.util.Event.addListener(divEl, "mouseout", mouseoutCallbackObj, divEl, false);
	return divEl;
}