function ActionsMenu(objectId,nameprefix,isTeamMenu, trnObj){
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
	
	this.trnObj	= {
			addResource: "<digi:trn>Add Resource</digi:trn>"
	};
	
	if (trnObj != null)
		this.trnObj		= trnObj;
}

ActionsMenu.prototype.render=function (){
	this.overlay 	= new YAHOO.widget.Panel(this.nameprefix, { context:[this.objectId,"tl","bl"],
		  visible:false,
		  width:"150px",
		  effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
		  close: true } );
	
	this.overlay.setHeader('<span>' + this.trnObj.addResource + '</span>');
	
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
		if(uploadDocenalbe){
		var addResLinkEl	= "<a  style=\"cursor:pointer; color: black; font-size: 11px;text-decoration:none;background: none\" >";
		addResLinkEl+='<span>'+ uploadDoc +'</span>'; // we use var instead of digi:trn because ie adds <?xml:namespace prefix=digi/> :(		
		addResLinkEl+="</a>";		
		
		var addResdivEl = document.createElement("div");
		addResdivEl.setAttribute("class", "actionsDivItem");
		var clickActionsForAddDoc	= function (e, addResdivEl) {
			if(teamView!=null && ! teamView){
				menuPanelForUser.toggleUserView();
			}else{
				menuPanelForTeam.toggleTeamView();
			}
			setType(ownType);
			configPanel(0, '', '', '', 0, false, null, '', '');
			showMyPanel(0, 'addDocumentDiv');
		};
		createActionDiv(addResdivEl,addResLinkEl,clickActionsForAddDoc);
		divEl.appendChild(addResdivEl);
		
		var hr1 = document.createElement("hr");
		hr1.height = '1px';
		divEl.appendChild(hr1);
		}
		
		//add web url link
		if (addWebLinkenable){
		var addUrlLinkEl	= "<a style=\"cursor:pointer; color: black; font-size: 11px;text-decoration:none;background: none\" >";
		addUrlLinkEl+='<span>'+ addWebLink +'</span>';
		addUrlLinkEl+="</a>";
		
		var addURLdivEl = document.createElement("div");
		addURLdivEl.setAttribute("class", "actionsDivItem");
		var clickActionsForAddLink = function (e, addURLdivEl) {
			if(teamView!=null && ! teamView){
				menuPanelForUser.toggleUserView();
			}else{
				menuPanelForTeam.toggleTeamView();
			}
			setType(ownType);
			configPanel(0, '', '', '', 0, true, null, '', '');
			showMyPanel(0, 'addDocumentDiv');
		};
		createActionDiv(addURLdivEl,addUrlLinkEl,clickActionsForAddLink);
		divEl.appendChild(addURLdivEl);
		
		var hr2 = document.createElement("hr");
		hr2.height = '1px';
		divEl.appendChild(hr2);
		}
		
		// debugger;
			//create from template link
		if(createfromtemplateenable){
			var createFromTemplateLinkEl="<a style=\"cursor:pointer; color: black; font-size: 11px;text-decoration:none;background: none\">";
			createFromTemplateLinkEl+='<span>'+createFromTemplate+'</span>';
			createFromTemplateLinkEl+="</a>";
			var createFromTempldivEl	= document.createElement("div");
			createFromTempldivEl.setAttribute("class", "actionsDivItem");
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
		}
	
	
	divEl.style.backgroundColor="white";
	//divEl.setAttribute("class", "res_opt_cont");
	
	
	/*
	 * bigDiv.appendChild(divEl);
	bigDiv.appendChild(brEl);
	this.overlay.setBody(bigDiv);
	 * */
	
	
	this.overlay.setBody(divEl);
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
	if (this.overlay != null) {
		this.overlay.hide();
		this.divEl.innerHTML	= "";
	}	
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