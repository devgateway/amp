<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@taglib uri="/taglib/struts-html" prefix="html"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs"%>


<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<digi:ref
	href="/TEMPLATE/ampTemplate/js_2/yui/assets/skins/sam/treeview.css"
	type="text/css" rel="stylesheet" />
<style type="text/css">
.ygtvlabel {
	font-size: 12px;
}
.ygtvtp, .ygtvtph, .ygtvlp,.ygtvlph{
background:url(/TEMPLATE/ampTemplate/img_2/ico_plus.gif) 0 0 no-repeat;
}
.ygtvtm,.ygtvtmh,.ygtvlm,.ygtvlmh{
background:url(/TEMPLATE/ampTemplate/img_2/ico_minus.gif) 0 0 no-repeat; ;
}
.ygtvfocus, .ygtvfocus .ygtvlabel,.ygtvfocus .ygtvlabel:link,.ygtvfocus .ygtvlabel:visited,.ygtvfocus .ygtvlabel:hover{
background-color:#FFFFFF;
}
</style>


<script language="JavaScript" type="text/javascript"
	src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/treeview/treeview-min.js"/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/tree/jktreeview.js"/>"></script><jsp:include
	page="scripts/npdScripts/programTree.jsp" flush="true" />
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/npdScripts/npdGraph.js"/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/npdScripts/npdGrid.js"/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/npdScripts/changeOptions.js"/>"></script>
<jsp:include page="scripts/npdScripts/activityList.jsp" flush="true" />


<digi:instance property="aimNPDForm" />
<digi:form action="/exportIndicators2xsl.do">

	<c:set var="noProgSelected">
		<digi:trn key="aim:npd:noProgSelected">
			Please select a program before selecting a filter !
	</digi:trn>
	</c:set>
	<html:hidden property="defaultProgram" styleId="defaultProgram" />
	<script language="javascript" type="text/javascript">
	var ptree;
	var curProgId;
	var curProgNodeIndex;
	var curNodeId;
	var curProgramName;
	var curGraphURL = null;
	var lastTreeUpdate=0;
	var line=new Array();
	var lineIter=0;
	var openNodes=new Array();
	var treeXML=null;
	var activityXML=null;
	var p1d='?';
	var pd='&';
	var curIndicatorIDs=[];
	var curIndicatorNames=[];
	var selIndicatorIDs=[];
	var selYear =[];
	var selActStatus = null;
	var selActDonors = null;
	var selActYearTo = null;
	var selActYearFrom = null;
	var progIdHistory = [];
	var progNodeHistory = [];
    var pr;
    var lastTimeStamp;
	var strNoActivities="<digi:trn key='aim:NPD:noActivitisLabel'>No Activities</digi:trn>";
	var strTotal="<digi:trn key='aim:NPD:totalLabels'>Totals:</digi:trn>";
<gs:test name="<%=org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS%>" compareWith="true" onTrueEvalBody="true">
	var strThousands="<digi:trn key='aim:NPD:amountThousandsOfDollarsLabel'>All amounts are in thousands (000) of</digi:trn> ${aimNPDForm.defCurrency}";
</gs:test>
<gs:test name="<%=org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS%>" compareWith="false" onTrueEvalBody="true">
	var strThousands="${aimNPDForm.defCurrency}";
</gs:test>
	var strPlanned="<digi:trn key='aim:NPD:sumplanedCommitments'>Planned Commitments</digi:trn>";
	var strActual="<digi:trn key='aim:NPD:sumactualCommitments'>Actual Commitments</digi:trn>";
    var strActualDisb="<digi:trn>Actual Disbursements</digi:trn>";
	var strProposed="<digi:trn key='aim:NPD:sumproposedPrjCost'>Proposed Project Cost</digi:trn>";
	var actCurrPage=1;
	var actMaxPages=0;
	var pgNext='<digi:trn key="aim:npd:pagination:next">Next</digi:trn>';
	var pgPrev='<digi:trn key="aim:npd:pagination:prev">Prev</digi:trn>';
	var pgLast='<digi:trn key="aim:npd:pagination:last">Last</digi:trn>';
	var pgFirst='<digi:trn key="aim:npd:pagination:first">First</digi:trn>';
	var pgPagesLabel='<digi:trn key="aim:npd:pagination:pageslabel">Pages:</digi:trn>';
	var status='<digi:trn key="aim:npd:status">Status</digi:trn>';
	var title='<digi:trn key="aim:npd:titl">Title</digi:trn>';
	var strDate='<digi:trn key="aim:npd:strdate">Start Date</digi:trn>';
	var donor='<digi:trn key="aim:npd:donor">Donor</digi:trn>';
	function clearChildren(node){
		while(node.firstChild!=null){	
			node.removeChild(node.firstChild);
		}	
	}
    	function addActionToURL(actionName){
    		var fullURL=document.URL;
    		var lastSlash=fullURL.lastIndexOf("/");
    		var partialURL=fullURL.substring(0,lastSlash);
    		if(partialURL.lastIndexOf("aim")!=(partialURL.length-3)){
    			partialURL+="/aim";
    		}
    		return partialURL+"/"+actionName;
    	}
    	function setLoadingImage(parent){
    		clearChildren(parent);
    		var div=document.createElement('div');
    		var img=document.createElement('img');
    		img.src="/TEMPLATE/ampTemplate/images/amploading.gif";
    		img.alt="loading...";
    		div.appendChild(img);
    		parent.appendChild(div);
    	}
    
    
    	function activitiesCallBack(status, statusText, responseText, responseXML){
		activityXML=responseXML;
		setUpActivityList(responseXML);
	}

	function setUpActivityList(xml){
		var tr= document.getElementById('activityResultsPlace');
		var paginationTr=document.getElementById('paginationPlace');
		var tbl= tr.parentNode;

		clearActivityTable(tr);
		var root=xml.getElementsByTagName('activityList')[0];
		if(root==null){
			root=xml.getElementsByTagName('error')[0];
			if (root!=null){
				showError(root,tbl);
			}else{
				var newTR=document.createElement('TR');
				newTR.innerHTML='<td colspan="8">Unknown Error</td>';
				tbl.appendChild(newTR);
			}
			return;
		}
		//total pages
		actMaxPages=root.getAttribute('totalPages');

		//get activities array
		var actList = root.childNodes;
		if (actList == null || actList.length == 0){
			var newTR=document.createElement('TR');
            var newTD=document.createElement('TD');
            newTD.innerHTML=strNoActivities;
            newTD.setAttribute("colSpan","8");
            newTR.appendChild(newTD);
			tr.parentNode.appendChild(newTR);
            var spn=document.getElementById("spnAmountText");
            if(spn!=null){
              spn.innerHTML="";
            }
			return;
		}



		//sum labels
		var labelsTR1 = document.createElement('TR');
		var titleLabelTD=document.createElement('TD');
		titleLabelTD.innerHTML='<b>'+title+' </b>';
		labelsTR1.appendChild(titleLabelTD);

		var statusLabelTD=document.createElement('TD');
		statusLabelTD.innerHTML='<b>'+status+' </b>';
		labelsTR1.appendChild(statusLabelTD);

		var donorLabelTD=document.createElement('TD');
		donorLabelTD.innerHTML='<b>'+donor+' </b>';
		labelsTR1.appendChild(donorLabelTD);

		var strDateLabelTD=document.createElement('TD');
		strDateLabelTD.innerHTML='<b>'+strDate+' </b>';
		labelsTR1.appendChild(strDateLabelTD);

		var labelTD1 = document.createElement('TD');
		labelTD1.innerHTML='<feature:display name="Proposed Project Cost" module="Funding"><b>'+strProposed+' </b></feature:display> ';
		labelsTR1.appendChild(labelTD1);

		
		var labelTD3 = document.createElement('TD');
		labelTD3.innerHTML='<b>'+strActual+' </b>';
		labelsTR1.appendChild(labelTD3);

        var labelTD2 = document.createElement('TD');
		labelTD2.innerHTML='<b>'+strActualDisb+' </b>';
		labelsTR1.appendChild(labelTD2);

        labelsTR1.bgColor='Silver';
		tbl.appendChild(labelsTR1);
		//end of sum labels


		for (var i=0; i< actList.length; i++) {
			if (actList[i].tagName=='activity'){
				var actTR = document.createElement('TR');
                if(i%2==1){
                    actTR.bgColor='#CCCCCC';
                }
                
				//name
				var actTDname = document.createElement('TD');
				var actAname = document.createElement('a');
				actAname.innerHTML=actList[i].getAttribute('name');
				var actURL = addActionToURL('aim/showPrinterFriendlyPage.do~edit=true~activityid=');
				actURL+=actList[i].getAttribute('id');
				actAname.href=actURL;
				actAname.target='_blank';
				actTDname.appendChild(actAname);
				actTR.appendChild(actTDname);
				//status
				var actTDstatus = document.createElement('TD');
				actTDstatus.innerHTML=actList[i].getAttribute('status');
				actTR.appendChild(actTDstatus);
				//donor
				var actTDdonor = document.createElement('TD');
				getDonorsHTML(actList[i].childNodes,actTDdonor);
				actTR.appendChild(actTDdonor);
				//sart year
				var actTDfromYear = document.createElement('TD');
				actTDfromYear.innerHTML=actList[i].getAttribute('date');
				actTR.appendChild(actTDfromYear);
                                
                           
                            
				//amount
				var actTDproposedAmount = document.createElement('TD');
				actTDproposedAmount.innerHTML = '<feature:display name="Proposed Project Cost" module="Funding">'+actList[i].getAttribute('proposedAmount')+'</feature:display>';
				if(actTDproposedAmount.innerHTML == "N/A"){
				   actTDproposedAmount.innerHTML = "--"
				}
				actTR.appendChild(actTDproposedAmount);

				

				var actTDActualAmount = document.createElement('TD');
				actTDActualAmount.innerHTML = actList[i].getAttribute('actualAmount');
				if(actTDActualAmount.innerHTML == "N/A"){
				   actTDActualAmount.innerHTML = "--"
				}
				actTR.appendChild(actTDActualAmount);

                var actualDisbAmountTD = document.createElement('TD');
				actualDisbAmountTD.innerHTML = actList[i].getAttribute('actualDisbAmount');
				if(actualDisbAmountTD.innerHTML == "N/A"){
				   actualDisbAmountTD.innerHTML = "--"
				}
				actTR.appendChild(actualDisbAmountTD);

				//row to table
				tbl.appendChild(actTR);
			}
		}//for


		//show Sum

		if (strTotal==null || strTotal==''){
			//if no translations
			strTotal='Total:';
		}
		//totals tr
		var lastTR = document.createElement('TR');

		var lastTD = document.createElement('TD');
		lastTD.colSpan=4;
		lastTD.align='right';
		lastTD.innerHTML='<strong>'+strTotal+' </strong>';
		lastTR.appendChild(lastTD);

		var propSumTD = document.createElement('TD');
		propSumTD.innerHTML= '<feature:display name="Proposed Project Cost" module="Funding">'+root.getAttribute('proposedSum')+'</feature:display>';
		lastTR.appendChild(propSumTD);

		

		var actSumTD = document.createElement('TD');
		actSumTD.innerHTML=root.getAttribute('actualSum');
		lastTR.appendChild(actSumTD);

        var actDisbSumtTD = document.createElement('TD');
		actDisbSumtTD.innerHTML=root.getAttribute('actualDisbSum');
		lastTR.appendChild(actDisbSumtTD);

		tbl.appendChild(lastTR);



		//tousands label
		if (strThousands==null || strThousands==''){
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
			strThousands='All amounts are in thousands (000)';
</gs:test>
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="false" onTrueEvalBody="true">
			strThousands='';
</gs:test>
		}

        var spn=document.getElementById("spnAmountText");
        if(spn!=null){
          spn.innerHTML=strThousands;
        }

        //var lastTR1 = document.createElement('TR');
		//var lastTD1 = document.createElement('TD');
		//lastTD1.colSpan=8;
		//lastTD1.align='right';
		//lastTD1.innerHTML='<font color="blue">'+strThousands+' </font>';
		//lastTR1.appendChild(lastTD1);
		//tbl.appendChild(lastTR1);

		setupPagination(paginationTr);

	}

	function setupPagination(placeToAdd){
		//alert(actMaxPages);
		if (actMaxPages<=1) return;
		//TODO clear tr first
		while(placeToAdd.firstChild != null){
			placeToAdd.removeChild(placeToAdd.firstChild);
		}


		var tooManyPages=actMaxPages>=11;
		var middleStart=-1;
		var middleEnd=actMaxPages;
		if (tooManyPages==true){
			middleStart=actCurrPage-4;
			if (middleStart<1){
				middleStart=1;
			}
			middleEnd=actCurrPage+4;
			if(middleEnd>actMaxPages){
				middleEnd=actMaxPages;
			}
		}

		var tdLabel = document.createElement('TD');
		tdLabel.innerHTML=pgPagesLabel;
		placeToAdd.appendChild(tdLabel);

		if (actCurrPage>1){
			var tdFirst = document.createElement('TD');
			tdFirst.innerHTML='<a href="javascript:gotoActListPage(1)">'+pgFirst+'</a>';
			placeToAdd.appendChild(tdFirst);

			var tdSp1 = document.createElement('TD');
			tdSp1.innerHTML='&nbsp;';
			placeToAdd.appendChild(tdSp1);

			var tdPrev = document.createElement('TD');
			tdPrev.innerHTML='<a href="javascript:gotoActListPage('+(actCurrPage-1)+')">'+pgPrev+'</a>';
			placeToAdd.appendChild(tdPrev);

			var tdSp2 = document.createElement('TD');
			tdSp2.innerHTML='&nbsp;';
			placeToAdd.appendChild(tdSp2);
		}

		for (var i=1; i <= actMaxPages; i++){
			if (tooManyPages){
				if (middleStart==i && middleStart>1){
					var tddots1 = document.createElement('TD');
					tddots1.innerHTML=' ... ';
					placeToAdd.appendChild(tddots1);
					var tdsp = document.createElement('TD');
					tdsp.innerHTML='&nbsp;';
					placeToAdd.appendChild(tdsp);
				}
				if (i>=middleStart && i<=middleEnd){
					//pages
					var td = document.createElement('TD');
					if (i==actCurrPage){
						td.innerHTML='<strong>'+i+'</strong>';
					}else{
						td.innerHTML='<a href="javascript:gotoActListPage('+i+')">'+i+'</a>';
					}
					placeToAdd.appendChild(td);
					//space
					var tdsp = document.createElement('TD');
					tdsp.innerHTML='&nbsp;';
					placeToAdd.appendChild(tdsp);

				}
				if (middleEnd==i && middleEnd<actMaxPages){
					var tddots2 = document.createElement('TD');
					tddots2.innerHTML=' ... ';
					placeToAdd.appendChild(tddots2);
					var tdsp = document.createElement('TD');
					tdsp.innerHTML='&nbsp;';
					placeToAdd.appendChild(tdsp);
				}
			}else{
				var td = document.createElement('TD');
				if (i==actCurrPage){
					td.innerHTML='<strong>'+i+'</strong>';
				}else{
					td.innerHTML='<a href="javascript:gotoActListPage('+i+')">'+i+'</a>';
				}
				placeToAdd.appendChild(td);
				var tdsp = document.createElement('TD');
				tdsp.innerHTML='&nbsp;';
				placeToAdd.appendChild(tdsp);
			}
		}//for each age ends

		if (actCurrPage<actMaxPages){
			var tdNext = document.createElement('TD');
			tdNext.innerHTML='<a href="javascript:gotoActListPage('+(actCurrPage+1)+')">'+pgNext+'</a>';
			placeToAdd.appendChild(tdNext);

			var tdSp3 = document.createElement('TD');
			tdSp3.innerHTML='&nbsp;';
			placeToAdd.appendChild(tdSp3);

			var tdLast = document.createElement('TD');
			tdLast.innerHTML='<a href="javascript:gotoActListPage('+actMaxPages+')">'+pgLast+'</a>';
			placeToAdd.appendChild(tdLast);
		}

	}

	/* pagination link handler */
	function gotoActListPage(pageNum){
		actCurrPage=pageNum;
		getActivities();
		//return false;
	}

	function getDonorsHTML(donors,target){
		if (donors !=null && donors.length >0 && donors[0].tagName=='donors'){
			var donorList = donors[0].childNodes;
			if (donorList !=null && donorList.length>0){
				var donorTable = document.createElement('table');
                                var donorTableBD = document.createElement('TBODY');
				donorTable.cellSpacing=0;
				for (var i=0; i<donorList.length; i++){
					var donorTr = document.createElement('tr');
					var donorTd = document.createElement('td');
					donorTd.innerHTML = donorList[i].getAttribute('name');
					donorTr.appendChild(donorTd);
                                        donorTableBD.appendChild(donorTr);
				}
                                donorTable.appendChild(donorTableBD);
				target.appendChild(donorTable);
			}else{
				target.innerHTML = ' ';
			}
		}else{
			target.innerHTML = ' ';
		}
	}

	function showError(stackList,where){
		if (stackList !=null && stackList.childNodes !=null){
			for (var i=0; i<stackList.childNodes.length; i++){
				if (stackList.childNodes[i].tagName=='frame'){
					var tr=document.createElement('TR');
					var td=document.createElement('TD');
					td.colSpan=6;
					td.innerHTML=stackList.childNodes[i].textContent;

					tr.appendChild(td);
					where.appendChild(tr);
				}
			}
		}
	}

	function clearActivityTable(firstTR){
		var par=firstTR.parentNode;
		while(firstTR.nextSibling != null){
			par.removeChild(firstTR.nextSibling);
		}
	}

	function setActivityLoading(firstTR){
		var par=firstTR.parentNode;
		clearActivityTable(firstTR);
		var tr=document.createElement('tr');
		var str='<td align="center" colspan="6"><img src="/TEMPLATE/ampTemplate/images/amploading.gif" alt="loading..."/>loading...</td>';
		tr.innerHTML=str;
		par.appendChild(tr);
	}
        
	function filterStatus(){
		var stat = document.getElementsByName('selectedStatuses')[0];
		selActStatus = stat.value;
		getActivities();
	}

	function filterDonor(){
		var donors = document.getElementsByName('selectedDonors')[0];
		selActDonors = donors.value;
		getActivities();
	}
        function applyFilter(){
            selActStatus="";
            selActDonors="";
            var stat = document.getElementsByName('selectedStatuses')[0];
            var donors = document.getElementsByName('selectedDonors')[0];
            var from = document.getElementsByName('yearFrom')[0];
            selActYearFrom = from.value;
            var to= document.getElementsByName('yearTo')[0];
            selActYearTo = to.value;
            for (var i=0; i<stat.length; i++) {
                       if(stat.options[i].selected ){
                           if(stat.options[i].value=='0'){
                               selActStatus='0'+',';
                               break;
                           }
                           selActStatus+=stat.options[i].value+',';
                       }
             } 
              for (var j=0; j<donors.length; j++) {
               
                       if(donors.options[j].selected ){
                           selActDonors+=donors.options[j].value+',';
                       }
             } 
             if(selActStatus.length>1){
                 selActStatus=selActStatus.substring(0,selActStatus.length-1)
             }
              if(selActDonors.length>1){
                 selActDonors=selActDonors.substring(0,selActDonors.length-1)
             }
             getFilterSettings(); // Getting filter settings
             getActivities();
             
             // hide filter panel
             if(filter!=null){
                filter.hide();
            }
        }

	

	/* ========  Activities list methods END ========= */



	/* function to be called at page load to asynchronously call server for tree data.
		Later we may convert all page to asynchronous style to not refresh page at all.
	 */
	function initTree(){
		var treeList=document.getElementById('tree');
		treeList.innerHTML="<i>Loading...</i>"

		var url=addActionToURL("getThemeTreeNode.do");
		var async=new Asynchronous();
		async.complete=treeCallBack;
		async.call(url);
	}

	function setupYears(){
		var ys=document.getElementsByName('myYears'); //document.forms['aimNPDForm'].selYears;
		for (var i=0; i<ys.length; i++){
			selYear[selYear.length]=ys[i].value;
		}
	}

	function loadInitial(){
        initFilterPanel();
		setupYears();
		initTree();
	}

	/**
	*The code below is related to the DHTML panel
	*
	*/
	var numOfPrograms;				// Number of programs (themes) displayed
	var informationPanel;			// The panel on which the information is displayed
	var themeArray	= new Array();  // Array containing the formatted information for the themes

	/*Gets total number of programs from the xml tree */
	function setNumOfPrograms (xml) {
		var elements	=	xml.getElementsByTagName("program");
		numOfPrograms	= elements.length;
		//alert ("xml: " + numOfPrograms);
	}
	/* When clicking on a '+' to expand the tree the listeners for the other elements are refreshed */
	function addRootListener () {
		var tree		= document.getElementById('tree');
		YAHOO.util.Event.addListener(tree, "click", addEventListeners);
	}
	/* Adds listeners for all elemets in the tree */
	function addEventListeners () {
		for(var j=1;j<=numOfPrograms;j++){
							var n	= document.getElementById('ygtvlabelel'+j);
							YAHOO.util.Event.addListener(n, "mouseover", eventFunction);
							YAHOO.util.Event.addListener(n, "mouseout", hidePanel);
		}
	}
	/* Function that is executed when mouse over an element */
	function eventFunction(e) {
		//alert('S-a apelat eventFunction	' + this.href + '||' + getIdFromHref(this.href) );
        var x = 0;
        var y = 0;
        if (e.pageX && e.pageY) 	{
            x = e.pageX;
            y = e.pageY;
        }
        else if (e.clientX &&e.clientY) 	{
            x = e.clientX + document.body.scrollLeft;
            y = e.clientY + document.body.scrollTop;
        }

		showPanel(this.innerHTML, getIdFromHref(this.href), x,y);
	}
	/* Extracts the id (database id of AmpTheme) from the href property */
	function getIdFromHref( href ) {
		var start	= href.indexOf("('");
		var end		= href.indexOf("',");
		return href.substring(start+2, end);
 	}
 	/* Creates the panel used to show information */
 	function createPanel(headerText, bodyText) {
 		//YAHOO.namespace("amp.container");
 		//alert('Create Panels');
 		//YAHOO.amp.container.panel2 = new YAHOO.widget.Panel("panel2", { width:"300px", visible:true, draggable:false, close:true } );
		//YAHOO.amp.container.panel2.setHeader("Panel #2 from Script");
		//YAHOO.amp.container.panel2.setBody("This is a dynamically generated Panel.");
		//YAHOO.amp.container.panel2.setFooter("End of Panel #2");
		//YAHOO.amp.container.panel2.render(document.body);

		//YAHOO.amp.container.panel2.moveTo(50,50);


		informationPanel	= new YAHOO.widget.Panel("infoPanel", { width:"300px", visible:false, draggable:false, close:false } );
		informationPanel.setHeader(headerText);
		informationPanel.setBody(bodyText);
		informationPanel.render(document.body);

		infoPanelObj	= document.getElementById('infoPanel');

		YAHOO.util.Event.addListener(infoPanelObj, "mouseover", makePanelVisible);
		YAHOO.util.Event.addListener(infoPanelObj, "mouseout", hidePanel);

 	}
 	/* Updates the panels header, body and position and makes it visible */
 	function showPanel(headerText, id, posX, posY) {
 		informationPanel.setHeader(headerText);
		informationPanel.setBody(themeArray[id]);
		informationPanel.moveTo(posX+2, posY+2);
		informationPanel.show();
 	}
 	/* Just makes the panel visible */
 	function makePanelVisible() {
 		informationPanel.show();
 	}
 	/* Just makes the panel invisible */
 	function hidePanel() {
 		informationPanel.hide();
 	}
 	/* Adds the information for a theme to the themeArray array in the corresponding position (=pid). */
 	function addProgramInformation(pid, programName, description, leadAgency, programCode, programType, targetGroups,
 				background, objectives, outputs, beneficiaries, environmentConsiderations) {
 			var panelBody =	"";
 			panelBody += '<table border="0">';
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:ProgramName'>Program Name</digi:trn>:</b>&nbsp;</td><td>"+ programName +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:Description'>Description</digi:trn>:</b>&nbsp;</td><td>"+ description +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:LeadAgency'>Lead Agency</digi:trn>:</b>&nbsp;</td><td>"+ leadAgency +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:ProgramCode'>Program Code</digi:trn>:</b>&nbsp;</td><td>"+ programCode +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:ProgramType'>Program Type</digi:trn>:</b>&nbsp;</td><td>"+ programType +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:TargetGroups'>Target Groups</digi:trn>:</b>&nbsp;</td><td>"+ targetGroups +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:Background'>Background</digi:trn>:</b>&nbsp;</td><td>"+ background +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:Objectives'>Objectives</digi:trn>:</b>&nbsp;</td><td>"+ objectives +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:Outputs'>Outputs</digi:trn>:</b>&nbsp;</td><td>"+ outputs +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:Beneficiaries'>Beneficiaries</digi:trn>:</b>&nbsp;</td><td>"+ beneficiaries +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:Environment'>Environment Considerations</digi:trn>:</b>&nbsp;</td><td>"+ environmentConsiderations +"</td></tr>";
 			panelBody += '</table>';

 			themeArray[pid]	= panelBody;

 	}
    
    
    
    
    	window.onload=loadInitial;
    </script>
	<script language="javascript" type="text/javascript">
    	<c:forEach var="theme" items="${aimNPDForm.allThemes}">
    	<c:set var="name">
    	${theme.name}
    	</c:set>
    	<c:set var="quote">'</c:set>
    	<c:set var="escapedQuote">\'</c:set>
    	<c:set var="escapedName">
    	${fn:replace(name,quote,escapedQuote)}
    	</c:set>
    		addProgramInformation(	'${theme.ampThemeId}',
    								 '${escapedName}',
    								'${theme.description}',
    								'${theme.leadAgency}',
    								'${theme.themeCode}',
    								'${theme.typeCategoryValue.value}',
    								'${theme.targetGroups}',
    								'${theme.background}',
    								'${theme.objectives}',
    								'${theme.outputs}',
    								'${theme.beneficiaries}',
    			 					'${theme.environmentConsiderations}'
    	                            );

    	</c:forEach>
</script>
	<!-- MAIN CONTENT PART START -->

	<table cellspacing="0" cellpadding="0" border="0" align="center" width="1000">
		<td valign="top" align="left">
		<div id="content" class="yui-skin-sam">
		<div id="demo" class="yui-navset">
		<ul class="yui-nav">
			<module:display name="National Planning Dashboard"
				parentModule="NATIONAL PLAN DASHBOARD">
				<li class="selected"><a style="cursor: default">
				<div><digi:trn key="aim:nplDashboard">National Planning Dashboard</digi:trn>
				</div>
				</a></li>
			</module:display>
			<feature:display name="Portfolio Dashboard" module="M & E">
				<li><digi:link
					href="/viewPortfolioDashboard.do~actId=-1~indId=-1">
					<div><digi:trn key="aim:portfolioDashboard">Dashboard</digi:trn>
					</div>
				</digi:link></li>
			</feature:display>
		</ul>

		<div class="yui-content" style="border: 1px solid rgb(208, 208, 208);">
		<div id="tabs-1" class="ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide">
		<fieldset style="border:none;"><legend><span class=legend_label><span
			id="graphHeader">&nbsp;</span></span></legend>
		<div>
		<div class="show_hide_setting"><digi:link
			href="/reportWizard.do" styleClass="l_sm">
			<digi:trn>Reports</digi:trn>
		</digi:link></div>
		<div class="dashboard_tab_opt">
		<div class="tab_opt_cont" style="background-color:#F2F2F2;"><a class="l_sm" href="#"><img
			border="0" src="/TEMPLATE/ampTemplate/img_2/ico-excel.png"></a>&nbsp;<a
			class="l_sm" href="#" onclick="exportToExcel();return false;">Export
		to Excel</a> &nbsp;|&nbsp; <a class="l_sm" href="#"><img border="0"
			src="/TEMPLATE/ampTemplate/img_2/ico-print.png"></a>&nbsp;<a
			class="l_sm" href="#" onclick="window.print(); return false;"><digi:trn>Print</digi:trn></a>
		&nbsp;|&nbsp; <a class="l_sm" id="showGridLink"
			href="javascript:showGridTable();"><digi:trn>View
		Table</digi:trn></a><a class="l_sm" id="hideGridLink"
			href="javascript:hideGridTable();" style="display: none"><digi:trn>Hide Table</digi:trn></a></div>
		</div>
		</div>

		<table width="100%">
			<tbody>
				<tr>
					<td width="50%" valign="top">
					<div class="dashboard_child_left_1">
					<div id="indicatorTable"></div>
					<div id="">
					<table>
						<tbody>
							<tr>
								<td>
								<div id="divGraphLoading"
									style="vertical-align: middle; display: none; width: ${aimNPDForm.graphWidth}px; height: ${aimNPDForm.graphHeight}px"
									align="center"><digi:img src="images/amploading.gif" /><digi:trn
									key="aim:NPD:loadingGraph">Loading...</digi:trn></div>
								<div id="divGraphImage" style="display: block"><digi:context
									name="showChart" property="/module/moduleinstance/npdGraph.do" />
								<c:url var="fullShowChartUrl" scope="page" value="${showChart}">
									<c:param name="actionMethod" value="displayChart" />
									<c:param name="currentProgramId"
										value="${aimNPDForm.programId}" />
									<c:forEach var="selVal" items="${aimNPDForm.selIndicators}">
										<c:param name="selectedIndicators" value="${selVal}" />
									</c:forEach>
								</c:url> <img id="graphImage" onload="graphLoaded()" alt="chart"
									src="${fullShowChartUrl}" width="${aimNPDForm.graphWidth}"
									height="${aimNPDForm.graphHeight}" usemap="#npdChartMap"
									border="0" /></div>
								</td>
								<td>
								<div class="jtree"
									style="width: 350px; height: 350px; overflow: scroll;">
								<div id="tree" ></div>
								</div>
								</td>
							</tr>
						</tbody>
					</table>

					</div>
					</div>
					<div class="dashboard_bottom">
					<div class="dashboard_filters">Filter:
					<table width="50%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td><c:set var="translation">
								<digi:trn key="aim:npd:dropDownAnyStatus">Any Status</digi:trn>
							</c:set> <category:showoptions firstLine="${translation}"
								name="aimNPDForm" property="selectedStatuses"
								keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.ACTIVITY_STATUS_KEY%>"
								multiselect="true" size="5" ordered="true"
								styleClass="inputx insidex" /></td>
							<td><html:select multiple="true" size="5"
								property="selectedDonors" styleClass="inputx insidex">
								<option value="-1"><digi:trn
									key="aim:npd:dropDownAnyDonor">Any Donor</digi:trn></option>
								<html:optionsCollection name="aimNPDForm" property="donors"
									value="value" label="label" />
							</html:select></td>
						</tr>
						<tr>
							<td><html:select property="yearFrom"
								styleClass="inputx insidex">
								<option value="-1"><digi:trn
									key="aim:npd:dropDownFromYear">From Year</digi:trn></option>
								<html:optionsCollection name="aimNPDForm" property="years"
									value="value" label="label" />
							</html:select></td>
							<td><html:select property="yearTo"
								styleClass="inputx insidex">
								<option value="-1"><digi:trn
									key="aim:npd:dropDownToYear">To Year</digi:trn></option>
								<html:optionsCollection name="aimNPDForm" property="years"
									value="value" label="label" />
							</html:select></td>
						</tr>
						<tr>
						<td colspan="2" align="center">
						<input type="button" class="buttonx_sm btn_save" value="Apply Filter" onclick="applyFilter()"/>
						</td>
						</tr>
					</table>
					
					<div class="dashboard_activities">
					<digi:trn>Activites for:</digi:trn>
					&nbsp;<span id="actListProgname">&nbsp;</span>
					<div id="activityResultsPlace"></div>
					<div id="paginationPlace"></div>
					</div>
					</div>
					<div class="dashboard_options">Change Options: <br />
					<span class="normal_options">Indicators (Any and all)</span> <br />
					<div id="indicatorsResultsPlace"></div>
					<span class="normal_options">Time Limit (5)</span>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td valign="top"><c:forEach var="year"
								items="${aimNPDForm.years}" end="4" varStatus="status">
								<c:forEach var="year" items="${aimNPDForm.years}"
									begin="${status.index}" step="5">
									<html:multibox name="aimNPDForm" property="selYears"
										onclick="return checkYearsRules()">
												${year.value}
											</html:multibox>
									<span class="normal_options">${year.label}</span>

								</c:forEach>
								<br />
							</c:forEach></td>
						</tr>
					</table>
					<br />
					<input type="button" class="buttonx_sm btn_save"
						value="Apply Changes" onclick="doChanges();"></div>
					</div>
					</td>
				</tr>
		</table>
		</fieldset>
		</div>
		<!-- MAIN CONTENT PART END --> <input type="hidden" id="hdYears"
			value="" /> <input type="hidden" id="hdIndicators" value="" /> <c:forEach
			var="sys" items="${aimNPDForm.selYears}">
			<html:hidden property="myYears" value="${sys}" />
		</c:forEach> <span id="graphMapPlace"> <map name="npdChartMap"
			id="npdChartMap">
		</map> </span></div>
		</div>
		</digi:form>