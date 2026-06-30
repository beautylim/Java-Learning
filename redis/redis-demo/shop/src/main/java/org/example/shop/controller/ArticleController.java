package org.example.shop.controller;

import com.github.pagehelper.PageInfo;
import org.example.shop.model.dto.ChannelResponse;
import org.example.shop.model.entity.Article;
import org.example.shop.model.entity.ArticleQuery;
import org.example.shop.service.ArticleService;
import org.example.shop.service.ChannelTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.url-prefix}")
    private String urlPrefix;

    @Autowired
    private ChannelTypeService channelTypeService;

    @Autowired
    private ArticleService articleService;

    @GetMapping("/type")
    public ResponseEntity<ChannelResponse> getType(){
        ChannelResponse channelResponse = new ChannelResponse();
        channelResponse.setData(channelTypeService.getArticleTypeList());
        return ResponseEntity.status(HttpStatus.OK).body(channelResponse);
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody Article article){
        int res = articleService.insertArticle(article);
        if (res == 1){
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"id\": \""+ article.getId()+"\"}");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        int res = articleService.deleteArticleById(id);
        if (res == 1){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{\"result\": \"success\"}");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"result\": \" not found\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(articleService.getArticleById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article article){
        article.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(articleService.updateArticleById(article));
    }

    @GetMapping("/my")
    public ResponseEntity<PageInfo<Article>> getMy(@RequestParam(defaultValue = "1") int pageNum,
                                                   @RequestParam(defaultValue = "4") int pageSize,
                                                   @RequestParam(required = false, defaultValue = "0") Integer status,
                                                   @RequestParam(required = false, defaultValue = "0") Integer channelId,
                                                   @RequestParam(required = false) String startTime,
                                                   @RequestParam(required = false) String endTime){
        ArticleQuery  articleQuery = new ArticleQuery();
        articleQuery.setStatus(status);
        articleQuery.setChannelId(channelId);
        articleQuery.setStartTime(startTime);
        articleQuery.setEndTime(endTime);
        articleQuery.setPageNum(pageNum);
        articleQuery.setPageSize(pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(articleService.selectMyArticles(articleQuery));
    }

    @PostMapping("/upload/image")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file){
        // 1. 检查文件是否为空
        if (file.isEmpty()) {
            return errorResponse("请选择要上传的图片");
        }

        // 2. 获取原始文件名和后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 3. 生成新文件名（避免重名覆盖）
        // 方案：时间戳 + UUID + 后缀
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String newFileName = timestamp + "_" + UUID.randomUUID().toString().substring(0, 8) + suffix;

        // 4. 确保上传目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 5. 保存文件到服务器
        File destFile = new File(uploadPath + newFileName);
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return errorResponse("图片上传失败：" + e.getMessage());
        }

        // 6. 返回可访问的图片URL
        // 例如：http://localhost:8080/images/20250625143022_a1b2c3d4.jpg
        String imageUrl = urlPrefix + newFileName;
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("{\"url\": \"" + "http://localhost:8440" + imageUrl + "\"}");

    }


    ResponseEntity<String> errorResponse(String message){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(String.format("{\"result\": \"failed\", \"message\": \"%s\"}", message));
    }
}
