<?php

/**
 * Renders a link to the activity displayed as an "icon"
 *
 * @TODO: Move icon to module.
 */
class amp_activity_handler_field_view_activity extends views_handler_field {

  function render($values) {
    global $language;

    // Get the icon path.
    $theme_path = drupal_get_path('theme', 'ampp');
    $icon_path = base_path() . $theme_path . '/images/magnifier_icon.png';

    // Get the activity URL.
    $amp_activity_url = variable_get('amp_activity_url', '/aim/viewActivityPreview.do~public=true~pageId=2~activityId=%aid%');

    // Update Activity ID and language code tokens.
    $activity_id = $values->{$this->field_alias};
    $amp_activity_url = strtr($amp_activity_url, array('%aid%' => $activity_id));
    $amp_activity_url = strtr($amp_activity_url, array('%lang_code%' => $language->language));

    // Build the JavaScript for the popup.
    $popup_info = variable_get('amp_activity_popup_str', 'location=no,menubar=no,status=no,toolbar=no,width=800,height=600,scrollbars=yes');
    $window_open_js = "window.open('${amp_activity_url}', 'activityInfo', '${popup_info}')";

    $icon_info = array(
      'path' => $icon_path,
      'alt' => t('view'),
      'title' => t('View activity in new window.'),
      'attributes' => array(
        'class' => array(
          'view-activity-icon',
        ),
        'onclick' => $window_open_js,
      ),
    );

    return theme('image', $icon_info);
  }
}
