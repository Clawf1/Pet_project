package com.clf.service.filterChain.model;

import com.clf.service.filterChain.AbstractFilterHandler;
import com.clf.model.Owner;
import com.clf.service.filterChain.handlers.BirthdayFilterHandler;
import com.clf.service.filterChain.handlers.NameFilterHandler;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class OwnerFilter implements BaseFilter<Owner> {
    private String name;
    private Date birthday;

    private AbstractFilterHandler<Owner, OwnerFilter> firstHandler;

    public OwnerFilter(String name, Date birthday) {
        this.name = name;
        this.birthday = birthday;
    }

    @Override
    public void createFilterChain() {
        firstHandler = new NameFilterHandler<>(OwnerFilter::getName);
        var birthdayHandler = new BirthdayFilterHandler<>(OwnerFilter::getBirthday);

        firstHandler.setNext(birthdayHandler);
    }

    @Override
    public void applyFilter(CriteriaBuilder cb, CriteriaQuery<Owner> cq, Root<Owner> root) {
        if (firstHandler == null) createFilterChain();

        firstHandler.applyFilter(cb, cq, root, this);
    }
}

