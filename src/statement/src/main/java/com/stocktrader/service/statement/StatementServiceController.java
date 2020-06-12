package com.stocktrader.service.statement;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController

public class StatementServiceController {

    @Autowired
    AccountStatementService statementService;
    private String acountStatement = "Account Statement";
    private String statement = "statement";

    @PostConstruct
    public void init() {
        try {
            statementService.addDefaultAccountStatement(acountStatement, statement);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @PostMapping("/trader/statement/add/{owner}")
    public String uploadStatement(@PathVariable String owner, @RequestParam("title") String title,
                                      @RequestParam("file") MultipartFile file) throws IOException {
        String id = statementService.addStatement(title, file, owner);
        return id;
    }

    @GetMapping("/trader/statement/{ownername}")
    public void getStatement(@PathVariable String ownername, HttpServletResponse response, @RequestParam String startDate, @RequestParam String endDate) throws Exception {
        AccountStatement statement = statementService.getStatement(ownername);
        FileCopyUtils.copy(statement.getStream(), response.getOutputStream());
    }
}
