<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/displaytag" prefix="display" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>




<style>
<!--
	/** Clearfix */
	.clearfix:after {
		clear: both;
	    content: ".";
	    display: block;
	    height: 0;
	    line-height: 0;
	    visibility: hidden;
	}
	.clearfix {
	    display: inline-block;
	}
	html[xmlns] .clearfix {
	    display: block;
	}
	* html .clearfix {
	    height: 1%;
	}
	/** End Clearfix */


	.pageTitle {
		margin: 20px;
	}
	.controlPanel{
		margin: 50px;
	}
	.searchTermInput{
		width: 150px;
	}
	
	.showhide{
		float:right;
		font-weight: bold;
		width: 30px;
		text-align: center;
		cursor: pointer;
	}
	.expandColapse{
		width: 150px;
	}
	.searchResultsSeparator {
		margin-bottom: 20px;
	}
	.listsBorder{
		margin: 50px;
	}
	.listPanels{
		width: 100%;
	}
	.searchResults{
		width: 99%;
		float:left;
	}
	.changesPanel{
		float:right;
		width: 1%;
	}
	.changesContainer{
		display: none;
	}
	
	.chgangesTable{
		width: 100%;
	}
	.chgangesTable * td {
		border-bottom : 1px black solid;
	} 
	th {
		font: bold;
		font-size: 12px;
		font-family: serif;
	}
	.initiallyHidden{
		display: none;
	}
	.msgGroupTitle {
		margin: 0px;
		padding: 0px;
		background-color: #d7eafd;
		font-weight: bold;
	}
	.msgGroupBodyOpened {
		padding: 0px;
		display: none;
	}
	.msgGroupBodyClosed {
		padding: 0px;
		display: block;
	}
	.msgGroupFooter {
		padding-left: 100px;
		background-color : #f0f0f0 ;
	}
	.msgGroupFooter table tr td{
		padding-right:10px; 
	}
	.trnLanguage {
		width: 30px;
		font-weight: bold;
	}
	.trnText input{
		width : 600px; 
	}
	.pagination{
		margin: 50px;
	}	
	
-->
</style>

<div class="advancedTranslations">
	<digi:form styleId="frmNewAdvancedTrns" action="/showNewAdvancedTranslation.do" method="post">
		<html:hidden name ="newAdvancedTrnForm" property="pageNumber"/>
		<html:hidden name ="newAdvancedTrnForm" property="changesOpened"/>
		<div class="pageTitle">
			<span class="subtitle-blue">Search Translations (Page is under construction)</span>
		</div>
		<div class="controlPanel">
			<div class="clearfix">
				<div style="float:left">
					<div>Search for:</div>
					<html:text name="newAdvancedTrnForm" property="searchTerm" styleClass="searchTermInput"/>
				</div>
				<div style="float:left">
					<div>&nbsp;</div>					
					<html:submit styleId="btnDoSearch" value="Search"/>
				</div>
			</div>
		</div>
		<div class="listsBorder">
			<div class="listPanels clearfix">
				<div class="searchResults">
					<c:if test="${empty newAdvancedTrnForm.resultList}">Nothing found</c:if>
					<c:if test="${!empty newAdvancedTrnForm.resultList}">
						<div class="searchResulToolBar">
							<input class="expandColapse" type="button" value="Expand All">
							<div class="showhide" title="Show unsaved chnages">
								&lt;&lt;
							</div>
						</div>
						<div class="searchResultsSeparator">
							<hr>
						</div>
						<c:forEach items="${newAdvancedTrnForm.resultList}" var="msgGroup">
							<div class="msgGroup">
								<div class="msgGroupTitle">
									<table>
										<tr>
											<td>
												<digi:img styleClass="imgGroupToggle" src="images/dhtml/DHTMLSuite_plus.gif"/>
											</td>
											<td>
												<bean:write name="msgGroup" property="defaultText" filter="true"/>
											</td>
										</tr>
									</table>
								</div>
								<div class="msgGroupBodyOpened">
									<div class="trnContainers">
										<c:forEach items="${msgGroup.sortedMessages}" var="msg">
											<div class="trnContainer">
												<html:hidden name="msg" property="locale"/>
												<html:hidden name="msg" property="key"/>
												<table>
													<tr>
														<td class="trnLanguage">${msg.locale}</td>
														<td class="trnText">
															<html:text name="msg" property="message" styleClass="txtTranslation" />
														</td>
													</tr>
												</table>
											</div>
										</c:forEach>
									</div>
									<div class="msgGroupFooter">
										<table>
											<tr>
												<td>
													Key: ${msgGroup.key}
												</td>
												<td>
													<input class="addTranslations" type="button" value="Add Translation" onclick="addTranslations(this,'${msgGroup.key}','<c:forEach items="${msgGroup.sortedMessages}" var="msg">${msg.locale},</c:forEach>')">
													<input class="initiallyHidden changeDetector" type="button" value="Applay">
													<input class="initiallyHidden changeDetector" type="button" value="Reset">
												</td>
											</tr>
										</table>
									</div>
								</div>
								<div class="msgGroupBodyClosed">
									<c:forEach items="${msgGroup.sortedMessages}" var="msg" varStatus="lngVarStatus">
											${msg.locale} <c:if test="${not lngVarStatus.last}">,&nbsp;</c:if>
									</c:forEach>
								</div>
							</div>
							<div class="msgGroupSeparator">&nbsp;</div>
						</c:forEach>
					</c:if>
				</div>
				<div class="changesPanel">
					<div class="changesContainer">
						<c:if test="${empty newAdvancedTrnForm.changesList}">
							No Changes
						</c:if>
						<c:if test="${not empty newAdvancedTrnForm.changesList}">
						
							<table class="chgangesTable">
								
								<tr style="background-color: #dbd6d6;">
									<th>Undo</th>
									<th>Oper</th>
									<th>Transl</th>
								</tr>
							
								<tbody>
									
									<c:forEach items="${newAdvancedTrnForm.changesList}" var="changedMsg">
										<tr>
											<td>
												<input type="checkbox" class="changedListItem" value="${changedMsg.element.locale}_${changedMsg.element.key}">
											</td>
											<td>
												${changedMsg.operation}
											</td>
											<td>
												${changedMsg.element.message}
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							<input id="btnUndoSelected" 	type="button" value="Undo selected"/>
							<input id="btnSaveAllChanges" 	type="button" value="Save All Changes"/>
						</c:if>
					</div>
				</div>
			</div>
		</div>
		<div class="pagination">Pages:
			<c:forEach begin="1" end="${newAdvancedTrnForm.totalPages}" var="pageNo">
				<c:if test="${pageNo==newAdvancedTrnForm.pageNumber+1}"><strong>${pageNo}</strong></c:if>&nbsp;
				<c:if test="${pageNo!=newAdvancedTrnForm.pageNumber+1}"><a href="javascript: gotoPage(${pageNo})">${pageNo}</a></c:if>&nbsp;
			</c:forEach>
		</div>
	</digi:form>
</div>

<script type="text/javascript">

	var imgPlus 	= '<digi:file src="images/dhtml/DHTMLSuite_plus.gif"/>';
	var imgMinus 	= '<digi:file src="images/dhtml/DHTMLSuite_minus.gif"/>';
	var arrAvailableLocales = new Array();
	<c:forEach items="${newAdvancedTrnForm.possibleLocales}" var="lng" varStatus="sts">
		arrAvailableLocales[${sts.index}] = '${lng}';
	</c:forEach>

	$(document).ready(function () {
		$('input.txtTranslation').bind('keypress',function(e){
			$(this).parents('div.msgGroup').find('changeDetector').show();
		});

		$('.expandColapse').toggle(
				function(){
					$('div.msgGroupBodyClosed').slideUp();	
					$('div.msgGroupBodyOpened').slideDown();	
					$('img.imgGroupToggle').attr('src',imgMinus);
					$('.expandColapse').attr({value:'Colapse All', title:'Colapse all translations'});
				},
				function(){
					$('div.msgGroupBodyOpened').slideUp();	
					$('div.msgGroupBodyClosed').slideDown();	
					$('img.imgGroupToggle').attr('src',imgPlus);
					$('.expandColapse').attr({value:'Expand All', title:'Expand all translations'});
				}
		);
		
		$('img.imgGroupToggle').toggle(
				function(){
					$(this).parents('div.msgGroup').find('div.msgGroupBodyClosed').slideUp();	
					$(this).parents('div.msgGroup').find('div.msgGroupBodyOpened').slideDown();	
					this.src=imgMinus;
				},
				function(){
					$(this).parents('div.msgGroup').find('div.msgGroupBodyOpened').slideUp();	
					$(this).parents('div.msgGroup').find('div.msgGroupBodyClosed').slideDown();	
					this.src=imgPlus;
				}
		);
		$('.showhide').toggle( 
				function(){
					$('div.searchResults').animate({width:'75%'},500);
					$('div.changesPanel').animate({width: '25%'},500,function(){					
						$('div.changesContainer').fadeIn('slow');
						$('div.showhide').html('&gt;&gt;');
					});
					var opened = document.getElementsByName('changesOpened')[0];
					opened = true;
				},
				function(){
					$('div.changesContainer').fadeOut('slow',function(){
						$('div.searchResults').animate({width : '99%' },500);
						$('div.changesPanel').animate({width : '1%'},500);
						$('div.showhide').html('&lt;&lt;');
					});
					var opened = document.getElementsByName('changesOpened')[0];
					opened = false;
				}
		);

		var opened = document.getElementsByName('changesOpened')[0];
		if (opened.value==true){
			$('.showhide').toggle();
		}

		$('#btnUndoSelected').click(function(){
			alert('Are you sure?');
		});
		
	});//document ready end

	function addTranslations(but,key,strUsedLocales){
		var arrUsedLocales = strUsedLocales.split(',');
		var arrUnusedLocales = getMissingLocales(arrUsedLocales);
		showPopin(but,key,arrUnusedLocales);
		return false;
	}

	function showPopin(but,key,locales){
		$(but).attr('disabled','disabled');
		$('#btnDoSearch').attr('disabled','disabled');
		var containers = $(but).parents('div.msgGroupBodyOpened').find('div.trnContainers');
		var newDivHtml='<div class="trnContainer">';
		newDivHtml+='	<input type="hidden" name="locale" value="">';
		newDivHtml+='	<input type="hidden" name="key" value="'+key+'">';
		newDivHtml+=	'<table><tr>';
		newDivHtml+=		'<td class="trnLanguage"><select class="selectLocaleToAdd" id="selectedLocale" name="selectedLocale">';
		for(i=0;i<locales.length;i++){
			newDivHtml+='<option value="'+locales[i]+'">'+locales[i]+'</option>';
		}
		newDivHtml+=		'</select></td>';
		newDivHtml+=		'<td class="trnText">';
		newDivHtml+=			'<input type="text" name="message" class="txtTranslation">';
		newDivHtml+=		'</td>';
		newDivHtml+=		'<td class="tdSave">';
		newDivHtml+=			'<input type="button" value="Save" onclick="saveAddTranslation(this)">';
		newDivHtml+=		'</td>';
		newDivHtml+=		'<td class="tdCancel">';
		newDivHtml+=			'<input type="button" value="Cancel" onclick="cancelAddTranslation(this)">';
		newDivHtml+=		'</td>';
		newDivHtml+=	'</tr></table>';
		newDivHtml+='</div>';
		//alert(containers.html());
		containers.append(newDivHtml);
	}

	function cancelAddTranslation(but){
		$(but).parents('div.msgGroupBodyOpened').find('input.addTranslations').removeAttr('disabled');
		$(but).parents('div.trnContainer').remove();
		$('#btnDoSearch').removeAttr('disabled');
	}

	function saveAddTranslation(but){
		var container = $(but).parents('div.trnContainer');
		container.find('select.selectLocaleToAdd').remove();
		var selectLocale = container.find('select.trnLanguage').val();
		alert(selectLocale);
		container.find('select.trnLanguage').append(selectLocale);

		container.find('input.tdCancel').remove();
		container.find('input.tdSave').remove();
		$(but).parents('div.msgGroupBodyOpened').find('input.addTranslations').removeAttr('disabled');
		$('#btnDoSearch').removeAttr('disabled');
		
	}
	
	function getMissingLocales(array1){
		var array2 = arrAvailableLocales;
		var array3 = new Array();
		for(i=0;i<array2.length;i++){
			var found = false;
			for(j=0;j<array1.length;j++){
				if(array2[i]==array1[j]){
					found = true;
					break;
				}
			}
			if(!found){
				array3[array3.length]=array2[i];
			}
		}
		return array3;
	}

	function sendAddTranslation(div,locale,message){
		alert('locale='+locale+' message='+message);
		appendTranslation(div,locale,message);
	}
	function appendTranslation(div,locale,message){
		var aaa=$(div).attr('class');
		alert(aaa);
	}
	function gotoPage(pageNumber){
		var el = document.getElementsByName('pageNumber')[0];
		el.value=pageNumber
		var myForm = document.newAdvancedTrnForm;
		myForm.submit();
	}


</script>