package com.sq.protocol.opc.repository;

import com.sq.protocol.opc.domain.MesuringPoint;
import com.sq.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 测点仓库.
 * User: shuiqing
 * Date: 2015/4/1
 * Time: 11:33
 * Email: shuiqing301@gmail.com
 * _
 * |_)._ _
 * | o| (_
 */
public interface MesuringPointRepository extends BaseRepository<MesuringPoint, Long> {

    /** 根据编码查询测点 */
    @Query("select m from MesuringPoint m where sourceCode = ?1")
    MesuringPoint fetchMpByCode(String postCode);


    /**
     * 根据条件查询数据总数
     * @param sourceName
     * @return
     */
    Integer countMesuringPoint(String sourceName);

    /**
     * 分页查询 MesuringPoint
     * @param pageNo
     * @param sourceName
     * @return
     */
    List<MesuringPoint> MesuringPointPage(final Integer pageNo, String sourceName);

    /**
     * 获取数据库中存在的sysId
     * @return
     */
    @Query("select distinct m.sysId from MesuringPoint m")
    List<Integer> getAllDBMesuringPointSysId();

   /**
    * 根据sysId获取数据库中对应的测点集合
    * @return
    */
    @Query("select m from MesuringPoint m where m.sysId=?1")
    List<MesuringPoint> getMesuringPointBySysId(Integer sysId);
}
