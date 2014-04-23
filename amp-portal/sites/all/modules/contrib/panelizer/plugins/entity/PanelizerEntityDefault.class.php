<?php
/**
 * @file
 * Base class for the Panelizer Entity plugin.
 */

/**
 * Interface to describe how PanelizerEntity plugin objects are implemented.
 */
interface PanelizerEntityInterface {
  /**
   * Initialize the plugin object.
   */
  public function init($plugin);

  // Public Drupal hooks
  public function hook_menu(&$items);
  public function hook_menu_alter(&$items);
  public function hook_form_alter(&$form, &$form_state, $form_id);
  public function hook_permission(&$items);
  public function hook_admin_paths(&$items);

  // Entity specific Drupal hooks
  public function hook_entity_load(&$entities);
  public function hook_entity_insert($entity);
  public function hook_entity_update($entity);
  public function hook_entity_delete($entity);
  public function hook_field_attach_delete_revision($entity);

  /**
   * Add entity specific form to the Panelizer settings form.
   *
   * This is primarily to allow bundle selection per entity type.
   */
  public function settings_form(&$form, &$form_state);

  /**
   * Validate entity specific settings on the Panelizer settings form.
   */
  public function settings_form_validate(&$form, &$form_state);

  /**
   * Submit entity specific settings on the Panelizer settings form.
   */
  public function settings_form_submit(&$form, &$form_state);

  /**
   * Load the named default panel for the bundle.
   */
  public function get_default_panelizer_object($bundle, $name);

  /**
   * Determine if a bundle is panelized
   */
  public function is_panelized($bundle);

  /**
   * Determine if a bundle has a defalt panel
   */
  public function has_default_panel($bundle);

  /**
   * Determine if a bundle is allowed choices.
   */
  public function has_panel_choice($bundle);

  /**
   * Get a default display for a newly panelized entity.
   *
   * This is meant to give administrators a starting point when panelizing
   * new entities.
   */
  function get_default_display();

  /**
   * Get a panelizer object for the key.
   *
   * This must be implemented for each entity type, as the default object
   * implements a special case for handling panelizer defaults.
   */
  function get_panelizer_object($key);

  /**
   * Render a panelized entity.
   */
  function render_entity($entity, $args = array());

  /**
   * Fetch an object array of CTools contexts from panelizer information.
   */
  public function get_contexts($panelizer, $entity = NULL);

  /**
   * Callback to get the base context for a panelized entity
   */
  public function get_base_contexts($entity = NULL);

  /**
   * Determine if the current user has $op access on the $entity.
   */
  public function entity_access($op, $entity);

  /**
   * Implement the save function for the entity.
   */
  public function entity_save($entity);

  /**
   * Determine if an entity allows revisions and whether or not the current
   * user has access to control that.
   *
   * @param $entity
   *   The entity in question.
   * @return
   *   An array. The first parameter is a boolean as to whether or not the
   *   entity supports revisions and the second parameter is whether or not
   *   the user can control whether or not a revision is created.
   */
  public function entity_allows_revisions($entity);

  /**
   * Get the visible identifier of the identity.
   *
   * This is overridable because it can be a bit awkward using the
   * default label.
   *
   * @return
   *   A translated, safe string.
   */
  public function entity_identifier($entity);

  /**
   * Get the name of bundles on the entity.
   *
   * Entity API doesn't give us a way to determine this, so the class must
   * do this.
   *
   * @return
   *   A translated, safe string.
   */
  public function entity_bundle_label();

}

/**
 * Base class for the Panelizer Entity plugin.
 */
abstract class PanelizerEntityDefault implements PanelizerEntityInterface {
  /**
   * True if the entity supports revisions.
   */
  public $supports_revisions = FALSE;

  /**
   * The plugin metadata.
   */
  public $plugin = NULL;

  /**
   * The entity type the plugin is for. This is from the $plugin array.
   */
  public $entity_type = '';

  /**
   * Initialize the plugin and store the plugin info.
   */
  function init($plugin) {
    $this->plugin = $plugin;
    $this->entity_type = $plugin['name'];
  }

  /**
   * Implements a delegated hook_permission.
   */
  public function hook_permission(&$items) {
    $entity_info = entity_get_info($this->entity_type);
    // Make a permission for each bundle we control.
    foreach ($this->plugin['bundles'] as $bundle => $settings) {
      if (empty($settings['status'])) {
        continue;
      }

      $items["administer panelizer $this->entity_type $bundle content"] = array(
        'title' => t('%entity_name %bundle_name: Administer Panelizer content', array(
          '%entity_name' => $entity_info['label'],
          '%bundle_name' => $entity_info['bundles'][$bundle]['label'],
        )),
      );
      $items["administer panelizer $this->entity_type $bundle context"] = array(
        'title' => t('%entity_name %bundle_name: Administer Panelizer context', array(
          '%entity_name' => $entity_info['label'],
          '%bundle_name' => $entity_info['bundles'][$bundle]['label'],
        )),
      );
      $items["administer panelizer $this->entity_type $bundle layout"] = array(
        'title' => t('%entity_name %bundle_name: Administer Panelizer layout', array(
          '%entity_name' => $entity_info['label'],
          '%bundle_name' => $entity_info['bundles'][$bundle]['label'],
        )),
      );
      $items["administer panelizer $this->entity_type $bundle settings"] = array(
        'title' => t('%entity_name %bundle_name: Administer Panelizer settings', array(
          '%entity_name' => $entity_info['label'],
          '%bundle_name' => $entity_info['bundles'][$bundle]['label'],
        )),
      );
      if (!empty($settings['choice'])) {
        $items["administer panelizer $this->entity_type $bundle choice"] = array(
          'title' => t('%entity_name %bundle_name: Choose panels', array(
            '%entity_name' => $entity_info['label'],
            '%bundle_name' => $entity_info['bundles'][$bundle]['label'],
          )),
          'description' => t('Allows the user to choose which default panel the entity uses.'),
        );
      }
    }
  }

  /**
   * Implements a delegated hook_menu.
   */
  public function hook_menu(&$items) {
    if (!empty($this->plugin['entity path'])) {
      // Figure out where in the path the entity will be.
      $bits = explode('/', $this->plugin['entity path']);
      foreach ($bits as $count => $bit) {
        if (strpos($bit, '%') === 0) {
          $position = $count;
          break;
        }
      }

      if (!isset($position)) {
        return;
      }

      $total = count($bits);

      // @todo delegate the following
      // Configure entity editing pages
      $base = array(
        'access callback' => 'panelizer_entity_plugin_callback_switcher',
        'access arguments' => array($this->entity_type, 'access', 'admin', $position, 'settings'),
        'type' => MENU_LOCAL_TASK,
      );

      $items[$this->plugin['entity path'] . '/panelizer'] = array(
        'title' => 'Panelizer',
        // make sure this is accessible to panelize entities with no defaults.
        'page callback' => 'panelizer_entity_plugin_switcher_page',
        'page arguments' => array($this->entity_type, 'settings', $position),
        'weight' => 11,
        'context' => MENU_CONTEXT_PAGE | MENU_CONTEXT_INLINE,
      ) + $base;

      $items[$this->plugin['entity path'] . '/panelizer/settings'] = array(
        'title' => 'Settings',
        'page callback' => 'panelizer_entity_plugin_switcher_page',
        'page arguments' => array($this->entity_type, 'settings', $position),
        'type' => MENU_DEFAULT_LOCAL_TASK,
        'weight' => 11,
      ) + $base;

      $items[$this->plugin['entity path'] . '/panelizer/context'] = array(
        'title' => 'Context',
        'page callback' => 'panelizer_entity_plugin_switcher_page',
        'page arguments' => array($this->entity_type, 'context', $position),
        'access arguments' => array($this->entity_type, 'access', 'admin', $position, 'context'),
        'weight' => 12,
      ) + $base;

      $items[$this->plugin['entity path'] . '/panelizer/layout'] = array(
        'title' => 'Layout',
        'page callback' => 'panelizer_entity_plugin_switcher_page',
        'page arguments' => array($this->entity_type, 'layout', $position),
        'access arguments' => array($this->entity_type, 'access', 'admin', $position, 'layout'),
        'weight' => 13,
      ) + $base;

      $items[$this->plugin['entity path'] . '/panelizer/content'] = array(
        'title' => 'Content',
        'page callback' => 'panelizer_entity_plugin_switcher_page',
        'page arguments' => array($this->entity_type, 'content', $position),
        'access arguments' => array($this->entity_type, 'access', 'admin', $position, 'content'),
        'weight' => 14,
      ) + $base;
    }
  }

  /**
   * Implements a delegated hook_menu.
   */
  public function hook_admin_paths(&$items) {
    if (!empty($this->plugin['entity path'])) {
      $bits = explode('/', $this->plugin['entity path']);
      foreach ($bits as $count => $bit) {
        if (strpos($bit, '%') === 0) {
          $bits[$count] = '*';
        }
      }

      $path = implode('/', $bits);
      $items[$path . '/panelizer*'] = TRUE;
    }
  }

  public function hook_menu_alter(&$items) {

  }

  public function hook_form_alter(&$form, &$form_state, $form_id) {

  }

  // Entity specific Drupal hooks
  public function hook_entity_load(&$entities) {
    ctools_include('export');
    $ids = array();
    $bundles = array();

    foreach ($entities as $entity) {
      list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);
      if ($this->is_panelized($bundle)) {
        $ids[] = $this->supports_revisions ? $revision_id : $entity_id;
        $bundles[$entity_id] = $bundle;
      }
    }

    if (!$ids) {
      return;
    }

    // Load all the panelizers associated with the list of entities.
    if ($this->supports_revisions) {
      $panelizers = db_query("SELECT * FROM {panelizer_entity} WHERE entity_type = '$this->entity_type' AND revision_id IN (:ids)", array(':ids' => $ids))->fetchAllAssoc('entity_id');
    }
    else {
      $panelizers = db_query("SELECT * FROM {panelizer_entity} WHERE entity_type = '$this->entity_type' AND entity_id IN (:ids)", array(':ids' => $ids))->fetchAllAssoc('entity_id');
    }
    $defaults = array();
    $dids = array();
    // Go through our entity list and generate a list of defaults and displays
    foreach ($entities as $entity_id => $entity) {
      // Don't bother if somehow we've already loaded and are asked to
      // load again.
      if (!empty($entity->panelizer)) {
        continue;
      }
      // Skip not panelized bundles.
      if (empty($bundles[$entity_id])) {
        continue;
      }

      // If no panelizer was loaded, queue up defaults to load.
      if (empty($panelizers[$entity_id])) {
        if ($this->has_default_panel($bundle)) {
          $defaults[] = implode(':', array($this->entity_type, $bundle, 'default'));
        }
      }
      else {
        $entity->panelizer = ctools_export_unpack_object('panelizer_entity', $panelizers[$entity_id]);
        // Panelizers that do not have dids are just a selection of defaults
        // that has never actually been modified.
        if (empty($entity->panelizer->did)) {
          $defaults[] = $entity->panelizer->name;
        }
        else {
          $dids[$entity->panelizer->did] = $entity->panelizer->did;
        }
      }
    }

    // Load any defaults we collected.
    if ($defaults) {
      $panelizer_defaults = $this->load_default_panelizer_objects($defaults);
    }

    // if any panelizers were loaded, get their attached displays.
    if ($dids) {
      $displays = panels_load_displays($dids);
    }

    // Now, go back through our entities and assign dids and defaults
    // accordingly.
    foreach ($entities as $entity_id => $entity) {
      // Skip not panelized bundles.
      if (empty($bundles[$entity_id])) {
        continue;
      }
      if (empty($entity->panelizer)) {
        $default_key = implode(':', array($this->entity_type, $bundles[$entity_id], 'default'));
        if (!empty($panelizer_defaults[$default_key])) {
          $entity->panelizer = clone $panelizer_defaults[$default_key];
          // make sure this entity can't write to the default display.
          $entity->panelizer->did = NULL;
        }
      }
      else if (empty($entity->panelizer->display)) {
        if (!empty($entity->panelizer->did)) {
          if (empty($displays[$entity->panelizer->did])) {
            // Somehow the display for this entity has gotten lost?
            $entity->panelizer->did = NULL;
            $entity->panelizer->display = $this->get_default_display();
          }
          else {
            $entity->panelizer->display = $displays[$entity->panelizer->did];
          }
        }
        else {
          if (!empty($panelizer_defaults[$entity->panelizer->name])) {
            $entity->panelizer->display = $panelizer_defaults[$entity->panelizer->name]->display;
          }
        }
      }
    }
  }

  public function hook_entity_insert($entity) {
    list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);
    if (!$this->is_panelized($bundle)) {
      return;
    }

    // If there's no panelizer information on the entity then there is nothing to do.
    if (empty($entity->panelizer)) {
      return;
    }

    // On entity insert, we only write the display if it is not a default. That
    // probably means it came from an export or deploy or something along
    // those lines.
    if (empty($entity->panelizer->name) && !empty($entity->panelizer->display)) {
      $panelizer = $this->clone_panelizer($entity->panelizer, $entity);
      // First write the display
      panels_save_display($panelizer->display);

      // Make sure we have the new did.
      $panelizer->did = $panelizer->display->did;

      // And write the new record.
      return drupal_write_record('panelizer_entity', $panelizer);
    }
    else {
      // We write the panelizer record to record which name is being used.
      // And ensure the did is NULL:
      $entity->panelizer->did = NULL;
      $entity->panelizer->entity_type = $this->entity_type;
      $entity->panelizer->entity_id = $entity_id;
      // The (int) ensures that entities that do not support revisions work
      // since the revision_id cannot be NULL.
      $entity->panelizer->revision_id = (int) $revision_id;
      return drupal_write_record('panelizer_entity', $entity->panelizer);
    }
  }

  public function hook_entity_update($entity) {
    list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);
    if (!$this->is_panelized($bundle)) {
      return;
    }

    // If there's no panelizer information on the entity then there is nothing to do.
    if (empty($entity->panelizer)) {
      return;
    }

    if ($this->supports_revisions) {
      if (empty($entity->panelizer->revision_id) || $entity->panelizer->revision_id != $revision_id) {
        $update = array();
      }
      else {
        $update = array('entity_type', 'revision_id');
      }
    }
    else {
      if (empty($entity->panelizer->entity_id)) {
        $update = array();
      }
      else {
        $update = array('entity_type', 'entity_id');
      }
    }

    // The editor will set this flag if the display is modified. This lets
    // us know if we need to clone a new display or not.
    // NOTE: This means that when exporting or deploying, we need to be sure
    // to set the display_is_modified flag to ensure this gets written.
    if (!empty($entity->panelizer->display_is_modified)) {
      // If this is a new entry or the entry is using a display from a default,
      // clone the display.
      if (!$update || empty($entity->panelizer->did)) {
        $panelizer = $this->clone_panelizer($entity->panelizer, $entity);
      }
      else {
        $panelizer = $entity->panelizer;
      }

      // First write the display
      panels_save_display($panelizer->display);

      // Make sure we have the did.
      $panelizer->did = $panelizer->display->did;

      // Ensure that we always write this as NULL when we have our own panel:
      $panelizer->name = NULL;

      // And write the new record.
      return drupal_write_record('panelizer_entity', $panelizer, $update);
    }
    else {
      $entity->panelizer->entity_type = $this->entity_type;
      $entity->panelizer->entity_id = $entity_id;
      // The (int) ensures that entities that do not support revisions work
      // since the revision_id cannot be NULL.
      $entity->panelizer->revision_id = (int) $revision_id;
      drupal_write_record('panelizer_entity', $entity->panelizer, $update);
    }
  }

  public function hook_entity_delete($entity) {
    $this->delete_entity_panelizer($entity);
  }

  public function hook_field_attach_delete_revision($entity) {
    list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);

    // Locate and delete all displays associated with the entity.
    $revisions = db_query("SELECT revision_id, did FROM {panelizer_entity} WHERE entity_type = '$this->entity_type' AND entity_id = :id", array(':id' => $entity_id))->fetchAllAssoc('revision_id');

    // It is possible to have the same did on multiple revisions, if none of
    // those revisions modified the display. Be careful NOT to delete a display
    // that might be in use by another revision.
    $seen = array();
    foreach ($revisions as $info) {
      if ($info->revision_id != $revision_id) {
        $seen[$info->did] = TRUE;
      }
    }

    if (!empty($revisions[$revision_id]->did) && empty($seen[$revisions[$revision_id]->did])) {
      panels_delete_display($revisions[$revision_id]->did);
    }

    db_delete('panelizer_entity')
      ->condition('entity_type', $this->entity_type)
      ->condition('entity_id', $entity_id)
      ->condition('revision_id', $revision_id)
      ->execute();
  }

  public function hook_field_attach_form($entity, &$form, &$form_state, $langcode) {
    list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);

    // Do not give choices to non panelized nodes, or nodes that already have
    // their won panel.
    if (!$this->has_panel_choice($bundle) || !empty($entity->panelizer->did)) {
      return;
    }
    $form_state['panelizer has choice'] = TRUE;
    $form['panelizer'] = array(
      '#type' => 'fieldset',
      '#access' => $this->panelizer_access('choice', $entity),
      '#title' => t('Panelizer'),
      '#collapsible' => TRUE,
      '#collapsed' => TRUE,
      '#group' => 'additional_settings',
      '#attributes' => array(
        'class' => array('panelizer-entity-options'),
      ),
      '#attached' => array(
        'js' => array(ctools_attach_js('panelizer-vertical-tabs', 'panelizer')),
      ),
      '#weight' => -10,
      '#tree' => TRUE,
      // Put these here because submit does not get a real entity with
      // the actual *(&)ing panelizer.
      '#revision_id' => isset($entity->panelizer->revision_id) ? $entity->panelizer->revision_id : NULL,
      '#entity_id' => isset($entity->panelizer->entity_id) ? $entity->panelizer->entity_id : NULL,
    );

    $panelizers = $this->get_default_panelizer_objects($bundle);
    $options = array();
    foreach ($panelizers as $name => $panelizer) {
      if (empty($panelizer->disabled)) {
        $options[$name] = $panelizer->title ? $panelizer->title : t('Default');
      }
    }

    if (!empty($entity->panelizer->name)) {
      $name = $entity->panelizer->name;
    }
    else {
      if ($this->has_default_panel($bundle)) {
        $name = implode(':', array($this->entity_type, $bundle, 'default'));
      }
      else {
        $name = '';
      }
    }

    if (!$this->has_default_panel($bundle)) {
      $options = array('' => t('-- No panel --')) + $options;
    }

    $form['panelizer']['name'] = array(
      '#title' => t('Panel'),
      '#type' => 'select',
      '#options' => $options,
      '#default_value' => $name,
    );
  }

  public function hook_field_attach_submit($entity, &$form, &$form_state) {
    list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);
    if (!empty($form_state['panelizer has choice'])) {
      // Guarantee we overwrite any previous settings or non-settings.
      $entity->panelizer = $this->get_default_panelizer_object($bundle, $form_state['values']['panelizer']['name']);
      $entity->panelizer->did = NULL;

      // Ensure original values are maintained:
      $entity->panelizer->entity_id = $form['panelizer']['#entity_id'];
      $entity->panelizer->revision_id = $form['panelizer']['#revision_id'];
    }
  }

  /**
   * Implement the save function for the entity.
   */
  public function entity_allows_revisions($entity) {
    $retval = array();

    list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);

    $node_options = variable_get('node_options_' . $bundle, array('status', 'promote'));
    $retval[0] = in_array('revision', $node_options);
    $retval[1] = user_access('administer nodes');
  }

  /**
   * Create a new, scrubbed version of a panelizer object.
   */
  public function clone_panelizer($panelizer, $entity) {
    list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);
    $panelizer_clone = clone $panelizer;

    // In order to ensure we don't actually use and modify the default display,
    // we export and re-import it.
    $code = panels_export_display($panelizer->display);
    ob_start();
    eval($code);
    ob_end_clean();

    $panelizer_clone->display = $display;
    $panelizer_clone->did = NULL;
    $panelizer_clone->name = NULL;
    $panelizer_clone->entity_type = $this->entity_type;
    $panelizer_clone->entity_id = $entity_id;
    // The (int) ensures that entities that do not support revisions work
    // since the revision_id cannot be NULL.
    $panelizer_clone->revision_id = (int) $revision_id;

    return $panelizer_clone;
  }

  function access_admin($entity, $op = NULL) {
    list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);
    if (!$this->is_panelized($bundle)) {
      return FALSE;
    }

    // If there is an $op, this must actually be panelized in order to pass.
    // If there is no op, then the settings page can provide us a "panelize it!"
    // page even if there is no panel.
    if ($op && $op != 'settings' && empty($entity->panelizer)) {
      return FALSE;
    }

    return $this->panelizer_access($op, $entity) && $this->entity_access('update', $entity);
  }

  /**
   * Determine if the user has access to the panelizer operation for this type.
   */
  function panelizer_access($op, $entity) {
    list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);

    return user_access('administer panelizer') || user_access("administer panelizer $this->entity_type $bundle $op");
  }

  /**
   * Switched page callback to give the settings form.
   */
  function page_settings($js, $input, $entity) {
    list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);

    if (empty($entity->panelizer)) {
      // If this entity is not yet panelized, and there is no default panel
      // do to configuration, give them the option of panelizing it.
      if ($this->has_default_panel($bundle)) {
        return MENU_NOT_FOUND;
      }

      // Set the form to the Panelize It! form.
      $form_id = 'panelizer_panelize_entity_form';

      // Fetch a special default panelizer that is only accessible with the
      // default_anyway flag.
      $panelizer = $this->get_internal_default_panelizer($bundle);
      $panelizer->name = NULL;
    }
    else {
      $form_id = 'panelizer_settings_form';
      $reset_button = TRUE;
      $panelizer = $entity->panelizer;
    }

    $form_state = array(
      'entity' => $entity,
      'revision info' => $this->entity_allows_revisions($entity),
      'panelizer' => $panelizer,
      'no_redirect' => TRUE,
    );

    if (!empty($reset_button)) {
      $form_state['reset button'] = TRUE;
    }

    ctools_include('common', 'panelizer');
    $output = drupal_build_form($form_id, $form_state);
    if (!empty($form_state['executed'])) {
      if (empty($form_state['clicked_button']['#reset'])) {
        drupal_set_message(t('The settings have been updated.'));
        $entity->panelizer = $form_state['panelizer'];
        // Make sure that entity_save knows that the panelizer settings
        // are modified and must be made local to the entity.
        if (empty($panelizer->did) || !empty($panelizer->name)) {
          $panelizer->display_is_modified = TRUE;
        }
        $this->entity_save($entity);
      }
      else {
        $this->delete_entity_panelizer($entity);
      }

      drupal_goto($_GET['q']);
    }

    return $output;
  }

  function page_context($js, $input, $entity) {
    list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);

    $cache_key = $entity_id;
    $panelizer = panelizer_context_cache_get($this->entity_type, $cache_key);

    if (empty($panelizer)) {
      return MENU_NOT_FOUND;
    }

    $form_state = array(
      'panelizer' => &$panelizer,
      'entity' => $entity,
      'revision info' => $this->entity_allows_revisions($entity),
      'panelizer type' => $this->entity_type,
      'cache key' => $cache_key,
      'no_redirect' => TRUE,
    );

    ctools_include('common', 'panelizer');
    $output = drupal_build_form('panelizer_default_context_form', $form_state);
    if (!empty($form_state['executed'])) {
      if (!empty($form_state['clicked_button']['#write'])) {
        drupal_set_message(t('The settings have been updated.'));
        $entity->panelizer = $form_state['panelizer'];
        $this->entity_save($entity);
      }
      else {
        drupal_set_message(t('Changes have been discarded.'));
      }

      panelizer_context_cache_clear($this->entity_type, $cache_key);
      drupal_goto($_GET['q']);
    }

    return $output;

  }

  function page_layout($js, $input, $entity, $step = NULL, $layout = NULL) {
    $panelizer = $entity->panelizer;
    if (empty($panelizer)) {
      return MENU_NOT_FOUND;
    }

    list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);

    $display = $panelizer->display;
    $display->context = $this->get_contexts($panelizer, $entity);

    $bits = explode('/', $this->plugin['entity path']);
    foreach ($bits as $count => $bit) {
      if (strpos($bit, '%') === 0) {
        $bits[$count] = $entity_id;
      }
    }

    $bits[] = 'panelizer';
    $finish_path = $bits;

    $bits[] = 'layout';
    $bits[] = '%step';

    $form_state = array(
      'entity' => $entity,
      'revision info' => $this->entity_allows_revisions($entity),
      'display' => $display,
      'wizard path' => implode('/', $bits),
      'allowed_layouts' => 'panelizer_' . $this->entity_type . ':' . $bundle,
    );

    ctools_include('common', 'panelizer');
    $output = panelizer_change_layout_wizard($form_state, $step, $layout);
    if (!empty($form_state['complete'])) {
      $entity->panelizer->display = $form_state['display'];
      $entity->panelizer->display_is_modified = TRUE;
      $this->entity_save($entity);
      drupal_set_message(t('The layout has been changed.'));
      drupal_goto(implode('/', $finish_path) . '/content');
    }

    return $output;
  }

  function page_content($js, $input, $entity) {
    $panelizer = $entity->panelizer;
    if (empty($panelizer)) {
      return MENU_NOT_FOUND;
    }

    list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);

    $form_state = array(
      'entity' => $entity,
      'revision info' => $this->entity_allows_revisions($entity),
      'display cache' => panels_edit_cache_get(implode(':', array('panelizer', $this->entity_type, $entity_id))),
      'no_redirect' => TRUE,
    );

    ctools_include('common', 'panelizer');
    $output = drupal_build_form('panelizer_edit_content_form', $form_state);
    if (!empty($form_state['executed'])) {
      if (!empty($form_state['clicked_button']['#save-display'])) {
        drupal_set_message(t('The settings have been updated.'));
        $entity->panelizer->display = $form_state['display'];
        $entity->panelizer->display_is_modified = TRUE;
        $this->entity_save($entity);
      }
      else {
        drupal_set_message(t('Changes have been discarded.'));
      }

      panels_edit_cache_clear($form_state['display cache']);
      drupal_goto($_GET['q']);
    }

    ctools_set_no_blocks(FALSE);
    drupal_set_page_content($output);
    $page = element_info('page');
    return $page;
  }

  function delete_entity_panelizer($entity) {
    list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);

    // Locate and delete all displays associated with the entity.
    $dids = db_query("SELECT did FROM {panelizer_entity} WHERE entity_type = '$this->entity_type' AND entity_id = :id", array(':id' => $entity_id))->fetchCol();

    foreach (array_unique($dids) as $did) {
      panels_delete_display($did);
    }

    db_delete('panelizer_entity')
      ->condition('entity_type', $this->entity_type)
      ->condition('entity_id', $entity_id)
      ->execute();
  }

  /**
   * Determine if a bundle is panelized.
   */
  public function is_panelized($bundle) {
    return !empty($this->plugin['bundles'][$bundle]) && !empty($this->plugin['bundles'][$bundle]['status']);
  }

  /**
   * Determine if a bundle has a default panel.
   */
  public function has_default_panel($bundle) {
    return $this->is_panelized($bundle) && !empty($this->plugin['bundles'][$bundle]['default']);
  }

  /**
   * Determine if a bundle is allowed choices.
   */
  public function has_panel_choice($bundle) {
    return !empty($this->plugin['bundles'][$bundle]['choice']);
  }

  /**
   * Get the default panels, keyed by names.
   */
  public function load_default_panelizer_objects($names) {
    ctools_include('export');
    $panelizers = ctools_export_load_object('panelizer_defaults', 'names', $names);
    return $panelizers;
  }

  /**
   * Get the default panelizers for the given bundle.
   */
  public function get_default_panelizer_objects($bundle) {
    $conditions = array(
      'panelizer_type' => $this->entity_type,
      'panelizer_key' => $bundle,
    );

    ctools_include('export');
    return ctools_export_load_object('panelizer_defaults', 'conditions', $conditions);
  }

  /**
   * Implements a delegated hook_panelizer_defaults().
   *
   * This makes sure that all panelized entities configured to have a
   * default actually have one.
   */
  public function hook_panelizer_defaults(&$panelizers) {
    // For features integration, if they have modified a default and put
    // it into the database, we do not want to show one as a default.
    // Otherwise, features can't latch onto it.
    $default_names = &drupal_static('panelizer_defaults_in_database', NULL);
    if (!isset($default_names)) {
      $default_names = drupal_map_assoc(db_query("SELECT name FROM {panelizer_defaults} WHERE name LIKE '%:default'")->fetchCol());
    }

    foreach ($this->plugin['bundles'] as $bundle => $info) {
      if (!empty($info['status']) && !empty($info['default'])) {
        $panelizer = $this->get_internal_default_panelizer($bundle);
        if (empty($default_names[$panelizer->name])) {
          $panelizers[$panelizer->name] = $panelizer;
        }
      }
    }
  }

  /**
   * An internal representation of a panelizer object, used to seed when
   * we have none and want something to get started.
   */
  public function get_internal_default_panelizer($bundle) {
    $load_name = implode(':', array($this->entity_type, $bundle, 'default'));
    $panelizer = ctools_export_crud_new('panelizer_defaults');
    $panelizer->panelizer_type = $this->entity_type;
    $panelizer->panelizer_key = $bundle;
    $panelizer->name = $load_name;
    $panelizer->display = $this->get_default_display();
    $panelizer->api_version = 1;
    $panelizer->title = t('Default');
    return $panelizer;
  }

  /**
   * Load the named default panel for the bundle.
   */
  public function get_default_panelizer_object($bundle, $name) {
    // If the name is in the format of entitytype:bundle:name which is the machine
    // name used, split that out automatically.
    if (strpos($name, ':') === FALSE) {
      $name = implode(':', array($this->entity_type, $bundle, $name));
    }

    ctools_include('export');
    return ctools_export_crud_load('panelizer_defaults', $name);
  }

  /**
   * Provide a default display for newly panelized entities.
   *
   * This should be implemented by the entity plugin.
   */
  function get_default_display() {
    // This is a straight up empty display.
    $display = panels_new_display();
    $display->layout = 'flexible';
    return $display;
  }

  /**
   * Get a panelizer object for the key.
   *
   * This must be implemented for each entity type.
   */
  function get_panelizer_object($entity_id) {
  }

  /**
   * Add entity specific form to the Panelizer settings form.
   *
   * This is primarily to allow bundle selection per entity type.
   */
  public function settings_form(&$form, &$form_state) {
    // Add entity settings
    // @todo change theme function name
    $form['entities'][$this->entity_type] = array(
      '#theme' => 'panelizer_settings_page_table',
      '#header' => array(
        array('data' => $this->entity_bundle_label(), 'width' => '15%'),
        t('Panelize'),
        t('Provide default panel'),
        t('Allow panel choice'),
        array('data' => t('Operations'), 'width' => '50%'),
      ),
      '#columns' => array('title', 'status', 'default', 'choice', 'links'),
    );

    $entity_info = entity_get_info($this->entity_type);
    $bundles = $entity_info['bundles'];
    drupal_alter('panelizer_default_types', $bundles, $this->entity_type);

    foreach ($bundles as $bundle => $bundle_info) {
      $base_id = str_replace(array('][', '_', ' '), '-', '#edit-entities-' . $this->entity_type . '-' . $bundle);
      $base_url = 'admin/config/content/panelizer/' . $this->entity_type . '/' . $bundle;

      $settings = !empty($this->plugin['bundles'][$bundle]) ? $this->plugin['bundles'][$bundle] : array('status' => FALSE, 'default' => FALSE, 'choice' => FALSE);

      $form['entities'][$this->entity_type][$bundle]['title'] = array(
        '#markup' => $bundle_info['label'],
      );

      $form['entities'][$this->entity_type][$bundle]['status'] = array(
        '#type' => 'checkbox',
        '#default_value' => !empty($settings['status']),
      );

      $form['entities'][$this->entity_type][$bundle]['default'] = array(
        '#type' => 'checkbox',
        '#default_value' => !empty($settings['default']),
        '#states' => array(
          'visible' => array(
            $base_id . '-status' => array('checked' => TRUE),
          ),
        ),
      );

      $form['entities'][$this->entity_type][$bundle]['choice'] = array(
        '#type' => 'checkbox',
        '#default_value' => !empty($settings['choice']),
        '#states' => array(
          'visible' => array(
            $base_id . '-status' => array('checked' => TRUE),
          ),
        ),
      );

      $form['entities'][$this->entity_type][$bundle]['links'] = array(
        '#prefix' => '<div class="container-inline">',
        '#suffix' => '</div>',
      );

      // Panelize is on all content types get this option
      if (!empty($settings['status'])) {
        $links_array = array(
          'settings' => array(
            'title' => t('allowed content'),
            'href' => $base_url . '/allowed',
          ),
        );
         $links = theme('links', array(
          'links' => $links_array,
          'attributes' => array('class' => 'links inline'),
        ));
      }
      else {
        $links = t('Save to access allowed content');
      }

      $form['entities'][$this->entity_type][$bundle]['links']['basic'] = array(
        '#type' => 'item',
        '#title' => $links,
        '#states' => array(
          'visible' => array(
            $base_id . '-status' => array('checked' => TRUE),
          ),
        ),
      );

      // Panelize is enabled and a default panel will be provided
      if (!empty($settings['status']) && !empty($settings['default']) && empty($settings['choice'])) {
        $links_array = array(
          'settings' => array(
            'title' => t('settings'),
            'href' => $base_url . '/settings',
          ),
          'context' => array(
            'title' => t('context'),
            'href' => $base_url . '/context',
          ),
          'layout' => array(
            'title' => t('layout'),
            'href' => $base_url . '/layout',
          ),
          'content' => array(
            'title' => t('content'),
            'href' => $base_url . '/content',
          ),
        );

        $links = theme('links', array(
          'links' => $links_array,
          'attributes' => array('class' => 'links inline'),
        ));
      }
      else {
        $links = t('Save to access default panel');
      }

      $form['entities'][$this->entity_type][$bundle]['links']['default'] = array(
        '#type' => 'item',
        '#title' => $links,
        '#states' => array(
          'visible' => array(
            $base_id . '-status' => array('checked' => TRUE),
            $base_id . '-default' => array('checked' => TRUE),
            $base_id . '-choice' => array('checked' => FALSE),
          ),
        ),
      );

      if (!empty($settings['status']) && !empty($settings['choice'])) {
        $links_array = array(
          'list' => array(
            'title' => t('list'),
            'href' => $base_url . '/list',
          ),
        );

        $links = theme('links', array(
          'links' => $links_array,
          'attributes' => array('class' => 'links inline'),
        ));
      }
      else {
        $links = t('Save to access panel list');
      }

      $form['entities'][$this->entity_type][$bundle]['links']['default2'] = array(
        '#type' => 'item',
        '#title' => $links,
        '#states' => array(
          'visible' => array(
            $base_id . '-status' => array('checked' => TRUE),
            $base_id . '-choice' => array('checked' => TRUE),
          ),
        ),
      );
    }
  }

  /**
   * Validate entity specific settings on the Panelizer settings form.
   */
  public function settings_form_validate(&$form, &$form_state) {

  }

  /**
   * Submit entity specific settings on the Panelizer settings form.
   */
  public function settings_form_submit(&$form, &$form_state) {
    if (empty($form_state['values']['entities'][$this->entity_type])) {
      return;
    }

    foreach ($form_state['values']['entities'][$this->entity_type] as $bundle => $values) {
      variable_set('panelizer_defaults_' . $this->entity_type . '_' . $bundle, $values);
    }

    // @todo if we enable caching of the plugins, which we should, this
    // needs to clear that cache so they get reloaded.
  }

  /**
   * Render the panels display for a given panelizer entity.
   *
   * @param stdClass $entity
   *   A fully-loaded entity object controlled by panelizer.
   * @param array $args
   *   Optional array of arguments to pass to the panels display.
   * @param string $address
   *   An optional address to send to the renderer to use for addressable
   *   content.
   *
   * @return array
   *   If the entity isn't panelized, this returns NULL. Otherwise, it returns an
   *   associative array as meant for use with CTools with the following keys:
   *   - 'content': String containing the rendered panels display output.
   *   - 'no_blocks': Boolean defining if the panels display wants to hide core
   *      blocks or not when being rendered.
   */
  function render_entity($entity, $args = array(), $address = NULL) {
    if (empty($entity->panelizer) || empty($entity->panelizer->display)) {
      return FALSE;
    }
    list($entity_id, $revision_id, $bundle) = entity_extract_ids($this->entity_type, $entity);

    $panelizer = $entity->panelizer;
    $display = $entity->panelizer->display;

    $display->context = $this->get_contexts($panelizer, $entity);
    $display->args = $args;
    $display->css_id = $panelizer->css_id;

    // This means the IPE will use our cache which means it will get appropriate
    // allowed content should it be selected.
    $display->cache_key = implode(':', array('panelizer', $this->entity_type, $entity_id));

    // Check to see if there is any CSS.
    if (!empty($panelizer->css)) {
      ctools_include('css');
      $filename = ctools_css_retrieve($display->cache_key);
      if (!$filename) {
        $filename = ctools_css_store($display->cache_key, $panelizer->css);
      }
      drupal_add_css($filename, array('group' => CSS_THEME));
    }

    // We think this is handled as a page, so set the current page display.
    panels_get_current_page_display($display);

    ctools_include('plugins', 'panels');
    $renderer = panels_get_renderer($panelizer->pipeline, $display);
    $renderer->address = $address;

    $info = array(
      'content' => panels_render_display($display, $renderer),
      'no_blocks' => !empty($panelizer->no_blocks),
    );

    return $info;
  }

  /**
   * Fetch an object array of CTools contexts from panelizer information.
   */
  public function get_contexts($panelizer, $entity = NULL) {
    ctools_include('context');
    if (empty($panelizer->base_contexts)) {
      $panelizer->base_contexts = $this->get_base_contexts($entity);
    }

    $contexts = ctools_context_load_contexts($panelizer);
    return $contexts;
  }

  /**
   * Callback to get the base context for a panelized entity
   */
  public function get_base_contexts($entity = NULL) {
    ctools_include('context');
    if ($entity) {
      $context = ctools_context_create('entity:' . $this->entity_type, $entity);
    }
    else {
      $context = ctools_context_create_empty('entity:' . $this->entity_type);
      // The placeholder is needed to create the form used for the live
      // preview.
      $context->placeholder = array(
        'type' => 'context',
        'conf' => array(
          'name' => $this->entity_type,
          'identifier' => $this->entity_identifier($entity),
          'keyword' => $this->entity_type,
          'context_settings' => array(),
        ),
      );
    }

    $context->identifier = $this->entity_identifier($entity);
    $context->keyword = $this->entity_type;
    return array('panelizer' => $context);
  }

  /**
   * Get the visible identifier if the identity.
   *
   * This is overridable because it can be a bit awkward using the
   * default label.
   */
  public function entity_identifier($entity) {
    $entity_info = entity_get_info($this->entity_type);
    return t('This @entity', array('@entity' => $entity_info['label']));
  }

  // Admin screens use a title callback for admin pages. This is used
  // to fill in that title.
  public function get_bundle_title($bundle) {
    $entity_info = entity_get_info($this->entity_type);

    return isset($entity_info['bundles'][$bundle]['label']) ? $entity_info['bundles'][$bundle]['label'] : '';
  }

  /**
   * Get the name of bundles on the entity.
   *
   * Entity API doesn't give us a way to determine this, so the class must
   * do this.
   *
   * @return
   *   A translated, safe string.
   */
  public function entity_bundle_label() {
    $entity_info = entity_get_info($this->entity_type);
    return t('@entity bundle', array('@entity' => $entity_info['label']));
  }

}

