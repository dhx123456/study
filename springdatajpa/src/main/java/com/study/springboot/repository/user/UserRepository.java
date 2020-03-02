package com.study.springboot.repository.user;

import com.study.springboot.entity.User;
import com.study.springboot.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Integer> {
}
