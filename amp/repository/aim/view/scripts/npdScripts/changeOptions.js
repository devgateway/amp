function changeOptions(indics,years,locations){
    selIndicatorIDs=indics;
    selYear=years;
    hideGridTable();
    getNewGraph();
}

function getIndicators(){
	if (curProgId == null) {
		alert('${noProgSelected}');
		return;
	}
	var indList=document.getElementById('indicatorsResultsPlace');
	setLoadingImage(indList);
	var url=getIndicatorsURL();
	var async=new Asynchronous();
	async.complete=indicatorsCallBack;
	async.call(url);
}

function getIndicatorsURL(){
	var result = addActionToURL('getProgramIndicators.do');
	result+=p1d+'programId='+curProgId;
	return result;
}

function indicatorsCallBack(status, statusText, responseText, responseXML){
	var place= document.getElementById('indicatorsResultsPlace');
	clearChildren(place);
	var root=responseXML.getElementsByTagName('indicatorsList')[0];
	
	if(root==null){
		alert("Error!");
		return;
	}
	//get indicators array
	var indList = root.childNodes;
	for (var i=0; i< indList.length; i++) {
		var indicator=indList[i];
		var checkbox = document.createElement('input');
		checkbox.type = "checkbox";
		checkbox.name = "selIndicators";
		checkbox.defaultChecked = true;
		checkbox.value = indicator.getAttribute('id');
		var label = document.createTextNode(indicator.getAttribute('name'));
		var newLine=document.createElement('br');
		place.appendChild(checkbox);
		place.appendChild(label);
		place.appendChild(newLine);
		
    }
	
}

function getSelectedIndicators(){
	var res = [];
	var localIndicators=$('input[type=checkbox][name="selIndicators"]:checked').each(function() {
		res.push($(this).val());
    }); 
	return res;
}

function getSelectedYears(){
	var res = [];
	var localYears =$('input[type=checkbox][name="selYears"]:checked').each(function() {
		res.push($(this).val());
    }); 
	return res;
}

function doChanges(){
	var ins=getSelectedIndicators();
	var yrs=getSelectedYears();
	yrs.sort();
	var partialUrl=addActionToURL('saveDefaultYearsForGraph.do');
	var url=getYearsForUrl(partialUrl);
	var async=new Asynchronous();
	async.complete=emptyFunction;
	async.call(url);
	changeOptions(ins,yrs,null);
}

function getYearsForUrl(url){
	var locYears = document.getElementsByName('selYears');		
	var result=url;
	if(locYears.length>0){
		var res;		
		for (var i = 0; i < locYears.length; i++) {
			if(locYears[i].checked){
				res=locYears[i].value;	
				result+='~selYears='+res;			
			}
		}			
	}		
	return result;
}

function emptyFunction (){
}

function checkYearsRules(){
	var locYears = document.getElementsByName('selYears');
	var cou=0;
	for(var i=0;i<locYears.length;i++){
		if (locYears[i].checked == true) cou++;
	}
	if (cou > 5) return false;
	return true;
}