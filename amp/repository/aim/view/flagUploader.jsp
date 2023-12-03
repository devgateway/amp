<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<style type="text/css">
div.fileinputs {
position: relative;
}

div.pseudoInput {
position: absolute;
top: -8px;

z-index: 1;
width:75px;
height:20px;
font-size:10px;
}

input.hide{
position:absolute;

left:0px;
-moz-opacity:0;
filter:alpha(opacity: 0);
opacity: 0;
z-index: 2;
}</style>

<!--[if IE]>
<style type="text/css">
div.pseudoInput {
position: absolute;
top: -12px;
z-index: 1;
}
input.hide
{
position:absolute;
left: 154px;
-moz-opacity:0;
filter:alpha(opacity: 0);
opacity: 0;
z-index: 2;
width:0px;
border-width:0px;
}
</style>
<![endif]-->

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<style type="text/css">
<!--
div.fileinputs {
	position: relative;
	height: 30px;
	width: 300px;
}
input.file {
	width: 300px;
	margin: 0;
}
input.file.hidden {
	position: relative;
	text-align: right;
	-moz-opacity:0 ;
	filter:alpha(opacity: 0);
	width: 300px;
	opacity: 0;
	z-index: 2;
}

div.fakefile {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile input {
	margin-bottom: 5px;
	margin-left: 0;
	width: 217px;
}
div.fakefile2 {
	position: absolute;
	top: 0px;
	left: 217px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile2 input{
	width: 83px;
}
-->
</style>
<script language="JavaScript">

function buttonClicked (status){
	
  if (status == "depressed"){
    document.getElementById("pseudobutton").style.borderStyle = "inset";
  }

  else{
    document.getElementById("pseudobutton").style.borderStyle = "outset";
    document.getElementById("pseudotext").value=document.getElementById("flagUploader").value;
  }
}



function validate() {

  if (document.aimFlagUploaderForm.countryId.value == "-1") {

    alert("Please select a country");

    document.aimFlagUploaderForm.countryId.focus();

    return false;

  }



  /*

  if (trim(document.aimFlagUploaderForm.flagFile.value).length == 0) {

    alert("Please select a flag");

    document.aimFlagUploaderForm.flagFile.focus();

    return false;

  }*/

}



function upload() {

  ret = validate();

  if (ret == true) {

    document.aimFlagUploaderForm.submit();

  }

  return ret;

}



function setAsDefault(id) {

  <digi:context name="url" property="context/ampModule/moduleinstance/setDefaultFlag.do" />

  document.aimFlagUploaderForm.action = "<%=url%>?id="+id;

  document.aimFlagUploaderForm.submit();

}



function deleteFlag(id) {
	if ( confirm("<digi:trn>Are you sure you want to delete this flag</digi:trn>?") ) {
  		<digi:context name="url" property="context/ampModule/moduleinstance/deleteFlag.do" />
  		document.aimFlagUploaderForm.action = "<%=url%>?CountryId="+id;
		document.aimFlagUploaderForm.submit();
	}
}



</script>



<digi:context name="displayFlag" property="context/ampModule/moduleinstance/displayFlag.do" />



<!--  AMP Admin Logo -->

<jsp:include page="teamPagesHeader.jsp"  />

<!-- End of Logo -->


 <h1 class="admintitle"> <digi:trn key="aim:flagUploaderSelector"> Flag uploader/selector</digi:trn></h1>

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="1000" align="center" border="0">

  <tr>

    <td align=left class=r-dotted-lg valign="top" width=750>

      <table cellPadding=5 cellspacing="0" width="100%" border="0">

        <!-- <tr> -->

          <!-- Start Navigation -->
         <!-- <td height=33 bgcolor=#F2F2F2><span class=crumb>
            <c:set var="trnViewAdmin">
              <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>

            </c:set>

            <digi:link href="/admin.do" styleClass="comment" title="${trnViewAdmin}" >

              <digi:trn key="aim:AmpAdminHome">

              Admin Home

              </digi:trn>

            </digi:link>&nbsp;&gt;&nbsp;

            <digi:trn key="aim:flagUploader">

            Flag uploader
            </digi:trn>

          </td>

          <!-- End navigation -->

        <!-- </tr>-->

        <tr>

          <td height=16 valign="center" width=571>

           
          </td>

        </tr>

        <tr>

          <digi:form action="/uploadFlag.do" method="post" enctype="multipart/form-data">
            <td noWrap width="100%" vAlign="top">

              <table width="100%" cellpadding="1" cellspacing="1" border="0">

                <tr><td noWrap vAlign="top" width="20"0>

                  <table cellPadding=4 cellspacing="1" width="200" valign="top" style="border:1px solid #CCCCCC; height:128px;">

                    <logic:iterate name="aimFlagUploaderForm" property="cntryFlags" id="flag"

                    type="org.digijava.ampModule.aim.helper.Flag">

                    <tr bgColor=#ffffff>

                      <td valign="top" align="center" width="60">

                        <img src="<%=displayFlag%>?id=<bean:write name="flag" property="cntryId" />"

                        border="0" height="34" width="50">

                      </td>

                      <td valign="top" align="left">

                        <bean:write name="flag" property="cntryName" />&nbsp;

                        <c:if test="${flag.defaultFlag == true}">

                          <digi:img src="images/bullet_green.gif" border="0" height="9" width="9" align="center" />

                        </c:if>

                        <c:if test="${flag.defaultFlag == false}">

                          <a href="javascript:setAsDefault('<bean:write name="flag" property="cntryId" />')"><digi:img src="images/bullet_grey.gif" border="0" height="9" width="9" align="center" /></a>

                        </c:if>

                        &nbsp;

                        <a href="javascript:deleteFlag('<bean:write name="flag" property="cntryId" />')">

                        <digi:img src="images/trash_12.gif" border="0" height="11" width="11" align="center" />

</a>

                      </td>

                    </tr>

                    </logic:iterate>

                  </table>

            </td>
<td width="20">&nbsp;</td>
            <td noWrap vAlign="top">

              <table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>

                <tr bgcolor="#aaaaaa">

                  <td vAlign="center" width="100%" align="center" class="textalb" height="20" bgcolor="#c7d4db">

				<b style="font-size:12px; font-family:Arial, Helvetica, sans-serif; color:#000000;">
                    <digi:trn key="aim:uploadFlag">
                      Upload Flag</digi:trn></b>

                  </td>

                </tr>

                <tr>

                  <td align="center">

                    <table cellpadding="3" cellspacing="3" style="font-size:12px;">

                      <tr>

                        <td colspan="2" align="left">

                          <digi:errors/>

                        </td>

                      </tr>

                      <tr>

                        <td>

                          <b><digi:trn  key="aim:country">Administrative Level 0</digi:trn></b>

                        </td>

                        <td>
                          <c:set var="selCountry">
                          <digi:trn key="aim:flagUploader:selectCountry">
                          Select Country
                        </digi:trn>


                          </c:set>

                          <html:select property="countryId" styleClass="inp-text">

                            <html:option value="-1">--<c:out value="${selCountry}" />--</html:option>
                            <c:forEach var="cn" items="${aimFlagUploaderForm.countries}">
                              <html:option value="${cn.id}">${cn.name}</html:option>
                            </c:forEach>

                            <%--<html:optionsCollection name="aimFlagUploaderForm" property="countries"	value="id" label="name" />--%>

                          </html:select>

                        </td>

                      </tr>

                      <tr>

                        <td>

                          <b><digi:trn  key="aim:flag">Flag</digi:trn></b>

                        </td>

                        <td nowrap>
                        	<a title="<digi:trn key="aim:FileLocation">Location of the document to be attached</digi:trn>">
								<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
									<input id="flagUploader" name="flagFile" type="file" class="file" />
								</div>
							</a>
                        
                        
               <%--
                <div class="fileinputs">
                            <div class="pseudoInput">
                              <input type="text" id="pseudotext"  READONLY/>
                              <input type="button" id="pseudobutton" value="<digi:trn key="aim:browseFile">Browse...</digi:trn>" style="width:75px; height:20px;font-size:10px;"/>
                              <html:file name="aimFlagUploaderForm" property="flagFile" styleId="flagUploader" styleClass="hide" onmousedown="buttonClicked('depressed');" onmouseup="buttonClicked('normal');" onmouseout="buttonClicked('phased');"  />


                            </div>
                          </div>
                --%>         
                         

                        </td>



                      </tr>

                      <tr>

                        <td colspan="2" align="center">

                          <table cellPadding=3 cellSpacing=3>

                            <tr>

                              <td>

                                <c:set var="trnUploadBtn">

                                  <digi:trn key="aim:flUploadBtn">Upload</digi:trn>

                                </c:set>

                                <input type="submit" value="${trnUploadBtn}" onclick="return upload()" class="buttonx">

                              </td>

                              <td>

                                <c:set var="trnClearBtn">

                                  <digi:trn key="aim:flClearBtn">Clear</digi:trn>

                                </c:set>

                                <input type="reset" value="${trnClearBtn}" class="buttonx">

                              </td>

                            </tr>

                          </table>

                        </td>

                      </tr>

                    </table>

                  </td>

                </tr>

              </table>

            </td>

                </tr>

              </table>

    </td>

          </digi:form>

        </tr>
<tr>
<td><hr /></td>
</tr>
        <tr>

          <td align="center" style="font-size:12px;">

            <digi:img src="images/bullet_green.gif" border="0" height="9" width="9" align="top" /> -

            <digi:trn key="aim:defaultFlag">Default Flag</digi:trn>

          </td>

        </tr>

        <tr>

          <td align="center" style="font-size:12px;">

            <digi:trn key="aim:flagUploadHelpPhrase1">Click the image</digi:trn>

            <digi:img src="images/bullet_grey.gif" border="0" height="9" width="9" align="top" />

            <digi:trn key="aim:flagUploadHelpPhrase2">next to the flag to make it as the default for the site</digi:trn>

          </td>

        </tr>

      </table>

</td>

  </tr>



</table>
<script  type="text/javascript" src="<digi:file src="ampModule/aim/scripts/fileUpload.js"/>"></script>
   	
<script type="text/javascript">
	initFileUploads('<digi:trn jsFriendly="true" key="aim:browse">Browse...</digi:trn>');
</script>

