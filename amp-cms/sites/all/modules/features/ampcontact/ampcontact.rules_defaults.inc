<?php
/**
 * @file
 * ampcontact.rules_defaults.inc
 */

/**
 * Implements hook_default_rules_configuration().
 */
function ampcontact_default_rules_configuration() {
  $items = array();
  $items['rules_contact_form_notifications'] = entity_import('rules_config', '{ "rules_contact_form_notifications" : {
      "LABEL" : "Contact Form Notifications",
      "PLUGIN" : "reaction rule",
      "OWNER" : "rules",
      "TAGS" : [ "AMP General" ],
      "REQUIRES" : [ "rules", "entityform" ],
      "ON" : { "entityform_insert" : [] },
      "DO" : [
        { "mail" : {
            "to" : "[entityform:field-contact-email]",
            "subject" : "Contact form submission notification - [site:name]",
            "message" : "Hello [entityform:field_contact_name],\\r\\nYour data has been received.\\r\\n\\r\\nHere is a copy of the submitted data:\\r\\n[entityform:textexport-email]\\r\\n",
            "language" : [ "" ]
          }
        },
        { "mail_to_users_of_role" : {
            "roles" : { "value" : { "3" : "3" } },
            "subject" : "New contact form submission [[site:name]]",
            "message" : "[entityform:textexport-email]\\r\\n"
          }
        }
      ]
    }
  }');
  return $items;
}
