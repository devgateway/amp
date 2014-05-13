
(function ($) {

Drupal.behaviors.shorten = {
  attach: function (context) {
    // Make sure we can run context.find().
    var ctxt = $(context);
    var shortenedURL = ctxt.find('.shorten-shortened-url');
    var shorten = shortenedURL[0];
    if (shorten) {
      shorten.select();
      shorten.focus();
    }
    shortenedURL.click(function() {
      shorten.select();
      shorten.focus();
    });
  }
}

})(jQuery);
