CREATE TABLE `t_originaldata_migration201501` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201501_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201501 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201501';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201501';




CREATE TABLE `t_originaldata_migration201502` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201502_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201502 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201502';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201502';




CREATE TABLE `t_originaldata_migration201503` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201503_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201503 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201503';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201503';





CREATE TABLE `t_originaldata_migration201504` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201504_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201504 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201504';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201504';






CREATE TABLE `t_originaldata_migration201505` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201505_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201505 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201505';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201505';





CREATE TABLE `t_originaldata_migration201506` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201506_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201506 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201506';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201506';






CREATE TABLE `t_originaldata_migration201507` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201507_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201507 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201507';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201507';





CREATE TABLE `t_originaldata_migration201508` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201508_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201508 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201508';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201508';






CREATE TABLE `t_originaldata_migration201509` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201509_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201509 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201509';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201509';




CREATE TABLE `t_originaldata_migration201510` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201510_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201510 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201510';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201510';





CREATE TABLE `t_originaldata_migration201511` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201511_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201511 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201511';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201511';







CREATE TABLE `t_originaldata_migration201512` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201512_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201512 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201512';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201512';




CREATE TABLE `t_originaldata_migration201601` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201601_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201601 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201601';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201601';


CREATE TABLE `t_originaldata_migration201602` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201602_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201602 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201602';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201602';





CREATE TABLE `t_originaldata_migration201603` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201603_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201603 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201603';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201603';



CREATE TABLE `t_originaldata_migration201604` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201604_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201604 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201604';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201604';






CREATE TABLE `t_originaldata_migration201605` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201605_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201605 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201605';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201605';


CREATE TABLE `t_originaldata_migration201606` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batchNum` bigint(20) DEFAULT NULL,
  `sysid` int(11) NOT NULL,
  `instanceTime` datetime DEFAULT NULL,
  `itemCode` varchar(255) DEFAULT NULL,
  `itemValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_orig_201606_itemCode_time` (`itemCode`,`instanceTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_originaldata_migration201606 (batchNum,sysid,instanceTime,itemCode,itemValue)
select batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,'%Y%m') = '201606';

delete from t_originaldata where date_format(instanceTime,'%Y%m') = '201606';




