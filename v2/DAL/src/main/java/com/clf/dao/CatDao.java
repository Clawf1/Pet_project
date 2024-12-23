package com.clf.dao;

import com.clf.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CatDao extends JpaRepository<Cat, Long>, JpaSpecificationExecutor<Cat> {

}
