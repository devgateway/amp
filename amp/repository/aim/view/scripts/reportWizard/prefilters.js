//form3 = aimReportsFilterPickerForm3;// document.getElementsByName('aimReportsFilterPickerForm3')[0];
// is this file ever used?

// load the TranslationManager object
$.getScript("/TEMPLATE/ampTemplate/script/common/TranslationManager.js");

var REPORT_URL = '/rest/data/report/';
var TEAM_URL = '/rest/security/ampTeam/';
var FILTER_OBJECT = 'workspace-filters-widget-format';
var WORKSPACE_FILTERS = 'workspace-filters';

function Filters(filterPanelName, connectionFailureMessage, filterProblemsMessage, loadingDataMessage,
                 savingDataMessage, cannotSaveFiltersMessage, doReset, settingsPanelName, validationMsgs, embedded) {
    this.connectionFailureMessage = connectionFailureMessage;
    this.filterProblemsMessage = filterProblemsMessage;
    this.loadingDataMessage = loadingDataMessage;
    this.savingDataMessage = savingDataMessage;
    this.cannotSaveFiltersMessage = cannotSaveFiltersMessage;
    this.validationMsgs = validationMsgs;
    this.settingsPanelName = settingsPanelName;
    this.filterPanelName = filterPanelName;
    this.additionalParameter = "";

    this.resetString = "&doreset=true";
    if (!doReset)
        this.resetString = "";

    this.filterPanel = new YAHOO.widget.Panel("new", {
        width: "870px",
        fixedcenter: true,
        constraintoviewport: true,
        underlay: "none",
        close: true,
        visible: false,
        modal: true,
        effect: {effect: YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
        draggable: true
    });
    this.filterPanel.setHeader(filterPanelName);
    this.filterPanel.setBody("");
    this.filterPanel.render(document.body);

    this.settingsPanel = new YAHOO.widget.Panel("new2", {
        width: "450px",
        fixedcenter: true,
        constraintoviewport: true,
        underlay: "none",
        close: true,
        visible: false,
        modal: true,
        effect: {effect: YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
        draggable: true
    });

    this.settingsPanel.setHeader(filterPanelName);
    this.settingsPanel.setBody("");
    this.settingsPanel.render(document.body);

    this.listFiltersDiv = document.getElementById("listFiltersDiv");
    this.hasFilters = document.getElementById("hasFilters");
    this.embedded = embedded;
}

// This method is used both by Filter Panel and Settings Panel.
function failureReportFunction(o) {
    var isLogged = false;
    var getUserInformation = {
        success: function (o) {
            var response = [];
            var currentPanel = o.argument.filter instanceof Filters ? o.argument.filter.filterPanel : o.argument.filter.panel;
            if (o.responseText !== undefined) {
                try {
                    // parse the json data
                    response = YAHOO.lang.JSON.parse(o.responseText);
                    // get the json attribute value of the 'logged' attribute
                    isLogged = response['logged'];
                    if (isLogged) {
                        // could be some connection issues
                        currentPanel.setBody("<font color='red'>" + this.filterProblemsMessage + "</font>");
                    } else {
                        // timeout to redirect
                        var timeout = 3;

                        // build the error message
                        var errorMessageUserLoggedOut = "<font color='red'>" + TranslationManager.getTranslated('Session is expired') + ". ";

                        errorMessageUserLoggedOut += TranslationManager.getTranslated('The page will be reloaded in') + " " + timeout + " " + TranslationManager.getTranslated('seconds') + "</font>.";
                        currentPanel.setBody(errorMessageUserLoggedOut);

                        var timer = setTimeout(function () {
                            window.location.reload();
                        }, timeout * 1000);
                    }
                } catch (x) {
                    var errorMessage = "<font color='red'> ";
                    errorMessage += TranslationManager.getTranslated('An error has occured') + x + "</font>.";
                    currentPanel.setBody(errorMessage);
                    return;
                }
            }
        },
        failure: function (o) {
            var currentPanel = o.argument instanceof Filters ? o.argument.filter.filterPanel : o.argument.filter.panel;
            var errorMessage = "<font color='red'> ";
            errorMessage += TranslationManager.getTranslated('The URL is unreachable') + o.responseText + "</font>.";
            currentPanel.setBody(errorMessage);
        },
        argument: {filter: this}
    };

    // get the information about the user
    YAHOO.util.Connect.asyncRequest("GET", "/rest/security/layout", getUserInformation);
};

Filters.prototype.success = function (o) {
    if (o.responseText.length > 2) {
        this.filterPanel.setBody(o.responseText);
        this.filterTabs = new YAHOO.widget.TabView('tabview_container');

        YAHOO.amptab.afterFiltersLoad();
        this.filterPanel.cfg.setProperty("height", "482px");

        this.filterPanel.show();

        this.saveFilters = new SaveFilters(this, false);

        //initCalendar();
        document.getElementById("filterPickerSubmitButton").onclick = function () {
            return false;
        };
        YAHOO.util.Event.removeListener("filterPickerSubmitButton", "click");
        YAHOO.util.Event.addListener("filterPickerSubmitButton", "click", this.saveFilters.saveFilters, this.saveFilters, this.saveFilters);

    } else {
        this.filterPanel.setBody("<font color='red'>" + this.filterProblemsMessage + "</font>");
    }
};

Filters.prototype.failure = failureReportFunction;

Filters.prototype.showFilters = function (reportContextId, auxId) {
    widgetFilter.reportContextId = reportContextId;
    if (widgetFilter.reportContextId === 'report_wizard') {
        widgetFilter.auxId = auxId; // used only for advanced search.
        if (widgetFilter.view.firstRender) {
            /*TODO: In order to improve loading speed of the filters we should avoid extra serialization/deserialization
            but right now those 2 processes do initializations we need for report generator.*/
            widgetFilter.deserialize({filters: {}}, {silent: true});
        }
        this.showFilterWidget();
    } else if (widgetFilter.reportContextId === 'workspace_editor') {
        var id = new URL(window.location).searchParams.get('tId');
        if (id && widgetFilter.gotSavedFilters !== true) {
            this.loadSavedFilterData(id, false);
        } else {
            this.showFilterWidget();
        }
    } else if (widgetFilter.gotSavedFilters !== true) {
        this.loadSavedFilterData(widgetFilter.reportContextId, true);
    } else {
        this.showFilterWidget();
    }
};

Filters.prototype.loadSavedFilterData = function (id, isReport) {
    var self = this;
    var url = isReport ? REPORT_URL : TEAM_URL;
    $.ajax({
        type: 'GET',
        url: url + id,
        success: function (data) {
            var filters = isReport ?
                data.reportMetadata.reportSpec.filters :
                (data[FILTER_OBJECT] ? data[FILTER_OBJECT] : {});
            filters.includeLocationChildren = isReport ?
                data.reportMetadata.reportSpec.includeLocationChildren :
                (data[WORKSPACE_FILTERS] ? data[WORKSPACE_FILTERS].includeLocationChildren : true);
            widgetFilter.deserialize({filters: filters}, {silent: true});
            self.showFilterWidget();
        }
    });
    widgetFilter.gotSavedFilters = true;
};

Filters.prototype.showFilterWidget = function () {
    widgetFilter.showFilters();
    $('#filter-popup').show();
};

Filters.prototype.showSettings = function (reportContextId) {
    initFormatPopup();
    this.saveFilters = new SaveFilters(this, true);
    var element = document.getElementById("customFormat");
    element.style.display = "inline";
    this.settingsPanel.setHeader(this.settingsPanelName);
    this.settingsPanel.setBody(element);
    this.settingsPanel.center();
    this.settingsPanel.show();

    YAHOO.util.Event.removeListener("applyFormatBtn", "click");
    document.getElementById("applyFormatBtn").onclick = function () {
        return false;
    };
    YAHOO.util.Event.addListener("applyFormatBtn", "click", this.saveFilters.validateAndSaveFilters, this.saveFilters, this.saveFilters);

    // Fix z-index problem on Public Report Generator without changing css loading order.
    this.fixZIndex("#new2_mask", 3);
};

Filters.prototype.fixZIndex = function (id, index) {
    $(id).attr('style', function (i, s) {
        return s + "z-index: " + index + " !important;"
    });
}

function SaveFilters(filterObj, showSettings) {
    this.filterObj = filterObj;
    this.showSettings = showSettings == null ? false : showSettings;
    this.panel = this.showSettings ? filterObj.settingsPanel : filterObj.filterPanel;
};

SaveFilters.prototype.validateAndSaveFilters = function (e, obj) {
    if (this.validateFormat()) {
        this.saveFilters(e, obj);
        var selector = document.getElementById('useFilter');
        if (selector) {
            selector.checked = true;
        }
    }
};
SaveFilters.prototype.validateFormat = function () {
    var decimalSymbol = document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
    decimalSymbol = ("custom" == decimalSymbol.toLowerCase()) ? document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value : decimalSymbol;

    var customDecimalPlaces = document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
    customDecimalPlaces = ("-2" == customDecimalPlaces.toLowerCase()) ? document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value : customDecimalPlaces;

    var customUseGrouping = document.aimReportsFilterPickerForm3.customUseGrouping.checked;

    var customGroupCharacter = document.aimReportsFilterPickerForm3.customGroupCharacter.value;
    customGroupCharacter = ("custom" == customGroupCharacter.toLowerCase()) ? document.aimReportsFilterPickerForm3.customGroupCharacterTxt.value : customGroupCharacter;

    var customGroupSize = document.aimReportsFilterPickerForm3.customGroupSize.value;

    if ((decimalSymbol == customGroupCharacter) && (customUseGrouping)) {
        var msg = this.filterObj.validationMsgs[0];
        alert(msg);
        return false;
    }
    var validNumbers = "0123456789";

    if (decimalSymbol == "" || customGroupCharacter == "") {
        var msg = this.filterObj.validationMsgs[1];
        alert(msg);
        return false;
    }


    if ((validNumbers.indexOf(decimalSymbol) != -1) || (validNumbers.indexOf(customGroupCharacter) != -1)) {
        var msg = this.filterObj.validationMsgs[2];
        alert(msg);
        return false;
    }

    if ((customGroupSize < 1) && (document.aimReportsFilterPickerForm3.customUseGrouping.checked == true)) {
        var msg = this.filterObj.validationMsgs[3];
        alert(msg);
        return false;
    }

    var yearStart = $('#renderStartYear') ? $('#renderStartYear').val() : null;
    var yearEnd = $('#renderEndYear') ? $('#renderEndYear').val() : null;
    if (yearStart && yearEnd) {
        yearStart = parseInt(yearStart);
        yearEnd = parseInt(yearEnd);
        if (yearStart > 0 && yearEnd > 0 && yearStart > yearEnd) {
            var msg = this.filterObj.validationMsgs[4];
            alert(msg);
            return false;
        }
        ;
    }
    return true;
};

SaveFilters.prototype.saveFilters = function (e, obj) {
    var avoidIECacheParam = "&time=" + new Date().getTime();
    var formName = "aimReportsFilterPickerForm" + (this.showSettings ? "3" : "");
    YAHOO.util.Connect.setForm(document.getElementsByName(formName)[0]);
    var additionalParams = this.showSettings ? "&sourceIsReportWizard=true" : "";
    if (this.showSettings)
        YAHOO.util.Connect.asyncRequest("POST", "/aim/reportsFilterPicker.do?apply=true&applyFormat=Apply%20Format" + avoidIECacheParam + additionalParams, obj);
    else
        YAHOO.util.Connect.asyncRequest("POST", "/aim/reportsFilterPicker.do?apply=true" + avoidIECacheParam + additionalParams, obj);


    if (this.showSettings) {

        var element = document.getElementById("customFormat");
        element.parentNode.removeChild(element);

        var hiddenDiv = document.getElementById("myHiddenDiv"); //document.createElement('DIV');
        //hiddenDiv.setAttribute('style','display:none;');
        //hiddenDiv.setAttribute('id','hiddenDiv');
        hiddenDiv.appendChild(element);
        //document.body.appendChild(hiddenDiv);

        //document.body.appendChild(element);
    }
    this.panel.setBody("<div style='text-align: center'>" + obj.filterObj.savingDataMessage +
        "... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>");

    return false;
};

SaveFilters.prototype.success = function (o) {
    if (o.responseText.length > 0) {
        this.panel.hide();
        if (this.filterObj.hasFilters !== null && this.filterObj.hasFilters !== undefined) {
            this.filterObj.hasFilters.value = true;
        }
        this.filterObj.listFiltersDiv.innerHTML = o.responseText;
        if (!(typeof repManager === 'undefined')) {
            repManager.showUseFilters(true);
        }
    } else
        this.panel.setBody(this.filterObj.cannotSaveFiltersMessage);
};

SaveFilters.prototype.failure = failureReportFunction;

function initFormatPopup() {


    var decimalSymbol = document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
    if (decimalSymbol.toLowerCase() == "custom") {
        document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled = false;

    } else {
        document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value = "";
        document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled = true;
    }

    var customDecimalPlaces = document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
    if (customDecimalPlaces.toLowerCase() == "-2") {
        document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.disabled = false;
    } else {
        document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value = "";
        document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.disabled = true;
    }


    var customUseGrouping = document.aimReportsFilterPickerForm3.customUseGrouping.checked;

    if (!customUseGrouping) {
        document.aimReportsFilterPickerForm3.customGroupCharacter.disabled = true;
    } else {
        document.aimReportsFilterPickerForm3.customGroupCharacter.disabled = false;
    }
    var customGroupCharacter = document.aimReportsFilterPickerForm3.customGroupCharacter.value;
    document.aimReportsFilterPickerForm3.customGroupSize.disabled = !customUseGrouping;
    document.aimReportsFilterPickerForm3.customGroupCharacterTxt.disabled = ((!customUseGrouping) || ("custom" != customGroupCharacter.toLowerCase()));

    changeFormat();
}

function changeFormat() {
    var decimalSymbol = document.aimReportsFilterPickerForm3.customDecimalSymbol.value;
    decimalSymbol = (decimalSymbol.toLowerCase() == "custom") ? document.aimReportsFilterPickerForm3.customDecimalSymbolTxt.value : decimalSymbol;

    var customDecimalPlaces = document.aimReportsFilterPickerForm3.customDecimalPlaces.value;
    customDecimalPlaces = (customDecimalPlaces.toLowerCase() == "-2") ? document.aimReportsFilterPickerForm3.customDecimalPlacesTxt.value : customDecimalPlaces;

    var customUseGrouping = document.aimReportsFilterPickerForm3.customUseGrouping.checked;


    var customGroupCharacter = document.aimReportsFilterPickerForm3.customGroupCharacter.value;
    customGroupCharacter = (customGroupCharacter.toLowerCase() == "custom") ? document.aimReportsFilterPickerForm3.customGroupCharacterTxt.value : customGroupCharacter;


    var customGroupSize = document.aimReportsFilterPickerForm3.customGroupSize.value;


    var num = Number(123456789.928);


    var format = new Format(decimalSymbol, customDecimalPlaces, customUseGrouping, customGroupCharacter, customGroupSize);
    document.getElementById("number").innerHTML = "<B>" + num.format(format) + "</B>";
    //alert(num.format(format));
    return true;
}

function ResetCustom(maxFractionDigits) {
    aimReportsFilterPickerForm3.customDecimalSymbol.value = ",";
    aimReportsFilterPickerForm3.customDecimalSymbolTxt.value = "";
    aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled = "true";
    aimReportsFilterPickerForm3.customDecimalPlaces.value = maxFractionDigits;
    aimReportsFilterPickerForm3.customDecimalPlacesTxt.value = "";
    aimReportsFilterPickerForm3.customDecimalPlacesTxt.disabled = "true"
    aimReportsFilterPickerForm3.customUseGrouping.checked = "true";
    aimReportsFilterPickerForm3.customGroupCharacter.value = ".";
    aimReportsFilterPickerForm3.customGroupCharacterTxt.value = "";
    aimReportsFilterPickerForm3.customGroupSize.value = 3;
    //alert(aimReportsFilterPickerForm3.amountinthousands.checked);
    aimReportsFilterPickerForm3.customAmountinThousands.options.selectedIndex = 0;
    aimReportsFilterPickerForm3.calendar.value = aimReportsFilterPickerForm3.initialCal.value;
    initFormatPopup();
    if (aimReportsFilterPickerForm3.currency)
        aimReportsFilterPickerForm3.currency.value = aimReportsFilterPickerForm3.defaultCurrency.value;
}



