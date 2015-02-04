<?php

/**
 * Field handler for entity links.
 */
class ampapi_activity_views_handler_field_uri extends entity_views_handler_field_uri {
  function option_definition() {
    $options = parent::option_definition();

    $options['display_as_link_text'] = array('default' => '');

    return $options;
  }

  /**
   * Provide link to the page being visited.
   */
  function options_form(&$form, &$form_state) {
    parent::options_form($form, $form_state);

    $form['display_as_link']['#weight'] = 1.01;
    $form['display_as_link_text'] = array(
      '#title' => t('Link text'),
      '#description' => t('If empty the URL will be used as text.'),
      '#type' => 'textfield',
      '#default_value' => !empty($this->options['display_as_link_text']),
      '#dependency' => array(
        'edit-options-display-as-link' => array('1'),
      ),
      '#weight' => 1.02,
    );
  }

  function render($values) {
    $value = $this->get_value($values);

    if (!empty($this->options['display_as_link'])) {
      $this->options['alter']['make_link'] = TRUE;
      $this->options['alter']['absolute'] = TRUE;
      $this->options['alter']['link_attributes'] = array('target' => '_blank');
      $this->options['alter']['path'] = $value;
      $text = !empty($this->options['display_as_link_text']) ? t($this->sanitize_value($this->options['display_as_link_text'])) : $this->sanitize_value($value, 'url');

      $svgicon = helpertheme_get_svg_icons('reports', array('width' => '2em', 'height' => '2em'));
      return $svgicon . '<span class="title">' . $text . '</span>';
    }
    else {
      return $this->sanitize_value($value, 'url');
    }
  }
}
