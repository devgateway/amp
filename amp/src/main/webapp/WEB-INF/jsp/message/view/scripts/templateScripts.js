
var MyArray=new Array();

function initMyArray(){
  var reslist = document.getElementById('whoIsReceiver');
  var teamIndex = 0;
  for(var i = 0; i < reslist.length; i++) {
    if(reslist.options[i].value.indexOf('t')==0){//it is a team
      MyArray[teamIndex]=new Array();
      MyArray[teamIndex][0]=reslist.options[i];
      teamIndex++;
     }
  }
}
function addUserOrTeam(){
    
  var reslist = document.getElementById('whoIsReceiver');
  var selreceivers=document.getElementById('selreceivers');

  if(reslist == null) {
    return false;
  }
  var Mindex=reslist.selectedIndex;
    
  initMyArray();//creates the empty array
  
  if(selreceivers.length!=0){
    getCurrentSelectedReceivers(); //fills the array with existing selected options
  }
  if (Mindex != -1) {
    for(var i = 0; i < reslist.length; i++) {
      if(reslist.options[i].selected){
        if(reslist.options[i].value=="all"){
          selectAllReceivers();
          break;
        }
        else if(document.getElementById('whoIsReceiver').options[i].value.indexOf('t')==0){//the option is a team
          var Myrow=getTeamRow(reslist.options[i].value);
          //clean all row
          for(var col=1; col<MyArray[Myrow].length; col++){
             MyArray[Myrow][col]=null;
          }
          //fill the row
          var Mycol=1;
          for(var index=0; index<reslist.length; index++){
            if(reslist.options[index].id==MyArray[Myrow][0].value){
              MyArray[Myrow][Mycol]=reslist.options[index];
              Mycol++
            }
          }
        }  
        else{//the option is a member
          if(!isOptionSelected(reslist.options[i])){//it is not at the list yet
            var Myrow=getTeamRow(reslist.options[i].id);
            var Mycol=MyArray[Myrow].length;
            MyArray[Myrow][Mycol]=reslist.options[i];
          } 
        }
      }
    }
    showReceivers();
  }
  return false;
}
function getTeamRow(idTeam){
  for(var row=0; row<MyArray.length; row++){
    if(MyArray[row][0].value==idTeam){
      return row;
    }
  }  
}
function isOptionSelected(option){
  var selreceivers=document.getElementById('selreceivers');
  for(var j=0; j<selreceivers.length;j++){
    if(selreceivers.options[j].value==option.value){
      return true;
    }
  }
  var row=getTeamRow(option.id);
  for(var col=1;col<MyArray[row].length;col++){
    if(MyArray[row][col].value==option.value){
      return true;
    }
  }
  return false
}

function selectAllReceivers(){
  var reslist = document.getElementById('whoIsReceiver');
  var selreceivers=document.getElementById('selreceivers');
  initMyArray();
  for(var h; h<selreceivers.length; h++){
    selreceivers.options[h]=null
  }  
  selreceivers.options.length=0;
  for(var i=1; i<reslist.length; i++){
    if(reslist.options[i].value.indexOf('m')==0){//it is not at the list yet
      var Myrow=getTeamRow(reslist.options[i].id);
      var Mycol=MyArray[Myrow].length;
      MyArray[Myrow][Mycol]=reslist.options[i];
    } 
  }      
}
function showReceivers(){
  var selreceivers=document.getElementById('selreceivers');
  for(var h; h<selreceivers.length; h++){
    selreceivers.options[h]=null
  }  
  selreceivers.options.length=0;
  for(var i=0; i<MyArray.length; i++){
    if(MyArray[i][1]!=null){
      for(var j=0; j<MyArray[i].length; j++){
        if(MyArray[i][j]!=null){
          addOnption(selreceivers,MyArray[i][j].text,MyArray[i][j].value, MyArray[i][j].id);
        }
      }
    }
  }
}
function getCurrentSelectedReceivers(){
  var selreceivers=document.getElementById('selreceivers');
  for(var i = 0; i < selreceivers.length; i++) {
    if(selreceivers.options[i].id.indexOf('t')==0){//filters team members, they have id=team's id
      var row = getTeamRow(document.getElementById('selreceivers')[i].id);
      var col = MyArray[row].length;
      MyArray[row][col]=selreceivers.options[i];
    }
  }
} 
function registerOrphanMember(orphans){
   for(var i=0; i<orphans.length; i++){
      var itsTeam = getTeam(orphans[i].value);
      orphans[i].id=itsTeam;
   }    	
}
function getTeam(memberValue){
  var reslist = document.getElementById('whoIsReceiver');
  var found = false;
  for(var i=reslist.length-1; i>0; i--){
  	if(reslist.options[i].value==memberValue){
  	   return reslist.options[i].id;
  	}
  }
  return "";
}
function addOnption(list, text, value, id){
    if (list == null) {
        return;
    }
    var option = document.createElement("OPTION");
    option.value = value;
    option.text = text;
    option.id = id;
    list.options.add(option);
    return false;
}
  
function removeUserOrTeam() {
  var tobeRemoved=document.getElementById('selreceivers');
  if(tobeRemoved==null){
    return;
  }   
  var teamId=-1;
  if($("#selreceivers > option[value='guest']:selected").length>0){
       $("#selreceivers > option[value^='c:']").remove();
  }
  for(var i=document.getElementById('selreceivers').length-1; i>=0; i--){
    if(document.getElementById('selreceivers')[i].selected){
      if(document.getElementById('selreceivers')[i].value.indexOf('t')==0){//it's a team
        var elem = document.getElementById('selreceivers').length
        for(var j=elem-1; j>=0; j--){
          if(document.getElementById('selreceivers')[j].id == document.getElementById('selreceivers')[i].value){
            document.getElementById('selreceivers')[j]=null;
          }
        }
      }
      else{// it is a team member
        //get team's id from the team member
        teamId = document.getElementById('selreceivers')[i].id;
      }
      document.getElementById('selreceivers')[i]=null;
      if(teamId!=-1){//if a member has been removed
        var noMember=true;
        //check if another member belonging to the same team exists
        for(var j=document.getElementById('selreceivers').length-1; j>=0; j--){
          if(document.getElementById('selreceivers')[j].id==teamId){
            noMember=false;//there is member belonging to the same team
          }
        }
        if(noMember){
          //there are not members so, remove the team from the receivers
          for(var h=document.getElementById('selreceivers').length-1; h>=0; h--){
            if(document.getElementById('selreceivers')[h].value==teamId){
              document.getElementById('selreceivers')[h]=null;
              i--;
            }
          }     
        }
      }     
    }     
  }
  if($("#selreceivers > option[value^='c:']").length==0){
        $("#selreceivers > option[value='guest']").remove();
   }
}

function addActionToURL(actionName){
  var fullURL=document.URL;
  var lastSlash=fullURL.lastIndexOf("/");
  var partialURL=fullURL.substring(0,lastSlash);
  return partialURL+"/"+actionName;
}