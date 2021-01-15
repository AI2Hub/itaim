package com.asset.management.dao;

import com.asset.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.xml.transform.sax.SAXTransformerFactory;

@Repository
public interface UserDao extends JpaRepository<User,Integer>{

    @Query(value = "select * from t_user where name = ?1", nativeQuery = true)
    User findByName(String name);
}
