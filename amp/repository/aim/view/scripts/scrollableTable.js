
function generateTableScrollbars(tableId,height){
//Autor Sebas, (I couldn't find a good solution on google)
// this scrtip is supported by IE FX and Crhome  

var table=document.getElementById(tableId);
table.style.visibility="hidden";

var theader=null;

	for (i=0;i< table.childNodes.length;i++){
		var node=table.childNodes[i];
		if (node.nodeName=="THEAD"){
			theader=node;
			break;
		}
	}
	
	var totalHeaderWidth=0;
	var lastHeaderCell;
	for (i=0;i < theader.rows.length;i++){
		for (j=0;j < theader.rows[i].cells.length -1;j++){
			theader.rows[i].cells[j].width=theader.rows[i].cells[j].offsetWidth;
			//theader.rows[i].cells[j].innerHTML=theader.rows[i].cells[j].offsetWidth;
			totalHeaderWidth+=theader.rows[i].cells[j].offsetWidth;
		}
		lastHeaderCell=theader.rows[i].cells[ theader.rows[i].cells.length -1];
		
	}
	


	
var tbody=null;
	for (i=0;i< table.childNodes.length;i++){
		var node=table.childNodes[i];
		if (node.nodeName=="TBODY"){
			tbody=node;
			break;
		}
	}
	
	var totalBodyWidth=0;
		for (j=0;j < tbody.rows[0].cells.length -1;j++){
			//tbody.rows[0].cells[j].innerHTML=tbody.rows[0].cells[j].offsetWidth;
			tbody.rows[0].cells[j].width=tbody.rows[0].cells[j].offsetWidth;
			totalBodyWidth+=tbody.rows[0].cells[j].offsetWidth;
		}
		
		
		
		lastHeaderCell.width=table.offsetWidth-totalHeaderWidth;
		//lastHeaderCell.innerHTML=table.offsetWidth-totalHeaderWidth;
		
		tbody.rows[0].cells[tbody.rows[0].cells.length -1].width=lastHeaderCell.width -16;
		//tbody.rows[0].cells[tbody.rows[0].cells.length -1].innerHTML=lastHeaderCell.width -16;
		
	
	
table.removeChild(theader);

//create an scroll div
var divContent=document.createElement("div");

divContent.style.border="0px solid red";
divContent.style.overflow="auto";
divContent.style.height=height+"px";
divContent.style.marginBottom="3px";
//insert the div  before the report table
table.parentNode.insertBefore(divContent,table)

//remove the report table
table.parentNode.removeChild(table);

//append the report table to the content div
divContent.appendChild(table);

//This is a clone of the orginal table, will be the new header table 
var newTable=document.createElement("table");
 newTable.setAttribute("cellSpacing",table.getAttribute("cellSpacing")); 
 newTable.setAttribute("cellPadding",table.getAttribute("cellPadding"));
 newTable.setAttribute("width",table.getAttribute("width"));
 newTable.setAttribute("class",table.getAttribute("class"));
 newTable.appendChild(theader);
 divContent.parentNode.insertBefore(newTable,divContent);

 table.style.visibility="visible";
}


function resize(){
	
	
	
	
}
