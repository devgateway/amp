<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<script language="javascript" type="text/javascript">
	function toggleDiv(id,state){
		if (state==true){
			document.getElementById('uncollapse'+id).style.display='block';
			document.getElementById('collapse'+id).style.display='none';
		}
		if (state==false){
			document.getElementById('collapse'+id).style.display='block';
			document.getElementById('uncollapse'+id).style.display='none';
		}
	}
</script>
<digi:instance property="helpForm" />
<div id="content"  class="yui-skin-sam" style="width:100%;"> 
	<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
               <ul class="yui-nav">
                          <li class="selected">
                          <a title='<digi:trn key="aim:PortfolioOfReports">Portfolio Reports </digi:trn>'>
                          <div>
                          	<digi:trn key="aim:helpTopic">Help Topics</digi:trn>
                          </div>
                          </a>
                          </li>
                        </ul>
                        <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                        <c:if test="${ not empty helpForm.topicTree}">
                        
                                  <c:if test="${not empty helpForm.parentId}">
                                    <c:if test="${helpForm.parentId!=''}">
                                      <c:set var="curTopicParentId">${helpForm.parentId}</c:set>
                                    </c:if>
                                  </c:if>
                                  
                                    <c:forEach var="parent" items="${helpForm.topicTree}">
                                      <c:if test="${curTopicParentId!=parent.helpTopicId}">
                                           <div id="collapse${parent.helpTopicId}" style="display:block; padding:3px">
                                            <digi:img  src="images/arrow_right.gif" onclick="javascript:toggleDiv(${parent.helpTopicId},true);"/>
                                                <a href="/help/helpActions.do?actionType=viewSelectedHelpTopic&amp;topicKey=${parent.topicKey}">
                                                    <digi:trn key="${parent.titleTrnKey}"></digi:trn>
                                                  </a> 
                                            </div>
                                            
                                                                                        
                                          <div id="uncollapse${parent.helpTopicId}" style="display: none;padding:3px">
                                          	<digi:img src="images/arrow_down.gif" onclick="javascript:toggleDiv(${parent.helpTopicId},false);"/>
                                                	<a href="/help/helpActions.do?actionType=viewSelectedHelpTopic&amp;topicKey=${parent.topicKey}">
                                                    	<digi:trn key="${parent.titleTrnKey}"></digi:trn>
                                                    </a>                                                   
                                                      <c:if test="${not empty parent.children}">
                                                       	<c:forEach var="child" items="${parent.children}">
                                                            <div id="item" style="padding-left:15px;padding-top:2px;padding-bottom:2px;">
                                                            <a href="/help/helpActions.do?actionType=viewSelectedHelpTopic&amp;topicKey=${child.topicKey}">
                                                                <digi:trn key="${child.titleTrnKey}"></digi:trn></a>
                                                              </div>
                                                          </c:forEach>
                                                     
                                                      </c:if>
                                                  
                                            
                                          </div>
                                        </c:if>
                                            <c:if test="${curTopicParentId==parent.helpTopicId}">
                                             <div id="collapse${parent.helpTopicId}" style="display:none;padding:3px">
                                              		<digi:img src="images/arrow_right.gif" onclick="javascript:toggleDiv(${parent.helpTopicId},true);"/>
                                                   <a href="/help/helpActions.do?actionType=viewSelectedHelpTopic&amp;topicKey=${parent.topicKey}">
                                                      <digi:trn key="${parent.titleTrnKey}"></digi:trn>
                                                    </a> 
                                                   
                                              </div>
                                              <div id="uncollapse${parent.helpTopicId}" style="display: block;padding:3px">
                                                <digi:img src="images/arrow_down.gif" onclick="javascript:toggleDiv(${parent.helpTopicId},false);"/>
                                                      <a href="/help/helpActions.do?actionType=viewSelectedHelpTopic&amp;topicKey=${parent.topicKey}">
														<digi:trn key="${parent.titleTrnKey}"></digi:trn>
                                                      </a>
                                                     
                                                        <c:if test="${not empty parent.children}">
                                                          <c:forEach var="child" items="${parent.children}">
                                                            <div id="item" style="padding-left:15px;padding-top:2px;padding-bottom:2px;">
                                                              <a href="/help/helpActions.do?actionType=viewSelectedHelpTopic&amp;topicKey=${child.topicKey}">
                                                                  <digi:trn key="${child.titleTrnKey}"></digi:trn>
                                                                </a> 
                                                            </div>
                                                            </c:forEach>
                                                      
                                                        </c:if>
                                                
                                              </div>
                                            </c:if>
                                      
                                    </c:forEach>
                                
                              </c:if>
                           
</div>



