update amp_columns set filterRetrievable=true where columnName like '%Sector%' AND cellType like '%MetaTextCell%';
update amp_columns set filterRetrievable=true where columnId IN (9,41,42,43,51);