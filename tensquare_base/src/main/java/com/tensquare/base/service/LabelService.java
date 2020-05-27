package com.tensquare.base.service;

import com.tensquare.base.pojo.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

public interface LabelService {
    List<Label> findAll();
    Label findById(String id);
    void add(Label label);
    void update(Label label);
    void deleteById(String id);
    Specification<Label> createSpecification(Label label);
    //条件搜索
    List<Label> findSearch(Label label);
    //带分页搜索
    Page<Label> findSearch(Label label, int page, int size);

}
