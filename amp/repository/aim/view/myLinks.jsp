<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<br/>
<div id="content"  class="yui-skin-sam" style="width:100%;"> 
	<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
        <ul class="yui-nav">
          <li class="selected">
          <a title='<digi:trn key="aim:ListofRelatedLinks">Frequently Used Links for Desktop</digi:trn>'>
          <div>
            <digi:trn key="aim:relatedLinks">Related Links</digi:trn>
          </div>
          </a>
          </li>
        </ul>
        <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
            <c:set var="translation">
	            <digi:trn key="aim:clickToViewLinkDetails">Click here to view Link Details</digi:trn>
            </c:set>				

            <logic:notEmpty name="myLinks" scope="session">
                <div title="${translation}">
                <logic:iterate name="myLinks" id="link" scope="session" type="org.digijava.module.aim.helper.Documents"> 
                        <IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>
                            <digi:link href="/viewQuickLinks.do">
                                <bean:write name="link" property="title"/>
                            </digi:link>
                            <br />
                </logic:iterate>
                </div>

                <bean:size id="linkCount" name="myLinks" scope="session" />
				<br />
                <c:if test="${linkCount > 5}">

                    <c:set var="translation">
                        <digi:trn key="aim:clickToViewMoreLinks">Click here to view more Links</digi:trn>
                    </c:set>					

                    <div title='${translation}'>
                        <digi:link href="/viewQuickLinks.do">
                            <digi:trn key="aim:more">...more</digi:trn>
                        </digi:link>							
                    </div>
                </c:if>
            </logic:notEmpty>

            <logic:empty name="myLinks" scope="session">
                <digi:trn key="aim:noDesktopLinks">No links</digi:trn>
            </logic:empty>



