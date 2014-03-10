var selectedSectors = new Array();
var selectedPrimarySectors = new Array();
var selectedSecondarySectors = new Array();
var selectedPrograms = new Array();
var selectedDonors = new Array();
var selectedAgencies = new Array();
var selectedLocations = new Array();
var selectedPeriodStart = '';
var selectedPeriodEnd = '';
var selectedKeywords = new Array();
var selectedBudget = 'all';
var selectedRegionalProjects = 0;

(function ($) {

  $(document).ready(function() {

    var lang = Drupal.settings.ampp_projects.lang;
    if (lang != '') lang = "/" + lang;

    if ( $("#edit-sector").length > 0 ) {
        $("#edit-sector").tokenInput(lang + "/ampp_search/sectors", {
          theme: "facebook",
          method: "POST",
            queryParam: "string",
            prePopulate: selectedSectors,
            onAdd: getResultCount,
            onDelete:  getResultCount
        });
    }
    if ( $("#edit-primary-sector").length > 0 ) {
        $("#edit-primary-sector").tokenInput(lang + "/ampp_search/primarysectors", {
          theme: "facebook",
          method: "POST",
            queryParam: "string",
            prePopulate: selectedPrimarySectors,
            onAdd: getResultCount,
            onDelete:  getResultCount
        });
    }
    if ( $("#edit-secondary-sector").length > 0 ) {
        $("#edit-secondary-sector").tokenInput(lang + "/ampp_search/secondarysectors", {
          theme: "facebook",
          method: "POST",
            queryParam: "string",
            prePopulate: selectedSecondarySectors,
            onAdd: getResultCount,
            onDelete:  getResultCount
        });
    }
    if ( $("#edit-program").length > 0 ) {
        $("#edit-program").tokenInput(lang + "/ampp_search/programs", {
          theme: "facebook",
          method: "POST",
            queryParam: "string",
            prePopulate: selectedPrograms,
            onAdd: getResultCount,
            onDelete:  getResultCount
        });
    }
    if ( $("#edit-donor").length > 0 ) {
        $("#edit-donor").tokenInput(lang + "/ampp_search/donors", {
          theme: "facebook",
          queryParam: "string",
            method: "POST",
            prePopulate: selectedDonors,
            onAdd: getResultCount,
            onDelete:  getResultCount
        });
      }
    if ( $("#edit-agency").length > 0 ) {
        $("#edit-agency").tokenInput(lang + "/ampp_search/agencies", {
          theme: "facebook",
          queryParam: "string",
            method: "POST",
            prePopulate: selectedAgencies,
            onAdd: getResultCount,
            onDelete:  getResultCount
        });
      }
    if ( $("#edit-region").length > 0 ) {
        $("#edit-region").tokenInput(lang + "/ampp_search/regions", {
          theme: "facebook",
          method: "POST",
            queryParam: "string",
            prePopulate: selectedLocations,
            onAdd: getResultCount,
            onDelete:  getResultCount
        });
    }

    $("#edit-period-start-datepicker-popup-0").val(selectedPeriodStart);
      $("#edit-period-start-datepicker-popup-0").bind('change', function(e){
        getResultCount();
      });

      $("#edit-period-end-datepicker-popup-0").val(selectedPeriodEnd);
      $("#edit-period-end-datepicker-popup-0").bind('change', function(e){
        getResultCount();
      });

       $('input[name=budget]').click (function () {
      getResultCount();
    });

      $('input[name=budget][value='+selectedBudget+']').attr('checked', 'checked');

      if (selectedRegionalProjects != 0) {
        $("#edit-country-level-projects").attr('checked', 'checked');
      }
      $('#edit-country-level-projects').click (function () {
      getResultCount();
    });

      $("#edit-keyword").val(selectedKeywords.join(','));
      $("#edit-keyword").bind('keyup', function(e){
        getResultCount();
      });

      $(".more-locations").click(function(){
        var activityId = $(this).attr('id');
        activityId = activityId.replace("more-","");
        activityId = activityId.replace("less-","")
        $("#hidden-"+activityId).toggle();
        $("#more-"+activityId).toggle();
        $("#less-"+activityId).toggle();
      });

      $("a.modal-selection").bind('click', function(e){
        e.preventDefault();
        //create modal dialog if it's not exists
        getModalSearchForm("modal-sector", $(this).attr('title'), $(this).attr('href'), $(this).attr('rel'));
      });

  });


  function getResultCount(){
    currenSectorsIds = ($("#edit-sector").length > 0) ? getItemsIds($("#edit-sector").tokenInput("get")) : "";

    currenPrimarySectorsIds = ($("#edit-primary-sector").length > 0) ? getItemsIds($("#edit-primary-sector").tokenInput("get")) : "";
    currenSecondarySectorsIds = ($("#edit-secondary-sector").length > 0) ? getItemsIds($("#edit-secondary-sector").tokenInput("get")) : "";
    currenProgramsIds = ($("#edit-program").length > 0) ? getItemsIds($("#edit-program").tokenInput("get")) : "";

    currentDonorsIds = ($("#edit-donor").length > 0) ? getItemsIds($("#edit-donor").tokenInput("get")) : "";
    currentAgenciesIds = ($("#edit-agency").length > 0) ? getItemsIds($("#edit-agency").tokenInput("get")) : "";
    currentRegionsIds = ($("#edit-region").length > 0) ? getItemsIds($("#edit-region").tokenInput("get")) : "";
    currentPeriodStart = ($("#edit-period-start-datepicker-popup-0").length > 0) ? $("#edit-period-start-datepicker-popup-0").val() : "";
    currentPeriodEnd = ($("#edit-period-end-datepicker-popup-0").length > 0) ? $("#edit-period-end-datepicker-popup-0").val() : "";
    currentKeywords = ($("#edit-keyword").length > 0) ? $("#edit-keyword").val() : "";

    Budget = $('input[name=budget]:checked').val();
    RegionalProjects = ($('#edit-country-level-projects').length > 0 && $('#edit-country-level-projects').is(':checked')) ? 1 : 0;
    PreviewAmount = ($('#preview_amount').length > 0) ? $('#preview_amount').val() : 0;

    $("#search_result_count").hide();
    $("#loading-count").css('display','inline');
    $.getJSON('/ampp_search/search_result_count', {'currentSectors[]' : currenSectorsIds, 'currentPrimarySectors[]' : currenPrimarySectorsIds,'currentSecondarySectors[]' : currenSecondarySectorsIds,'currentPrograms[]' : currenProgramsIds, 'currentDonors[]':currentDonorsIds, 'currentAgencies[]':currentAgenciesIds, 'currentRegions[]':currentRegionsIds,'currentPeriodStart':currentPeriodStart,'currentPeriodEnd':currentPeriodEnd,'currentKeywords':currentKeywords,'budget':Budget,'RegionalProjects':RegionalProjects,'PreviewAmount':PreviewAmount},  function(data) {
      $("#loading-count").css('display','none');
      $("#search_result_count").show();
      if(data['error'] != undefined && data['error'] !== null){
        $('#search_result_count').html(data['error']);
      }
      if(data['count'] != undefined && data['count'] !== null){
        $('#search_result_count').html(data['count']);
      }
    });
  }

  function getItemsIds(items){
    var itemsIds = new Array();
    $.each(items, function(index, item) {
      itemsIds.push(item.id);
    });
    return itemsIds;
  }

  function getModalSearchForm(dialog_id, dialog_title, load_url, token_target) {
    var mdialog = $("div#" + dialog_id);
    var iframe_id = dialog_id + '-iframe';
    var separator = '&';
    var append_query = 't=' + new Date().getTime();
    if(load_url.indexOf('?') == -1) {
      separator = '?';
    }
    load_url +=  separator + append_query;

      if($(mdialog).length == 0) {
        $('<div id="' + dialog_id + '"><div id="' + dialog_id + '-content"></div></div>').appendTo('body');
      }

      var dialog_width = 800;
      var dialog_height = 600;

      var docwidth = dialog_width; //$(document).width();
      docwidth  = docwidth * 0.9;

      var docheight = $(window).height();
      docheight = 0.9 * docheight;
      var frameheight = docheight * 0.9;
      var framewidth  = 0.9 * docwidth;


      $("div#" + dialog_id).dialog({
        modal: true,
        title: dialog_title,
        width: docwidth,
        height: docheight,
        open: function(event, ui) {
          $("#" + dialog_id + "-content").html('<iframe id="' + iframe_id + '" src="' + load_url + '" width="' + framewidth + '" height="' + (frameheight * 0.7) +'"></iframe>').height(frameheight * 0.8);
          //$("#" + iframe_id).attr('scrollHeight', frameheight);
        },
        buttons: [{
          text: "ok",
          click: function(){
            fill_mainpage_search_form(iframe_id, token_target);
            $(this).dialog('close');
          }
        }]
      });
  }

  function fill_mainpage_search_form(iframe_id, token_input_id) {
    //get child frame form
    var iframe_body_content = document.getElementById(iframe_id).contentWindow.document.body;
    var iform = $(iframe_body_content).find('form');
    var form_build_id =  $(iform).find('input[name="form_build_id"]').val();
    var form_id = $(iform).find('input[name="form_id"]').val();

    $.ajax({
      type: 'POST',
      url: Drupal.settings.ampp_projects.url,
      data: {form_build_id: form_build_id, form_id: form_id},
      success: function(result){
        if(result.data.length > 0) {
          for(var i=0; i < result.data.length; i++) {
            $("#" + token_input_id).tokenInput('add', {id: result.data[i].id, 'name':result.data[i].name});
          }
        }
      },
      dataType: 'json'
    });
  }

}) (jQuery);

