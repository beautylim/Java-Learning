package org.example.shop.service;

import com.github.pagehelper.PageInfo;
import org.example.shop.model.entity.Article;
import org.example.shop.model.entity.ArticleQuery;

public interface ArticleService {
    int insertArticle(Article article);
    PageInfo<Article> selectMyArticles(ArticleQuery articleQuery);

    int deleteArticleById(Long id);

    Article getArticleById(Long id);

    Article updateArticleById(Article article);
}
