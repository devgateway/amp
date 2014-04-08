(function ($) {

Drupal.ampp = Drupal.ampp || {};

// This is used to reduce the number of requests made to the server.
Drupal.ampp.delay = 300;

/**
 * Extracts relevant values from the projects search form.
 */
Drupal.ampp.getSearchFormData = function() {
  var form_values = {}

  // Get the search form input values.
  var search_form = $('#ampp-search-form');

  // Retreive the form data.
  var form_data = search_form.serializeArray();

  // Clean the form data.
  form_data.forEach(function(entry) {
    if (entry.value == '') {
      return;
    }

    switch (entry.name) {
      case 'form_build_id':
      case 'form_token':
      case 'form_id':
        // Do not include form information in the request.
        break;

      case 'period_start[date]':
      case 'period_end[date]':
        var name = entry.name.replace('[date]', '');
        form_values[name] = entry.value
        break;

      default:
        form_values[entry.name] = entry.value
        break;
    }
  });

  return form_values;
}

/**
 * Updates the projects_search_form info.
 */
Drupal.ampp.updateSearchFormInfo = function(timeout) {
  var settings = Drupal.settings;

  // Clear all requests that were not executed.
  if (Drupal.ampp.timer) {
    clearTimeout(Drupal.ampp.timer);
  }

  // Get the search form input values.
  var search_form = $('#ampp-search-form');

  if (!$('.form-actions', search_form).hasClass('calculating')) {
    $('.form-actions', search_form).addClass('calculating');
  }

  ajax_settings = {
    url: settings.projectsSearchForm.forminfo_endpoint,
    cache: false, // @TODO: Remove!
    type: 'GET',
    dataType: 'json', // Prevent auto-evaluation of response.
    global: false, // Do not trigger global AJAX events.
    success: function(response) {
      if (response['message']) {
        $('.search-form-info').html(response['message']);
      }
    },
    complete: function () {
      $('.form-actions', search_form).removeClass('calculating');
    },
    error: function (response) {
      // @TODO: handle errors.
      // console.log(response, 'error');
    },
  };

  // The form data needs to be extracted right before the request is made.
  if (timeout) {
    Drupal.ampp.timer = setTimeout(function() {
      ajax_settings.data = Drupal.ampp.getSearchFormData();
      $.ajax(ajax_settings);
    }, timeout);
  }
  else {
    ajax_settings.data = Drupal.ampp.getSearchFormData();
    $.ajax(ajax_settings);
  }
}

/**
 * Displays a popup for filters
 *
 * @TODO: Cleanup and fix the iframe size!!!
 */
Drupal.ampp.getModalSearchForm = function(dialog_title, iframe_url, target_id) {
  var dialog_id = 'dialog-for-' + target_id;
  var iframe_id = 'iframe-for-' + dialog_id;

  // @TODO: Find a different way toinvalidate browser side caching.
  var separator = '&';
  if (iframe_url.indexOf('?') == -1) {
    separator = '?';
  }
  var append_query = 't=' + new Date().getTime();
  iframe_url +=  separator + append_query;

  if ($('#' + dialog_id).length == 0) {
    $('<div id="' + dialog_id + '"></div>')
      .html('<div id="' + dialog_id + '-content"></div>')
      .appendTo('body');
  }

  var docwidth  = 0.9 * 800;
  var docheight = 0.9 * $(window).height();

  var framewidth  = 0.7 * docwidth;
  var frameheight = 0.7 * docheight;

  $('#' + dialog_id).dialog({
    modal: true,
    title: dialog_title,
    width: docwidth,
    height: docheight,
    open: function(event, ui) {
      var iframe_markup = '<iframe id="' + iframe_id + '" src="' + iframe_url + '" width="' + framewidth + '" height="' + frameheight +'"></iframe>';
      $('#' + dialog_id + '-content').html(iframe_markup);
    },
    buttons: [{
      text: 'ok',
      click: function(){
        Drupal.ampp.updateSearchFormFilters(iframe_id, target_id);
        $(this).dialog('close');
      }
    }]
  });
}

/**
 * Updates the search form filters
 *
 * @TODO: Cleanup!!!
 */
Drupal.ampp.updateSearchFormFilters = function(iframe_id, target_id) {
  var settings = Drupal.settings;

  // Get child frame form.
  var iframe_body_content = document.getElementById(iframe_id).contentWindow.document.body;
  var popup_form = $(iframe_body_content).find('form');
  var form_build_id =  $(popup_form).find('input[name="form_build_id"]').val();
  var form_id = $(popup_form).find('input[name="form_id"]').val();

  $.ajax({
    url: settings.projectsSearchForm.modalform_values_endpoint,
    data: {
      form_build_id: form_build_id,
      form_id: form_id,
    },
    type: 'POST',
    dataType: 'json',
    success: function(result){
      if (result.data.length > 0) {
        for (var i=0; i < result.data.length; i++) {
          $('#' + target_id).tokenInput('add', {
            'id': result.data[i].id,
            'name': result.data[i].name
          });
        }
      }
    },
  });
}

/**
 * Setup the projects search form behavior.
 */
Drupal.behaviors.projectsSearchFormInit = {
  attach: function (context, settings) {
    var search_form = $('#ampp-search-form', context);

    if (search_form.length === 0) {
      return;
    }

    var elements_selector = '.form-checkbox, .form-radio, .form-select, .form-type-date-popup .form-text';
    $(elements_selector, search_form).change(function() {
      // Update the search form info when the values have changed.
      Drupal.ampp.updateSearchFormInfo(Drupal.ampp.delay);
    });

    $('.form-item-keywords .form-text', search_form).bind('keyup', function() {
      // Update the search form info when the values have changed.
      Drupal.ampp.updateSearchFormInfo(Drupal.ampp.delay);
    });

    // Get the autocomplete endpoint.
    var autocomplete_endpoint = settings.projectsSearchForm.autocomplete_endpoint;

    // Setup autocomplete fields.
    for (field_name in settings.projectsSearchForm.autocomplete) {
      if (!settings.projectsSearchForm.autocomplete.hasOwnProperty(field_name)) {
        continue;
      }

      var $input_field = $('input[name="' + field_name + '"]', search_form);

      if ($input_field.length > 0) {
        $input_field.tokenInput(autocomplete_endpoint + '/' + field_name, {
          theme: 'facebook',
          method: 'POST',
          queryParam: 'string',
          prePopulate: settings.projectsSearchForm.autocomplete[field_name],

          // Update the search form info when the values have changed.
          onAdd: function() {
            Drupal.ampp.updateSearchFormInfo(Drupal.ampp.delay);
          },
          onDelete: function() {
            Drupal.ampp.updateSearchFormInfo(Drupal.ampp.delay);
          },
        });
      }
    }

    // @TODO: Move to the projects_search_result plugin.
    $('.more-locations').click(function(){
      var activityId = $(this).attr('id');
      activityId = activityId.replace('more-', '');
      activityId = activityId.replace('less-', '')
      $('#hidden-' + activityId).toggle();
      $('#more-' + activityId).toggle();
      $('#less-' + activityId).toggle();
    });

    // Update the search form info.
    Drupal.ampp.updateSearchFormInfo(Drupal.ampp.delay);
  }
};

/**
 * Setup the jQuery Token Input autocomplete behavior.
 *
 * @TODO: Cleanup!
 */
Drupal.behaviors.projectsSearchFormModalDialog = {
  attach: function (context, settings) {
    if ($('#ampp-search-form', context).length === 0) {
      return;
    }

    // Get the search form input values.
    var search_form = $('#ampp-search-form', context);

    $('a.modal-selection', search_form).bind('click', function(e){
      e.preventDefault();

      // Create modal dialog.
      var dialog_title = $(this).attr('title');
      var iframe_url = $(this).attr('href');
      var target_id = $(this).attr('rel');
      Drupal.ampp.getModalSearchForm(dialog_title, iframe_url, target_id);
    });
  }
};

})(jQuery);
