<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@taglib uri="/taglib/editlivejava" prefix="elj" %>

<digi:errors />
<digi:instance property="sdmForm"/>
<digi:form action="/uploadImage.do" method="POST">
  <table cellpadding="0" cellspacing="5" width="100%">
    <tr>
       <td align="center"><H3>EditLive! for Java</H3></td>
    </tr>
    <tr valign="top">
	<td align="center">
            <elj:ELJGlobal downloadDir="http://www.demosite.ge:8080/digijava/redistributables/editlivejava" />
            <elj:ELJInstance name="eljApplet" configurationFile="http://www.demosite.ge:8080/digijava/redistributables/editlivejava/sample_eljconfig.xml" height="400" width="800"  />
        </td>
    </tr>
    <tr>
       <td><hr></td>
    </tr>
    <tr>
       <td><html:submit value="update" /></td>
    </tr>
</table>
</digi:form>
