package com.spring.tutorial.controllers;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
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
        map.addAttribute("title", "index");
        return "index";
    }

    @RequestMapping(value = "/my-drive", method = RequestMethod.GET)
    public String myDrive(ModelMap map, HttpServletRequest request) {
        map.addAttribute("title", "my-drive");
        try {

            MongoFileManager fileManager = new MongoFileManager(request.getSession().getAttribute("username").toString());
            map.addAttribute("files", fileManager.getFiles());

        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }

        return "mydrive/mydrive";
    }

    @RequestMapping(value = "/files/{id}", method = RequestMethod.GET)
    public String getFile(@PathVariable("id") String id, ModelMap map,
            HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {

        String username = request.getSession().getAttribute("username").toString();
        MongoData data = new MongoData();
        GridFS collection = new GridFS(data.getDB(), username + "_files");

        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        GridFSDBFile file = collection.findOne(query);

        DBCollection metaFileCollection = data.getDB().getCollection(username + "_files_meta");
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

        String redirectUri = "http://127.0.0.1:8080/FOU_1/dropbox-auth-finished";
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
            String redirectUrl = getUrl(request, "http://127.0.0.1:8080/FOU_1/dropbox-auth-finished");

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
            response.sendRedirect("http://127.0.0.1:8080/FOU_1/my-drive" + ex.getMessage());
            return returnTo;
        } catch (DbxWebAuth.CsrfException ex) {
            log("On /dropbox-auth-finish: CSRF mismatch: " + ex.getMessage());
            return returnTo;
        } catch (DbxWebAuth.NotApprovedException ex) {

            return returnTo;
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

            BasicDBObject searchQuery = new BasicDBObject().append("username", request.getSession().getAttribute("username"));

            collection.update(searchQuery, newDocument);
        }

        map.addAttribute("title", "my-drive");
        request.getSession().setAttribute("dropbox_token", accessToken);
        return "mydrive/dropbox";
    }

    @RequestMapping(value = "/my-drive/dropbox/{path}", method = RequestMethod.GET)
    public String dropbox(@PathVariable ("path") String path, HttpServletRequest request, ModelMap map) throws DbxException, IOException {
        map.addAttribute("title", "dropbox");
        if (!request.getSession().getAttribute("dropbox_token").equals("")) {
            config = new DbxRequestConfig(
                    "JavaTutorial/1.0", Locale.getDefault().toString());
            DbxClient client = new DbxClient(config, (String) request.getSession().getAttribute("dropbox_token"));
            DropBoxAuth dropBoxAuth = new DropBoxAuth();
            dropBoxAuth.authenticate((String) request.getSession().getAttribute("dropbox_token"));
            if (path.equals("user-all"))
               map.addAttribute("dropbox_entities", dropBoxAuth.getFilesAndFolders(client, "/"));
            else 
                map.addAttribute("dropbox_entities", dropBoxAuth.getFilesAndFolders(client, "/" + URLDecoder.decode(path)));
            map.addAttribute("dropbox_username", client.getAccountInfo().displayName);
        }
        return "mydrive/dropbox";
    }

}