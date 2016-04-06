/**
 * @file
 * owlcarousel.views.js
 *
 * Override or extend upon default views JavaScript functionality.
 */

(function($) {

  if (typeof Drupal.views != 'undefined') {
    /**
     * Modified attach ajax behavior to a singe link.
     */
    Drupal.views.ajaxView.prototype.attachPagerLinkAjax = function(id, link) {
      var $link = $(link);

      // @todo, add check for remaining rows.
      if ($link.hasClass('next')) {
        var viewData = {};
        var href = $link.attr('href');

        $.extend(viewData, this.settings, Drupal.Views.parseQueryString(href), Drupal.Views.parseViewArgs(href, this.settings.view_base_path));
        $.extend(viewData, Drupal.Views.parseViewArgs(href, this.settings.view_base_path));
        this.element_settings.submit = viewData;

        var owl = $(this.element_settings.selector).find('.owl-carousel');
        var view = owl.parent().parent();

        if (owl.length) {
          this.element_settings.url = Drupal.settings.basePath + 'owlcarousel/views/ajax';
          this.element_settings.success = onSuccess;
        }

        this.pagerAjax = new Drupal.ajax(false, $link, this.element_settings);
      }

      /**
       * Append new slide.
       */
      function onSuccess(content) {
        var page = owl.data('owlCarousel').currentItem;

        owl.data('owlCarousel').addItem(content);
        owl.data('owlCarousel').jumpTo(page);
        view.find('.ajax-progress-throbber').remove();

        if (isNaN(this.element_settings.submit.page)) {
          this.element_settings.submit.page = 1;
        }
        this.element_settings.submit.page++;
      }
    };
  }

}(jQuery));
