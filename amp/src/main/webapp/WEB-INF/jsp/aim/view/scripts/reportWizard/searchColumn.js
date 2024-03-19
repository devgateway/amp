

var arrayLI = new Array();
var scrollArray = new Array();
var scrollTreeArray = new Array();
var currentScrollItem = 0;

var minusImage = 'DHTMLSuite_minus.gif';
var plusImage = 'DHTMLSuite_plus.gif';

function searchFunction(no_match_msg)
{

	var searchCriteria = document.getElementById("searchCriteria").value;

	var dhtmlArray = document.getElementsByName("dhtmltreeArray");
	
//	IE Fix - document.getElementsByName() doesn't work for ie
	if (navigator.appVersion.indexOf("MSIE") != -1)
       {

	 var elem = document.getElementsByTagName('ul');
	 dhtmlArray = new Array();
     for(i = 0,iarr = 0; i < elem.length; i++) {
          att = elem[i].getAttribute("name");
          if(att == "dhtmltreeArray") {
        	  dhtmlArray[iarr] = elem[i];
               iarr++;
          }
       }
    
    }
	

	var searchableObjectsLIObject = new Array();
	var searchableObjectsLITree = new Array();
	var contador = 0;
	for(index=0; index < dhtmlArray.length;index++)
	{
		var liArray = document.getElementById(dhtmlArray[index].id).getElementsByTagName("LI");
		for(index2 = 0; index2 < liArray.length; index2++)
		{
			searchableObjectsLIObject[contador] = liArray[index2];
			searchableObjectsLITree[contador] = dhtmlArray[index].id;
			contador++;
		}
	}
	
	isKeycaptureOn = true;
	if(searchCriteria.length < 3) {
	//	alert("<digi:trn key="fm:search:searchQuestion">Please, enter a search of more than 2 characters.</digi:trn>");
		return;
	}
	
	for(index=0; index < dhtmlArray.length;index++)
	{
		treeCollapseAll(dhtmlArray[index].id);
	}
	for(index=0; index < dhtmlArray.length;index++)
	{
		resetHighlight(dhtmlArray[index].id);
	}
    currentScrollItem = 0;
	scrollArray = new Array();
	scrollTreeArray = new Array();
	
	searchExpandedNodes = "";
	var countMatches = 0;
	for(a=0; a< searchableObjectsLIObject.length; a++){
		var span = searchableObjectsLIObject[a].getElementsByTagName("SPAN")[0];
		var text = span.innerText;
		if(!text)
			text=span.textContent;
		
		if(text.toLowerCase().search(searchCriteria.toLowerCase()) != -1)
		{
			countMatches++;
			enableSearchButtons();

			var objectLI = searchableObjectsLIObject[a];
			var treeLI = searchableObjectsLITree[a];
			
			arrayLI = new Array();
			setBranch(objectLI, treeLI);
			searchExpandedNodes = treeLI;
			

			for(b=1;b<arrayLI.length;b++){
				searchExpandedNodes += "," + arrayLI[b];
			}			

			if(searchExpandedNodes){
				var nodes = searchExpandedNodes.split(',');
				for(var no=0;no<nodes.length;no++){
					if(nodes[no])
						{
						showNodeObject(false,nodes[no], treeLI);
						
						}
							
				}		
			}
		
			scrollArray[scrollArray.length] = objectLI;
		}
	}
	document.getElementById('nextSearchButton').focus();

	if(countMatches > 0){
		setSearchMessage('');
		scrollArray[0].style.backgroundColor = '#FF0000';
		scrollArray[0].style.color = '#EEEEEE';
		scrollArray[0].scrollIntoView(false);
	//	setSearchMessage( countMatches + " <digi:trn key="fm:search:matchesFound">items found</digi:trn>.");
	}
	else
	{
//		showNodeObject(false,"dhtmlgoodies_tree");
		setSearchMessage(no_match_msg);
		document.getElementById("prevSearchButton").style.display = 'none';
		document.getElementById("nextSearchButton").style.display = 'none';
	}	
	
}

function selectMeasure(measureEnglishName) {
	$('#source_measures_ul input[type=checkbox]').attr('checked', false);
	$('#source_measures_ul span[original_measure_name="' + measureEnglishName + '"]').closest('li').find('input[type=checkbox]').attr('checked', true);
	MyDragAndDropObject.selectObjs('source_measures_ul', 'dest_measures_ul');
}

function resetSearch(){
	disableSearchButtons();

	var dhtmlArray = document.getElementsByName("dhtmltreeArray");
	for(index=0; index < dhtmlArray.length;index++)
	{
		treeCollapseAll(dhtmlArray[index].id);
	}
	for(index=0; index < dhtmlArray.length;index++)
	{
		resetHighlight(dhtmlArray[index].id);
	}

	document.getElementById("searchCriteria").value = "";
	document.getElementById("spanSearchMessage").innerHTML = "";
	scrollArray = new Array();
}

function nextResult()
{
	highlightResultReset(scrollArray[currentScrollItem]);

	currentScrollItem++;
	if(currentScrollItem == scrollArray.length)
		currentScrollItem = 0;
//    if(scrollArray[currentScrollItem].getAttribute("display")=='none')
//    	nextResult();
    
	highlightResult(scrollArray[currentScrollItem]);
	scrollArray[currentScrollItem].scrollIntoView(false);

}

function prevResult()
{
	highlightResultReset(scrollArray[currentScrollItem]);

	currentScrollItem--;
	if(currentScrollItem == -1)
		currentScrollItem = scrollArray.length-1;
//	if(scrollArray[currentScrollItem].getAttribute("display")=='none')
//		prevResult();
	scrollArray[currentScrollItem].scrollIntoView(false);
	highlightResult(scrollArray[currentScrollItem]);

}



function highlightResultReset(linkObject)
{
	linkObject.style.backgroundColor = '#E1E1E1';
	linkObject.style.color = '#111111';
}

function highlightResult(linkObject)
{
	linkObject.style.backgroundColor = '#FF0000';
	linkObject.style.color = '#EEEEEE';
}



function setBranch(objectLI, treeLI)
{
	var tempObject = objectLI;
	var tempString = "";

	while(tempObject.id != treeLI)
	{
		if(tempObject.tagName == "LI")
		{
			arrayLI[arrayLI.length] = tempObject.id;
		}
		tempObject = tempObject.parentNode;
	}
}

function resetHighlight(treeId)
{
	var treeObject = document.getElementById(treeId);
	var collectionA = treeObject.getElementsByTagName("LI");
	if(!collectionA)
		return;
	
	for(ind = 0; ind < collectionA.length; ind++)
	{
			collectionA[ind].style.fontWeight='normal';
			highlightResultReset(collectionA[ind]);
		}
	
}


function treeCollapseAll(treeId)
{
	var treeObject = document.getElementById(treeId);
	var collectionUL = treeObject;
	
		if(collectionUL.style.display=='block')
		{
			collectionUL.style.display='none';
			var imageExpand = collectionUL.parentNode.getElementsByTagName("IMG");
			if(imageExpand[0].src.indexOf(minusImage)>=0){
				imageExpand[0].src = imageExpand[0].src.replace(minusImage, plusImage);
			}
		}
	
	
}
function enableSearchButtons()
{
	if(document.getElementById("prevSearchButton").style.display == 'none' && document.getElementById("nextSearchButton").style.display == 'none' )
	{
		document.getElementById("prevSearchButton").style.display = '';
		document.getElementById("nextSearchButton").style.display = '';
		document.getElementById("prevSearchButton").disabled = false;
		document.getElementById("nextSearchButton").disabled = false;
	}
}
function disableSearchButtons()
{

	if(!document.getElementById("prevSearchButton").disabled && !document.getElementById("nextSearchButton").disabled )
	{
		document.getElementById("prevSearchButton").style.display = 'none';
		document.getElementById("nextSearchButton").style.display = 'none';
		document.getElementById("prevSearchButton").disabled = true;
		document.getElementById("nextSearchButton").disabled = true;
	}
}


function setSearchMessage(stringMessage){
	var spanSearchMessage = document.getElementById("spanSearchMessage");
	spanSearchMessage.innerHTML = stringMessage;
}


	function showNodeObject(e,inputId, treeLI)
	{
		thisNode = document.getElementById(inputId);
		if(!thisNode){
			thisNode = document.getElementById(treeLI);
		}
		var ul = thisNode;
		ul.style.display='block';
		var img = ul.parentNode.getElementsByTagName('IMG')[0];
		img.src = img.src.replace(plusImage,minusImage);
		
	}
