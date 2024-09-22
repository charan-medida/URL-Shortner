package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    @Query("SELECT DISTINCT shortUrl FROM UrlMapping  WHERE originalUrl = :originalUrl")
    String findByOriginalUrl(@Param("originalUrl") String originalUrl);

    // @Query("SELECT u.shortUrl FROM UrlMapping u WHERE u.userid = :userid AND u.originalUrl = :originalUrl")
    // String findByUseridAndOriginalUrl(@Param("userid") Long userid, @Param("originalUrl") String originalUrl);

    @Query(value = "SELECT * FROM url_mapping WHERE short_url = :shortUrl LIMIT 1", nativeQuery = true)
    UrlMapping findByShortUrl(String shortUrl);


    //List<UrlDto> findByUserid(Long userid);

    @Query("SELECT u.id FROM UrlMapping u WHERE u.originalUrl = :originalUrl")
    Long findIdByOriginalUrl(@Param("originalUrl") String originalUrl);

    
    
}

