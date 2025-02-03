package com.roomify.detection_be.Repository;

import com.roomify.detection_be.web.entity.Users.GithubUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GithubUserRepository extends CrudRepository<GithubUser, String> {}
