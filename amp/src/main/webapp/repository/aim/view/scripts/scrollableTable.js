
function scrollableTable(tableId,height){
	//Global Variables
	this.debug=false;
	this.usePercentage=false;
	this.table=document.getElementById(tableId);
	this.theader=null;
	this.lastHeaderCell=null;
	this.tbody=null;
	this.maxRowDepth=-1;
	this.headerValues=new Array();
	this.useFixForDisplayNoneRows=false;

	//Main Function
	this.scroll=function(){

		var isIE=navigator.appName.indexOf("Microsoft")!=-1;
		try{
			this.table.style.visibility="hidden";
			//find header rows
			for (i=0;i< this.table.childNodes.length;i++){
				var node=this.table.childNodes[i];
				if (node.nodeName=="THEAD"){
					this.theader=node;
					break;
				}
			}
	
			for (i=0;i< this.table.childNodes.length;i++){
				var node=this.table.childNodes[i];
				if (node.nodeName=="TBODY"){
					this.tbody=node;
					break;
				}
			}
			
			//check scroll % or px
			var scrollSize = height ? 16 : 0;
			if (this.usePercentage) {
				scrollSize = (16*100)/this.table.offsetWidth;
			}
			
			// the lines below were commented out to take into consideration height parameter
			//check scroll % or px
			//var scrollSize=(this.usePercentage)?(16*100)/this.table.offsetWidth:16;
			//set the body cells width =offsetWidth  set the last one =offsetWidth-scrollSize
		
			//get the body rows
			this.maxRowDepth=(this.maxRowDepth==-1)?this.tbody.rows.length:this.maxRowDepth;
			
			
			this.setHeaderWidth=function(){
			

				cellWidths = []
				counter = 0;
				for (i=0;i < this.theader.rows.length;i++){
					var padding=0;
					var border=0;
					var perPadding=0;
					var perBorder=0;

					if (!height){//there is no scroll bar, so the last header empty column is not necessary
						var lastIndex = this.theader.rows[i].cells.length - 1;
						this.theader.rows[i].removeChild (this.theader.rows[i].cells[lastIndex]);
					}
					
					for (j=0;j < this.theader.rows[i].cells.length  ;j++){
						var thisCell = this.theader.rows[i].cells[j]
						if(!isIE){
							var paddingRight = parseInt(thisCell.style.paddingLeft)
							var paddingLeft = parseInt(thisCell.style.paddingRight)
							if (isNaN(paddingRight)) paddingRight = 0;
							if (isNaN(paddingLeft)) paddingLeft = 0;
							padding = paddingRight + paddingLeft;
							perPadding = (padding * 100)/this.table.offsetWidth;
	
							var borderRight = parseInt(thisCell.style.borderRightWidth)
							if (isNaN(borderRight)) borderRight = 0;
							var borderLeft = parseInt(thisCell.style.borderLeftWidth)
							if (isNaN(borderLeft)) borderLeft = 0;
							border = borderRight + borderLeft
							perBorder=(border*100)/this.table.offsetWidth;
						} 
						var ow = thisCell.offsetWidth;
						var pxValue = ow - padding - border;
						var perValue = ((ow*100)/this.table.offsetWidth) - perPadding - perBorder;
						cellWidths[counter] = (this.usePercentage)?(perValue+"%") : pxValue;
						counter++;
					}
					counter++;
				}
	
					counter = 0;
					for (i=0;i < this.theader.rows.length;i++){
						for (j=0;j < this.theader.rows[i].cells.length  ;j++){
							var thisCell = this.theader.rows[i].cells[j]
							thisCell.width = cellWidths[counter];
							counter++;
						}
						counter++;
					}
			}	
				
			//this function should be used only in the case that the rows are using display = none
			 this.setBodyForIE=function(){
			 			var rowIndex	= 3;
			 			if ( this.tbody.rows.length <= 3 )
			 				rowIndex	= this.tbody.rows.length -1;
						var nclone=this.tbody.rows[rowIndex].cloneNode(true);//the clone of the first  full row
						nclone.style.display='';
						
						nclone.setAttribute("id","ignoreToggle");
						this.tbody.appendChild(nclone);//appends the clone
					
					for (j=0;j <nclone.cells.length ;j++){
						
						var perValue=((nclone.cells[j].offsetWidth*100)/this.table.offsetWidth);
						var pxValue=nclone.cells[j].offsetWidth;
						//nclone.cells[j].innerHTML="";
						if (j==nclone.cells.length -1){
								if(pxValue >0){
									nclone.cells[j].width=(this.usePercentage)?((perValue-scrollSize)+"%"):pxValue-scrollSize;
								}
									nclone.cells[j].innerHTML="";
								if (this.debug){
									nclone.cells[j].innerHTML=nclone.cells[j].width;
								}
							}else{//no last row
								if(pxValue >0){
									nclone.cells[j].width=(this.usePercentage)?(perValue+"%"):pxValue;
								}
									nclone.cells[j].innerHTML="";
								if (this.debug){
									nclone.cells[j].innerHTML=nclone.cells[j].width;
								}
							}				
							
					}//end for cells
			 }//end function
			
			//this is the normal setbodyWidthFUnction
			 this.setBodyWidth=function(){
//			 	var maxRowNum	= this.maxRowDepth;
//			 	if ( this.maxRowDepth > this.tbody.rows.length ) 
				//longestCellCount is used to stop this process in the longest cell, in case is a hierarchy
				longestCellCount = 0;
				for(i=0;i<this.tbody.rows.length;i++)
				{
					if(longestCellCount < this.tbody.rows[i].cells.length)
						longestCellCount = this.tbody.rows[i].cells.length
				}
			 	var	maxRowNum	= this.tbody.rows.length;

				counter = 0;
				cellWidths = []
				for (i=0; i<maxRowNum  ;i++){
						//set cells widths
						for (j=0; j<this.tbody.rows[i].cells.length ;j++){
							var padding=0;
							var border=0;
							var perBorder=0;
							var perPadding=0;
							if(!isIE){
								//calculate padding
								var str=this.tbody.rows[i].cells[j].style.paddingLeft.substr(0,this.tbody.rows[i].cells[j].style.paddingLeft.length-2);
								if (str==""){str="0"};
								var padding=parseInt(str);
								str=this.tbody.rows[i].cells[j].style.paddingRight.substr(0,this.tbody.rows[i].cells[j].style.paddingRight.length-2);
								if (str==""){str="0"};
								padding+=parseInt(str);
								var perPadding=(padding*100)/this.table.offsetWidth;
							
								//calculate border
								str=this.tbody.rows[i].cells[j].style.borderRightWidth.substr(0,this.tbody.rows[i].cells[j].style.borderRightWidth.length-2);
								if (str==""){str="0"};	
								var border=parseInt(str);
								str=this.tbody.rows[i].cells[j].style.borderLeftWidth.substr(0,this.tbody.rows[i].cells[j].style.borderLeftWidth.length-2);
								if (str==""){str="0"};	
								border+=parseInt(str);
								var perBorder=(border*100)/this.table.offsetWidth;
							}
								
							var perValue=((this.tbody.rows[i].cells[j].offsetWidth*100)/this.table.offsetWidth) - perPadding -perBorder;
							var pxValue=this.tbody.rows[i].cells[j].offsetWidth - padding -border;
							cellWidths[counter] =  this.usePercentage ? (perValue+"%") : pxValue;
							counter++;
						
						}	
						//Stop this process in the longest cell, that should be inside a hierarchy
						if(longestCellCount == this.tbody.rows[i].cells.length) {
							break;
						}
					//end for cells	
			}	
				///end for rows
				counter = 0
				for (i=0; i<maxRowNum  ;i++){
						//set cells widths
						for (j=0; j<this.tbody.rows[i].cells.length ;j++){
							perValue = pxValue = cellWidths[counter];
							counter++;
							//last row
							if (j==this.tbody.rows[i].cells.length -1){
								if(pxValue > 0){
									this.tbody.rows[i].cells[j].width=(this.usePercentage)?((perValue-scrollSize)+"%"):pxValue-scrollSize;
								}
								if (this.debug){
									this.tbody.rows[i].cells[j].innerHTML="last:"+this.tbody.rows[i].cells[j].width;
								}
							}else{//no last row
								if(pxValue >0){
									this.tbody.rows[i].cells[j].width=(this.usePercentage)?(perValue+"%"):pxValue;
								}
								if (this.debug){
									this.tbody.rows[i].cells[j].innerHTML=this.tbody.rows[i].cells[j].innerHTML+"-"+this.tbody.rows[i].cells[j].width;
								}
							}				
						
							this.tbody.rows[i].cells[j].style.overflow="hidden";
						}
						//Stop this process in the longest cell, that should be inside a hierarchy
						if(longestCellCount == this.tbody.rows[i].cells.length) {
							break;
						}
					//end for cells	
			}	
				
				
			}		
		//end function	
		
			
		
		
			
		if(this.useFixForDisplayNoneRows){
			this.setBodyForIE();
		}
		
		this.setHeaderWidth();
		this.setBodyWidth();
		
		
		//remove the table header 
		this.table.removeChild(this.theader);
		
		//create the body table container DIV
		var divContent=document.createElement("div");
		
			//set container properties
		if (this.debug)
			divContent.style.border="1px solid red";
		divContent.style.overflowX="hidden";
		if(height){
			divContent.style.overflow="scroll";
			divContent.style.height=height+"px";
		}else{
			divContent.style.overflow="auto";
			divContent.style.height="100%";
		}
		divContent.style.marginBottom="3px";
		
		//insert the div  before the original table
		this.table.parentNode.insertBefore(divContent,this.table)
		
		//remove the original table
		this.table.parentNode.removeChild(this.table);
		
		//append the original table to the content div
		divContent.appendChild(this.table);
		
		//create a new table for the header
		var newTable=document.createElement("table");
		 newTable.setAttribute("cellSpacing",this.table.getAttribute("cellSpacing")); 
		 newTable.setAttribute("cellPadding",this.table.getAttribute("cellPadding"));
		 newTable.setAttribute("width",this.table.getAttribute("width"));
		 newTable.setAttribute("class",this.table.getAttribute("class"));
		 newTable.appendChild(this.theader);
		 divContent.parentNode.insertBefore(newTable,divContent);
		 this.table.style.visibility="visible";
		}catch(e){
			//alert(e);
		}
	}
	
	//end scroll function

}//end scrollable table
