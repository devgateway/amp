<meta http-equiv="Content-Language" content="en-us">
<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<html:javascript formName="userBioForm" />

<!-- Top Nav start-->
<table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="center">
<table cellpadding=2 cellspacing=2 border="0">
<digi:form action="/userBio.do" enctype="multipart/form-data" onsubmit="return validateUserBioForm(this);">
<tr>
	<td><img src="images/trans.gif" height=1 width="20" border="0"></td>
	<td colspan=2 align=left class="Title"><c:out value="${userBioForm.name}"/>
    <digi:trn key="um:portraitBio">Portrait and Bio</digi:trn></td>
	<td><img src="images/trans.gif" height=1 width="20" border="0"></td>
</td>
<tr>
	<td><img src="images/trans.gif" height=1 width="20" border="0"></td>
	<td colspan=2 align=left class="text"><digi:trn key="um:howWorldSeesYou">How would you like the world to see you</digi:trn></td>
	<td><img src="images/trans.gif" height=1 width="20" border="0"></td>
</td>
<tr>
	<td><img src="images/trans.gif" height=1 width="20" border="0"></td>
	<td colspan=2 align=left class="text">
	<digi:trn key="um:uploadInfoText">
		Upload your portrait, a scanned JPEG or GIF, from your desktop computer system (note that you can't refer to an image elsewhere on the Internet, this image must be on your computer's hard drive).</digi:trn>
	</td>
	<td><img src="images/trans.gif" height=1 width="20" border="0"></td>
</td>
</tr>
<tr><td><img src="images/trans.gif" height=10 width=1 border="0"></td></tr>
<tr>
	<td><img src="images/trans.gif" height=1 width="20" border="0"></td>
	<td valign="top" class=text>
	<digi:errors/>
		<digi:trn key="um:fileName">Filename:</digi:trn><br>
		<html:file property="photoFile" size="30"/>
		<br><small><digi:trn key="um:useBrowseToLocatePic">
		Use the 'Browse...' button to locate your file, then click 'Update'.</digi:trn></small><br><digi:img src="module/common/images/arrow.gif" border="0" />
		<digi:link href="/userBio.do?clear=image" styleClass="text"><digi:trn key="um:clickYoErase">Click here if you want to erase it.</digi:trn></digi:link>
	</td>
	<td rowspan=3 align="center">
		<digi:context name="showImage" property="context/showImage.do" />
		<img src="<%=showImage%>" height="106" />
		<br><small>September 21, 2003</small>
	</td>
	<td><img src="images/trans.gif" height=1 width="20" border="0"></td>
</tr>
<tr><td><img src="images/trans.gif" height=15 width=1 border="0"></td></tr>
<tr>
	<td><img src="images/trans.gif" height=1 width="20" border="0"></td>
	<td colspan=2 class=text align=left>
		<digi:trn key="um:biography">Biography:</digi:trn><br>
		<html:textarea property="bioText" cols="45" rows="6"/>
		<br><small><digi:trn key="um:maxLengthIs">Maximum length is</digi:trn> 2000<digi:trn key="um:characters"> characters</digi:trn></small>
	</td>
	<td><img src="images/trans.gif" height=1 width="20" border="0"></td>
</tr>
<tr>
	<td><img src="images/trans.gif" height=1 width="20" border="0"></td>
	<td colspan=2 align=right class="comment"><html:submit />
	<br><small><digi:trn key="um:refreshWindow">After updating please refresh the window to view updated information.</digi:trn></small>
</td>
	<td><img src="images/trans.gif" height=1 width="20" border="0"></td>
</tr></digi:form>
</table>


</td>
</tr>
<tr><td height="1" bgcolor="#000000"><img src="images/trans.gif" width="1" height="1" alt=""></td></tr>
<tr><td height="20" align="center" valign="center" bgcolor="#DDDDDD"><a href="javascript:window.close()" class="navtop4"><digi:trn key="um:closeThisWindow">Close this window</digi:trn></a></td></tr>
</table>