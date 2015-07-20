<?php

/**
 * @file
 * This template handles the layout of the views exposed filter form.
 *
 * @see helpertheme_preprocess_views_exposed_form()
 */
?>
<?php if (!empty($q)): ?>
  <?php
    // This ensures that, if clean URLs are off, the 'q' is added first so that
    // it shows up first in the URL.
    print $q;
  ?>
<?php endif; ?>

<?php print $extra_widgets_toggle; ?>
<div class="views-widgets-wrapper">
  <?php if (!empty($basewidget)): ?>
  <div id="<?php print $basewidget->id; ?>-wrapper" class="views-exposed-widget views-base-widget">
    <?php if (!empty($basewidget->label)): ?>
      <label for="<?php print $basewidget->id; ?>">
        <?php print $basewidget->label; ?>
      </label>
    <?php endif; ?>
    <?php if (!empty($basewidget->description)): ?>
      <div class="description">
        <?php print $basewidget->description; ?>
      </div>
    <?php endif; ?>
    <div class="views-widget">
      <?php if (!empty($basewidget->operator)): ?>
        <div class="views-operator">
          <?php print $basewidget->operator; ?>
        </div>
      <?php endif; ?>
      <?php print $basewidget->widget; ?>
    </div>
  </div>
  <?php endif; ?>

  <div class="views-extra-widgets clearfix">
    <?php foreach ($widgets as $id => $widget): ?>
      <div id="<?php print $widget->id; ?>-wrapper" class="views-exposed-widget views-widget-<?php print $id; ?>">
        <?php if (!empty($widget->label)): ?>
          <?php // @TODO: Fix label for tokeninput widgets. ?>
          <label for="<?php print $widget->id; ?>">
            <?php print $widget->label; ?>
          </label>
        <?php endif; ?>
        <?php if (!empty($widget->description)): ?>
          <div class="description">
            <?php print $widget->description; ?>
          </div>
        <?php endif; ?>
        <div class="views-widget">
          <?php if (!empty($widget->operator)): ?>
            <div class="views-operator">
              <?php print $widget->operator; ?>
            </div>
          <?php endif; ?>
          <?php print $widget->widget; ?>
        </div>
      </div>
    <?php endforeach; ?>
    <?php if (!empty($sort_by)): ?>
      <div class="views-exposed-widget views-widget-sort-by">
        <?php print $sort_by; ?>
      </div>
      <div class="views-exposed-widget views-widget-sort-order">
        <?php print $sort_order; ?>
      </div>
    <?php endif; ?>
    <?php if (!empty($items_per_page)): ?>
      <div class="views-exposed-widget views-widget-per-page">
        <?php print $items_per_page; ?>
      </div>
    <?php endif; ?>
    <?php if (!empty($offset)): ?>
      <div class="views-exposed-widget views-widget-offset">
        <?php print $offset; ?>
      </div>
    <?php endif; ?>
  </div>

  <input type="hidden" name="language" value="<?php print $GLOBALS['language']->language ?>" />
</div>

<div class="views-actions">
  <?php print $button; ?>
</div>
