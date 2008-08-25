 
delete p from amp_widget as w, amp_widget_place as p where w.widget_type like 'indsecchart' and p.assigned_widget_id = w.id; 


delete w from amp_widget as w where w.widget_type like 'indsecchart'; 

delete v from amp_indicator_connection as c, amp_indicator_values as v where sub_clazz like 's' and c.id = v.ind_connect_id; alter

delete from amp_indicator_connection where sub_clazz like 's'; 

