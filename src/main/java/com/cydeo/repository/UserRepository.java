package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByIsDeletedOrderByFirstNameDesc(Boolean isDeleted);

    User findByUserNameAndIsDeleted(String username, Boolean isDeleted);

    @Transactional
    void deleteByUserName(String username);

    List<User> findAllByRoleDescriptionIgnoreCaseAndIsDeleted(String description, Boolean isDeleted);

}
