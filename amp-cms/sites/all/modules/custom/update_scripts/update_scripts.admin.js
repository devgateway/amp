/**
 * @file
 * Javascript behaviors for the "Update Scripts" module.
 */

(function($) {

Drupal.behaviors.updateScriptsFieldsetSummaries = {
  attach: function (context) {
    // Set phase summary text.
    $('#update-scripts-run-form .vertical-tabs-pane', context).drupalSetSummary(function(context) {
      // Add summary for task specific scripts tab.
      if ($(context).attr('id') == 'edit-phase-task') {
        return Drupal.t('A list of reusable setup scripts');
      }

      // Add summary for environment specific scripts tab.
      if ($(context).attr('id') == 'edit-phase-environment') {
        return Drupal.t('Environment setup scripts.');
      }

      // Add summary for environment specific scripts tab.
      if ($(context).attr('data-closed') == 'true') {
        return Drupal.t('This phase is closed.');
      }

      // Add summary for open phases.
      var t_args = {
        '@total': $('tr.script-row', context).length,
        '@successful': $('tr.script-row.status', context).length
      };
      return Drupal.t('Successful: @successful out of @total', t_args);
    });

  }
};

})(jQuery);
