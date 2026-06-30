package org.example.shop.mapper;

import org.example.shop.model.entity.Article;
import org.example.shop.model.entity.ArticleQuery;

import java.util.List;

public interface ArticleMapper {

    int insert(Article article);
    List<Article> selectArticleByAuthorId(ArticleQuery articleQuery);

    int deleteById(Long id);

    Article getById(Long id);

    int update(Article article);
}
