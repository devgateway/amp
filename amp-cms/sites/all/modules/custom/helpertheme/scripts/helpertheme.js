(function ($) {

Drupal.behaviors.browserWarnings = {
  attach: function(context) {
    // Add anchor links in the markup.
    $('.oldbrowser-warning .ignore-link', context).click(function (e) {
      e.preventDefault();

      // Set a ignore oldbrowser cookie.
      document.cookie = 'ignore_oldbrowser=1; path=/';

      // Remove the curent warning.
      $(this).closest('.oldbrowser-warning').remove();
    });
  }
};

})(jQuery);
