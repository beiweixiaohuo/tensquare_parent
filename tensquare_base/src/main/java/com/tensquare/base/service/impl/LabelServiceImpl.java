package com.tensquare.base.service.impl;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LabelServiceImpl implements LabelService {
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;

    @Override
    public List<Label> findAll() {
        return labelDao.findAll();
    }

    @Override
    public Label findById(String id) {
        return labelDao.findById(id).get();
    }

    @Override
    public void add(Label label) {
        label.setId(idWorker.nextId()+"");//设置ID
        labelDao.save(label);
    }

    @Override
    public void update(Label label) {
        labelDao.save(label);
    }

    @Override
    public void deleteById(String id) {
        labelDao.deleteById(id);
    }

    @Override
    public List<Label> findSearch(Label label) {
        Specification<Label> specification = createSpecification(label);
        return labelDao.findAll(specification);
    }

    @Override
    public Page<Label> findSearch(Label label, int page, int size) {
        Specification<Label> specification = createSpecification(label);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return labelDao.findAll(specification,pageRequest);
    }

    @Override
    public Specification<Label> createSpecification(Label label) {
        return new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (StringUtils.isNotBlank(label.getLabelname())){
                    list.add(criteriaBuilder.like(root.get("labelname").as(String.class),"%"+label.getLabelname()+"%"));
                }
                if (StringUtils.isNotBlank(label.getState())){
                    list.add(criteriaBuilder.equal(root.get("state").as(String.class),label.getState()));
                }
                if (StringUtils.isNotBlank(label.getRecommend())){
                    list.add(criteriaBuilder.equal(root.get("recommend").as(String.class),label.getRecommend()));
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        };
    }
}

