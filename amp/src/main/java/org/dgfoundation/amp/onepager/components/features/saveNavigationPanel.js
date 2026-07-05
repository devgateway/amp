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
        
        saveAsDraftPanel.beforeHideEvent.subscribe(function() {
        	enableButtons2();
        	});
        saveAsDraftPanel.render();
        $("#saveAsDraftPanel_mask").css("z-index", 2);
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
//Functions for the window to reject activity
var rejectActivityPanel;
function showRejectActivityPanel(){
    if(rejectActivityPanel==null){
    	rejectActivityPanel = new YAHOOAmp.widget.Panel("rejectActivityPanel", {
            width : "350px",
            fixedcenter : true,
            constraintoviewport : true,
            underlay : "shadow",
            close : true,
            visible : true,
            modal : true,
            draggable : false
        });
        $("span[name=rejectActivityPanelButton]").find('input').removeAttr('disabled');
        $("#rejectActivityPanel").show();
        rejectActivityPanel.render();

    }
    else{
        $("span[name=saveAsDraftPanelButton]").find('input').removeAttr('disabled');
        rejectActivityPanel.show();
    }
    return false;
}
function hideRejectActivityPanel(){
    if(rejectActivityPanel!=null){
    	rejectActivityPanel.hide();
    }
}

var trubudgetClosePanel;
function showTruBudgetClosePanel(){
    if(trubudgetClosePanel==null){
        trubudgetClosePanel = new YAHOOAmp.widget.Panel("trubudgetClosePanel", {
            width : "420px",
            fixedcenter : true,
            constraintoviewport : true,
            underlay : "shadow",
            close : true,
            visible : true,
            modal : true,
            draggable : false
        });

        $("#closeProjectOnTruBudgetCheckbox").prop("checked", false);
        $("#trubudgetClosePanel").show();

        trubudgetClosePanel.beforeHideEvent.subscribe(function() {
            enableButtons2();
        });
        trubudgetClosePanel.render();
    }
    else{
        $("#closeProjectOnTruBudgetCheckbox").prop("checked", false);
        trubudgetClosePanel.show();
    }

    return false;
}

function hideTruBudgetClosePanel(){
    if(trubudgetClosePanel!=null){
        trubudgetClosePanel.hide();
    }
}
