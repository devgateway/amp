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

<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>

<digi:instance property="tempDocManagerForm"/>

<div id="popin" class="invisible-item">
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



<script langauage="JavaScript">
	function addNewField() {
		<digi:context name="addEdiNewField" property="context/module/moduleinstance/tempDocManager.do?actType=addTemplateDocumentField" />
		tempDocManagerForm.action = "<%=addEdiNewField%>";        
		tempDocManagerForm.submit();
	}

	function manageField(fieldTempId){
		var fieldTypeSelectId='fieldType_'+fieldTempId;
		var selectedFieldType=document.getElementById(fieldTypeSelectId);
		if(selectedFieldType==null || selectedFieldType.value==-1){
			alert('Select Field Type First !');
			return false;
		}else{
			<digi:context name="commentUrl" property="context/module/moduleinstance/manageField.do?" />
			var url = "<%=commentUrl %>";
			var temlateName=document.getElementById('tempName').value;
			var params="&action=manage&templateDocFieldTemporaryId="+fieldTempId+"&selectedFieldType="+selectedFieldType.value+"&templateName="+temlateName;
			YAHOOAmp.util.Connect.asyncRequest("POST", url, callback, params);
		}
	}

	function addNewPreDefinedValue(){
		<digi:context name="addVal" property="context/module/moduleinstance/manageField.do?"/>;
        var url="${addVal}&action=addNewValue";
        var parameters=getFieldParams();
        YAHOOAmp.util.Connect.asyncRequest("POST", url, callback,parameters);
	}

	function deleteData(valueId,isTextArea){ //delete pre-defined value
		  var flag = confirm("Delete this data?");
		  if(flag == true){
			  <digi:context name="addVal" property="context/module/moduleinstance/manageField.do?"/>;
		        var url="${addVal}&action=deleteValue&valueId="+valueId;
		        var parameters=getFieldParams(isTextArea);
		        YAHOOAmp.util.Connect.asyncRequest("POST", url, callback,parameters);
		  }
	}

	function submitPreDefinedValues(isTextArea){
		//check that all predefined values are filled and non of them is empty
		if(isTextArea=='true'){
			var values=$("textArea[id^='val_']");
		}else{
			var values=$("input[id^='val_']");
		}
		
    	if(values!=null){
        	for(var i=0;i < values.length; i++){
        		if(values[i].value==''){
            		alert('Please enter value');
            		return false;
        		}
        	}
    	}
    	
		<digi:context name="addVal" property="context/module/moduleinstance/manageField.do?"/>;
        var url="${addVal}&action=saveValues";
        var parameters=getFieldParams(isTextArea);
        YAHOOAmp.util.Connect.asyncRequest("POST", url, callbackAfterSave,parameters);
	}

	function getFieldParams(isTextArea) {
		var params='';
		if(isTextArea=='true'){
			var values=$("textArea[id^='val_']");
		}else{
			var values=$("input[id^='val_']");
		}
    	if(values!=null){
        	for(var i=0;i < values.length; i++){
        		params+= "&preDefinedValue="+values[i].value;
        	}
    	}
    	return params;
	}

	function cancel()
	{
		var subForm				= document.forms["tempDocManagerForm"];
		subForm.action			= "/contentrepository/tempDocManager.do~actType=viewTemplateDocuments";
		subForm.submit();
		return false;
	}
	
	function validateDoc(){
		var tempName=document.getElementById('tempName');
		if(tempName==null || tempName.value==''){
			var msg='<digi:trn jsFriendly="true">Please Enter Name</digi:trn>';
			alert(msg);
			return false;
		}
		var fields=$("select[id^='fieldType_']");
		if(fields==null || fields.length==0){
			var msg='<digi:trn jsFriendly="true">Please Add at least one field</digi:trn>';
			alert(msg);
			return false;
		}

		if(fields!=null && fields.length>0){
			for(var i=0;i<fields.length;i++){
				if(fields[i].value=='-1'){
					var msg='<digi:trn jsFriendly="true">Select Field Type</digi:trn>';
					alert(msg);
					return false;
				}
			}
		}
	}

	function deleteFields(){
		var checkboxes=$("#templateFieldsTbl").find("input.selectedTempFieldsIds:checked");
		if(checkboxes!=null && checkboxes.length>0){
            var flag = confirm("<digi:trn jsFriendly='true'>Delete this data?</digi:trn>");
            if(flag){
			<digi:context name="remFields" property="context/module/moduleinstance/tempDocManager.do?actType=deleteTemplateDocumentField" />
			tempDocManagerForm.action = "${remFields}";
			tempDocManagerForm.submit();
            }
		}else{
			var msg='<digi:trn jsFriendly="true">Please select Field to remove</digi:trn>';
			alert(msg);
    		return false;
		}
	}

	function fieldTypeChanged(fieldTempId){
		var fieldTypeSelId="fieldType_"+fieldTempId;
		var fieldType=document.getElementById(fieldTypeSelId).value;
		<digi:context name="addEdiNewField" property="context/module/moduleinstance/tempDocManager.do?actType=editTemplateDocumentField" />
		tempDocManagerForm.action = "<%=addEdiNewField%>&templateDocFieldTemporaryId="+fieldTempId+"&selFieldType="+fieldType;
		tempDocManagerForm.submit();
	}

</script>

<digi:form action="/tempDocManager.do?actType=saveTemplateDocument" method="post">
<h1 class="admintitle" style="text-align:left;"><digi:trn>Create/Edit template document</digi:trn></h1>
<table border="0" cellpadding="0" align="center" width=1000>
	<!--<tr>
		<td colspan="2" style="padding-bottom:15px;">
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
	</tr>-->
	<tr><td colspan="2"><digi:errors/></td></tr>
	<tr>
		<td colspan="2" bgcolor=#c7d4db height=25 align=center>
			<span class="subtitle-blue"><b style="font-size:12px;"><digi:trn>Create/Edit template document</digi:trn></b></span>
		</td>
	</tr>
	<tr>
		<td>
			<table border="0" cellpadding="5" align="center" style="font-family:verdana;font-size:11px; border:1px solid silver;" width="100%">
				<tr>
					<td nowrap="nowrap" align=center>
						<font color="red">*</font><strong><digi:trn>Name</digi:trn>:</strong>
						<html:text name="tempDocManagerForm" property="templateName" style="width : 200px" styleId="tempName"/>					</td>
				</tr>								
				<tr>
					<td>
						<!-- rows to add new fields -->						
							<logic:notEmpty name="tempDocManagerForm" property="pendingFields">
							<table id="templateFieldsTbl" class="inside" align=center>
								<c:forEach var="pf" items="${tempDocManagerForm.pendingFields}">
									<tr>
										<td class="inside">
											<html:multibox property="selectedFieldsIds" styleClass="selectedTempFieldsIds">
								             	${pf.fieldTemporaryId}								             </html:multibox>										</td>
										<td class="inside">
											<html:select property="fieldType" name="pf" styleClass="inp-text" styleId="fieldType_${pf.fieldTemporaryId}" onchange="fieldTypeChanged('${pf.fieldTemporaryId}')">
												<html:option value="-1"><digi:trn>Select from below</digi:trn></html:option>
												<logic:iterate id="ft" name="tempDocManagerForm" property="availableFields">																																															
													<html:option value="${ft.value}"><digi:trn>${ft.label}</digi:trn></html:option>																		
												</logic:iterate>
											</html:select>										</td>
										<td class="inside">
											<c:set var="trnManage"><digi:trn>Manage Field</digi:trn></c:set>
						    				<input type="button" class="buttonx" style="font-family:verdana;font-size:11px;" name="addValBtn" value="${trnManage}" onclick="manageField('${pf.fieldTemporaryId}')" id="manBut_${pf.fieldTemporaryId}">										</td>
									</tr>
								</c:forEach>
							</table>
						</logic:notEmpty>					</td>
				</tr>
				<tr>					
					<td align=center>
					<hr />
						<c:set var="trnadd"><digi:trn>Add Field</digi:trn></c:set>
				    	<input type="button" class="buttonx" style="font-family:verdana;font-size:11px;" name="addValBtn" value="${trnadd}" onclick="addNewField()">
						&nbsp;
						<c:set var="trndel"><digi:trn>Remove Selected Fields</digi:trn></c:set>
				    	<input type="button" style="font-family:verdana;font-size:11px;" class="buttonx" name="delValBtn" value="${trndel}" onclick="deleteFields()">
				    	&nbsp;
						<html:submit styleClass="buttonx" onclick="return validateDoc()" styleId="saveTemplateBtn"><digi:trn>Save Template</digi:trn></html:submit>					
						&nbsp;
						<html:submit styleClass="buttonx" onclick="return cancel()" styleId="saveTemplateBtn"><digi:trn>Cancel</digi:trn></html:submit>		
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