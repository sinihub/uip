##日志表
CREATE TABLE `t_quotacurrentlog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `indicatorCode` varchar(255) DEFAULT NULL,
  `indicatorName` varchar(255) DEFAULT NULL,
  `computeTime` datetime DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `computeResult` varchar(255) DEFAULT NULL,
  `legend` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

alter table t_quotacurrentlog add column dataSource int(11) DEFAULT NULL;
alter table t_quotacurrentlog add column status tinyint(1) NOT NULL;

##迁移指标日志每月一张表存入当月日志数据
CREATE PROCEDURE `quota_migrate_month`(IN calculateDay VARCHAR(254))
BEGIN
  set @calculateMonth = left(calculateDay,6);
  ##查询当前需要迁移的月份数据表是否存在，如果不存在创建月份数据表
	SET @FLAG = (SELECT count(1) from information_schema.tables where table_name like CONCAT('t_quotalog_migration',@calculateMonth));
	IF @FLAG = 0 THEN
      set @SQL_CREATE_TABLE = CONCAT('create table t_quotalog_migration',@calculateMonth,
          '(  `id` bigint(20) NOT NULL AUTO_INCREMENT,
							`indicatorCode` varchar(255) DEFAULT NULL,
							`indicatorName` varchar(255) DEFAULT NULL,
							`computeTime` datetime DEFAULT NULL,
							`createTime` datetime DEFAULT NULL,
							`computeResult` varchar(255) DEFAULT NULL,
							`legend` varchar(255) DEFAULT NULL,
							PRIMARY KEY (`id`))');
			select @SQL_CREATE_TABLE;
			prepare createstmt from @SQL_CREATE_TABLE;
			execute createstmt;
  END IF;
  ##插入数据到月表
  set @SQL_INSERT_DATA = CONCAT('insert into t_quotalog_migration',@calculateMonth,'(indicatorCode,indicatorName,computeTime,createTime,computeResult,legend)',' select indicatorCode,indicatorName,computeTime,createTime,computeResult,legend from t_quotacurrentlog where date_format(computeTime,''%Y%m%d'') = ',calculateDay);
  select @SQL_INSERT_DATA;
	prepare insertstmt from @SQL_INSERT_DATA;
	execute insertstmt;
  ##删除源表的当天数据
  set @SQL_DELETE_DATA = CONCAT('delete from t_quotacurrentlog where date_format(computeTime,''%Y%m%d'') = ',calculateDay);
	prepare deletestmt from @SQL_DELETE_DATA;
	execute deletestmt;
END

