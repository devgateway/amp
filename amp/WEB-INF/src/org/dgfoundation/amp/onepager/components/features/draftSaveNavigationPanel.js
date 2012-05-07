var saveAsDraftPanel;
function showDraftPanel(){
    if(saveAsDraftPanel==null){
        saveAsDraftPanel = new YAHOOAmp.widget.Panel("saveAsDraftPanel", {
		width : "350px",
		fixedcenter : true,
		constraintoviewport : true,
		underlay : "shadow",
		close : true,
		visible : true,
		modal : true,
		draggable : false
	});
        $("span[name=saveAsDraftPanelButton]").find('input').removeAttr('disabled');
        $("#saveAsDraftPanel").show();
	saveAsDraftPanel.render();
        
    }
    else{
         $("span[name=saveAsDraftPanelButton]").find('input').removeAttr('disabled');
         saveAsDraftPanel.show();
    }
    return false;
}
function hideDraftPanel(){
	if(saveAsDraftPanel!=null){
		saveAsDraftPanel.hide();
	}
}
 