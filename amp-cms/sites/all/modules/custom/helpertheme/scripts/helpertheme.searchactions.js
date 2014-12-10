(function ($) {

Drupal.behaviors.searchActions = {
  attach: function(context, settings) {
    $('.activity-search-actions .action-export', context).click(function(e) {
      e.preventDefault();
      var extra_container = $(this).siblings('.activity-search-actions-extra');
      settings.searchActions = settings.searchActions || {};

      if (!settings.searchActions.response || settings.searchActions.waiting) {
        var message = $('<div class="ajax-progress">' + Drupal.t('Please wait...') + ' <div class="throbber"></div></div>');
        extra_container.html(message);
      }

      if (!settings.searchActions.response && !settings.searchActions.waiting) {
        settings.searchActions.waiting = true;

        // Add the current applied query parameters.
        var request_url = settings.basePath + 'activities/export';

        if (window.location.search) {
          request_url += window.location.search;
        }

        // The export view does not know about table sort but it can use a parent display.
        if (window.location.search) {
          request_url += '&attach=search_page';
        }
        else {
          request_url += '?attach=search_page';
        }

        $.ajax({
          url: request_url,
          cache: false,
          dataType: 'json',
          success: function (response) {
            settings.searchActions.waiting = false;
            settings.searchActions.response = response;

            if (response.markup) {
              var response_markup = $('<div class="action"></div>').html(response.markup);
              extra_container.html(response_markup);
            }
          },
          error: function (xmlhttp) {
            var string = Drupal.ajaxError(xmlhttp, settings.basePath + 'activities/export');
            var error = $('<div class="messages error"></div>').html(string);
            extra_container.html(error);
          }
        });

      }

      if (settings.searchActions.response) {
        var response_markup = $('<div class="action"></div>').html(settings.searchActions.response.markup);
        extra_container.html(response_markup);
      }
    });

    $('.activity-search-actions .action-permalink', context).click(function(e) {
      e.preventDefault();
      var extra_container = $(this).siblings('.activity-search-actions-extra');

      // Create a new text input containing the current URL.
      var share_url = document.location.toString();
      var message = $('<input class="form-text share-panel-url" name="share_url" size="40" value="' + share_url + '" title="' + Drupal.t('Share link') + '">');
      extra_container.html(message);

      extra_container.find('.form-text').focus().select();
    });
  }
};

})(jQuery);
