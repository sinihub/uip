#修改指标实例中部分实例的聚合方式为算术平均值
UPDATE t_indicatorinstancecurrent set operCalType = 3 where operCalType is null and indicatorCode in ('RD_B1CO_Hnum',
'RD_B1DUST_Hnum',
'RD_B1HCL_Hnum',
'RD_B1HXTYL_Hnum',
'RD_B1LTCKYQWD_Hnum',
'RD_B1LTJKYQWD_Hnum',
'RD_B1NOX_Hnum',
'RD_B1O2_Hnum',
'RD_B1SHYL_Hnum',
'RD_B1SO2_Hnum',
'RD_B1WBYDCKYQWD_Hnum',
'RD_B1WBYDJKYQWD_Hnum',
'RD_B2CO_Hnum',
'RD_B2DUST_Hnum',
'RD_B2HCL_Hnum',
'RD_B2HXTYL_Hnum',
'RD_B2LTCKYQWD_Hnum',
'RD_B2LTJKYQWD_Hnum',
'RD_B2NOX_Hnum',
'RD_B2O2_Hnum',
'RD_B2SHYL_Hnum',
'RD_B2SO2_Hnum',
'RD_B2WBYDCKYQWD_Hnum',
'RD_B2WBYDJKYQWD_Hnum',
'RD_B3CO_Hnum',
'RD_B3DUST_Hnum',
'RD_B3HCL_Hnum',
'RD_B3HXTYL_Hnum',
'RD_B3LTCKYQWD_Hnum',
'RD_B3LTJKYQWD_Hnum',
'RD_B3NOX_Hnum',
'RD_B3O2_Hnum',
'RD_B3SHYL_Hnum',
'RD_B3SO2_Hnum',
'RD_B3WBYDCKYQWD_Hnum',
'RD_B3WBYDJKYQWD_Hnum',
'RD_B4CO_Hnum',
'RD_B4DUST_Hnum',
'RD_B4HCL_Hnum',
'RD_B4HXTYL_Hnum',
'RD_B4NOX_Hnum',
'RD_B4O2_Hnum',
'RD_B4SHYL_Hnum',
'RD_B4SO2_Hnum',
'RD_B4WBYDCKYQWD_Hnum',
'RD_B4WBYDJKYQWD_Hnum',
'RD_B5CO_Hnum',
'RD_B5DUST_Hnum',
'RD_B5HCL_Hnum',
'RD_B5HXTYL_Hnum',
'RD_B5NOX_Hnum',
'RD_B5O2_Hnum',
'RD_B5SHYL_Hnum',
'RD_B5SO2_Hnum',
'RD_B5WBYDCKYQWD_Hnum',
'RD_B5WBYDJKYQWD_Hnum'
);


#修改指标实例中部分实例的聚合方式为累计值
UPDATE t_indicatorinstancecurrent set operCalType = 6 where operCalType is null and indicatorCode in ('RD_B1CO_Hnum',
'RD_B1LJFSL_Dsum',
'RD_B2LJFSL_Dsum',
'RD_B3LJFSL_Dsum',
'RD_B4LJFSL_Dsum',
'RD_B5LJFSL_Dsum',
'RD_B4LJFSL_Msum',
'RD_B5LJFSL_Msum',
'RD_B4LJFSL_Ysum',
'RD_B5LJFSL_Ysum'
);

#为指标实例中StringValue为null的指标实例赋值为floatValue
UPDATE
	t_indicatorinstancecurrent a
set a.stringValue = a.floatValue
WHERE
	(
		stringValue IS NULL || stringValue = 'null'
	)
AND floatValue IS NOT NULL;

DROP TABLE t_originaldata_migration201607;