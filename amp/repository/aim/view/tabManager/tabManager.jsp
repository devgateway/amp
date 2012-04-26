<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<c:set var="connectionErrMsg">
	<digi:trn key="aim:tabmanager:connecionProblem">There was a problem connecting to the server. Please close this panel and try again. </digi:trn>
</c:set>
<c:set var="pleaseWaitMsg">
	<digi:trn key="aim:tabmanager:pleasewait">Please wait</digi:trn>
</c:set>

<script type="text/javascript">

	function initializeTabManager () {
		
		tabManager	= new TabManager();
		tabManager.showPanel();
	}
	
	function TabManager() {
		this.bodyEl			= document.getElementById("panelBody");
		this.footerEl		= document.getElementById("panelFooter");
		this.btnDivEl		= document.getElementById("buttonDiv");
		this.btnEl			= document.getElementById("tabManagerButton");
		
		this.doubleTabErrEl		= document.getElementById("doubleTabErr");
		
		this.saveDataManager	= new SaveDataManager("panelBody");
	}
	
	TabManager.prototype.showPanel	= function () {
		if ( this.panel == null ) {
			document.getElementById("tabManagerPanel").style.display	= "";
			this.panel	= new YAHOOAmp.widget.Panel("tabManagerPanel", 
						{ 	visible:true,
							width: "400px", 
							constraintoviewport:true, 
							fixedcenter: true, 
							underlay:"shadow", 
							modal: true,
							close:true, 
							visible:false, 
							draggable:true } );
			this.panel.render();
			
		}
		this.hideButton();
		this.disableButton();
		this.setFooter("");
		this.setBody( "${pleaseWaitMsg}... <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='14px'/>" );
		this.panel.show();
		
		new GetDataManager().getData();
	}
	TabManager.prototype.hidePanel	= function () {
		this.panel.hide();
	}
	TabManager.prototype.showButton	= function () {
		this.btnDivEl.style.display	= "";
	}
	TabManager.prototype.hideButton	= function () {
		this.btnDivEl.style.display	= "none";
	}
	TabManager.prototype.showDoubleTabErr	= function () {
		this.doubleTabErrEl.style.display	= "";
	}
	TabManager.prototype.hideDoubleTabErr	= function () {
		this.doubleTabErrEl.style.display	= "none";
	}
	TabManager.prototype.enableButton	= function () {
		this.btnEl.style.color	= "black";
		this.btnEl.disabled		= false;
	}
	TabManager.prototype.disableButton	= function () {
		this.btnEl.style.color	= "lightgrey";
		this.btnEl.disabled		= true;
	}
	TabManager.prototype.showBody	= function () {
		this.bodyEl.style.display	= "";
	}
	TabManager.prototype.hideBody	= function () {
		this.bodyEl.style.display	= "none";
	}
	TabManager.prototype.setBody	= function ( content ) {
		var tmpDiv = document.createElement("DIV");
		tmpDiv.innerHTML = content;
		this.bodyEl.innerHTML = "";
		this.bodyEl.appendChild(tmpDiv);
	}
	TabManager.prototype.setFooter	= function ( content ) {
		this.footerEl.innerHTML		= content;
	}
	TabManager.prototype.save		= function ( content ) {
		this.saveDataManager.saveData();
	}
	
	TabManager.prototype.check 		= function () {
		var items		= document.getElementById("panelBody").getElementsByTagName( "select" );
		for ( var i=0; i<items.length; i++ )  {
			var compIdx		= items[i].selectedIndex;
			var compOption	= items[i].options[compIdx];
			if ( parseInt(compOption.value) > 0 ) {
				for (var j=0; j<items.length; j++) {
					if ( i!=j ) {
						var idx		= items[j].selectedIndex;
						var option	= items[j].options[idx];
						if ( compOption.value == option.value ){
							this.disableButton();
							this.showDoubleTabErr();
							return;
						}
					}
				}
			}
		}
		this.hideDoubleTabErr();
		this.enableButton();
		return;
	}

	function GetDataManager () {
		;
	}
	GetDataManager.prototype.success		= function (o) {
		tabManager.showBody();
		tabManager.setBody( o.responseText );
		if ( o.responseText.indexOf("dataSuccessful") >= 0 ) {
			tabManager.showButton();
			tabManager.enableButton();
		}
	}

	GetDataManager.prototype.failure		= function (o) {
		tabManager.hideBody();
		tabManager.hideButton();
		tabmanager.setFooter('${connectionErrMsg}');
	}
	
	GetDataManager.prototype.getData	= function () {
		var timestamp		= new Date().getTime();	
		YAHOOAmp.util.Connect.asyncRequest("GET", "/aim/tabManager.do?get=true&iestamp="+timestamp, this);
	}
	
	function SaveDataManager(destContainerId) {
		this.destContainerId	= destContainerId;
	}
	
	SaveDataManager.prototype.success		= function (o) {
		if ( o.responseText.indexOf("saveSuccessful") >= 0 ) {
			tabManager.hidePanel();
		}
		else {
			tabManager.setFooter(o.responseText);
			tabManager.enableButton();
		}
	}
	
	SaveDataManager.prototype.failure		= function (o) {
		tabManager.enableButton();
		tabmanager.setFooter('${connectionErrMsg}');
	}
	SaveDataManager.prototype.createPostString		= function () {
			var items		= document.getElementById(this.destContainerId).getElementsByTagName( "select" );
			var ret			= "";
			
			for (var i=0; i<items.length; i++) {
					var idx		= items[i].selectedIndex;
					var option	= items[i].options[idx];
					ret 		+= "tabsId=" + option.value;
					if ( i < items.length-1 )
							ret += "&";
			}
			// alert(ret);
			return ret;
			
	}
	SaveDataManager.prototype.saveData				= function () {
		tabManager.disableButton();
		tabManager.setFooter( "${pleaseWaitMsg}... <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='14px'/>" );
	
		var postString		= this.createPostString();
		YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/tabManager.do", this, postString);
	}

</script>

	<div id="tabManagerPanel" class="invisible-item">
		<div class="hd" style="font-size: 8pt;">
			<digi:trn key="aim:tabmanager:dtSelection">Desktop Tab Selection </digi:trn>
		</div>
		<div class="bd" align="center">
			<span id="doubleTabErr" style="display: none; color: red;"><digi:trn key="aim:tabmanager:errDoubleTab">Please do not select the same tab in 2 or more positions</digi:trn></span>
			<div id="panelBody" style="font-size: 8pt;">
			</div>
			<br />
			<div id="buttonDiv" style="display: none;" align="center">
				<button  type="button" class="buton"  id="tabManagerButton"
					style="color: lightgray" onclick="tabManager.save()">
					<digi:trn key="aim:tabmanager:save">Save</digi:trn>
				</button>
			</div>
		</div>
		<div class="ft" align="center" id="panelFooter" style="font-size: 8pt;">
			
		</div>
	</div>