<?php

/**
 * Views field handler for rendering node links that point to panelizer tabs.
 */
class panelizer_handler_field_node_link extends views_handler_field_node_link {
  /**
   * Mapping of node paths (the part after node/N) to human-readable labels.
   *
   * This array is used both for the options_form in the select dropdown, and
   * also when rendering the link to define the link text if not overridden.
   */
  protected $tab_map = array();

  function construct() {
    parent::construct();
    $this->tab_map = array(
      'panelizer' => t('Settings'),
      'panelizer/context' => t('Context'),
      'panelizer/layout' => t('Layout'),
      'panelizer/content' => t('Content'),
    );
  }

  function option_definition() {
    $options = parent::option_definition();
    $options['panelizer_tab'] = array('default' => 'panelizer');
    return $options;
  }


  function options_form(&$form, &$form_state) {
    parent::options_form($form, $form_state);
    $form['panelizer_tab'] = array(
      '#type' => 'select',
      '#title' => 'Panelizer tab to link to',
      '#options' => $this->tab_map,
      '#default_value' => $this->options['panelizer_tab'],
    );
  }

  function render_link($data, $values) {
    $this->options['alter']['make_link'] = TRUE;
    $tab_path = $this->options['panelizer_tab'];
    $path = 'node/' . $data . '/' . $tab_path;
    $this->options['alter']['path'] = $path;
    $this->options['alter']['query'] = drupal_get_destination();
    $text = !empty($this->options['text']) ? $this->options['text'] : $this->tab_map[$tab_path];
    return $text;
  }
}
