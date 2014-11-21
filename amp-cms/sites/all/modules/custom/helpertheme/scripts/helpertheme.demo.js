(function ($) {

Drupal.behaviors.variantsToggleLink = {
  attach: function(context) {
    if (!$('.palette-overview-toggle').length) {
      return;
    }

    // Add anchor links in the markup.
    $('.palette-overview-toggle').click(function () {
      $(this).closest('.helpertheme-color-palette-wrapper').toggleClass('variants-hidden');
    });
  }
};

})(jQuery);
