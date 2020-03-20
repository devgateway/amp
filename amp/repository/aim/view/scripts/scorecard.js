// Step 1 - Scorecard Settings

 $('#Step1').show();
 
 var excludedDonors = {};

function unselectAllActivities() {
	$('select[name=selectedCategoryValues]').find(":selected").prop('selected', false);
}
function isValidScorecardSetting() {
	  return validate();
}

function validate(){
	if( $("input[name=validationPeriod]").is(":checked") && isEmpty($('select[name="validationTime"]').val())){
		alert(alertValidationTime);
		return false;
	}
	if( $("input[name=validationPeriod]").is(":checked") === false && isEmpty($('select[name="validationTime"]').val()) === false){
		alert(alertValidationPeriod);
		return false;
	}
	
	if (!validatePercentage()) {
		return false;
	}
	
	return true;
}

function handleValidationPeriodClick(chkbox){
	if(!chkbox.checked){
		$('select[name="validationTime"]').val("");
	}
}

function handleValidationTimeChange(select) {
	if (isEmpty($(select).val())) {
		$("input[name=validationPeriod]").prop('checked', false);
	} else {
		$("input[name=validationPeriod]").prop('checked', true);
	}
}

function validatePercentage() {
	var percentage = $('input[name="percentageThreshold"]').val();
	var floatValue = parseFloat(percentage);
	
	if (isNaN(floatValue) || (floatValue <= 0) || (floatValue > 100)) {
		alert(alertValidationPercentage);
		return false;
	}
	
	return true;
}

function saveScorecardSetting() {
    var categoryValues = [];
    $('select[name=selectedCategoryValues] :selected').each(function() {
    	categoryValues.push({ "id" : $(this).val()});
    });
    
    var jsonSettings = {
    		validationPeriod: $('input[name="validationPeriod"]').prop('checked'), 
    		percentageThreshold: parseFloat($('input[name="percentageThreshold"]').val()), 
    		validationTime: $('select[name="validationTime"]').val(), 
    		categoryValues: categoryValues
    };
    
    var jsonString = JSON.stringify(jsonSettings);
	$.ajax({
		  url: "/rest/scorecard/manager/settings",
		  method: "POST",
		  context: document.body,
		  dataType: "json",
		  contentType: "application/json",
		  data : jsonString
	}).error(function(data) {
	    showErrors(data);
    });
}

// Step 2 - Donors Tree filter

$.ajax({
	  url: "/rest/scorecard/manager/donors",
	  method: "GET",
	  context: document.body,
}).done(function(data) {
	buildDonorsTree(data.children);
}).error(function(data) {
    showErrors(data);
});

function buildDonorsTree(treeData) {
	$(function(){
		$("#donorsTree").fancytree({
			//  extensions: ["select"],
			checkbox: true,
			selectMode: 3,
			source: treeData,
		  	loadChildren: function(event, ctx) {
		  		ctx.node.fixSelection3AfterClick();
		  	},
		  	select: function(event, data) {
		  		// Get a list of all selected nodes, and convert to a key array:
				var selKeys = $.map(data.tree.getSelectedNodes(), function(node) {
					if (!node.isFolder()) {
						var title = node.title;
						var key = node.key;
						excludedDonors[key] = title;
						return node.key;
					}
				});
				$("#echoSelection3").text(selKeys.join(","));
		  	},
		  	dblclick: function(event, data) {
		  		data.node.toggleSelected();
		  	},
		  	keydown: function(event, data) {
		  		if(event.which === 32 ) {
		  			data.node.toggleSelected();
		  			return false;
		  		}
		  	},
		});
	});
}

// Step 3 - No Updates Donors

function loadFilteredDonors() {
	
	var jsonDonors = { "donorIds" : JSON.parse("[" + $("#echoSelection3").text() + "]")};
	
	$.ajax({
		  url: "/rest/scorecard/manager/donors/filtered",
		  method: "POST",
		  context: document.body,
		  contentType: "application/json",
		  data : JSON.stringify(jsonDonors),
		  dataType: "json"
	}).success(function(data) {
	    cleanUpErrors();
		reloadDonorsNoUpdates(data);
	}).error(function(data) {
	    showErrors(data);
    });
	
}

function cleanUpErrors() {
    $("#scorecardErrors").empty();
}

function showErrors(data) {
    if (data && data.responseText) {
    	cleanUpErrors();
        try {
            $("#scorecardErrors").html(JSON.stringify(data.responseJSON.error));
        } catch (e) {
        }
        $("#btnNext").hide();
    }
}

function reloadDonorsNoUpdates(data) {
	
	$('#allDonors').empty();
	$('#noUpdateDonors').empty();
	
	$.each(data.allFilteredDonors,function(index, value){
	    $('#allDonors').append('<option value="' + value.id + '">' + value.name + '</option>');
	});  
	
	$.each(data.noUpdatesFilteredDonors,function(index, value){
	    $('#noUpdateDonors').append('<option value="' + value.id + '">' + value.name + '</option>');
	});  
	
	sortSelectList($('#allDonors').first());
	sortSelectList($('#noUpdateDonors').first());
}

function saveDonorsNoUpdates() {
	
  	var noUpdArr = [];
  	$('#noUpdateDonors option').each(function(i, selectedElement) {
  		noUpdArr.push($(this).val());
  	});
  	
  	var jsonDonors = {donorsNoUpdates : noUpdArr};
  	
	$.ajax({
		  url: "/rest/scorecard/manager/donors/noupdates",
		  method: "POST",
		  context: document.body,
		  contentType: "application/json",
		  data : JSON.stringify(jsonDonors),
		  dataType: "json"
	}).error(function(data) {
	    cleanUpErrors();
	    showErrors(data);
    });;
}


function addDonors() {
	moveItems($('#allDonors'), $('#noUpdateDonors'));
}

function removeDonors() {
	moveItems($('#noUpdateDonors'), $('#allDonors'));
}

function moveItems(origin, dest)
{
	origin.find(':selected').removeAttr("selected").appendTo(dest);
    sortSelectList(dest.first());
}

function sortSelectList(selList)
{
	var options = selList.find('option');
	options.sort(function(a, b) {
		return a.label.toLowerCase().localeCompare(b.label.toLowerCase());
	});

	$(selList).empty().append(options);
}


// Step 4 - Review and Settings save

function loadFinalReview() {
	if ($('input[name="validationPeriod"]').prop('checked')) {
		 $('#validationYes').show();
		 $('#validationNo').hide();
	} else {
		 $('#validationYes').hide();
		 $('#validationNo').show();
	}
   
    $('#percentageThreshold').text($('input[name="percentageThreshold"]').val());
    if ($('select[name="validationTime"]').val() != 0) {
    	$('#validationTime').text($('select[name="validationTime"]').find(":selected").text());
    	$("#validationNone").hide();
    } else {
    	$('#validationTime').text('');
    	$('#validationNone').show();
    }
    
    var summaryAcitivityStatus = $('#summaryAcitivityStatus');
    var activityStautus = $('select[name="selectedCategoryValues"]');
    // Copy rows from selectSource to selectDestination from bottom to top
    if (activityStautus.find(":selected").length > 0) {
    	summaryAcitivityStatus.empty();
    	var selActivities = activityStautus.find(':selected').map(function() {return $(this).text();}).get();
    	summaryAcitivityStatus.text(selActivities.join(", "));
    	$('#validationActivityStatusesNone').hide();
    } else {
    	$('#validationActivityStatusesNone').show();
    }
    
    var selectExcludedSummary = $('#summaryExcludedDonors').first();
  
	
    if (Object.keys(excludedDonors).length > 0) {
    	selectExcludedSummary.empty();
    	$.each(excludedDonors, function(id, label) {
    		selectExcludedSummary.append($("<option></option>")
    		         .attr("option" + id, label)
    		         .text(label));
    		
    	});
    	selectExcludedSummary.show();
    } else {
    	selectExcludedSummary.hide();
    }
    
    var selectSummary = $('#summaryNoUpdateDonors').first();
    var noUpdateDonors = $('#noUpdateDonors').first();
    // Copy rows from selectSource to selectDestination from bottom to top
    if (noUpdateDonors.find('option').length > 0) {
    	selectSummary.empty();
    	noUpdateDonors.find('option').clone().appendTo(selectSummary);
	    selectSummary.show();
    } else {
    	selectSummary.hide();
    }
}

function exportScorecard () {
	window.location =  "/rest/scorecard/export";
}

//This function handles style and display changes for each next button click
function handleWizardNext() {
	var btnNext = $('#btnNext');
	var btnPrev = $('#btnPrev');
	var btnSubmit = $('#btnSubmit');

	// init button next
	$("#btnNext").show();
	
    if (btnNext.attr('name')=='Step2' && isValidScorecardSetting()) {
    	saveScorecardSetting();
        // Change the button name - we use this to keep track of which step to display on a click
        btnNext.attr('name', 'Step3');
        btnPrev.attr('name', 'Step1');
        btnPrev.show();
        
        $('#Step1').hide();
        $('#Step2').show();
    } else if (btnNext.attr('name')=='Step3') {
    	loadFilteredDonors()
        btnNext.attr('name', 'Step4');
        btnPrev.attr('name', 'Step2');
        btnPrev.show();
        
        $('#Step2').hide();
        $('#Step3').show();
    } else if (btnNext.attr('name')=='Step4') {
    	saveDonorsNoUpdates();
    	
    	btnNext.attr('name', '');
    	btnPrev.attr('name', 'Step3');
    	btnNext.hide();
    	btnSubmit.show();
    	
    	$('#Step3').hide();
    	$('#Step4').show();

        // Load table elements for final review step
        loadFinalReview();
    }
}

// This function handles style and display changes for each previous button click
function handleWizardPrevious() {
	var btnNext = $('#btnNext');
	var btnPrev = $('#btnPrev');
	var btnSubmit = $('#btnSubmit');

    // init button next
    $("#btnNext").show();
	
    if (btnPrev.attr('name')=='Step1') {
        // Change the button name - we use this to keep track of which step to display on a click
        btnNext.attr('name','Step2');
        btnPrev.attr('name','');

        // Disable/enable buttons when reach reach start and review steps
        btnPrev.hide();

        $('#Step2').hide();
        $('#Step1').show();
    } else if (btnPrev.attr('name')=='Step2') {
        btnNext.attr('name','Step3');
        btnPrev.attr('name','Step1');

        $('#Step3').hide();
        $('#Step2').show();
    } else if (btnPrev.attr('name')=='Step3') {
        btnNext.attr('name','Step4');
        btnPrev.attr('name','Step2');

        btnNext.show();
        btnSubmit.hide();

        $('#Step4').hide();
        $('#Step3').show();
    }
    cleanUpErrors();
}

function cleanupValidationTime() {
	// This is needed in case of an old issue when the table had validation_period = false and validation_time > 0
	if($("input[name=validationPeriod]").is(":checked") !== true) {
		$('select[name="validationTime"]').val("");
	}
}
