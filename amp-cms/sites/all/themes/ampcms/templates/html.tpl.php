<?php

/**
 * @file
 * Theme implementation to display the basic html structure of a single page.
 *
 * NOTES:
 *  - RDF specific variables were removed.
 */

/**
 * Avoid outputting anything before the DOCTYPE declaration. Some browsers took the specification seriously!
 * @see http://www.w3.org/TR/2011/WD-html5-20110525/tokenization.html#before-doctype-name-state
 */
?><!DOCTYPE html>
<?php /* @see http://www.paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither */ ?>
<!--[if IE 8 ]> <html class="no-js ie8 lt-ie9" <?php print drupal_attributes($html_attributes); ?>> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!--> <html class="no-js" <?php print drupal_attributes($html_attributes); ?>> <!--<![endif]-->
<head>
  <?php print $head; ?>
  <title><?php print $head_title; ?></title>
  <?php print $styles; ?>
  <?php print $scripts; ?>
</head>
<body class="<?php print $classes; ?>" <?php print $attributes;?>>
  <div id="skip-link">
    <a href="#main-content" class="element-invisible element-focusable"><?php print t('Skip to main content'); ?></a>
  </div>

  <?php print $page_top; ?>
  <?php print $page; ?>
  <?php print $page_bottom; ?>
</body>
</html>
