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

var warnSavePanel;
function showWarningPanel(){
    if(warnSavePanel==null){
        warnSavePanel = new YAHOOAmp.widget.Panel("warnSavePanel", {
            width : "450px",
            fixedcenter : true,
            constraintoviewport : true,
            underlay : "shadow",
            close : true,
            visible : true,
            modal : true,
            draggable : false
        });
        $("#warnSavePanel").show();
        warnSavePanel.render();
    }
    else{
        warnSavePanel.show();
    }
    return false;
}
function hideWarningPanel(){
    if(warnSavePanel!=null){
        warnSavePanel.hide();
    }
}
