<?php
/**
 * @file
 * Custom filter.
 */

/**
 *
 * @see efq_views_handler_filter_property_boolean
 */
class ampapi_activity_views_handler_filter_boolean extends efq_views_handler_filter_property_boolean {
  function init(&$view, &$options) {
    $this->definition['type'] = 'yes-no';
    parent::init($view, $options);
  }

  function query() {
    $plugin = ampapi_get_mapper_by_property_name($this->options['field']);
    if (empty($plugin['report filter endpoint'])) {
      return FALSE;
    }

    $class_name = ctools_plugin_get_class($plugin, 'handler');
    if (empty($class_name) || !in_array('activityMapperOptionsInterface', class_implements($class_name))) {
      return FALSE;
    }

    /** @var activityMapperOptionsInterface $handler */
    $handler = new $class_name();
    $api_value = $handler->getMatchedValue($plugin, $this->value);

    if (isset($api_value)) {
      $this->value = $api_value;

      $this->query->query->propertyCondition($this->real_field, $this->value, "=");
    }
  }
}
