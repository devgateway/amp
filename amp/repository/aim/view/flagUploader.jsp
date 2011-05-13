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




<script language="JavaScript">

function buttonClicked (status)
{
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

  <digi:context name="url" property="context/module/moduleinstance/setDefaultFlag.do" />

  document.aimFlagUploaderForm.action = "<%=url%>?id="+id;

  document.aimFlagUploaderForm.submit();

}



function deleteFlag(id) {

  <digi:context name="url" property="context/module/moduleinstance/deleteFlag.do" />

  document.aimFlagUploaderForm.action = "<%=url%>?CountryId="+id;

  document.aimFlagUploaderForm.submit();

}



</script>



<digi:context name="displayFlag" property="context/module/moduleinstance/displayFlag.do" />



<!--  AMP Admin Logo -->

<jsp:include page="teamPagesHeader.jsp" flush="true" />

<!-- End of Logo -->



<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>

  <tr>

    <td class=r-dotted-lg width=14>&nbsp;</td>

    <td align=left class=r-dotted-lg vAlign=top width=750>

      <table cellPadding=5 cellSpacing=0 width="100%" border=0>

        <tr>

          <!-- Start Navigation -->

          <td height=33><span class=crumb>

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

        </tr>

        <tr>

          <td height=16 vAlign=center width=571>

            <span class=subtitle-blue>

              <digi:trn key="aim:flagUploaderSelector">

              Flag uploader/selector

              </digi:trn>

            </span>

          </td>

        </tr>

        <tr>

          <digi:form action="/uploadFlag.do" method="post" enctype="multipart/form-data">
            <td noWrap width=100% vAlign="top">

              <table width=730 cellpadding=1 cellSpacing=1 border=0>

                <tr><td noWrap vAlign="top">

                  <table cellPadding=4 cellSpacing=1 width="200" valign="top">

                    <logic:iterate name="aimFlagUploaderForm" property="cntryFlags" id="flag"

                    type="org.digijava.module.aim.helper.Flag">

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

            <td noWrap vAlign="top">

              <table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>

                <tr bgcolor="#aaaaaa">

                  <td vAlign="center" width="100%" align ="center" class="textalb" height="20">

                    <digi:trn key="aim:uploadFlag">

                      Upload Flag</digi:trn>

                  </td>

                </tr>

                <tr>

                  <td align="center">

                    <table width="100%" cellpadding="2" cellspacing="1">

                      <tr>

                        <td colspan="2" align="center">

                          <digi:errors/>

                        </td>

                      </tr>

                      <tr>

                        <td>

                          <digi:trn key="aim:country">Country</digi:trn>

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

                          <digi:trn key="aim:flag">Flag</digi:trn>

                        </td>

                        <td nowrap>
                          <div class="fileinputs">
                            <div class="pseudoInput">
                              <input type="text" id="pseudotext"  READONLY/>
                              <input type="button" id="pseudobutton" value="<digi:trn key="aim:browseFile">Browse...</digi:trn>" style="width:75px; height:20px;font-size:10px;"/>
                              <html:file name="aimFlagUploaderForm" property="flagFile" styleId="flagUploader" styleClass="hide" onmousedown="buttonClicked('depressed');" onmouseup="buttonClicked('normal');" onmouseout="buttonClicked('phased');"  />


                            </div>
                          </div>

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

                                <input type="submit" value="${trnUploadBtn}" class="dr-menu" onclick="return upload()">

                              </td>

                              <td>

                                <c:set var="trnClearBtn">

                                  <digi:trn key="aim:flClearBtn">Clear</digi:trn>

                                </c:set>

                                <input type="reset" value="${trnClearBtn}" class="dr-menu">

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

          <td>

            <digi:img src="images/bullet_green.gif" border="0" height="9" width="9" align="top" /> -

            <digi:trn key="aim:defaultFlag">Default Flag</digi:trn>

          </td>

        </tr>

        <tr>

          <td>

            <digi:trn key="aim:flagUploadHelpPhrase1">Click the image</digi:trn>

            <digi:img src="images/bullet_grey.gif" border="0" height="9" width="9" align="top" />

            <digi:trn key="aim:flagUploadHelpPhrase2">next to the flag to make it as the default for the site</digi:trn>

          </td>

        </tr>

      </table>

</td>

  </tr>



</table>

