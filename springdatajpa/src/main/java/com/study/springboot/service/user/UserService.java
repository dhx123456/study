package com.study.springboot.service.user;

import com.study.springboot.entity.User;
import com.study.springboot.entity.form.UserForm;
import com.study.springboot.repository.user.UserRepository;
import com.study.springboot.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends BaseService<User, Integer> {
    @Autowired
    private UserRepository userRepository;

    public Page<User> getUserByPage(UserForm userForm, int pageNumber, int pageSize) {
        return userRepository.findAll(this.buildSpec(userForm), PageRequest.of(pageNumber, pageSize));
    }

    public Specification<User> buildSpec(UserForm form) {
        Specification<User> spec = new Specification<User>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //本集合用于封装查询条件
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.hasText(form.getSearch())) {
                    Predicate name = cb.like(root.get("name"), "%" + form.getSearch() + "%");
                    /*Predicate username = cb.like(root.get("username"), "%" + form.getSearch() + "%");
                    Predicate or = cb.or(name, username);*/
                    predicates.add(name);
                }
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            }
        };
        return spec;
    }
}
