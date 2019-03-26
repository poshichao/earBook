package com.android.earBook.repository;

import com.android.earBook.entity.Article;
import org.springframework.data.repository.CrudRepository;

/**
 * 文章仓库，用户数据库操作
 * @author poshchao
 */
public interface ArticleRepository extends CrudRepository<Article, Long> {
}
