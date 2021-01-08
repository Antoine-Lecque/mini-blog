package com.uca;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.uca.core.ArticleCore;
import com.uca.core.CommentCore;
import com.uca.core.UserCore;
import com.uca.dao._Initializer;
import com.uca.entity.ArticleEntity;
import com.uca.entity.CommentEntity;
import com.uca.entity.UserEntity;
import com.uca.gui.*;
import spark.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class StartServer {

    public static void main(String[] args) {
        //Configure Spark
        staticFiles.location("/static/");
        port(8081);


        _Initializer.Init();

        //Defining our routes
        get("/", (req, res) -> {
            return ArticleGUI.getAllArticles();
        });

        // API

        /*-----------------*/
        /* CRUD articles   */
        /*-----------------*/

        // get all articles
        get("/api/articles", (req, res) -> {
            Boolean useXML = useXML(req);
            if (useXML == null) {
                res.status(406);
                return "";
            }

            ArrayList<ArticleEntity> entities = ArticleCore.getAllArticles();
            if (entities == null || entities.size() == 0) {
                res.status(204);
                return "";
            }

            res.header("Content-Type", useXML ? "application/xml" : "application/json");
            return parseContent(useXML, entities);
        });

        // get article by id
        get("/api/articles/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Boolean useXML = useXML(req);
            if (useXML == null) {
                res.status(406);
                return "";
            }

            ArticleEntity entity = ArticleCore.getArticleById(id);
            System.out.println("-----------------------------------------------------------------------"+entity);
            if (entity == null) {
                res.status(204);
                return "";
            }

            res.header("Content-Type", useXML ? "application/xml" : "application/json");
            return parseContent(useXML, entity);
        });

        //Create new Article
        post("/api/articles", (req, res) -> {
            Boolean useXML = useXML(req);
            if (useXML == null) {
                res.status(406);
                return "";
            }

            ArticleEntity entity = new ArticleEntity();
            entity.setName(req.queryParams("name"));
            entity.setAuthor(req.queryParams("author"));
            entity.setContent(req.queryParams("content"));
            ArticleCore.create(entity);

            res.status(201);

            res.header("Content-Type", useXML ? "application/xml" : "application/json");
            return parseContent(useXML, entity);
        });

        //update artcle by id
        put("/api/articles/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));

            ArticleEntity entity = ArticleCore.getArticleById(id);
            if (entity != null) {
                entity.setName(req.queryParams("name"));
                entity.setContent(req.queryParams("content"));

                ArticleCore.update(entity);
                res.status(200);

                return "article with id " + id + " is updated!";
            } else {
                res.status(404);
                return "article not found";
            }

        });

        //Delete article by id
        delete("/api/articles/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));

            ArticleEntity entity = ArticleCore.getArticleById(id);

            if (entity != null) {
                ArticleCore.delete(id);
                res.status(200);
                return "article with id " + id + " is deleted!";
            } else {
                res.status(404);
                return "article not found";
            }
        });


        /*-----------------*/
        /* CRUD comments   */
        /*-----------------*/

        // get all comments
        get("/api/comments", (req, res) -> {
            Boolean useXML = useXML(req);
            if (useXML == null) {
                res.status(406);
                return "";
            }

            ArrayList<CommentEntity> entities = CommentCore.getAllComments();
            if (entities == null || entities.size() == 0) {
                res.status(204);
                return "";
            }

            res.header("Content-Type", useXML ? "application/xml" : "application/json");
            return parseContent(useXML, entities);
        });

        // get article by id
        get("/api/comments/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Boolean useXML = useXML(req);
            if (useXML == null) {
                res.status(406);
                return "";
            }

            CommentEntity entity = CommentCore.getCommentById(id);
            if (entity == null) {
                res.status(204);
                return "";
            }

            res.header("Content-Type", useXML ? "application/xml" : "application/json");
            return parseContent(useXML, entity);
        });

        //Create new comment
        post("/api/comments", (req, res) -> {
            Boolean useXML = useXML(req);
            if (useXML == null) {
                res.status(406);
                return "";
            }
            CommentEntity entity = new CommentEntity();

            //Cannot create a comment on an non-existant article
            if (ArticleCore.getArticleById(Integer.parseInt(req.queryParams("article"))) != null) {
                entity.setArticle(Integer.parseInt(req.queryParams("article")));
                entity.setAuthor(req.queryParams("author"));
                entity.setContent(req.queryParams("content"));
                CommentCore.create(entity);
                res.status(201);
            }else{
                res.status(204);
                return "non-existant article, check the article id";
            }

            res.header("Content-Type", useXML ? "application/xml" : "application/json");
            return parseContent(useXML, entity);
        });

        //update comment by id
        put("/api/comments/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));

            CommentEntity entity = CommentCore.getCommentById(id);
            if (entity != null) {
                entity.setContent(req.queryParams("content"));

                CommentCore.update(entity);
                res.status(200);

                return "comment with id " + id + " is updated!";
            } else {
                res.status(404);
                return "comment not found";
            }
        });

        //Delete comment by id
        delete("/api/comments/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));

            CommentEntity entity = CommentCore.getCommentById(id);

            if (entity != null) {
                CommentCore.delete(id);
                res.status(200);
                return "comment with id " + id + " is deleted!";
            } else {
                res.status(404);
                return "comment not found";
            }
        });

        /*-----------------*/
        /* users           */
        /*-----------------*/

        // get all users
        get("/api/users", (req, res) -> {
            Boolean useXML = useXML(req);
            if (useXML == null) {
                res.status(406);
                return "";
            }

            ArrayList<UserEntity> entities = UserCore.getAllUsers();
            if (entities == null || entities.size() == 0) {
                res.status(204);
                return "";
            }

            res.header("Content-Type", useXML ? "application/xml" : "application/json");
            return parseContent(useXML, entities);
        });


        //TODO getbyid, login, register, create, delete
    }

    private static Boolean useXML(Request req) {
        if (req.headers("Accept") != null && !req.headers("Accept").isEmpty() && !req.headers("Accept").equals("*/*")) {
            int json = req.headers("Accept").indexOf("application/json");
            int xml = req.headers("Accept").indexOf("application/xml");
            if (json == -1 && xml == -1) {
                return null;
            }
            if (xml == -1) {
                return false;
            } else {
                return json == -1 || json >= xml;
            }
        }
        return true;
    }

    private static String parseContent(boolean useXML, Object obj) throws JsonProcessingException {
        if (obj.getClass().getName().equals("java.util.ArrayList")) {
            Map<String, Object> map = new HashMap<>();
            map.put("content", obj);
            obj = map;
        }

        if (useXML) {
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.writeValueAsString(obj);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}