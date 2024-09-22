package com.example.demo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserUrlRepository extends JpaRepository<UserUrl, Long> {
    
    @Query("SELECT u.urlId FROM UserUrl u WHERE u.userId = :userId AND u.urlId = :urlId")
    Long findUrlId(@Param("userId") Long userId, @Param("urlId") Long urlId);


    // @Query(value = "SELECT new com.example.demo.UrlDto(u.original_url,u.short_url) FROM url_mapping u JOIN User_Url uu on u.id = uu.url_Id where uu.user_Id = :userId limit 1"
    // ,nativeQuery=true)
    @Query("SELECT new com.example.demo.UrlDto(u.originalUrl, u.shortUrl) FROM UrlMapping u JOIN UserUrl uu ON u.id = uu.urlId WHERE uu.userId = :userId")
    List<UrlDto> findByUserid(Long userId);
}
