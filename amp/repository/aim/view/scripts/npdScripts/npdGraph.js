function getNewGraph(){
		//var now = new Date().getTime();
		lastTimeStamp = new Date().getTime();
		var url=constructGraphUrl(lastTimeStamp);
		var graphTag=document.getElementById('graphImage');
		graphTag.src=url;
	}

	function constructGraphUrl(timestmp){
		var url=addActionToURL('npdGraph.do');
		
		url+=p1d+'actionMethod=displayChart';
		url+=pd+'timestamp='+timestmp;
		if (curProgId != null){
			url += pd + 'currentProgramId=' + curProgId;
			if ( (selIndicatorIDs != null) && (selIndicatorIDs.length > 0)){
				for (var i=0; i<selIndicatorIDs.length; i++){
					url += pd + 'selectedIndicators=' + selIndicatorIDs[i];
				}
			}
			if ( selYear != null && selYear.length>0) {
				for (var y=0; y<selYear.length; y++) {
					url += pd + 'selectedYears=' + selYear[y];
				}
			}
		}
		curGraphURL = url;
		return url;
	}

	function mapCallBack(status, statusText, responseText, responseXML){
		updateMap(responseText);
	}

	function updateMap(resp){
		var mapHolder= document.getElementById('graphMapPlace');
		var map= document.getElementById('npdChartMap');
		var image = document.getElementById('graphImage');
		image.removeAttribute('usemap');
		mapHolder.innerHTML=resp;
		image.setAttribute('usemap','#npdChartMap');
	}

	function constructMapUrl(timestmp){
		var url=addActionToURL('getNpdGraphMap.do');
		url+=p1d+'timestamp='+timestmp;
		return url;
	}

	function getGraphMap(timestamp){
		var url=constructMapUrl(timestamp);
		var async=new Asynchronous();
		async.complete=mapCallBack;
		async.call(url);
	}

	function graphLoaded(){
			getGraphMap(lastTimeStamp);
			setGraphVisibility(true);
	}

	function setGraphVisibility(show){
		var loadingDiv=document.getElementById('divGraphLoading');
		var graphDiv=document.getElementById('divGraphImage');
		if(show){
			loadingDiv.style.display='none';
			graphDiv.style.display='block';
		}else{
			loadingDiv.style.display='block';
			graphDiv.style.display='none';
		}
	}