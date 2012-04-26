insert into amp_columns (columnName, cellType, extractorView)
(select 'Sub-Vote','org.dgfoundation.amp.ar.cell.TextCell','v_subvote'
from dual where (select count(columnId) from amp_columns where extractorView="v_subvote")=0);

/* create/replace views */
create or replace view v_subvote AS (select a.amp_activity_id, a.`subVote` from amp_activity a  where trim(a.subVote)!="");


insert into amp_columns (columnName, cellType, extractorView)
(select 'Sub-Program','org.dgfoundation.amp.ar.cell.TextCell','v_subProgram'
from dual where (select count(columnId) from amp_columns where extractorView="v_subProgram")=0);

/* create/replace views */
create or replace view v_subProgram AS (select a.amp_activity_id, a.subProgram from amp_activity a  where trim(a.subProgram)!="");


insert into amp_columns (columnName, cellType, extractorView)
(select 'Sub-Program','org.dgfoundation.amp.ar.cell.TextCell','v_subProgram'
from dual where (select count(columnId) from amp_columns where extractorView="v_subProgram")=0);

/* create/replace views */
create or replace view v_subProgram AS (select a.amp_activity_id, a.subProgram from amp_activity a  where trim(a.subProgram)!="");


insert into amp_columns (columnName, cellType, extractorView)
(select 'Vote','org.dgfoundation.amp.ar.cell.TextCell','v_vote'
from dual where (select count(columnId) from amp_columns where extractorView="v_vote")=0);

/* create/replace views */
create or replace view v_vote AS (select a.amp_activity_id,a.vote from amp_activity a  where trim(a.vote)!="");