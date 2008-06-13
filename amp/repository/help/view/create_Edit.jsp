<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="helpForm"/>
<div id="content"  class="yui-skin-sam" style="width:100%;"> 
	<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
               
                        <ul class="yui-nav">
                          <li class="selected">
                          <a title='><digi:trn key="aim:help:editcreate"> Edit/Create</digi:trn>'>
                          <div>
                            <digi:trn key="aim:help:editcreate"> Edit/Create</digi:trn>
                          </div> 
                          </a>
                          </li>
                        </ul>
                        <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">

<div style="padding:2px";>
<c:set var="topicEdit">
      <digi:trn key="aim:help:clickToEditHelpTopic">Click here to Edit Help Topic</digi:trn>
    </c:set>
      
      <digi:link href="/helpActions.do?actionType=editHelpTopic&amp;topicKey=${helpForm.topicKey}&amp;wizardStep=0" 
								title="${topicEdit}" >
        <digi:trn key="aim:help:editTopic">Edit Topic</digi:trn>
      </digi:link>
      | <c:set var="topicCreate">
      <digi:trn key="aim:help:clickToAddHelpTopic">Click here to Create Help Topic</digi:trn>
    </c:set>
      
      <digi:link href="/helpActions.do?actionType=createHelpTopic&amp;wizardStep=0" 
								title="${topicCreate}" >
        <digi:trn key="aim:help:createTopic">Create Topic</digi:trn>
      </digi:link>
      | <c:set var="topicDelete">
      <digi:trn key="aim:help:clickToDeleteHelpTopic">Click here to Delete Help Topic</digi:trn>
    </c:set>
      
      <digi:link href="/helpActions.do?actionType=deleteHelpTopic&amp;topicKey=${helpForm.topicKey}" 
								title="${topicDelete}" >
        <digi:trn key="aim:help:removeTopic">Remove Topic</digi:trn>
      </digi:link>
    </div> 
      					</div>     
	</div>
    </div>
    

