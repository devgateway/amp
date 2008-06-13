function addUserOrTeam(){
		
	var reslist = document.getElementById('whoIsReceiver');
    var selreceivers=document.getElementById('selreceivers');

    if (reslist == null) {
        return false;
    }
	
    var index = reslist.selectedIndex;
    if (index != -1) {
        for(var i = 0; i < reslist.length; i++) {
            if (reslist.options[i].selected){
              if(selreceivers.length!=0){
                var flag=false;
                for(var j=0; j<selreceivers.length;j++){
                  if(selreceivers.options[j].value==reslist.options[i].value && selreceivers.options[j].text==reslist.options[i].text){
                    flag=true;
                  }
                }
                if(!flag){
                  addOnption(selreceivers,reslist.options[i].text,reslist.options[i].value);
                }
              }else{
                addOnption(selreceivers,reslist.options[i].text,reslist.options[i].value);
              }
            }	
        }
    }
    return false;
		
}
	
function addOnption(list, text, value){
    if (list == null) {
        return;
    }
    var option = document.createElement("OPTION");
    option.value = value;
    option.text = text;
    list.options.add(option);
    return false;
}
	
function removeUserOrTeam() {
	var tobeRemoved=document.getElementById('selreceivers');
	if(tobeRemoved==null){
		return;
	}		
	
	for(var i=tobeRemoved.length-1; i>=0; i--){
		if(tobeRemoved.options[i].selected){
			tobeRemoved.options[i]=null;
		}			
	}			
}