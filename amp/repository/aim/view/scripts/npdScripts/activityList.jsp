<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>
<script type="text/javascript">

	function getActivities(){
		if (curProgId == null) {
			alert('${noProgSelected}');
			return;
		}
		var actList=document.getElementById('activityResultsPlace');
		//actList.innerHTML="<i>Loading...</i>"
		setLoadingImage(actList);
		var url=getActivitiesURL();
		var async=new Asynchronous();
		async.complete=activitiesCallBack;
		async.call(url);
	}
	
	function getActivitiesURL(){
		var result = addActionToURL('getActivities.do');
		result+=getURL();
		result+= pd + 'currentPage='+actCurrPage+'&timestamp=' +new Date().getTime();  ;
		return result;
	}

	function activitiesCallBack(status, statusText, responseText, responseXML){
		activityXML=responseXML;
		setUpActivityList(responseXML);
	}
	

	function setUpActivityList(xml){
		var divActivityResult= document.getElementById('activityResultsPlace');
		var paginationTr=document.getElementById('paginationPlace');
		clearChildren(divActivityResult);
		var root=xml.getElementsByTagName('activityList')[0];
		var table=document.createElement('table');
		table.className="inside";
		var tbody=document.createElement('tbody');
		table.appendChild(tbody);
		divActivityResult.appendChild(table);
		if(root==null){
			root=xml.getElementsByTagName('error')[0];
			if (root!=null){
				showError(root,tbody);
			}else{
				var newTR=document.createElement('TR');
				newTR.innerHTML='<td colspan="8">Unknown Error</td>';
				tbody.appendChild(newTR);
			}
			return;
		}
		//total pages
		actMaxPages=root.getAttribute('totalPages');

		//get activities array
		var actList = root.childNodes;
	
		if (actList == null || actList.length == 0){           
           	var noactivities= document.createTextNode(strNoActivities);          
			divActivityResult.appendChild(noactivities);
            var spn=document.getElementById("spnAmountText");
            if(spn!=null){
              spn.innerHTML="";
            }
			return;
		}



		//sum labels
		var labelsTR1 = document.createElement('TR');
		var titleLabelTD=document.createElement('TD');
		titleLabelTD.className="inside";
		titleLabelTD.innerHTML='<b>'+title+' </b>';
		labelsTR1.appendChild(titleLabelTD);

		var statusLabelTD=document.createElement('TD');
		statusLabelTD.className="inside";
		statusLabelTD.innerHTML='<b>'+status+' </b>';
		labelsTR1.appendChild(statusLabelTD);

		var donorLabelTD=document.createElement('TD');
		donorLabelTD.className="inside";
		donorLabelTD.innerHTML='<b>'+donor+' </b>';
		labelsTR1.appendChild(donorLabelTD);

		var strDateLabelTD=document.createElement('TD');
		strDateLabelTD.className="inside";
		strDateLabelTD.innerHTML='<b>'+strDate+' </b>';
		labelsTR1.appendChild(strDateLabelTD);

		var labelTD1 = document.createElement('TD');
		labelTD1.className="inside";
		labelTD1.innerHTML='<feature:display name="Proposed Project Cost" ampModule="Funding"><b>'+strProposed+' </b></feature:display> ';
		labelsTR1.appendChild(labelTD1);

		
		var labelTD3 = document.createElement('TD');
		labelTD3.className="inside";
		labelTD3.innerHTML='<b>'+strActual+' </b>';
		labelsTR1.appendChild(labelTD3);

        var labelTD2 = document.createElement('TD');
        labelTD2.className="inside";
		labelTD2.innerHTML='<b>'+strActualDisb+' </b>';
		labelsTR1.appendChild(labelTD2);

        labelsTR1.bgColor='#C7D4DB';
		tbody.appendChild(labelsTR1);
		//end of sum labels


		for (var i=0; i< actList.length; i++) {
			if (actList[i].tagName=='activity'){
				var actTR = document.createElement('TR');
                
                
				//name
				var actTDname = document.createElement('TD');
				actTDname.className="inside";
				var actAname = document.createElement('a');
				actAname.innerHTML=actList[i].getAttribute('name');
				var actURL = addActionToURL('showPrinterFriendlyPage.do~edit=true~activityid=');
				actURL+=actList[i].getAttribute('id');
				actAname.href=actURL;
				actAname.target='_blank';
				actTDname.appendChild(actAname);
				actTR.appendChild(actTDname);
				//status
				var actTDstatus = document.createElement('TD');
				actTDstatus.className="inside";
				actTDstatus.innerHTML=actList[i].getAttribute('status');
				actTR.appendChild(actTDstatus);
				//donor
				var actTDdonor = document.createElement('TD');
				actTDdonor.className="inside";
				getDonorsHTML(actList[i].childNodes,actTDdonor);
				actTR.appendChild(actTDdonor);
				//sart year
				var actTDfromYear = document.createElement('TD');
				actTDfromYear.className="inside";
				actTDfromYear.innerHTML=actList[i].getAttribute('date');
				actTR.appendChild(actTDfromYear);
                                
                           
                            
				//amount
				var actTDproposedAmount = document.createElement('TD');
				actTDproposedAmount.className="inside";
				actTDproposedAmount.innerHTML = '<feature:display name="Proposed Project Cost" ampModule="Funding">'+actList[i].getAttribute('proposedAmount')+'</feature:display>';
				if(actTDproposedAmount.innerHTML == "N/A"){
				   actTDproposedAmount.innerHTML = "--"
				}
				actTR.appendChild(actTDproposedAmount);

				

				var actTDActualAmount = document.createElement('TD');
				actTDActualAmount.className="inside";
				actTDActualAmount.innerHTML = actList[i].getAttribute('actualAmount');
				if(actTDActualAmount.innerHTML == "N/A"){
				   actTDActualAmount.innerHTML = "--"
				}
				actTR.appendChild(actTDActualAmount);

                var actualDisbAmountTD = document.createElement('TD');
                actualDisbAmountTD.className="inside";
				actualDisbAmountTD.innerHTML = actList[i].getAttribute('actualDisbAmount');
				if(actualDisbAmountTD.innerHTML == "N/A"){
				   actualDisbAmountTD.innerHTML = "--"
				}
				actTR.appendChild(actualDisbAmountTD);

				//row to table
				tbody.appendChild(actTR);
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
		lastTD.className="inside_inner_title";
		lastTD.colSpan=4;
		lastTD.align='right';
		lastTD.innerHTML='<strong>'+strTotal+' </strong>';
		lastTR.appendChild(lastTD);

		var propSumTD = document.createElement('TD');
		propSumTD.className="inside_inner_title";
		propSumTD.innerHTML= '<feature:display name="Proposed Project Cost" ampModule="Funding">'+root.getAttribute('proposedSum')+'</feature:display>';
		lastTR.appendChild(propSumTD);

		

		var actSumTD = document.createElement('TD');
		actSumTD.className="inside_inner_title";
		actSumTD.innerHTML=root.getAttribute('actualSum');
		lastTR.appendChild(actSumTD);

        var actDisbSumtTD = document.createElement('TD');
        actDisbSumtTD.className="inside_inner_title";
		actDisbSumtTD.innerHTML=root.getAttribute('actualDisbSum');
		lastTR.appendChild(actDisbSumtTD);
		tbody.appendChild(lastTR);
		



		//tousands label
		if (strThousands==null || strThousands==''){
<gs:test name="<%=org.digijava.ampModule.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS%>" compareWith="2" onTrueEvalBody="true">
			strThousands='All amounts are in millions (000 000)';
</gs:test>			
<gs:test name="<%=org.digijava.ampModule.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS%>" compareWith="1" onTrueEvalBody="true">
			strThousands='All amounts are in thousands (000)';
</gs:test>
<gs:test name="<%=org.digijava.ampModule.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS%>" compareWith="0" onTrueEvalBody="true">
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
	
	function getFilterSettings(){
		var url=addActionToURL('getNPDFilters.do');
                url+=getURL()+'&timestamp=' +new Date().getTime();
		var async=new Asynchronous();
		async.complete=filterSettingsCallBack;
		async.call(url);
	}

    function getURL(){
        var url='';
         if (curProgId != null ){
			url+=p1d+'programId='+curProgId;
		}
        if (selActStatus != null && selActStatus != '0'&& selActStatus != ''){
			url += pd + 'statusId='+ selActStatus;
		}
		if(selActDonors !=null && selActDonors.match('-1') == null){
			url+= pd+ 'donorIds='+selActDonors;
		}
		if (selActYearTo != null && selActYearTo != -1){
			url+= pd + 'endYear='+selActYearTo;
		}
		if (selActYearFrom != null && selActYearFrom != -1){
			url+= pd + 'startYear='+selActYearFrom;
		}
        return url;
    }

	  


		
		function getDonorsHTML(donors,target){
			if (donors !=null && donors.length >0 && donors[0].tagName=='donors'){
				var donorList = donors[0].childNodes;
				if (donorList !=null && donorList.length>0){
					var donorsList = document.createElement('ul');
					
					for (var i=0; i<donorList.length; i++){
						var donorli = document.createElement('li');
						var name =document.createTextNode(donorList[i].getAttribute('name'));
						donorli.appendChild(name);
						donorsList.appendChild(donorli);
					}
					target.appendChild(donorsList);
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
						td.colSpan="6";
						td.innerHTML=stackList.childNodes[i].textContent;
						tr.appendChild(td);
						where.appendChild(tr);
					}
				}
			}
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
            var from = document.getElementById('yearFrom');
            selActYearFrom = from.value;
            var to= document.getElementById('yearTo');
            selActYearTo = to.value;
            if( selActYearTo!=-1 && selActYearFrom > selActYearTo){
            	var msg='<digi:trn jsFriendly="true">Please choose correct year range</digi:trn>';
            	alert(msg);
            	return false;
            }
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
            getActivities();             
            
        }
    </script>