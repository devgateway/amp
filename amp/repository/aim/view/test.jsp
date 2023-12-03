/*
function loadPage(){
  <digi:context name="selectLoc" property="context/ampModule/moduleinstance/editOrgGroup.do" />
  var id = document.aimAddOrgForm.ampOrgId.value;
  url = "<%= selectLoc %>?action=createGroup&ampOrgId=" + id;
  openOrgWindow(610, 190);
  var oldAct=document.aimAddOrgForm.action;
  var oldCurrUrl=document.aimAddOrgForm.currUrl.value;
  document.aimAddOrgForm.action = url;
  document.aimAddOrgForm.currUrl.value = "<%= selectLoc %>";
  document.aimAddOrgForm.target = popupPointer.name;
  document.aimAddOrgForm.submit();
  document.aimAddOrgForm.target="";
  document.aimAddOrgForm.action=oldAct;
}
	
	*/