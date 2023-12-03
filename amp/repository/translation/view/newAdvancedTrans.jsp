<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/displaytag" prefix="display" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<style>
<!-- /** Clearfix */
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

.controlPanel {
	margin: 50px;
}

.searchTermInput {
	width: 150px;
}

.showhide {
	float: right;
	font-weight: bold;
	width: 30px;
	text-align: center;
	cursor: pointer;
}

.expandColapse {
	width: 150px;
	background-color: #5E8AD1;
	border-top: 1px solid #99BAF1;
	border-left: 1px solid #99BAF1;
	border-right: 1px solid #225099;
	border-bottom: 1px solid #225099;
	font-size: 11px;
	color: #FFFFFF;
	font-weight: bold;
	padding-left: 5px;
	padding-right: 5px;
	padding-top: 3px;
	padding-bottom: 3px;
}

.ResultsSeparator {
	margin-bottom: 20px;
}

.listsBorder {
	margin: 50px;
}

.listPanels {
	width: 100%;
}

.searchResults {
	width: 70%;
	float: left;
}

.changesPanel {
	
	width: 30%;
	position:fixed;
	bottom:5px;
	right:10px;
	background:white;
	border:2px solid black;
}

.chgangesTable {
	width: 100%;
}

.chgangesTable * td {
	border-bottom: 1px black solid;
}

th {
	font: bold;
	font-size: 12px;
	font-family: serif;
}

.initiallyHidden {
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
	background-color: #f0f0f0;
}

.msgGroupFooter table tr td {
	padding-right: 10px;
}

.trnLanguage {
	width: 30px;
	font-weight: bold;
}

.trnText input {
	width: 500px;
}

.pagination {
	margin: 50px;
}
-->
</style>

<div class="advancedTranslations">
	<digi:form styleId="frmNewAdvancedTrns" action="/showNewAdvancedTranslation.do" method="post">
		<html:hidden styleId="hiddenPageNumber" name ="newAdvancedTrnForm" property="pageNumber"/>
		<html:hidden styleId="hiidenChangesOpened" name ="newAdvancedTrnForm" property="changesOpened"/>
		<html:hidden styleId="submitAction" name ="newAdvancedTrnForm" property="action"/>
		<div class="pageTitle">
			<span class="subtitle-blue"><digi:trn>Search Translations</digi:trn></span>
		</div>
		<div style="margin-bottom:20px;"><html:checkbox styleId="showOnlyEnglishCHK" name ="newAdvancedTrnForm" property="showOnlyEnglish" onclick="showOnlyEnglishTRNs();"/>
		<digi:trn>Show translations which have value only in english</digi:trn></div>
		<div class="controlPanel">
			<div class="clearfix">
				<div style="float:left;padding: 5px">
					<div><digi:trn>Search for</digi:trn>:</div>
					<html:text style="top:10px;" name="newAdvancedTrnForm" property="searchTerm" styleClass="searchTermInput" styleId="searchTerm"/>&nbsp;
				</div>
				<div style="float:left;padding: 5px">
				<div><digi:trn>Search language</digi:trn>:</div>
					<html:select style="top:10px;" property="selectedLocale">
						<c:forEach var="lng" items="${newAdvancedTrnForm.languages}">
							<html:option value="${lng.code}"><digi:trn>${lng.name}</digi:trn></html:option>
						</c:forEach>
					</html:select>
					
				</div>
				
				<div style="float:left;padding: 5px">
				<div><digi:trn>Result</digi:trn>:</div>
					<html:select style="top:10px;" property="itemsPerPage">
							<html:option value="10">10</html:option>
							<html:option value="20">20</html:option>
							<html:option value="25">25</html:option>
							<html:option value="50">50</html:option>
							<html:option value="100">100</html:option>
					</html:select>
				</div>
				<div style="float:left;padding:5px;">
					<div>&nbsp;</div>&nbsp;
                    <input type="submit" value="<digi:trn>Search</digi:trn>" id="btnDoSearch" class="buttonx"/>
				</div>
				<div style="float: left;padding:5px;">
					<div>&nbsp;</div>&nbsp;
					<input type="submit" value="<digi:trn>View All</digi:trn>"
						id="btnViewAll" class="buttonx"  title="<digi:trn>Click here to view all translations in database</digi:trn>"/>
				</div>
			</div>
		</div>
		<div class="listsBorder" style="width:100% !important;">
			<div class="listPanels clearfix">
				<div class="searchResults" style="width:100% !important;">
					<c:if test="${empty newAdvancedTrnForm.resultList}"><digi:trn>Nothing found</digi:trn></c:if>
					<c:if test="${!empty newAdvancedTrnForm.resultList}">
						<div class="searchResulToolBar">
							<input class="expandColapse toggle" type="button" value="<digi:trn>Expand All</digi:trn>">
							<div class="showhide toggle" title="Show unsaved changes">
								&gt;&gt;
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
												<digi:img styleClass="imgGroupToggle toggle" src="/TEMPLATE/ampTemplate/img_2/ico_plus.gif"/>
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
												<html:hidden styleClass="hiddenLocale" name="msg" property="locale"/>
												<html:hidden styleClass="hiddenKey" name="msg" property="key"/>
												<table>
													<tr>
														<td class="trnLanguage">${msg.locale}</td>
														<td class="trnText">
															<html:text name="msg" property="message" styleClass="txtTranslation" />
														</td>
														<td>
															<a class="deleteTranslation"><img alt="Delete translation" src="<digi:file src='images/deleteIcon.gif'/>"></a>
															<input type="button" class="delete_trans" value="<digi:trn>apply</digi:trn>" onclick="updateTranslation(this)">		
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
													Key: ${msgGroup.key}  Prefix : ${msgGroup.prefix}

												</td>
												<td>
													<input class="addTranslations" type="button" value="<digi:trn>Add Translation</digi:trn>" onclick="addTranslations(this,'${msgGroup.key}','<c:forEach items="${msgGroup.sortedMessages}" var="msg">${msg.locale},</c:forEach>')">
													
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
				<c:if test="${!empty newAdvancedTrnForm.resultList}">
				<div class="changesPanel">
					<div class="changesContainer">
                                            <digi:trn>No Changes</digi:trn>
					</div>
				</div>
				</c:if>
			</div>
		</div>
<!--		<digi:trn key="aim:npd:pagination:pageslabel">Pages:</digi:trn>-->
		<div class="pagination"><digi:trn>Pages:</digi:trn>		
			<c:forEach begin="1" end="${newAdvancedTrnForm.totalPages}" var="pageNo">
				<c:if test="${pageNo==newAdvancedTrnForm.pageNumber}"><strong>${pageNo}</strong></c:if>&nbsp;
				<c:if test="${pageNo!=newAdvancedTrnForm.pageNumber}"><a href="javascript: gotoPage(${pageNo})">${pageNo}</a></c:if>&nbsp;
			</c:forEach>
		</div>
	</digi:form>
</div>

<script type="text/javascript">

	<digi:context name="undoAction" property="context/ampModule/moduleinstance/AdvTranUndoChanges.do" />
	<digi:context name="addAction" property="context/ampModule/moduleinstance/newAdvTranAdd.do" />
	<digi:context name="delAction" property="context/ampModule/moduleinstance/newAdvTranDelete.do" />
	<digi:context name="getChanges" property="context/ampModule/moduleinstance/AdvTranGetChanges.do" />
	<digi:context name="saveChanges" property="context/ampModule/moduleinstance/AdvTranSaveChanges.do" />
	<digi:context name="updateChanges" property="context/ampModule/moduleinstance/AdvTranUpdate.do" />
	
	var imgPlus 		= '<digi:file src="/TEMPLATE/ampTemplate/img_2/ico_plus.gif"/>';
	var imgMinus 		= '<digi:file src="/TEMPLATE/ampTemplate/img_2/ico_minus.gif"/>';
	var imgLoading		= '<digi:file src="images/amploading.gif"/>';
	var undoUrl			= '<%=undoAction%>';
	var saveAllUrl		= '<%=saveChanges%>';
	var addUrl			= '<%=addAction%>';
	var delUrl			= '<%=delAction%>';
	var getChangesUrl	= '<%=getChanges%>';
	var updateUrl	= '<%=updateChanges%>';
	
	var arrAvailableLocales = new Array();
	<c:forEach items="${newAdvancedTrnForm.possibleLocales}" var="lng" varStatus="sts">
		arrAvailableLocales[${sts.index}] = '${lng}';
	</c:forEach>

	$(document).ready(function () {

		$('#btnViewAll').click(function() {
			$('#hiddenPageNumber').val(1);
			 $('#searchTerm').val('');
			  $('#submitAction').val("viewAll");
			  	return true;
		});
		$('#btnDoSearch').click(function() {
			if($('#searchTerm').val()==''){
				alert("<digi:trn>Please provide search criteria</digi:trn>");
				return false;
			}
			  $('#submitAction').val("search");
			  $('#hiddenPageNumber').val(1);
			  	return true;
		});

		$('.expandColapse').click(
			function() {
				if($(this).hasClass('toggle')) {
					$('div.msgGroupBodyClosed').slideUp();
					$('div.msgGroupBodyOpened').slideDown();
					$('img.imgGroupToggle').attr('src',imgMinus);
					$('.expandColapse').attr({value:'<digi:trn jsFriendly="true">Collapse All</digi:trn>', title:'Colapse all translations'});
				} else {
					$('div.msgGroupBodyOpened').slideUp();
					$('div.msgGroupBodyClosed').slideDown();
					$('img.imgGroupToggle').attr('src',imgPlus);
					$('.expandColapse').attr({value:'<digi:trn jsFriendly="true">Expand All</digi:trn>', title:'Expand all translations'});
				}
				$(this).toggleClass('toggle');
			}
		);
		
		$('img.imgGroupToggle').click(
			function(){
				if($(this).hasClass('toggle')) {
					$(this).parents('div.msgGroup').find('div.msgGroupBodyClosed').slideUp();
					$(this).parents('div.msgGroup').find('div.msgGroupBodyOpened').slideDown();
					$(this).attr('src', imgMinus);
				} else {
					$(this).parents('div.msgGroup').find('div.msgGroupBodyOpened').slideUp();
					$(this).parents('div.msgGroup').find('div.msgGroupBodyClosed').slideDown();
					$(this).attr('src',imgPlus);
				}
				$(this).toggleClass('toggle');
			}
		);
		$('.showhide').click(
			function() {
				if ($(this).hasClass('toggle')) {
					openChangesList(false);
				} else {
					openChangesList(true);
				}
				$(this).toggleClass('toggle');
			}
		);

		$('a.deleteTranslation').click(function(){
			deleteTranslation(this);
		});
		
		loadChanges();

		var opened = $('#hiidenChangesOpened').val();
		if (opened=='true'){
			openChangesList(true);
		}
		var show = $('#showOnlyEnglishCHK').is(':checked');
		if(show){
			 $('.controlPanel').hide();
		}
		else{
			$('.controlPanel').show();	
		}

		
	});//document ready end
	
function showOnlyEnglishTRNs(){

	$('#hiddenPageNumber').val(1);
	var myForm = document.newAdvancedTrnForm;
	myForm.submit();
	}

	function loadChanges(){
		var imgTag = '<div align="center"><img src="'+imgLoading+'"></div>';
		$('div.changesContainer').html(imgTag);
		var resp=$.ajax({
			   type: 'GET',
			   url: getChangesUrl,
			   cache : false,
			   success: function(data,msg){
			     $('div.changesContainer').html(data);
			     attachChangesButtonEvents();
			   },
		   	   error : function(XMLHttpRequest, textStatus, errorThrown){alert('Error, cannot get chages list.');} 
		});
	}


	function openChangesList(open){
		if (open){
			$('div.searchResults').animate({width:'70%'},300);
			$('div.changesPanel').animate({width: '30%'},300,function(){					
				$('div.changesContainer').fadeIn('slow');
				$('div.showhide').html('&gt;&gt;');
			});
			$('#hiidenChangesOpened').val(true);
		}else{
			$('div.changesContainer').fadeOut('slow',function(){
				$('div.searchResults').animate({width : '99%' },300);
				$('div.changesPanel').animate({width : '1%'},300);
				$('div.showhide').html('&lt;&lt;');
			});
			$('#hiidenChangesOpened').val(false);
		}
		
	}
	
	function deleteTranslation(but){
		if (confirm('Are you sure?')){
			var key = $(but).parents('div.trnContainer').find('input.hiddenKey').val();
			var loc = $(but).parents('div.trnContainer').find('input.hiddenLocale').val();
			var resp=$.ajax({
				   type: 'POST',
				   url: delUrl,
				   data: ({addKey : key, addLocale : loc}),
				   cache : false,
				   success: function(data,msg){
				   		$(but).parents('div.trnContainer').remove();
						loadChanges();
				   },
			   	   error : function(XMLHttpRequest, textStatus, errorThrown){alert('Error, cannot delete translation.');} 
			});
		}
	}
	function updateTranslation(but){
			var key = $(but).parents('div.trnContainer').find('input.hiddenKey').val();
			var loc = $(but).parents('div.trnContainer').find('input.hiddenLocale').val();
			var newMsg = $(but).parents('div.trnContainer').find('input[name="message"]').val();
			var resp=$.ajax({
				   type: 'POST',
				   url: updateUrl,
				   data: ({updateKey : key, updateLocale : loc, updateMessage:newMsg}),
				   cache : false,
				   success: function(data,msg){
						loadChanges();
				   },
			   	   error : function(XMLHttpRequest, textStatus, errorThrown){alert('Error, cannot delete translation.');} 
			});
	}
	
	function attachChangesButtonEvents(){

		$('#btnUndoSelected').click(function(){
			var checkboxes = $('input.changedListItem:checked');
			if (checkboxes!=null && checkboxes.length>0){
				if (confirm('<digi:trn jsFriendly="true">Are you sure</digi:trn>?')){
					var changes = [];
					checkboxes.each(function(index){
						changes[changes.length]=$(this).val();
					});
					var resp=$.ajax({
						   type: 'POST',
						   url: undoUrl,
						   data: ({undoChanges : changes}),
						   cache : false,
						   success: function(data,msg){
						     loadChanges();
						   },
					   	   error : function(XMLHttpRequest, textStatus, errorThrown){alert('Error, undo is not done.');} 
					})
				}
			}else{
				alert('Please select changes to undo.');
			}
		});//undo all

		$('#btnSaveAllChanges').click(function(){
			$.ajax({
				   type: 'GET',
				   url: saveAllUrl,
				   cache : false,
				   success: function(data,msg){
				     loadChanges();
				   },
			   	   error : function(XMLHttpRequest, textStatus, errorThrown){alert('Error, cannot save changes.');} 
			})
		});//save all
		
	}
	
	function addTranslations(but,key,strUsedLocales){
		var arrUsedLocales = strUsedLocales.split(',');
		var arrUnusedLocales = getMissingLocales(arrUsedLocales);
		showPopin(but,key,arrUnusedLocales);
		return false;
	}

	function showPopin(but,key,locales){
		$(but).attr('disabled','disabled');
		$('#btnDoSearch').attr('disabled','disabled');
		$('#btnViewAll').attr('disabled');
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
		newDivHtml+=			'<input type="button" value="<digi:trn>Save</digi:trn>" onclick="saveAddTranslation(this)">';
		newDivHtml+=		'</td>';
		newDivHtml+=		'<td class="tdCancel">';
		newDivHtml+=			'<input type="button" value="<digi:trn>Cancel</digi:trn>" onclick="cancelAddTranslation(this)">';
		newDivHtml+=		'</td>';
		newDivHtml+=	'</tr></table>';
		newDivHtml+='</div>';
		//alert(containers.html());
		containers.append(newDivHtml);
		$('select#selectedLocale').focus();
	}

	function saveAddTranslation(but){
		var container = $(but).parents('div.trnContainer');
		var selectLocale = container.find('#selectedLocale').val();
		var key = container.find('input[name="key"]').val()
		var message = container.find('input[name="message"]').val()
		
		sendAddTranslation(selectLocale,message, key);

		container.find('select.selectLocaleToAdd').remove();
		container.find('td.trnLanguage').html(selectLocale);
		var closedGroup = container.parents('div.msgGroup').find('div.msgGroupBodyClosed');
		closedGroup.html(closedGroup.html()+', '+selectLocale);
		

		container.find('td.tdCancel').remove();
		container.find('td.tdSave').remove();
		container.parents('div.msgGroup').find('.addTranslations').removeAttr('disabled');
		$('#btnDoSearch').removeAttr('disabled');
		$('#btnViewAll').removeAttr('disabled');
	}

	function sendAddTranslation(locale,message, key){
		var resp=$.ajax({
			   type: 'POST',
			   url: addUrl,
			   data: ({addLocale : locale, addMessage : message, addKey : key}),
			   cache : false,
			   success: function(data,msg){
			   		loadChanges();
			   },
		   	   error : function(XMLHttpRequest, textStatus, errorThrown){alert('Error, cannot add.');} 
		})
	}
	

	function cancelAddTranslation(but){
		$(but).parents('div.msgGroupBodyOpened').find('input.addTranslations').removeAttr('disabled');
		$(but).parents('div.trnContainer').remove();
		$('#btnDoSearch').removeAttr('disabled');
		$('#btnViewAll').removeAttr('disabled');
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

	function gotoPage(pageNumber){
		$('#hiddenPageNumber').val(pageNumber);
		var myForm = document.newAdvancedTrnForm;
		myForm.submit();
	}
	
	

</script>