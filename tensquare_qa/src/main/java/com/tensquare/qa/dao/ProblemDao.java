package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{
	//根据标签ID查询最新问题列表
    //@Query("select p from Problem p where id in (select problemid from pl where labelid = ?1)order by replytime desc")
    @Query(value="select * from tb_problem,tb_pl where id=problemid and labelid=? order by replytime desc",nativeQuery=true)
    Page<Problem> findNewListByLabelId(String labelId, Pageable pageable);

    //按回复数降序排序
    @Query(value="select * from tb_problem,tb_pl where id=problemid and labelid=? order by reply desc",nativeQuery=true)
    Page<Problem> findHotListByLabelId(String labelId,Pageable pageable);

    //根据标签ID查询等待回答列表
    @Query(value="select * from tb_problem,tb_pl where id=problemid and labelid=? and reply=0 order by createtime desc",nativeQuery=true)
    Page<Problem> findWaitListByLabelId(String labelId,Pageable pageable);

}
