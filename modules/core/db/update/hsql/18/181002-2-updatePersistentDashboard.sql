alter table DASHBOARD_PERSISTENT_DASHBOARD alter column DASHBOARD_GROUP_ID rename to DASHBOARD_GROUP_ID__U09668 ^
drop index IDX_DASHBOARD_PERSISTENT_DASHBOARD_ON_DASHBOARD_GROUP ;
alter table DASHBOARD_PERSISTENT_DASHBOARD drop constraint FK_DASHBOARD_PERSISTENT_DASHBOARD_ON_DASHBOARD_GROUP ;
alter table DASHBOARD_PERSISTENT_DASHBOARD add column GROUP_ID varchar(36) ;
