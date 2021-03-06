package com.spring.tutorial.controllers;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxStandardSessionStore;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.util.LangUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.spring.tutorial.dropbox.DropBoxAuth;
import com.spring.tutorial.dropbox.DropboxEntity;
import com.spring.tutorial.dropbox.DropboxEntityFactory;
import com.spring.tutorial.entitites.FOUFile;
import com.spring.tutorial.entitites.MongoFile;
import com.spring.tutorial.entitites.MongoFileManager;
import com.spring.tutorial.mongo.MongoData;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import static java.rmi.server.LogStream.log;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import static javax.servlet.SessionTrackingMode.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.portlet.ModelAndView;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author petricioiurobert
 */
@Controller
public class DefaultController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap map) {
        return "redirect:login";
    }

    @RequestMapping(value = "/my-drive", method = RequestMethod.GET)
    public String myDrive(ModelMap map, HttpServletRequest request) {
        map.addAttribute("title", "my-drive");
        try {
            String id = request.getSession().getAttribute("id").toString();
            MongoFileManager fileManager = new MongoFileManager(request.getSession().getAttribute("id").toString());
            map.addAttribute("files", fileManager.getFiles());

        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }

        return "mydrive/mydrive";
    }

    @RequestMapping(value = "/files/{id}", method = RequestMethod.GET)
    public String getFile(@PathVariable("id") String id, ModelMap map,
            HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {

        String _id = request.getSession().getAttribute("id").toString();
        MongoData data = new MongoData();
        GridFS collection = new GridFS(data.getDB(), _id + "_files");

        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        GridFSDBFile file = collection.findOne(query);

        DBCollection metaFileCollection = data.getDB().getCollection(_id + "_files_meta");
        BasicDBObject metaQuery = new BasicDBObject();
        metaQuery.put("file-id", new ObjectId(id));
        DBObject metaFileDoc = metaFileCollection.findOne(metaQuery);
        MongoFile metaFile = new MongoFile(metaFileDoc);

        ServletOutputStream out = response.getOutputStream();
        String mimeType = metaFile.getType();
        response.setContentType(mimeType);
        response.setContentLength((int) file.getLength());
        String headerKey = "Content-Disposition";

        File f = File.createTempFile(file.getFilename(), metaFile.getType().substring(metaFile.getType().indexOf("/") + 1));
        String headerValue = String.format("attachment; filename=\"%s\"",
                file.getFilename());
        response.setHeader(headerKey, headerValue);
        file.writeTo(f);

        FileInputStream inputStream = new FileInputStream(f);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while (inputStream.read(buffer, 0, 4096) != -1) {
            out.write(buffer, 0, 4096);
        }
        inputStream.close();
        out.flush();
        out.close();

        return "mydrive/temp";
    }
    DbxWebAuth auth = null;
    DbxRequestConfig config = null;

    @RequestMapping(value = "/dropbox", method = RequestMethod.GET)
    public void sendAuthRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String APP_KEY = "mt6yctmupgrodlm";
        final String APP_SECRET = "p68kiiyvdctftuv";

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        config = new DbxRequestConfig(
                "JavaTutorial/1.0", Locale.getDefault().toString());
        HttpSession session = request.getSession();
        String sessionKey = "dropbox-auth-csrf-token";

        DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, sessionKey);

        String redirectUri = "http://localhost:8080/FOU_1/dropbox-auth-finished";
        auth = new DbxWebAuth(config, appInfo, redirectUri, csrfTokenStore);

        String authorizePageUrl = auth.start();
        response.sendRedirect(authorizePageUrl);
    }

    public DbxRequestConfig getRequestConfig(HttpServletRequest request) {
        return new DbxRequestConfig(
                "JavaTutorial/1.0", Locale.getDefault().toString());
    }

    public String getUrl(HttpServletRequest request, String path) {
        URL requestUrl;
        try {
            requestUrl = new URL(request.getRequestURL().toString());
            return new URL(requestUrl, path).toExternalForm();
        } catch (MalformedURLException ex) {
            throw LangUtil.mkAssert("Bad URL", ex);
        }
    }

    @RequestMapping(value = "/dropbox-auth-finished", method = RequestMethod.GET)
    public String authenticated(HttpServletResponse response, HttpServletRequest request, ModelMap map) throws DbxException, IOException {

        DbxAuthFinish authFinish;
        String returnTo = "mydrive/mydrive";
        final String APP_KEY = "mt6yctmupgrodlm";
        final String APP_SECRET = "p68kiiyvdctftuv";

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        try {
            String redirectUrl = getUrl(request, "http://localhost:8080/FOU_1/dropbox-auth-finished");

            // Select a spot in the session for DbxWebAuth to store the CSRF token.
            HttpSession session = request.getSession();
            String sessionKey = "dropbox-auth-csrf-token";
            DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, sessionKey);

            auth = new DbxWebAuth(getRequestConfig(request), appInfo, redirectUrl, csrfTokenStore);
            authFinish = auth.finish(request.getParameterMap());

        } catch (DbxWebAuth.BadRequestException ex) {
            log("On /dropbox-auth-finish: Bad request: " + ex.getMessage());
            response.sendError(400);
            return returnTo;
        } catch (DbxWebAuth.BadStateException ex) {
            // Send them back to the start of the auth flow.
            response.sendRedirect("http://localhost:8080/FOU_1/dropbox-auth-finished" + ex.getMessage());
            return returnTo;
        } catch (DbxWebAuth.CsrfException ex) {
            log("On /dropbox-auth-finish: CSRF mismatch: " + ex.getMessage());
            return returnTo;
        } catch (DbxWebAuth.NotApprovedException ex) {
            
            return "redirect:my-drive/dropbox/home";
        } catch (DbxWebAuth.ProviderException ex) {
            log("On /dropbox-auth-finish: Auth failed: " + ex.getMessage());
            response.sendError(503, "Error communicating with Dropbox." + ex.getMessage());
            return returnTo;
        } catch (DbxException ex) {
            log("On /dropbox-auth-finish: Error getting token: " + ex.getMessage());
            response.sendError(503, "Error communicating with Dropbox." + ex.getMessage());
            return returnTo;
        }

        String accessToken = authFinish.accessToken;

        DbxClient client = new DbxClient(config, accessToken);

        MongoClient mongoClient = new MongoClient();
        DB db = mongoClient.getDB("fou");

        char[] pass = "mongo".toCharArray();
        boolean auth = db.authenticate("emime", pass);
        DBCollection collection = db.getCollection("users");
        if (auth == true) {
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", new BasicDBObject().append("dropbox_token", accessToken));

            BasicDBObject searchQuery = new BasicDBObject().append("id", request.getSession().getAttribute("id"));

            collection.update(searchQuery, newDocument);
        }

        map.addAttribute("title", "my-drive");
        request.getSession().setAttribute("dropbox_token", accessToken);
        response.sendRedirect("http://localhost:8080/FOU_1/my-drive/dropbox/home");
        return "mydrive/dropbox";
    }

    @RequestMapping(value = "/my-drive/dropbox/**", method = RequestMethod.GET)
    public String dropbox(HttpServletRequest request, ModelMap map) throws DbxException, IOException, Exception {
        String path = request.getServletPath().substring(request.getServletPath().indexOf("/my-drive/dropbox/") + 17);

        map.addAttribute("title", "dropbox");

        HttpSession session = request.getSession();

        if (request.getSession().getAttribute("dropbox_token") != null) {
            config = new DbxRequestConfig(
                    "JavaTutorial/1.0", Locale.getDefault().toString());
            DropboxEntityFactory dbxFactory = new DropboxEntityFactory();

            if (path.equals("/home")) {
                map.addAttribute("dropbox_entities", dbxFactory.getFilesAndFolders((String) session.getAttribute("id"), "/"));
            } else {
                map.addAttribute("dropbox_entities", dbxFactory.getFilesAndFolders((String) session.getAttribute("id"), URLDecoder.decode(path, "UTF-8") + "/"));
            }
            if (path.equals("/home")) {
                map.addAttribute("url_path", "home");
            } else {
                map.addAttribute("url_path", ("home" + URLDecoder.decode(path, "UTF-8")).split("/"));
            }
        }
        return "mydrive/dropbox";
    }

    @RequestMapping(value = "/my-drive/dropbox/downloads/**", method = RequestMethod.GET)
    public String getDropboxFile(HttpServletRequest request, HttpServletResponse response) throws IOException, DbxException {

        config = new DbxRequestConfig(
                "JavaTutorial/1.0", Locale.getDefault().toString());
        DbxClient client = new DbxClient(config, (String) request.getSession().getAttribute("dropbox_token"));

        String path = request.getServletPath().substring(request.getServletPath().indexOf("/my-drive/dropbox/downloads/") + 27);
        ServletOutputStream outputStream = response.getOutputStream();
        try {

            DbxEntry.File downloadedFile = client.getFile(path, null,
                    outputStream);
            String mimeType = "application/octet-stream";
            response.setContentType(mimeType);
            response.setContentLength((int) downloadedFile.numBytes);
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", downloadedFile.name);
        } finally {
            outputStream.close();
        }
        return "";
    }

    @RequestMapping(value = "/my-drive/search", method = RequestMethod.GET)
    public @ResponseBody
    List<FOUFile> getSearchedFile(HttpServletRequest request) throws UnknownHostException, Exception {

        String tag = request.getParameter("tag");
        HttpSession session = request.getSession();
        List<FOUFile> files = new ArrayList<>();

        String title = request.getParameter("url");
        if (title.equals("my-drive")) {
            MongoData mongoData = new MongoData();
            DB db = mongoData.getDB();
            DBCollection collection = db.getCollection(session.getAttribute("id") + "_files_meta");
            DBCursor cursor = collection.find();

            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                String[] tags = document.get("tags").toString().split(" ,");
                if (document.get("name").toString().toLowerCase().contains(tag.toLowerCase()) || tag.equals("all")) {
                    files.add(new MongoFile(document));
                } else {
                    for (String oneTag : tags) {
                        String[] inputTags = tag.split(" ");
                        for (String inputTag : inputTags) {
                            if (oneTag.trim().contains(inputTag)) {
                                files.add(new MongoFile(document));
                                break;
                            }
                        }
                    }
                }
            }
            return files;
        } else {
            MongoData mongoData = new MongoData();
            DB db = mongoData.getDB();
            DBCollection collection = db.getCollection(session.getAttribute("id") + "_dropbox_files_meta");
            DBCursor cursor = collection.find();

            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                //  String[] tags = document.get("tags").toString().split(" ,");
                if (document.get("name").toString().toLowerCase().contains(tag.toLowerCase()) || tag.equals("all")) {
                    files.add(new DropboxEntity(document));
                }/* else {
                 for (String oneTag : tags) {
                 String[] inputTags = tag.split(" ");
                 for (String inputTag : inputTags) {
                 if (oneTag.trim().contains(inputTag)) {
                 files.add(new MongoFile(document));
                 break;
                 }
                 }
                 }
                        
                 }*/

            }
            return files;
        }
    }

    @RequestMapping(value = "/my-drive/delete", method = RequestMethod.POST)
    public @ResponseBody
    String delete(HttpServletRequest request) throws UnknownHostException, Exception {
        MongoData mongoData = new MongoData();
        DB db = mongoData.getDB();
        DBCollection collection = db.getCollection(request.getSession().getAttribute("id") + "_files_meta");

        BasicDBObject document = new BasicDBObject();
        String fileID = request.getParameter("fileID").toString();
        document.put("file-id", new ObjectId(fileID));
        collection.remove(document);

        document = new BasicDBObject();
        GridFS gfs = new GridFS(db, request.getSession().getAttribute("id").toString() + "_files.files");
        document.put("_id", new ObjectId(fileID));
        gfs.remove(document);

        collection.remove(document);

        return fileID;
    }

    @RequestMapping(value = "/my-drive/dropbox/reload", method = RequestMethod.GET)
    public String reloadDropbox(HttpServletRequest request) throws IOException, DbxException, Exception {

        DbxClient client = new DbxClient(config, (String) request.getSession().getAttribute("dropbox_token"));
        DropBoxAuth dropBoxAuth = new DropBoxAuth();

        dropBoxAuth.authenticate((String) request.getSession().getAttribute("dropbox_token"));

        String userId = (String) request.getSession().getAttribute("id");

        dropBoxAuth.removeJunk(userId, client, "/");
        dropBoxAuth.fetchFilesAndFolders(userId, client, "/");

        return "redirect:/my-drive/dropbox/home";
    }

    @RequestMapping(value = "/my-drive/dropbox/edit", method = RequestMethod.POST)
    public @ResponseBody
    String editDropboxFile(HttpServletRequest request) throws IOException, DbxException, Exception {
        String tags = request.getParameter("tags");
        String description = request.getParameter("description");
        String path = request.getParameter("path");

        MongoData mongoData = new MongoData();
        DB db = mongoData.getDB();
        DBCollection collection = db.getCollection(request.getSession().getAttribute("id") + "_dropbox_files_meta");

        BasicDBObject document = new BasicDBObject();
        document.append("tags", tags);
        document.append("description", description);

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", document);
        BasicDBObject searchQuery = new BasicDBObject().append("path", path);
        collection.update(searchQuery, newDocument);

        return "success";
    }

    @RequestMapping(value = "/my-drive/dropbox/get-info", method = RequestMethod.GET)
    public @ResponseBody
    List<String> getDropboxFileInfo(HttpServletRequest request) throws IOException, DbxException, Exception {

        String path = request.getParameter("path");

        List<String> result = new ArrayList<>();

        MongoData mongoData = new MongoData();
        DB db = mongoData.getDB();
        DBCollection collection = db.getCollection(request.getSession().getAttribute("id") + "_dropbox_files_meta");

        DBCursor cursor = collection.find();

        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            if (document.get("path").equals(path)) {
                result.add((String) document.get("tags"));
                result.add((String) document.get("description"));
            }
        }
        return result;
    }

    @RequestMapping(value = "/my-drive/dropbox/delete", method = RequestMethod.POST)
    public @ResponseBody
    String deleteDropboxFile(HttpServletRequest request) throws DbxException, UnknownHostException, Exception {
        String path = request.getParameter("path");

        DbxRequestConfig config = new DbxRequestConfig(
                "JavaTutorial/1.0", Locale.getDefault().toString());
        DbxClient client = new DbxClient(config, (String) request.getSession().getAttribute("dropbox_token"));
        client.delete(path);
        MongoData mongoData = new MongoData();
        DB db = mongoData.getDB();
        DBCollection collection = db.getCollection(request.getSession().getAttribute("id") + "_dropbox_files_meta");

        BasicDBObject document = new BasicDBObject();
        document.put("path", path);
        collection.remove(document);

        return "sucessfull";
    }

    @RequestMapping(value = "/check-user-existence", method = RequestMethod.GET)
    public @ResponseBody
    boolean checkIfUserExists(HttpServletRequest request) throws IOException, DbxException, Exception {
        String username = (String) request.getParameter("username");
        MongoData mongoData = new MongoData();
        DB db = mongoData.getDB();
        DBCollection collection = db.getCollection("users");
        DBCursor cursor = collection.find();
        
        while(cursor.hasNext()) {
            DBObject doc = (DBObject) cursor.next();
            String retUsername = (String) doc.get("username");
            if (retUsername.equals(username))
                return true;
        }
        return false;
    }
}
