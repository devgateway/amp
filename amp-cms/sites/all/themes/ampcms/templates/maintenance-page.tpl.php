<?php

/**
 * @file
 * Default theme implementation to display a single Drupal page while offline.
 *
 * NOTES:
 *  - RDF specific variables were removed.
 */

/**
 * Avoid outputting anything before the DOCTYPE declaration. Some browsers took the specification to seriously!
 * @see http://www.w3.org/TR/2011/WD-html5-20110525/tokenization.html#before-doctype-name-state
 */
?><!DOCTYPE html>
<?php /* @see http://www.paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither */ ?>
<!--[if IE 8 ]> <html class="ie8 lt-ie9" lang="<?php print $language->language; ?>" dir="<?php print $language->dir; ?>"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!--> <html lang="<?php print $language->language; ?>" dir="<?php print $language->dir; ?>"> <!--<![endif]-->
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

  <div class="page-wrapper">
    <div class="messages-wrapper clearfix">
      <?php print $messages; ?>
    </div>

    <div id="main-content" class="main-content">
      <div class="main-header">
        <div class="main-header-inner clearfix">
          <?php if ($logo && $site_name): ?>
            <h1 class="site-name">
              <a href="<?php print $front_page; ?>" rel="home">
                <img src="<?php print $logo; ?>" alt="<?php print $site_name; ?>" title="<?php print $site_name; ?>">
              </a>
            </h1>
          <?php endif; ?>
          <?php // print theme('header_items'); ?>
        </div>
      </div>

      <div class="main-content-inner">
        <?php if ($title): ?>
          <div class="page-title-wrapper">
            <?php print render($title_prefix); ?>
            <h1 class="page-title"><?php print $title; ?></h1>
            <?php print render($title_suffix); ?>
          </div>
        <?php endif; ?>

        <?php print render($content); ?>
      </div>
    </div>

  </div>

</body>
</html>
