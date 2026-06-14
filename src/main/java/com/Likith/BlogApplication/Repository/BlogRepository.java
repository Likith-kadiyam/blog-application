package com.Likith.BlogApplication.Repository;

import com.Likith.BlogApplication.Entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BlogRepository extends JpaRepository<Posts, Long> {

    @Query(value = """
        SELECT DISTINCT p.*
        FROM posts p
        LEFT JOIN post_tags pt ON p.id = pt.post_id
        LEFT JOIN tags t ON t.id = pt.tag_id
        WHERE
        (
            CAST(:search AS varchar) IS NULL
            OR LOWER(p.title) LIKE LOWER('%' || CAST(:search AS varchar) || '%')
            OR LOWER(p.content) LIKE LOWER('%' || CAST(:search AS varchar) || '%')
            OR LOWER(p.author) LIKE LOWER('%' || CAST(:search AS varchar) || '%')
            OR LOWER(t.tag_name) LIKE LOWER('%' || CAST(:search AS varchar) || '%')
        )
        AND
        (
            CAST(:author AS varchar) IS NULL
            OR LOWER(p.author) = LOWER(CAST(:author AS varchar))
        )
        AND
        (
            CAST(:date AS date) IS NULL
            OR p.published_at::date = CAST(:date AS date)
        )
        ORDER BY p.published_at DESC
        """,
            countQuery = """
        SELECT COUNT(DISTINCT p.id)
        FROM posts p
        LEFT JOIN post_tags pt ON p.id = pt.post_id
        LEFT JOIN tags t ON t.id = pt.tag_id
        WHERE
        (
            CAST(:search AS varchar) IS NULL
            OR LOWER(p.title) LIKE LOWER('%' || CAST(:search AS varchar) || '%')
            OR LOWER(p.content) LIKE LOWER('%' || CAST(:search AS varchar) || '%')
            OR LOWER(p.author) LIKE LOWER('%' || CAST(:search AS varchar) || '%')
            OR LOWER(t.tag_name) LIKE LOWER('%' || CAST(:search AS varchar) || '%')
        )
        AND
        (
            CAST(:author AS varchar) IS NULL
            OR LOWER(p.author) = LOWER(CAST(:author AS varchar))
        )
        AND
        (
            CAST(:date AS date) IS NULL
            OR p.published_at::date = CAST(:date AS date)
        )
        """,
            nativeQuery = true)
    Page<Posts> searchPosts(
            @Param("search") String search,
            @Param("author") String author,
            @Param("date") LocalDate date,
            Pageable pageable
    );
}
