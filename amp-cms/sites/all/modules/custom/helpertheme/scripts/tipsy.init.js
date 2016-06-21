(function ($) {

  Drupal.behaviors.tipsyInit = {
    attach: function(context, settings) {

      // Add anchor links in the markup.
      $('.self-tipsy').tipsy({
        title: function() {
          return $(this).text();
        },
        delayIn: 500
      });
    }

  };
})(jQuery);
