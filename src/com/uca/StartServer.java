package com.uca;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.uca.core.ArticleCore;
import com.uca.core.CommentCore;
import com.uca.core.DoLogin;
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
        get("/", (req, res) -> ArticleGUI.getAllArticles());

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

            String auth = req.headers("authentification");

            try {
                Map<String, String> infoLogin = DoLogin.introspect(auth);

                if (! UserCore.isAdmin(infoLogin.get("uuid")) || UserCore.isBanned(infoLogin.get("uuid"))) {
                    res.status(401);
                    return "not admin or banned";
                }

                ArticleEntity entity = new ArticleEntity();
                entity.setName(req.queryParams("name"));
                entity.setAuthor(infoLogin.get("sub"));
                entity.setContent(req.queryParams("content"));
                ArticleCore.create(entity);

                res.status(201);

                res.header("Content-Type", useXML ? "application/xml" : "application/json");
                return parseContent(useXML, entity);
            }catch(Exception e){
                res.status(401);
                return "";
            }
        });

        //update article by id
        put("/api/articles/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));

            String auth = req.headers("authentification");

            try {
                Map<String, String> infoLogin = DoLogin.introspect(auth);
                ArticleEntity entity = ArticleCore.getArticleById(id);

                if (! UserCore.isAdmin(infoLogin.get("uuid")) || UserCore.isBanned(infoLogin.get("uuid"))) {
                    res.status(401);
                    return "not admin or banned";
                }

                if (entity != null) {
                    if (req.queryParams("name") != null)
                        entity.setName(req.queryParams("name"));

                    if (req.queryParams("content") != null)
                        entity.setContent(req.queryParams("content"));

                    ArticleCore.update(entity);
                    res.status(200);

                    return "article with id " + id + " is updated!";
                } else {
                    res.status(404);
                    return "article not found";
                }
            }catch(Exception e){
                res.status(401);
                return "";
            }
        });

        //Delete article by id
        delete("/api/articles/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            String auth = req.headers("authentification");

            try {
                Map<String, String> infoLogin = DoLogin.introspect(auth);
                ArticleEntity entity = ArticleCore.getArticleById(id);

                if (! UserCore.isAdmin(infoLogin.get("uuid")) || UserCore.isBanned(infoLogin.get("uuid"))) {
                    res.status(401);
                    return "not admin or banned";
                }

                if (entity != null) {
                    ArticleCore.delete(id);
                    res.status(200);
                    return "article with id " + id + " is deleted!";
                } else {
                    res.status(404);
                    return "article not found";
                }
            }catch(Exception e){
                res.status(401);
                return "";
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

            String auth = req.headers("authentification");

            try {
                Map<String, String> infoLogin = DoLogin.introspect(auth);
                CommentEntity entity = new CommentEntity();

                if (UserCore.isBanned(infoLogin.get("uuid"))) {
                    res.status(401);
                    return "not admin or banned";
                }

                //Cannot create a comment on an non-existant article
                if (ArticleCore.getArticleById(Integer.parseInt(req.queryParams("article"))) != null) {
                    entity.setArticle(Integer.parseInt(req.queryParams("article")));
                    entity.setAuthor(infoLogin.get("sub"));
                    entity.setContent(req.queryParams("content"));
                    CommentCore.create(entity);
                    res.status(201);
                }else{
                    res.status(204);
                    return "non-existant article, check the article id";
                }

                res.header("Content-Type", useXML ? "application/xml" : "application/json");
                return parseContent(useXML, entity);
            }catch(Exception e){
                res.status(401);
                return "";
            }
        });

        //update comment by id
        put("/api/comments/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));

            String auth = req.headers("authentification");

            try {
                Map<String, String> infoLogin = DoLogin.introspect(auth);
                CommentEntity entity = CommentCore.getCommentById(id);

                if (UserCore.isBanned(infoLogin.get("uuid"))) {
                    res.status(401);
                    return "not admin or banned";
                }

                if (entity != null) {
                    entity.setContent(req.queryParams("content"));

                    CommentCore.update(entity);
                    res.status(200);

                    return "comment with id " + id + " is updated!";
                } else {
                    res.status(404);
                    return "comment not found";
                }
            }catch(Exception e){
                res.status(401);
                return "";
            }
        });

        //Delete comment by id
        delete("/api/comments/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));

            String auth = req.headers("authentification");

            try {
                Map<String, String> infoLogin = DoLogin.introspect(auth);
                CommentEntity entity = CommentCore.getCommentById(id);

                if (UserCore.isBanned(infoLogin.get("uuid"))) {
                    res.status(401);
                    return "not admin or banned";
                }

                if (entity != null) {
                    CommentCore.delete(id);
                    res.status(200);
                    return "comment with id " + id + " is deleted!";
                } else {
                    res.status(404);
                    return "comment not found";
                }

            }catch(Exception e){
                res.status(401);
                return "";
            }
        });

        /*-----------------*/
        /* CRUD users      */
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

        // get article by id
        get("/api/users/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Boolean useXML = useXML(req);
            if (useXML == null) {
                res.status(406);
                return "";
            }

            UserEntity entity = UserCore.getUserById(id);
            if (entity == null) {
                res.status(204);
                return "";
            }

            res.header("Content-Type", useXML ? "application/xml" : "application/json");
            return parseContent(useXML, entity);
        });

        //Create new users
        post("/api/users", (req, res) -> {
            Boolean useXML = useXML(req);
            if (useXML == null) {
                res.status(406);
                return "";
            }
            UserEntity entity = new UserEntity();
            entity.setUsername(req.queryParams("username"));
            entity.setPassword(req.queryParams("password"));
            entity.setAdmin(Boolean.parseBoolean(req.queryParams("isAdmin")));
            entity.setBanned(Boolean.parseBoolean(req.queryParams("isBanned")));
            UserCore.create(entity);

            res.status(201);

            res.header("Content-Type", useXML ? "application/xml" : "application/json");
            return parseContent(useXML, entity);
        });

        //update user by id
        put("/api/users/:id", (req, res) -> {

            String auth = req.headers("authentification");

            try {
                Map<String, String> infoLogin = DoLogin.introspect(auth);

                if (! UserCore.isAdmin(infoLogin.get("uuid")) || UserCore.isBanned(infoLogin.get("uuid"))) {
                    res.status(401);
                    return "not admin or banned";
                }
            }catch(Exception e){
                res.status(401);
                return "";
            }

            int id = Integer.parseInt(req.params(":id"));
            UserEntity entity = UserCore.getUserById(id);

            if (entity != null) {
                if (req.queryParams("username") != null)
                    entity.setUsername(req.queryParams("username"));

                if (req.queryParams("isAdmin") != null)
                    entity.setAdmin(Boolean.parseBoolean(req.queryParams("isAdmin")));

                if (req.queryParams("isBanned") != null)
                    entity.setBanned(Boolean.parseBoolean(req.queryParams("isBanned")));

                UserCore.update(entity);
                res.status(200);

                return "user with id " + id + " is updated!";
            } else {
                res.status(404);
                return "user not found";
            }
        });

        //Delete user by name
        delete("/api/users/:id", (req, res) -> {
            String auth = req.headers("authentification");

            try {
                Map<String, String> infoLogin = DoLogin.introspect(auth);

                if (! UserCore.isAdmin(infoLogin.get("uuid")) || UserCore.isBanned(infoLogin.get("uuid"))) {
                    res.status(401);
                    return "not admin or banned";
                }
            }catch(Exception e){
                res.status(401);
                return "";
            }

            int id = Integer.parseInt(req.params(":id"));
            UserEntity entity = UserCore.getUserById(id);

            if (entity != null) {
                UserCore.delete(id);
                res.status(200);
                return "user with id " + id + " is deleted!";
            } else {
                res.status(404);
                return "user not found";
            }
        });


        /*-----------------*/
        /* LOGIN           */
        /*-----------------*/

        post("/api/login", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            if ( UserCore.check(username, password) ){
                String auth = DoLogin.createToken(username, UserCore.getIdbyName(username));
                res.status(200);
                res.header("authentification", auth);
            }else{
                res.status(400);
                return "identifiant ou mot de passe incorrect";
            }

            return "";
        });
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