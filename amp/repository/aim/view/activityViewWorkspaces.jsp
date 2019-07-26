<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<div id="viewWorkspaces" class="invisible-item">
    <div id="viewWorkspacesContent" class="content">
    </div>
    <div id="tableContent" class="invisible-item">
        <table id="viewWorkspacesTable" width="100%">
            <tr height="20">
                <td bgcolor="#999999"></td>
                <td bgcolor="#999999" nowrap>
                    <b><digi:trn>Workspace where activity is displayed</digi:trn></b>
                </td>
                <td bgcolor="#999999" nowrap width="120px">
                    <div class="ws-tooltip">
                        <b><digi:trn>Workspace Type</digi:trn></b>
                        <img src="/TEMPLATE/ampTemplate/images/tooltip-help.png" >
                        <span class="ws-tooltiptext">
                            <div class="ws-tooltip-item">
                                <b>TEAM</b> - <digi:trn>Members can create new activities for the workspace but can also pull
                                activities from other workpaces (computed).</digi:trn>
                            </div>
                            <div class="ws-tooltip-item">
                                <b>MANAGEMENT</b> - <digi:trn>Members can view all of the Activities of the Team Workspaces
                                that have been assigned as Child Workspaces.</digi:trn>
                            </div>
                            <div><b>COMPUTED</b> - <digi:trn>Members can view all activities assigned to a particular organisation
                                denoted in the Funding or Related Organisations portion of the Activity Form.</digi:trn>
                            </div>
                        </span>
                    </div>
                </td>
                <td bgcolor="#999999">
                    <b><digi:trn>How the activity is linked to the workspace</digi:trn></b>
                </td>
            </tr>
        </table>
    </div>

</div>


<script type="text/javascript">
    YAHOOAmp.namespace("YAHOOAmp.amptab");

    YAHOOAmp.amptab.init = function () {
        tabView = new YAHOOAmp.widget.TabView('tabview_container');
    };

    var viewWorkspacesPanel = new YAHOOAmp.widget.Panel("newVewWorkspaces", {
            width: "800px",
            height: "510px",
            fixedcenter: true,
            constraintoviewport: true,
            underlay: "none",
            close: true,
            visible: false,
            modal: true,
            draggable: true,
            context: ["showbtn", "tl", "bl"]
        }
    );

    function initScriptsViewWorkspaces() {
        var msgP6 = '\n<digi:trn jsFriendly="true">View Workspaces</digi:trn>';
        viewWorkspacesPanel.setHeader(msgP6);
        viewWorkspacesPanel.setBody("");
        viewWorkspacesPanel.render(document.body);
        panelFirstShow = 1;
    }

    function showViewWorkspaces() {
        var content = document.getElementById("viewWorkspacesContent");
        var element6 = document.getElementById("viewWorkspaces");
        var loading = '\n<digi:trn jsFriendly="true">Loading...</digi:trn>';
        content.innerHTML = '<p align="center"><img align="top" src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader.gif" /><font size="3"><b>' + loading + '</b></font></p>';
        viewWorkspacesPanel.setBody(element6.innerHTML);
        panelFirstShow = 0;
        document.getElementById("viewWorkspacesContent").scrollTop = 0;
        viewWorkspacesPanel.show();
    }

    var responseSuccessViewWorkspaces = function (o) {
        //var response = o.responseText;

        // var response = "[{\"name\":\"Workspace 1\",\"reason\":\"Computed\"}, " +
        //     "{\"name\":\"Workspace mgmt\",\"reason\":\"Workspace 1 mgmt -> Workspace 1\"}]";

        // convert string to JSON
        response = $.parseJSON(o.responseText);

        $(function () {
            $.each(response, function (i, item) {
                var $tr = $('<tr>').append(
                    $('<td>'),
                    $('<td>').text(item.name),
                    $('<td>').text(item.type),
                    $('<td>').text(item.extraInfo),
                ).appendTo('#viewWorkspacesTable');
            });
        });

        $('#viewWorkspacesContent').html($('#tableContent').html());

        postsuccessScriptsViewWorkspaces();
    }


    var responseFailureViewWorkspaces = function (o) {
        var content = document.getElementById("viewWorkspacesContent");
        var errorTitle = '\n<digi:trn jsFriendly="true">Error</digi:trn>: ';
        var errorText = JSON.stringify(JSON.parse(o.responseText).error);
        content.innerHTML = '<p class="ws-error">' + errorTitle + errorText + '</p>';

        if (o.status === 401) {
            alert(errorText);
            location.reload();
        }
    }

    var viewWorkspacesCallback =
        {
            success: responseSuccessViewWorkspaces,
            failure: responseFailureViewWorkspaces
        };

    function viewWorkspaces(id) {
        showViewWorkspaces();
        YAHOOAmp.util.Connect.asyncRequest("GET", "/rest/activity/" + id + "/preview/workspaces", viewWorkspacesCallback);
    }

    function postsuccessScriptsViewWorkspaces() {
        setStripsTable("viewWorkspacesTable", "tableEven", "tableOdd");
    }

    var currentViewWorkspaces = window.onload;

    addLoadEvent(function () {
        currentViewWorkspaces.apply(currentViewWorkspaces);
    });

    addLoadEvent(initScriptsViewWorkspaces);

</script>

<style type="text/css">
    #viewWorkspacesTable td {
        height: 20px;
        padding-right: 10px;
    }

    .ws-tooltip {
        position: relative;
        white-space: normal;
    }

    .ws-tooltip .ws-tooltiptext {
        display : none;
        position: absolute;
        z-index: 100;
        background-color: white;
        border-style: solid;
        border-width: 1px;
        border-color: #777777;
        padding: 3px;
        width: 300px;
        top: 20px;
        left: 20px;
    }

    .ws-tooltip:hover .ws-tooltiptext {
        display: block;
        opacity: 1;
    }

    .ws-tooltip-item {
        margin-bottom: 10px;
    }

    .ws-error {
        color: red;
    }
</style>
