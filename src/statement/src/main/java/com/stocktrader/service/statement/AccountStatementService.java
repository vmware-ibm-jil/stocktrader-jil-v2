package com.stocktrader.service.statement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

@Service
public class AccountStatementService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations operations;

    public String addDefaultAccountStatement(String title, String ownerName) throws IOException {
//        Resource resource = new ClassPathResource("classpath:" + ownerName + ".pdf");
//        InputStream inputStream = resource.getInputStream();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(ownerName + ".pdf");
        MultipartFile multipartFile = new MockMultipartFile(ownerName + ".pdf", inputStream);
        DBObject metaData = new BasicDBObject();
        metaData.put("owner", ownerName);
        metaData.put("title", title);
        gridFsTemplate.delete(new Query(Criteria.where("filename").is(ownerName + ".pdf")));
        ObjectId id = gridFsTemplate.store(
        		multipartFile.getInputStream(), ownerName +".pdf", multipartFile.getContentType(), metaData); 
        return id.toString(); 
    }


    public String addStatement(String title, MultipartFile file, String ownerName) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("owner", ownerName);
        metaData.put("title", title);
        gridFsTemplate.delete(new Query(Criteria.where("filename").is(ownerName + ".pdf")));
        ObjectId id = gridFsTemplate.store(
                file.getInputStream(), ownerName + ".pdf", file.getContentType(), metaData);
        return id.toString();
    }

    public AccountStatement getStatement(String ownerName) throws IllegalStateException, IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(ownerName + ".pdf")));
        if (file == null) // Get default statement if not added
            file = gridFsTemplate.findOne(new Query(Criteria.where("filename").is("statement.pdf")));
        AccountStatement statement = new AccountStatement();
        statement.setTitle(file.getMetadata().get("title").toString());
        statement.setStream(operations.getResource(file).getInputStream());
        statement.setOwner(file.getMetadata().get("owner").toString());
        return statement;
    }
}