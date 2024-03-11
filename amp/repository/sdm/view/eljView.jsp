<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org" prefix="digi" %>
<%@taglib uri="http://digijava.org/eljTagLibrary" prefix="elj" %>

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
