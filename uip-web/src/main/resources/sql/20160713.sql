CREATE PROCEDURE `originaldata_migration`(IN calculateDayFirst VARCHAR(254),IN calculateDayEnd VARCHAR(254))
BEGIN
	 /**
	   按月迁移数据，传入开始月和尾月(YYYYMM,YYYYMM)
       @user:yaowenjie
	   @method:最多一次性迁移一年的数据
       @ps:不可跨年迁移
     */
		set @firstDatee = right(calculateDayFirst,2);
		set @endDatee = right(calculateDayEnd,2);
		set @calculateMonth = left(calculateDayFirst,4);
    while @firstDatee <= @endDatee do
			set @SQL_CREATE_TABLE = CONCAT('create table t_originaldata_migration',concat(@calculateMonth,@firstDatee),
					'(id bigint not null auto_increment,
					batchNum bigint,sysid integer not null,
					instanceTime datetime,
					itemCode varchar(255),
					itemValue varchar(255),
					primary key (id))');
			select @SQL_CREATE_TABLE;
			prepare createstmt from @SQL_CREATE_TABLE;
			execute createstmt;

			set @SQL_CREATE_INDEX = CONCAT('create INDEX ind_orig_', concat(@calculateMonth,@firstDatee), '_itemCode_time on
t_originaldata_migration',concat(@calculateMonth,@firstDatee),' (itemCode, instanceTime)');
			prepare createIndexstmt from @SQL_CREATE_INDEX;
			execute createIndexstmt;

			set @SQL_INSERT_DATA = CONCAT('insert into
t_originaldata_migration',@calculateMonth,@firstDatee,'(batchNum,sysid,instanceTime,itemCode,itemValue)',' select
batchNum,sysid,instanceTime, itemCode,itemValue from t_originaldata where date_format(instanceTime,''%Y%m'') = ',concat
(@calculateMonth,@firstDatee));
			select @SQL_INSERT_DATA;
			prepare insertstmt from @SQL_INSERT_DATA;
			execute insertstmt;
			set @firstDatee = @firstDatee+1;
			if @firstDatee <10 then
				set @firstDatee = concat(0,@firstDatee);
			end if;
	end while;

		set @SQL_DELETE_DATA = CONCAT('delete from t_originaldata where date_format(instanceTime,''%Y%m'')>=',calculateDayFirst,' and
date_format(instanceTime,''%Y%m'') <= ',calculateDayEnd);
		prepare deletestmt from @SQL_DELETE_DATA;
		execute deletestmt;

end