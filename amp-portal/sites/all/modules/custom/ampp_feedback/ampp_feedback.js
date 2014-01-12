// $Id: ampp_feedback.js,v 1.2 2012/02/08 08:56:07 anush.martirosyan Exp $

(function ($) {


/**
 * alter feedback behavior
 */
Drupal.behaviors.ampp_feedback_form = {
  attach: function (context) {
	// delete feedback default behaviors
	delete Drupal.behaviors.feedbackForm;
    delete Drupal.behaviors.feedbackFormToggle;
    delete Drupal.behaviors.feedbackFormSubmit;

    // remove unneded descriptions from Captcha
    var captcha = $('.captcha');
    $('legend', captcha).remove();
    $('.fieldset-description', captcha).remove();
    $('.form-item-captcha-response > label').remove();
    $('.form-item-captcha-response > .description').remove();
    
  }
};

})(jQuery);
