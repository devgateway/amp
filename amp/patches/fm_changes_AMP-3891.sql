delete amp3 from amp_modules_templates amp3,
(select  amp1.id from amp_modules_visibility amp1,
(select id from amp_modules_visibility where name = 'PROJECT MANAGEMENT') as amp2
 where name = 'Physical Progress' and amp1.parent = amp2.id
) as amp4
 where amp3.module = amp4.id;


delete amp1 from amp_modules_visibility amp1,
(select id from amp_modules_visibility where name = 'PROJECT MANAGEMENT') as amp2
 where name = 'Physical Progress' and amp1.parent = amp2.id;