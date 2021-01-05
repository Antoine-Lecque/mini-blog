package com.uca;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.uca.core.ArticleCore;
import com.uca.core.UserCore;
import com.uca.dao._Initializer;
import com.uca.entity.ArticleEntity;
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

            ArticleEntity entity = new ArticleEntity();
            entity.setName(req.queryParams("name"));
            entity.setAuthor(req.queryParams("author"));
            entity.setContent(req.queryParams("content"));
            ArticleCore.create(entity);

            res.status(201);

            res.header("Content-Type", useXML ? "application/xml" : "application/json");
            return parseContent(useXML, entity);
        });

        //Delete article by id
        delete("/api/articles/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            ArticleEntity entity = ArticleCore.getArticleById(id);

            if (entity != null) {
                ArticleCore.delete(id);
                res.status(202);
                return "user with id " + id + " is deleted!";
            } else {
                res.status(404);
                return "user not found";
            }
        });


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