package org.example.shop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.auth.LoginUser;
import org.example.shop.mapper.ArticleMapper;
import org.example.shop.model.entity.Article;
import org.example.shop.model.entity.ArticleQuery;
import org.example.shop.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public int insertArticle(Article article) {
        UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) userDetails.getPrincipal();
        article.setAuthorId(loginUser.getUserId());
        return articleMapper.insert(article);
    }

    @Override
    public PageInfo<Article> selectMyArticles(ArticleQuery articleQuery) {
        UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) userDetails.getPrincipal();
        articleQuery.setAuthorId(loginUser.getUserId());
        PageHelper.startPage(articleQuery.getPageNum(), articleQuery.getPageSize());
        List<Article> articles = articleMapper.selectArticleByAuthorId(articleQuery);
        return new PageInfo<>(articles);
    }

    @Override
    public int deleteArticleById(Long id) {
        return articleMapper.deleteById(id);
    }

    @Override
    public Article getArticleById(Long id) {
        return articleMapper.getById(id);
    }

    @Override
    public Article updateArticleById(Article article) {
        int res = articleMapper.update(article);
        if (res == 1) {
            return articleMapper.getById(article.getId());
        } else {
            return article;
        }
    }
}
