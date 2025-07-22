<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script>
  function fnOnAdd() {
      <digi:context name="addCommonInstance" property="context/module/moduleinstance/addCommonInstance.do" />
      document.commonInstancesForm.action = "<%= addCommonInstance%>";
      document.commonInstancesForm.submit();
  }
  function fnOnDelete( index) {
      <digi:context name="deleteCommonInstance" property="context/module/moduleinstance/deleteCommonInstance.do" />
      document.commonInstancesForm.action = "<%= deleteCommonInstance%>?index=" + index;
      document.commonInstancesForm.submit();
  }
</script>

<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<tr class="yellow">
		<td><digi:img src="module/admin/images/yellowLeftTile.gif" border="0" width="20"/></td>
		<td width="100%">
			<font class="sectionTitle">
				<digi:trn key="admin:commonInstances">COMMON INSTANCES</digi:trn>
			</font>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="yellow" valign="top" align="left">

<digi:errors />
<digi:form method="post" action="/saveCommonInstances.do">
<digi:instance property="commonInstancesForm" />
<table border="1" cellspacing="0" cellpadding="3" bordercolor="#E3DDC1" style="border-collapse: collapse;">
<tr>
	<td noWrap class="text"><digi:trn key="admin:module">Module</digi:trn></td>
	<td noWrap class="text"><digi:trn key="admin:instance">Instance</digi:trn></td>
	<td noWrap class="text"><digi:trn key="admin:numOfItemsInTeaser">Number of items in teaser</digi:trn></td>
	<td colspan="3">&nbsp;</td>
</tr>
<logic:iterate indexId="index" name="commonInstancesForm" id="commonInstance" property="commonInstances" type="org.digijava.module.admin.form.CommonInstancesForm.CommonInstanceInfo">
<tr>
<td>
	<html:hidden name="commonInstance" property="id" indexed="true"/>
	<html:select name="commonInstance" property="module" indexed="true">
	   <html:options property="modules" />
	</html:select>
</td>
<td>
    <html:text name="commonInstance" property="instance" indexed="true" />
</td>
<td>  
	<html:select name="commonInstance" property="selectedNumOfItemsInTeaser" indexed="true">
    	<c:set var="numberItems" value="${commonInstancesForm.numOfItemsInTeaser}" scope="page" />
	    <html:options  collection="numberItems" property="value" labelProperty="value" />
    </html:select>
</td>
<td noWrap>
	<a href='javascript:fnOnDelete("<%= index %>")'>
		<digi:trn key="admin:delete">Delete</digi:trn>
	</a>
</td>
</tr>
</logic:iterate>
<tr>
	<td colspan="10"><a href='javascript:fnOnAdd()' ><digi:trn key="admin:add">Add</digi:trn></a>
</td>
</tr>
</table>
<br />
<html:submit />
</digi:form>