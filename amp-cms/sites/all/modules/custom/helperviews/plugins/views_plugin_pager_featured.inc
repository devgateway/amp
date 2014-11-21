<?php

/**
 * @file
 * Definition of views_plugin_pager_featured.
 */

/**
 * The plugin to handle mini pager.
 *
 * @ingroup views_pager_plugins
 */
class views_plugin_pager_featured extends views_plugin_pager_full {
  function summary_title() {
    $summary = parent::summary_title();
    return $summary . format_plural($this->options['items_first_page'], ', @count on first page', ', @count on first page', array('@count' => $this->options['items_first_page']));
  }

  /**
   * Overrides views_plugin_pager_full::option_definition().
   */
  function option_definition() {
    $options = parent::option_definition();

    $options['items_first_page'] = array('default' => 10);

    return $options;
  }

  /**
   * Overrides views_plugin_pager_full::options_form().
   */
  function options_form(&$form, &$form_state) {
    $form['items_first_page'] = array(
      '#title' => t('Items on first page'),
      '#type' => 'textfield',
      '#description' => t('The number of items to display on the first page.'),
      '#default_value' => $this->options['items_first_page'],
      '#required' => TRUE,
    );

    parent::options_form($form, $form_state);
  }

  function query() {
    if ($this->items_per_page_exposed()) {
      // @TODO: Get value for first page.
      if (!empty($_GET['items_per_page']) && $_GET['items_per_page'] > 0) {
        $this->options['items_per_page'] = $_GET['items_per_page'];
      }
      elseif (!empty($_GET['items_per_page']) && $_GET['items_per_page'] == 'All' && $this->options['expose']['items_per_page_options_all']) {
        $this->options['items_per_page'] = 0;
      }
    }
    if ($this->offset_exposed()) {
      if (isset($_GET['offset']) && $_GET['offset'] >= 0) {
        $this->options['offset'] = $_GET['offset'];
      }
    }

    $limit = $this->options['items_per_page'];
    if ($this->current_page == 0) {
      $limit = $this->options['items_first_page'];
    }

    $offset = $this->options['offset'];
    if ($this->current_page > 0) {
      // $offset = $this->current_page * $this->options['items_per_page'] + $this->options['offset'];
      $offset = $this->options['items_first_page'] + ($this->current_page - 1) * $this->options['items_per_page'] + $this->options['offset'];
    }

    if (!empty($this->options['total_pages'])) {
      if ($this->current_page >= $this->options['total_pages']) {
        $limit = $this->options['items_per_page'];
        $offset = $this->options['total_pages'] * $this->options['items_per_page'];
      }
    }

    if (!empty($this->options['total_pages'])) {
      if ($this->current_page >= $this->options['total_pages']) {
        $offset = $this->options['items_first_page'] + (($this->options['total_pages'] - 1) * $this->options['items_per_page']);
      }
    }

    $this->view->query->set_limit($limit);
    $this->view->query->set_offset($offset);
  }

  /**
   * Get how many items per page this pager will display.
   *
   * All but the leanest pagers should probably return a value here, so
   * most pagers will not need to override this method.
   */
  function get_items_first_page() {
    return isset($this->options['items_first_page']) ? $this->options['items_first_page'] : 0;
  }

  function get_pager_total() {
    $items_per_page = intval($this->get_items_per_page());
    if (empty($items_per_page)) {
      return 1;
    }

    $diff = $items_per_page - intval($this->get_items_first_page());
    return ceil(($this->total_items + $diff) / $items_per_page);
  }
}
