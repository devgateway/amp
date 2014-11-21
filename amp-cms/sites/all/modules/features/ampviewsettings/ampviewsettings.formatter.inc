<?php
/**
 * @file
 * ampviewsettings.formatter.inc
 */

/**
 * Implements hook_field_formatter_info().
 */
function ampviewsettings_field_formatter_info() {
  return array(
    'clean_text_summary_or_trimmed' => array(
      'label' => t('Clean summary or trimmed'),
      'field types' => array('text_with_summary'),
      'settings' => array(
        'trim_length' => 600,
      ),
    ),
  );
}

/**
 * Implements hook_field_formatter_settings_form().
 */
function ampviewsettings_field_formatter_settings_form($field, $instance, $view_mode, $form, &$form_state) {
  $display = $instance['display'][$view_mode];
  $settings = $display['settings'];

  $element = array();

  if ($display['type'] == 'clean_text_summary_or_trimmed') {
    $element['trim_length'] = array(
      '#title' => t('Trim length'),
      '#type' => 'textfield',
      '#size' => 10,
      '#default_value' => $settings['trim_length'],
      '#element_validate' => array('element_validate_integer_positive'),
      '#required' => TRUE,
    );
  }

  return $element;
}

/**
 * Implements hook_field_formatter_settings_summary().
 */
function ampviewsettings_field_formatter_settings_summary($field, $instance, $view_mode) {
  $display = $instance['display'][$view_mode];
  $settings = $display['settings'];

  $summary = array();

  if ($display['type'] == 'clean_text_summary_or_trimmed') {
    $summary[] = t('Trim length') . ': ' . $settings['trim_length'];
  }

  return implode('<br />', $summary);
}

/**
 * Implements hook_field_formatter_view().
 */
function ampviewsettings_field_formatter_view($entity_type, $entity, $field, $instance, $langcode, $items, $display) {
  $result = array();

  switch ($display['type']) {
    case 'clean_text_summary_or_trimmed':
      foreach ($items as $delta => $item) {
        if (!empty($item['summary'])) {
          $output = _text_sanitize($instance, $langcode, $item, 'summary');
        }
        else {
          $output = _text_sanitize($instance, $langcode, $item, 'value');
          $output = _ampviewsettings_clean_text_summary($output, $instance['settings']['text_processing'] ? $item['format'] : NULL, $display['settings']['trim_length']);
        }

        $result[$delta] = array('#markup' => $output);
      }
      break;
  }

  return $result;
}

/**
 * Generate a trimmed, formatted version of a text field value.
 *
 * @see: text_summary().
 */
function _ampviewsettings_clean_text_summary($text, $format = NULL, $size = NULL) {
  // We don't really care about the input format filter so we change it here!
  $format = 'clean_summary';

  // Check for a cached version of the cleaned version of this piece of text.
  $cache_id = $format . ':cts:' . hash('sha256', $text);
  if ($cached = cache_get($cache_id, 'cache_filter')) {
    return $cached->data;
  }

  // Make sure only some HTML tags are allowed.
  $text = check_markup($text, $format, '', TRUE);
  if (!isset($size)) {
    // What used to be called 'teaser' is now called 'summary', but
    // the variable 'teaser_length' is preserved for backwards compatibility.
    $size = variable_get('teaser_length', 600);
  }

  // Remove all empty anchor tags.
  $text = preg_replace('/\s*<a[^>]*>\W*<\/a>\s*/', ' ', $text);

  // Remove any paragraph tags that do not contain at least 5 words and all the space around them.
  $text = preg_replace('/\s*<p>(?:\W*\w*\W*){0,4}<\/p>\s*/', '', $text);

  // Remove extra whitespace between tags.
  $text = preg_replace('/>[\s\W]+</', '> <', $text);

  // Get a trimmed summary and count the characters not displayed to the user: tag attributes.
  $trimmed_text = truncate_utf8($text, $size * 2);

  // Count the number of 'irrelevant' characters.
  $extra = 0;
  if (preg_match_all('/<(\/?[a-z]+\ ?[^>]+)>/', $trimmed_text, $matches)) {
    if (!empty($matches[1])) {
      foreach ($matches[1] as $value) {
        $extra += drupal_strlen($value);
      }
    }
  }

  // The summary may not be longer than maximum length specified. Initial slice.
  $size += $extra;
  $summary = truncate_utf8($text, $size);

  // Remove tags that are not closed.
  if (preg_match('/<\/?[a-z]+[^>]+$/', $summary, $matches)) {
    $summary = preg_replace('/<\/?[a-z]+[^>]+$/', '', $summary);
  }

  // Remove paragraphs that contain only one link.
  // This cleans up linked images and files that are at the beginning of the post.
  if (preg_match('/<p>\ *<a[^>]+>[^<]+<\/a>\ *<\/p>/', $summary, $matches)) {
    $summary = preg_replace('/<p>\ *<a[^>]+>[^<]+<\/a>\ *<\/p>/', '', $summary);
  }

  // Store the actual length of the UTF8 string -- which might not be the same
  // as $size.
  $max_rpos = strlen($summary);

  // How much to cut off the end of the summary so that it doesn't end in the
  // middle of a paragraph, sentence, or word.
  // Initialize it to maximum in order to find the minimum.
  $min_rpos = $max_rpos;

  // Store the reverse of the summary. We use strpos on the reversed needle and
  // haystack for speed and convenience.
  $reversed = strrev($summary);

  // Build an array of arrays of break points grouped by preference.
  $break_points = array();

  // A paragraph near the end of sliced summary is most preferable.
  $break_points[] = array('</p>' => 0);

  // If no complete paragraph then treat line breaks as paragraphs.
  $line_breaks = array('<br />' => 6, '<br>' => 4);

  // If the first paragraph is too long, split at the end of a sentence.
  $break_points[] = array('. ' => 1, '! ' => 1, '? ' => 1, ', ' => 1, ': ' => 1, '; ' => 1);

  // Iterate over the groups of break points until a break point is found.
  foreach ($break_points as $points) {
    // Look for each break point, starting at the end of the summary.
    foreach ($points as $point => $offset) {
      // The summary is already reversed, but the break point isn't.
      $rpos = strpos($reversed, strrev($point));
      if ($rpos !== FALSE && $rpos < $size / 3) {
        // Only trim if the break point is located in the last third of the string.
        $min_rpos = min($rpos + $offset, $min_rpos);
      }
    }

    // If a break point was found in this group, slice and stop searching.
    if ($min_rpos !== $max_rpos) {
      // Don't slice with length 0. Length must be <0 to slice from RHS.
      $summary = ($min_rpos === 0) ? $summary : substr($summary, 0, 0 - $min_rpos);
      break;
    }
  }

  // Apply the HTML corrector filter to the generated summary.
  $summary = _filter_htmlcorrector(trim($summary));

  // Cache the clean summary.
  cache_set($cache_id, $summary, 'cache_filter');

  return $summary;
}
