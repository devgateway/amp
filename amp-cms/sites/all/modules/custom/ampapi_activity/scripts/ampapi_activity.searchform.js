(function ($) {

/**
 * Setup the jQuery Token Input autocomplete behavior.
 *
 * @TODO: Cleanup!
 */
Drupal.behaviors.projectsSearchFormModalDialog = {
  attach: function (context, settings) {
    if ($('.views-exposed-form-activities-search-page', context).length === 0) {
      return;
    }

    $('.views-exposed-form-activities-search-page .tokeninput-textfield', context).each(function() {
      var field_name = $(this).attr('data-name');
      // @HACK: Add language code to requests, to prevent browser caching.
      var lang_code = settings.viewsTokeninput['language'];
      var endpoint = settings.basePath + 'ampapi/autocomplete/' + field_name + '?language=' + lang_code;

      $(this).tokenInput(endpoint, {
        theme: 'ampcms',
        method: 'get',
        queryParam: 'string',
        prePopulate: settings.viewsTokeninput[field_name]['values'],
        hintText: Drupal.t('Type in a search term'),
        noResultsText: Drupal.t('No results'),
        searchingText: Drupal.t('Searching...'),

        // Update the search form info when the values have changed.
        onAdd: function (item) {
          $(this).closest('.views-accordion-widget-wrapper').addClass('has-values');
        },
        onDelete: function(item) {
          var saved_tokens = this.tokenInput('get');
          if (saved_tokens.length == 0) {
            $(this).closest('.views-accordion-widget-wrapper').removeClass('has-values');
          }
        },
      });
    });
  }
};

})(jQuery);
