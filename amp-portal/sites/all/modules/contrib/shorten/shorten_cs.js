
(function ($) {

Drupal.behaviors.shorten_cs = {
  attach: function (context) {
    var tagItemName = '.form-item-tag';
    // Make sure we can run context.find().
    ctxt = $(context);
    if (ctxt.find('.shorten-cs-apply-js input:radio[name=type]:checked').val() == 'text') {
      ctxt.find('.shorten-cs-apply-js ' + tagItemName).hide();
    }
    ctxt.find('.shorten-cs-apply-js input:radio[name=type]').change(function() {
      if ($(this).val() == 'text') {
        ctxt.find('.shorten-cs-apply-js ' + tagItemName).hide('fast');
      }
      else {
        ctxt.find('.shorten-cs-apply-js ' + tagItemName).show('fast');
      }
    });
  }
}

})(jQuery);
