insert into amp_category_class(category_name,keyName,is_multiselect,is_ordered)	select 'Event type','event_type',false,true from dual where (select count(id) from amp_category_class where keyName like 'event_type')=0;

insert into amp_category_class(category_name,keyName,is_multiselect,is_ordered)	select 'Colors','event_color',false,true from dual where (select count(id) from amp_category_class where keyName like 'event_color')=0;

SET @lastIndex=	(select count(*)-1 from amp_category_value where amp_category_class_id=(select id from amp_category_class where keyName='event_color'));

insert into `amp_category_value` (`category_value`, `amp_category_class_id`, `index_column`) select 'aqua',id,@lastIndex:=@lastIndex+1 from amp_category_class where (select count(*) from amp_category_value where category_value like 'aqua')=0 and id=(select id from amp_category_class where keyName='event_color');

insert into `amp_category_value` (`category_value`, `amp_category_class_id`, `index_column`) select 'black',id,@lastIndex:=@lastIndex+1 from amp_category_class where (select count(*) from amp_category_value where category_value like 'black')=0 and id=(select id from amp_category_class where keyName='event_color');

insert into `amp_category_value` (`category_value`, `amp_category_class_id`, `index_column`) select 'blue',id,@lastIndex:=@lastIndex+1 from amp_category_class where (select count(*) from amp_category_value where category_value like 'blue')=0 and id=(select id from amp_category_class where keyName='event_color');

insert into `amp_category_value` (`category_value`, `amp_category_class_id`, `index_column`) select 'fuchsia',id,@lastIndex:=@lastIndex+1 from amp_category_class where (select count(*) from amp_category_value where category_value like 'fuchsia')=0 and id=(select id from amp_category_class where keyName='event_color');

insert into `amp_category_value` (`category_value`, `amp_category_class_id`, `index_column`) select 'silver',id,@lastIndex:=@lastIndex+1 from amp_category_class where (select count(*) from amp_category_value where category_value like 'silver')=0 and id=(select id from amp_category_class where keyName='event_color');
				
insert into `amp_category_value` (`category_value`, `amp_category_class_id`, `index_column`) select 'green',id,@lastIndex:=@lastIndex+1 from amp_category_class where (select count(*) from amp_category_value where category_value like 'green')=0 and id=(select id from amp_category_class where keyName='event_color');
				
insert into `amp_category_value` (`category_value`, `amp_category_class_id`, `index_column`) select 'lime',id,@lastIndex:=@lastIndex+1 from amp_category_class where (select count(*) from amp_category_value where category_value like 'lime')=0 and id=(select id from amp_category_class where keyName='event_color');
				
insert into `amp_category_value` (`category_value`, `amp_category_class_id`, `index_column`) select 'maroon',id,@lastIndex:=@lastIndex+1 from amp_category_class where (select count(*) from amp_category_value where category_value like 'maroon')=0 and id=(select id from amp_category_class where keyName='event_color');
				
insert into `amp_category_value` (`category_value`, `amp_category_class_id`, `index_column`) select 'navy',id,@lastIndex:=@lastIndex+1 from amp_category_class where (select count(*) from amp_category_value where category_value like 'navy')=0 and id=(select id from amp_category_class where keyName='event_color');
				
insert into `amp_category_value` (`category_value`, `amp_category_class_id`, `index_column`) select 'purple',id,@lastIndex:=@lastIndex+1 from amp_category_class where (select count(*) from amp_category_value where category_value like 'purple')=0 and id=(select id from amp_category_class where keyName='event_color');
				
insert into `amp_category_value` (`category_value`, `amp_category_class_id`, `index_column`) select 'teal',id,@lastIndex:=@lastIndex+1 from amp_category_class where (select count(*) from amp_category_value where category_value like 'teal')=0 and id=(select id from amp_category_class where keyName='event_color');
				
insert into `amp_category_value` (`category_value`, `amp_category_class_id`, `index_column`) select 'yellow',id,@lastIndex:=@lastIndex+1 from amp_category_class where (select count(*) from amp_category_value where category_value like 'yellow')=0 and id=(select id from amp_category_class where keyName='event_color');



SET @idx:=(select count(id)-1 from amp_category_value where amp_category_class_id=(select id from amp_category_class where keyName='event_type'));
insert into amp_category_value (category_value, amp_category_class_id, index_column) select et.NAME, cls.id, @idx:=@idx+1 from amp_event_type as et, amp_category_class as cls where cls.keyName like 'event_type' and not exists (select * from amp_category_value cv where cv.amp_category_class_id=cls.id and cv.category_value=et.NAME);

drop temporary table if exists temp_event_type;
create temporary table temp_event_type(
	SELECT evType.id as event_type_id ,cat.id as cat_id,cat.category_value as cat_val 
	FROM amp_category_value cat join amp_event_type evType 
	on evType.name=cat.category_value
);
update amp_calendar c set events_type_id=(select cat_id from temp_event_type where c.event_type_id=event_type_id) where events_type_id is null;
drop temporary table temp_event_type;

 
SET @et:=(select id from `amp_category_class` where keyName = 'event_type');
SET @ec:=(select id from `amp_category_class` where keyName = 'event_color');
insert into `amp_categories_used`(`category_id`,`used_category_id`,`categories_index`) values (@et,@ec,0);
