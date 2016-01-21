enableComputateVisibleSec7tions = false;
onepagerMode = ${onepagerMode};
isTabView = ${isTabView};

function getFile(){
    $("#upfile").click();
}

function setLabel(obj){
          var file = obj.value;
          var fileName = file.split("\\");
          var text = fileName[fileName.length-1];
          if (text.length > 15) {
              text = text.substring (0,12) + "....";
          }
          $("#uploadLabel").text(text);
}

function isScrolledIntoView(docViewTop, docViewBottom, elem){
    var elemTop = $(elem).offset().top;
    var elemBottom = elemTop + $(elem).height();

    return (((elemBottom >= docViewTop) && (elemBottom <= docViewBottom)) 
      || ((elemTop <= docViewBottom) && (elemTop >= docViewTop)) 
      || ((elemTop <= docViewTop) && (elemBottom >= docViewBottom)));
}


function computateVisibleSections(){
	var docViewTop = $(window).scrollTop();
    var docViewBottom = docViewTop + $(window).height();

	if (enableComputateVisibleSections){
		$('#qListItems').find('li').removeClass('quickListHighlight');
		$('span[name=section]').each(function (){
			if (isScrolledIntoView(docViewTop, docViewBottom, this))
				$('#qItem'+$(this).find('a:first').attr('id')).parent().parent().addClass('quickListHighlight');
		});
	}
}

// Manage the position of the right menu in AF after scrolling
function adjustQuickLinks(){
	var contentMarginLeft = $('#stepHead').offset().left;
	var contentMarginTop = $('#stepHead').offset().top;
	var contentHeight = $('#stepHead').height() + $('#mainContent').height() + 55; 
	var contentWidth = $('#stepHead').width() + 40;
	var rightMenuHeight = $('#rightMenu').height();
	var rightMenuWidth = $('#rightMenu').width();
	
	// the initial position of the right menu should be below the next menu
	if (contentMarginTop < 130) {
		contentMarginTop = 130;
	}
	
	var currentScrollLeft = $(window).scrollLeft();
	rightMenuMargin = contentMarginLeft + contentWidth - currentScrollLeft;
	
	if (($(window).scrollTop() + rightMenuHeight) > contentHeight) {
		$('#rightMenu').css('position', 'absolute');
		$('#rightMenu').css('top', (contentHeight + contentMarginTop - rightMenuHeight) + "px");
		if ($(window).width() < (contentWidth + rightMenuWidth)) {
			$('#rightMenu').css('left', contentWidth + "px");
		} else {
			$('#rightMenu').css('left', rightMenuMargin + "px");
		}
	} else {
		$('#rightMenu').css('position', 'fixed')
		$('#rightMenu').css('top', contentMarginTop + "px");
		$('#rightMenu').css('left', rightMenuMargin + "px");
	}
	
	if (onepagerMode) {
		computateVisibleSections();
	}
}


function highlightQItem(currentItem){
	$('#qListItems').find('li').removeClass('quickListHighlight');
	$(currentItem).parent().parent().addClass('quickListHighlight');
}

function showSection(itemId){
	if (onepagerMode){
		$('#' + itemId).parent().parent().siblings('div:first').show();
		$('html, body').animate({scrollTop: $('#' + itemId).offset().top}, 1200);
	}
	else{
		window.scrollTo(0,0);
		$('span[name=section]').hide();
		$('#'+itemId).parents('span[name=section]').show();
	}
	highlightQItem($("#qItem"+itemId));
}

function switchOnepagerMode(){
	if (onepagerMode){
		onepagerMode = false;
		$('span[name=section]').hide();
		$('span[name=section]').eq(0).show();
		$('#imgGroupMode').hide();
		$('#imgOnepagerMode').show();
	}
	else{
		onepagerMode = true;
		$('span[name=section]').show();
		$('#imgOnepagerMode').hide();
		$('#imgGroupMode').show();
	}
	highlightQItem($("#qListItems").find('a:first'));
}

function translationsEnable(){
	$("#switchTranslationMode").attr('href', 'javascript:wicketSwitchTranslationMode()');
	$("#switchFMMode").css("display", "block");
}
function subSectionsSliderEnable(){
	$("a.slider").click(function(){
		$(this).siblings("div:first").slideToggle();
		return false;
	});
}
function rightMenuEnable(){
	$('#wicketRightMenu').replaceWith($('#rightMenu'));
	$('#imgGroupMode').attr("title", $("#imgGroupModeTitle").html());
	$('#imgOnepagerMode').attr("title", $("#imgOnepagerModeTitle").html());
	if (typeof(onepagerMode) !== 'undefined') {
		if(onepagerMode){
			$('#imgGroupMode').show();
		}
		else{
			$('#imgOnepagerMode').show();
		}
	}

	var mainContentTop = $('#stepHead').offset().top;
	var mainContentLeft = $('#stepHead').offset().left + $('#stepHead').width() + 40;
	$('#rightMenu').css('top', mainContentTop + "px");
	$('#rightMenu').css('left', mainContentLeft + "px");

	adjustQuickLinks();
	$('#rightMenu').css('display', 'block');
	enableComputateVisibleSections = true;
	if (onepagerMode)
		computateVisibleSections();
	else
		highlightQItem($("#qListItems").find('a:first'));
}
function pageLeaveConfirmationEnabler(){
	window.onbeforeunload = function (e) {
		  e = e || window.event;
		  
		  // For IE and Firefox prior to version 4
		  if (e) {
			  if (navigator.appName == 'Microsoft Internet Explorer') {
				  return 'Are you sure you want to navigate away from this page?';
			  }else{
				  e.returnValue = ' ';
			  }
		  }
	}
	oldAjaxCallProcessAjaxResponse = Wicket.Ajax.Call.prototype.processAjaxResponse;
	Wicket.Ajax.Call.prototype.processAjaxResponse = function (data, textStatus, jqXHR, context){
		if (jqXHR != null && jqXHR.readyState == 4){
			var tmp = jqXHR.getResponseHeader('Ajax-Location');
			if (typeof(tmp) != "undefined" && tmp != null){
				window.onbeforeunload=null;
			}
		}
		return oldAjaxCallProcessAjaxResponse.call(this, data, textStatus, jqXHR, context);
	}
}
function switchTabs(lastIndex) {
	if (isTabView) {
		$('div[data-is_tab=true]').each(function(index) {
			$(this).appendTo("#theContent");
		});

		var loader = new YAHOO.util.YUILoader({
			base : "//ajax.googleapis.com/ajax/libs/yui/2.9.0/build/",
			require : [ "tabview" ],
			onSuccess : function() {
				var myFundingTabs = new YAHOO.widget.TabView("fundingTabs");
				if (lastIndex == -1) {// if its -1 we go the the last one
					var newIndex = myFundingTabs.get('tabs').length - 1;
					myFundingTabs
							.selectTab(myFundingTabs.get('tabs').length - 1);
				} else {
					if (lastIndex >= 0) { // if its grater or equals than 0 we
											// focus on that tab
						myFundingTabs.selectTab(lastIndex);
						$('div[data-is_tab=true]').find(
								".organization_box_content").last().find(
								".collapsable").first().show();
					} else {
						// if no index is provided we focuse on the first tab
						if (myFundingTabs.get('tabs').length > 0) {
							myFundingTabs.selectTab(0);
						}
					}
				}
			}
		});
		loader.insert();
	}
}
$(document).ready(function(){
	translationsEnable();
	subSectionsSliderEnable();
	rightMenuEnable();
	pageLeaveConfirmationEnabler();
	if(isTabView){
		switchTabs();
	}
});

$(window).resize(function() {
	adjustQuickLinks();
});

$(window).scroll(function() {
	adjustQuickLinks();
});
