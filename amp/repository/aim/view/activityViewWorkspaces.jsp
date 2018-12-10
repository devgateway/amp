<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<div id="viewWorkspaces" class="invisible-item">
    <div id="viewWorkspacesContent" class="content">
    </div>
    <div id="tableContent" class="invisible-item">
        <table id="viewWorkspacesTable" width="100%">
            <tr height="20">
                <td bgcolor="#999999"></td>
                <td bgcolor="#999999">
                    <b>Workspace Name</b>
                </td>
                <td bgcolor="#999999">
                    <b>Workspace Type</b>
                </td>
                <td bgcolor="#999999">
                    <b>Reason</b>
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
                    $('<td>').css("padding-right","10px"),
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
        alert("Connection Failure!");
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
        height: 20px
    }
</style>
