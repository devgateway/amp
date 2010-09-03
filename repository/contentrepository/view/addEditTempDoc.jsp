<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<!-- popin for pre-defined values -->

<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>

<div id="popin" style="display: none">
    <div id="popinContent" class="content">
    </div>
</div>

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
        background-color:#ffffff;
        padding:10px;
    }
</style>

<script langauage="JavaScript">
<!--

YAHOOAmp.namespace("YAHOOAmp.amp");

	var myPanel = new YAHOOAmp.widget.Panel("newpopins", {
		width:"450px",
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

	function initScript() {
		var msg='\n<digi:trn>Add Values</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
		}); 
	
		myPanel.render(document.body);
	}


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
        //checkErrorAndClose();
    }

     var responseSuccessAfterSave = function(o){
    	 myPanel.hide();
     }
         
     var callbackAfterSave =
         {
         success:responseSuccessAfterSave,
         failure:responseFailure
     };
-->	
</script>
<!--end-->

<digi:instance property="tempDocManagerForm" />
<digi:context name="digiContext" property="context" />

<script langauage="JavaScript">
	function addNewField() {
		  <digi:context name="addEdiNewField" property="context/module/moduleinstance/tempDocManager.do?actType=addTemplateDocumentField" />
          document.myForm.action = "<%=addEdiNewField%>";
          document.myForm.submit();
	}

	function manageField(fieldTempId){
		var fieldTypeSelectId='fieldType_'+fieldTempId;
		var selectedFieldType=document.getElementById(fieldTypeSelectId);
		if(selectedFieldType==null || selectedFieldType.value==-1){
			alert('Select Field Type First !');
			return false;
		}else{
			<digi:context name="commentUrl" property="context/module/moduleinstance/tempDocManager.do?actType=manageDocumentField" />
			var url = "<%=commentUrl %>";
			var params="&action=manage&templateDocFieldTemporaryId="+fieldTempId+"&selectedFieldType="+selectedFieldType.value;
			YAHOOAmp.util.Connect.asyncRequest("POST", url, callback, params);
		}
	}

	function addNewPreDefinedValue(){
		<digi:context name="addVal" property="context/module/moduleinstance/tempDocManager.do?actType=manageDocumentField"/>;
        var url="${addVal}&action=addNewValue";
        var parameters=getFieldParams();
        YAHOOAmp.util.Connect.asyncRequest("POST", url, callback,parameters);
	}

	function deleteData(valueId){ //delete pre-defined value
		  var flag = confirm("Delete this data?");
		  if(flag == true){
			  <digi:context name="addVal" property="context/module/moduleinstance/tempDocManager.do?actType=manageDocumentField"/>;
		        var url="${addVal}&action=deleteValue&valueId="+valueId;
		        var parameters=getFieldParams();
		        YAHOOAmp.util.Connect.asyncRequest("POST", url, callback,parameters);
		  }
	}

	function submitPreDefinedValues(){
		<digi:context name="addVal" property="context/module/moduleinstance/tempDocManager.do?actType=manageDocumentField"/>;
        var url="${addVal}&action=saveValues";
        var parameters=getFieldParams();
        YAHOOAmp.util.Connect.asyncRequest("POST", url, callbackAfterSave,parameters);
	}

	function getFieldParams() {
		var params='';
		var values=$("input[id^='val_']");
    	if(values!=null){
        	for(var i=0;i < values.length; i++){
        		params+= "&preDefinedValue="+values[i].value;
        	}
    	}
    	return params;
	}
	function validateDoc(){
		var tempName=document.getElementById('tempName');
		if(tempName==null || tempName.value==''){
			var msg='<digi:trn>Please Enter Name</digi:trn>';
			alert(msg);
			return false;
		}
		var fields=$("select[id^='fieldType_']");
		if(fields==null || fields.length==0){
			var msg='<digi:trn>Please Add at least one field</digi:trn>';
			alert(msg);
			return false;
		}

		if(fields!=null && fields.length>0){
			for(var i=0;i<fields.length;i++){
				if(fields[i].value=='-1'){
					var msg='<digi:trn>Select Field Type</digi:trn>';
					alert(msg);
					return false;
				}
			}
		}
	}

	function deleteFields(){
		var checkboxes=$("#templateFieldsTbl").find("input.selectedTempFieldsIds:checked");
		if(checkboxes!=null && checkboxes.length>0){
			<digi:context name="remFields" property="context/module/moduleinstance/tempDocManager.do?actType=deleteTemplateDocumentField" />
			document.myForm.action = "${remFields}";
            document.myForm.target = "_self"
            document.myForm.submit();
		}else{
			var msg='<digi:trn>Please select Field to remove</digi:trn>';
			alert(msg);
    		return false;
		}
	}

	function fieldTypeChanged(fieldTempId){
		<digi:context name="addEdiNewField" property="context/module/moduleinstance/tempDocManager.do?actType=editTemplateDocumentField" />
        document.myForm.action = "<%=addEdiNewField%>&templateDocFieldTemporaryId="+fieldTempId;
        document.myForm.submit();
	}
	
</script>

<digi:form action="/tempDocManager.do?actType=saveTemplateDocument" name="myForm" type="tempDocManagerForm">
<table border="0" cellpadding="15">
	<tr>
		<td colspan="2">
			<span class="crumb">
              <c:set var="translation">
                <digi:trn>Click here to goto Admin Home</digi:trn>
              </c:set>
              <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                <digi:trn>Admin Home</digi:trn>
              </html:link>&nbsp;&gt;&nbsp;
              <digi:link href="/tempDocManager.do?actType=viewTemplateDocuments" styleClass="comment">
                <digi:trn>Template Documents Manager</digi:trn>
              </digi:link>&nbsp;&gt;&nbsp;
              <digi:trn>Template Document form</digi:trn>		
			</span>
		</td>
	</tr>
	<tr><td colspan="2"><digi:errors/></td></tr>
	<tr>
		<td colspan="2">
			<span class="subtitle-blue"><digi:trn>Create/Edit template document</digi:trn></span>
		</td>
	</tr>
	<tr>
		<td>
			<table border="0" cellpadding="5" align="center" style="font-family:verdana;font-size:11px; border:1px solid silver;" width="100%">
				<tr>
					<td align="right" nowrap="nowrap">
						<font color="red">*</font><strong><digi:trn>Name</digi:trn>:</strong>
					</td>
					<td>
						<html:text name="tempDocManagerForm" property="templateName" style="width : 200px" styleId="tempName"/>
					</td>
				</tr>								
				<tr>
					<td colspan="2">
						<!-- rows to add new fields -->						
							<logic:notEmpty name="tempDocManagerForm" property="pendingFields">
							<table id="templateFieldsTbl">
								<c:forEach var="pf" items="${tempDocManagerForm.pendingFields}">
									<tr>
										<td>
											<html:multibox property="selectedFieldsIds" styleClass="selectedTempFieldsIds">
								             	${pf.fieldTemporaryId}
								             </html:multibox>
										</td>
										<td>
											<html:select property="fieldType" name="pf" styleClass="inp-text" styleId="fieldType_${pf.fieldTemporaryId}" onchange="fieldTypeChanged('${pf.fieldTemporaryId}')">
												<html:option value="-1"><digi:trn>Select from below</digi:trn></html:option>
												<logic:iterate id="fieldType" name="tempDocManagerForm" property="availableFields">																																															
													<html:option value="${fieldType.value}"><digi:trn>${fieldType.label}</digi:trn></html:option>																		
												</logic:iterate>
											</html:select>
										</td>
										<td>
											<c:set var="trnManage"><digi:trn>Manage Field</digi:trn></c:set>
						    				<input type="button" style="font-family:verdana;font-size:11px;" name="addValBtn" value="${trnManage}" onclick="manageField('${pf.fieldTemporaryId}')" id="manBut_${pf.fieldTemporaryId}">
										</td>
									</tr>
								</c:forEach>
							</table>
						</logic:notEmpty>						
						
					</td>
				</tr>
				<tr>					
					<td colspan="2">
						<c:set var="trndel"><digi:trn>Remove Selected Fields</digi:trn></c:set>
				    	<input type="button" style="font-family:verdana;font-size:11px;" name="delValBtn" value="${trndel}" onclick="deleteFields()">
				    	&nbsp;
						<c:set var="trnadd"><digi:trn>Add New Field</digi:trn></c:set>
				    	<input type="button" style="font-family:verdana;font-size:11px;" name="addValBtn" value="${trnadd}" onclick="addNewField()">			    	
				    	
					</td>
				</tr>
							
				<tr>
					<td colspan="2">
						<html:submit onclick="return validateDoc()"><digi:trn>Save Template</digi:trn></html:submit>
					</td>
				</tr>
			</table>
		</td>
	</tr>	
</table>
</digi:form>
<script language="JavaScript" type="text/javascript">
    addLoadEvent(initScript);
  </script>