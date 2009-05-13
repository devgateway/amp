create index lucene1 on amp_activity(amp_activity_id);
create index lucene2 on dg_editor(EDITOR_KEY);

CREATE OR REPLACE VIEW `v_lucene_index_data` AS select amp_activity_id, name title, d.BODY description, d2.BODY objective, d3.BODY purpose, d4.BODY results from amp_activity a left outer join dg_editor d on  a.description = d.EDITOR_KEY left outer join dg_editor d2 on a.objectives = d2.EDITOR_KEY left outer join dg_editor d3 on  a.purpose = d3.EDITOR_KEY left outer join dg_editor d4 on a.results = d4.EDITOR_KEY order by amp_activity_id;

