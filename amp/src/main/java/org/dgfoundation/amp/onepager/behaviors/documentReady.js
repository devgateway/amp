enableComputateVisibleSections = false;
onepagerMode = ${onepagerMode};
isTabView = ${isTabView};
var isRtl = ${isRtl};

//the distance between the content body and the menu should be 40px
const DISTANCE_BETWEEN_CONTENT_AND_MENU = 40;
const INIT_RTL_DISTANCE_BETWEEN_CONTENT_AND_MENU = 68;

function getFile(){
    $("#upfile").click();
}

function setOpentip(){
    if (typeof isRtl !== 'undefined' && isRtl) {
		Opentip.styles.standard.tipJoint = "top right";
    }
    Opentip.findElements();
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
	var contentMarginTop = $('#stepHead').offset().top;
	var contentHeight = $('#stepHead').height() + $('#mainContent').height() + 55;
	var rightMenuHeight = $('#rightMenu').height();
	
	// the initial position of the right menu should be below the next menu
	if (contentMarginTop < 130) {
		contentMarginTop = 130;
	}
	
	var leftPositionOfRightMenu = 0;
	
	if ((($(window).scrollTop() + rightMenuHeight) > contentHeight)) {
		var menuTop = contentHeight + contentMarginTop - rightMenuHeight;
		
		// the initial position of the right menu should be below the next menu
		// 130px is the height of the navigation bar (header). The menu should be always located below the header
		if (menuTop < 130) {
			menuTop = 130;
		}
		
		$('#rightMenu').css('position', 'absolute');
		$('#rightMenu').css('top', menuTop + "px");
		leftPositionOfRightMenu = getLeftPositionOfRightMenu(true);
	} else {
		$('#rightMenu').css('position', 'fixed')
		$('#rightMenu').css('top', contentMarginTop + "px");
		leftPositionOfRightMenu = getLeftPositionOfRightMenu(false);
	}

	$('#rightMenu').css('left', leftPositionOfRightMenu + "px");

	if (onepagerMode) {
		computateVisibleSections();
	}
}

//Manage the position of the right menu in AF on initialization
function initQuickLinksInRtlMode(){

	var contentMarginTop = $('#stepHead').offset().top;
	var contentHeight = $('#stepHead').height() + $('#mainContent').height() + 55;
	var rightMenuHeight = $('#rightMenu').height();
	var contentWidth = $('#stepHead').width() + DISTANCE_BETWEEN_CONTENT_AND_MENU;
	var rightMenuWidth = $('#rightMenu').width();

	// the initial position of the right menu should be below the next menu
	if (contentMarginTop < 130) {
		contentMarginTop = 130;
	}

	var rightMenuLeftPosition = (window.outerWidth - $('#stepHead').outerWidth()) / 2 + INIT_RTL_DISTANCE_BETWEEN_CONTENT_AND_MENU - $('#rightMenu').width();

	if ($(window).width() < (contentWidth + rightMenuWidth + DISTANCE_BETWEEN_CONTENT_AND_MENU)) {
		rightMenuLeftPosition = $('#stepHead').offset().left - $('#rightMenu').width() - INIT_RTL_DISTANCE_BETWEEN_CONTENT_AND_MENU;
	}

	$('#rightMenu').css('position', 'fixed')
	$('#rightMenu').css('top', contentMarginTop + "px");
	$('#rightMenu').css('left', rightMenuLeftPosition + "px");
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

function getLeftPositionOfRightMenu(isAbsolutePosition) {
	var contentMarginLeft = $('#stepHead').offset().left;
	var contentWidth = $('#stepHead').width() + DISTANCE_BETWEEN_CONTENT_AND_MENU;
	var rightMenuWidth = $('#rightMenu').width();
	var currentScrollLeft = $(window).scrollLeft();

	if (isAbsolutePosition) {
		return getAbsoluteLeftPositionOfRightMenu(contentMarginLeft, contentWidth, rightMenuWidth, currentScrollLeft);
	}

	return getFixedLeftPositionOfRightMenu(contentMarginLeft, contentWidth, rightMenuWidth, currentScrollLeft);
}

function getFixedLeftPositionOfRightMenu(contentMarginLeft, contentWidth, rightMenuWidth, currentScrollLeft) {

	if (isRtl) {
		return contentMarginLeft - rightMenuWidth - DISTANCE_BETWEEN_CONTENT_AND_MENU - currentScrollLeft;
	}

	return contentMarginLeft + contentWidth - currentScrollLeft;
}

function getAbsoluteLeftPositionOfRightMenu(contentMarginLeft, contentWidth, rightMenuWidth, currentScrollLeft) {

	if (isRtl) {
		return contentMarginLeft - rightMenuWidth - DISTANCE_BETWEEN_CONTENT_AND_MENU;
	}

	if ($(window).width() < (contentWidth + rightMenuWidth)) {
		return contentWidth;
	} else {
		return contentMarginLeft + contentWidth - currentScrollLeft;
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

  setOpentip();
	
	// change the min-height of the main content div when the height of the right menu is greater than DEFAULT_MAIN_BODY_MIN_HEIGHT
	// the 20px value is used to make the distance between the lower point of the menu and the footer to be minimal
	$("#mainBodyContent").css("min-height", function(){ 
		// this is the defaul min height. Taken from AmpHeaderFooter.html
		var DEFAULT_MAIN_BODY_MIN_HEIGHT = 440;
		
	    return $('#rightMenu').height() > DEFAULT_MAIN_BODY_MIN_HEIGHT ? ($('#rightMenu').height() - 20) : DEFAULT_MAIN_BODY_MIN_HEIGHT;
	});

	// in RTL Mode the initial position of menu should be calculated in a different way
	if (isRtl) {
		initQuickLinksInRtlMode();
	} else {
		adjustQuickLinks();
	}
});

$(window).resize(function() {
	adjustQuickLinks();
});

$(window).scroll(function() {
	adjustQuickLinks();
});
