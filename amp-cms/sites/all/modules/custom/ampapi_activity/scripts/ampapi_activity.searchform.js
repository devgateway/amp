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
      var endpoint = settings.basePath + 'ampapi/autocomplete/' + field_name;

      $(this).tokenInput(endpoint, {
        theme: 'ampcms',
        // theme: 'facebook',
        method: 'get',
        minChars: '2',
        queryParam: 'string',
        prePopulate: settings.viewsTokeninput[field_name]['values'],

        // Update the search form info when the values have changed.
        // onAdd: function() {
        //   Drupal.ampp.updateSearchFormInfo(Drupal.ampp.delay);
        // },
        // onDelete: function() {
        //   Drupal.ampp.updateSearchFormInfo(Drupal.ampp.delay);
        // },
      });
    });
  }
};

})(jQuery);
