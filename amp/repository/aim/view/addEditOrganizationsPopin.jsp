<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>


<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>

<div id="popin" style="display: none">
    <div id="popinContent" class="content">
    </div>
</div>

<script type="text/javascript">
    <!--

    YAHOOAmp.namespace("YAHOOAmp.amp");

    var myPanel = new YAHOOAmp.widget.Panel("newpopins", {
        width:"600px",
        fixedcenter: true,
        constraintoviewport: false,
        underlay:"none",
        close:true,
        visible:false,
        modal:true,
        draggable:true,
        context: ["showbtn", "tl", "bl"]
    });
    var panelStart=0;
    var checkAndClose=false;
    function initSectorScript() {
        var msg='\n<digi:trn key="aim:addSector">Add Sectors</digi:trn>';
        myPanel.setHeader(msg);
        myPanel.setBody("");
        myPanel.beforeHideEvent.subscribe(function() {
            panelStart=1;
        });

        myPanel.render(document.body);
    }
    //this is called from editActivityMenu.jsp
    //window.onload=initSectorScript();
    -->
</script>
<style type="text/css">
    .mask {
        -moz-opacity: 0.8;
        opacity:.80;
        filter: alpha(opacity=80);
        background-color:#2f2f2f;
    }

    #popin .content {
        overflow:auto;
        height:455px;
        background-color:fff;
        padding:10px;
    }


</style>



<script language="JavaScript">
    <!--

    //DO NOT REMOVE THIS FUNCTION --- AGAIN!!!!
    function mapCallBack(status, statusText, responseText, responseXML){
        window.location.reload();
    }


    var responseSuccess = function(o){
        /* Please see the Success Case section for more
         * details on the response object's properties.
         * o.tId
         * o.status
         * o.statusText
         * o.getResponseHeader[ ]
         * o.getAllResponseHeaders
         * o.responseText
         * o.responseXML
         * o.argument
         */
        var response = o.responseText;
        var content = document.getElementById("popinContent");
        //response = response.split("<!")[0];
        content.innerHTML = response;
        //content.style.visibility = "visible";

        showContent();
    }

    var responseFailure = function(o){
        // Access the response object's properties in the
        // same manner as listed in responseSuccess( ).
        // Please see the Failure Case section and
        // Communication Error sub-section for more details on the
        // response object's properties.
        //alert("Connection Failure!");
    }
    var callback =
        {
        success:responseSuccess,
        failure:responseFailure
    };

    function showContent(){
        var element = document.getElementById("popin");
        element.style.display = "inline";
        if (panelStart < 1){
            myPanel.setBody(element);
        }
        if (panelStart < 2){
            document.getElementById("popin").scrollTop=0;
            myPanel.show();
            panelStart = 2;
        }
        checkErrorAndClose();
    }

    function generateFields(type){
        var ret="";
        if(type==1){//add sector  or reload sectors
            ret=
                "sectorReset="    	+document.getElementsByName("sectorReset")[0].value+"&"+
                "sectorScheme="   	+document.getElementsByName("sectorScheme")[0].value+"&"+
                "sector="         	+document.getElementsByName("sector")[0].value+"&";
            if(document.getElementsByName("subsectorLevel1")[0]!=null){
                ret+="subsectorLevel1="	+document.getElementsByName("subsectorLevel1")[0].value+"&";
            }
            if(document.getElementsByName("subsectorLevel2")[0]!=null){
                ret+="subsectorLevel2="	+document.getElementsByName("subsectorLevel2")[0].value+"&";
            }
            ret+=
                "addButton="		+document.getElementsByName("addButton")[0].value+"&"+
                "keyword="          +document.getElementsByName("keyword")[0].value+"&"+
                 "configId=" +1;
        }
        else if (type==2){
            ret=
                "keyword="          +document.getElementsByName("keyword")[0].value+"&"+
                "tempNumResults="   +document.getElementsByName("tempNumResults")[0].value+"&"+
                "sectorReset="      +document.getElementsByName("sectorReset")[0].value;
            if(document.getElementsByName("sectorScheme")[0]!=null){
                 "&sectorScheme="+document.getElementsByName("sectorScheme")[0].value;
            }

        }
        else if (type==3){//add sectors chosen from the list
            ret+="sectorReset="+document.getElementsByName("sectorReset")[0].value+"&"+
                "addButton="  +document.getElementsByName("addButton")[0].value+"&"+
                "edit="       +document.getElementsByName("edit")[0].value	;
            if(document.getElementsByName("selSectors")!=null){
                var sectors = document.getElementsByName("selSectors").length;
                for(var i=0; i< sectors; i++){
                    if(document.getElementsByName("selSectors")[i].checked){
                        ret+="&"+document.getElementsByName("selSectors")[i].name+"="+document.getElementsByName("selSectors")[i].value;
                    }
                }
            }
        }
        else if (type==4){// when changing the page from button links
            ret+="sectorReset="+document.getElementsByName("sectorReset")[0].value+"&"+
                "addButton="  +document.getElementsByName("addButton")[0].value+"&"+
                "edit="       +document.getElementsByName("edit")[0].value	;

        }
        return ret;
    }
    function myclose(){
        myPanel.hide();
        panelStart=1;

    }
    function closeWindow() {
        myclose();
    }

    function selectSector() {
        var check = checkSectorEmpty();
        if(check)
        {
            document.aimSelectSectorForm.submit();

        }
    }
    function addSectorBtnPushed(){
            var postString		= "edit=true&" + generateFields(1);
    <digi:context name="url" property="context/aim/selectSectors.do"/>
            var url="${url}?"+postString;
            var async=new Asynchronous();
            async.complete=addSector;
            async.call(url);
            myclose();
        }


    function buttonAdd(){
        if(document.aimSelectSectorForm.sector.value != -1){
            addSectorBtnPushed();
        }
        else{
            alert("Please, select a sector first!");
        }
    }
                            function generateFieldsLocation(){

                                var ret="locationReset=" + document.getElementsByName("locationReset")[0].value+"&"+
                                    "parentLocId=" + document.getElementsByName("parentLocId")[0].value;

                                if (document.getElementsByName('userSelectedLocs').length > 0){
                                    var opt = document.getElementsByName('userSelectedLocs')[0].length;
                                    for(var i=0; i< opt; i++){
                                        if(document.getElementsByName('userSelectedLocs')[0].options[i].selected==true){
                                            ret += "&userSelectedLocs=" + document.getElementsByName('userSelectedLocs')[0].options[i].value;
                                        }
                                    }
                                }
                                return ret;
                            }

                            function buttonAddLocation(){
                                var postString		= generateFieldsLocation();
    <digi:context name="commentUrl" property="context/aim/locationSelected.do"/>
                        var url = "<%=commentUrl%>";
                        YAHOOAmp.util.Connect.asyncRequest("POST", url, callback, postString);
                        checkAndClose=true;
                        document.aimEditActivityForm.submit();
                    }

                    function resetSectors(){
                        document.aimSelectSectorForm.sector.value = -1
                        if(document.aimSelectSectorForm.subsectorLevel1!=null){
                            document.aimSelectSectorForm.subsectorLevel1.value = -1;
                        }
                        if(document.aimSelectSectorForm.subsectorLevel2!=null){
                            document.aimSelectSectorForm.subsectorLevel2.value = -1;
                        }
                    }
                    function reloadSector(value) {
                        if(document.getElementsByName("subsectorLevel1")[0]){
                            document.aimSelectSectorForm.subsectorLevel1.disabled=false;
                        }
                        if(document.getElementsByName("subsectorLevel2")[0]){
                            document.aimSelectSectorForm.subsectorLevel2.disabled=false;
                        }
                        if (value == 1) {
                            document.aimSelectSectorForm.sector.value = -1;
                        } else if (value == 2) {
                            if(document.getElementsByName("subsectorLevel1")[0]){
                                document.aimSelectSectorForm.subsectorLevel1.value = -1;
                            }
                        } else if (value == 3) {
                            if(document.getElementsByName("subsectorLevel2")[0]){
                                document.aimSelectSectorForm.subsectorLevel2.value = -1;
                            }
                        }
                        var postString		= "edit=true&" + generateFields(1);
    <digi:context name="commentUrl" property="context/aim/selectSectors.do"/>
                        var url = "<%=commentUrl%>";
                        YAHOOAmp.util.Connect.asyncRequest("POST", url, callback, postString);
                        //YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);

                    }

                    function addSelectedSectors(){
                        var postString		= generateFields(3);
    <digi:context name="Url" property="context/aim/addSelectedSectors.do"/>
                        var url = "<%=Url%>";
                        YAHOOAmp.util.Connect.asyncRequest("POST", url, callback, postString);
                        checkAndClose=true;
                    }
                    function checkErrorAndClose(){
                        if(checkAndClose==true){
                            if(document.getElementsByName("someError")[0]==null || document.getElementsByName("someError")[0].value=="false"){
                                myclose();
                                addSector();
                            }
                            checkAndClose=false;
                        }
                    }
                    function selectPageSectors(pagedata){
                        var postString="";
                        pagedata = pagedata.replace(/\}$/,"");
                        pagedata = pagedata.replace(/^\{/,"");
                        var myarray= pagedata.split(", ");
                        //myarray = pagedata;
                        //alert(myarray);
                        var postString="";
                        for(i=0; i<myarray.length; i++){
                            postString+=myarray[i]+((i<myarray.length-1)?"&":"");
                        }
                        postString+="&"+generateFields(4);
    <digi:context name="commentUrl" property="context/aim/searchSectors.do"/>
                        var url = "<%=commentUrl%>?"+postString;
                        YAHOOAmp.util.Connect.asyncRequest("POST", url, callback, postString);

                    }
                    function checkSectorEmpty()
                    {
                        var sectorFlag = true;
                        if(document.aimSelectSectorForm.sector.value == -1)
                        {
                            alert("Please Select a sector First")
                            sectorFlag = false;
                        }

                        return sectorFlag;
                    }
                    function checkEmpty() {
                        var flag=true;
                        if(trim(document.getElementsByName("keyword")[0].value) == "")
                        {
                            alert("Please Enter a Keyword....");
                            flag=false;
                        }
                        if(trim(document.getElementsByName("tempNumResults")[0].value) == 0)
                        {
                            alert("Invalid value at 'Number of results per page'");
                            flag=false;
                        }

                        return flag;
                    }
                    function checkNumeric(objName,comma,period,hyphen)
                    {
                        var numberfield = objName;
                        if (chkNumeric(objName,comma,period,hyphen) == false)
                        {
                            numberfield.select();
                            numberfield.focus();
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }

                    function chkNumeric(objName,comma,period,hyphen)
                    {
                        // only allow 0-9 be entered, plus any values passed
                        // (can be in any order, and don't have to be comma, period, or hyphen)
                        // if all numbers allow commas, periods, hyphens or whatever,
                        // just hard code it here and take out the passed parameters
                        var checkOK = "0123456789" + comma + period + hyphen;
                        var checkStr = objName;
                        var allValid = true;
                        var decPoints = 0;
                        var allNum = "";

                        for (i = 0;  i < checkStr.value.length;  i++)
                        {
                            ch = checkStr.value.charAt(i);
                            for (j = 0;  j < checkOK.length;  j++)
                                if (ch == checkOK.charAt(j))
                                    break;
                            if (j == checkOK.length)
                            {
                                allValid = false;
                                break;
                            }
                            if (ch != ",")
                                allNum += ch;
                        }
                        if (!allValid)
                        {
                            alertsay = "Please enter only numbers in the \"Number of results per page\"."
                            alert(alertsay);
                            return (false);
                        }
                    }
                    function selectSector() {
    <digi:context name="selectSec" property="context/aim/selectSectors.do" />
                        var url = "<%= selectSec%>";
                        YAHOOAmp.util.Connect.asyncRequest("POST", url, callback, "edit=true");
                    }

                    function searchSector() {
                        if(checkNumeric(document.getElementsByName("tempNumResults")[0],'','','')==true){
                            var flg=checkEmpty();
                            if(flg){
                                var postString		= generateFields(2);
    <digi:context name="searchSctr" property="context/aim/searchSectors.do" />
                                          var url = "<%= searchSctr%>";
                                          YAHOOAmp.util.Connect.asyncRequest("POST", url, callback, "edit=true&"+postString);

                                          return true;
                                      }
                                  }
                                  else
                                      return false;
                              }
                              function showPanelLoading(msg){
                                  myPanel.setHeader(msg);
                                  var content = document.getElementById("popinContent");
                                  content.innerHTML = "<div style='text-align: center'>" + "Loading..." +
                                      "... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";
                                  showContent();
                              }
                              function myAddSectors(params) {
                                  var msg='\n<digi:trn key="aim:addLocation">Add Sectors</digi:trn>';
                                  showPanelLoading(msg);
    <digi:context name="commentUrl" property="context/aim/selectSectors.do" />
                        var url = "<%=commentUrl%>";
                        YAHOOAmp.util.Connect.asyncRequest("POST", url, callback, params);
                    }
                    function myAddLocation(params) {
                        var msg='\n<digi:trn key="aim:addLocation">Add Location</digi:trn>';
                        showPanelLoading(msg);
    <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocation.do" />
                        var url = "<%=selectLoc%>";
                        YAHOOAmp.util.Connect.asyncRequest("POST", url, callback, params);
                    }


                    function locationChanged( selectId ) {
                        var selectEl		= document.getElementById(selectId);
                        if ( selectEl.value != "-1" ) {
                            document.selectLocationForm.parentLocId.value=selectEl.value;
    <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocation.do" />
                                    var url = "<%=selectLoc%>";
                                    YAHOOAmp.util.Connect.asyncRequest("POST", url, callback, "edit=true&"+generateFieldsLocation());
                                }
                            }





                            -->

</script>
