DELETE FROM amp_features_templates WHERE feature=(SELECT id FROM amp_features_visibility WHERE name="Recurring Event Button");
DELETE FROM amp_features_visibility WHERE name="Recurring Event Button";