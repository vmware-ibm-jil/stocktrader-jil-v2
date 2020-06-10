package com.stocktrader.service.statement;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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
	private String defaultUserJohn = "Jhon";
	private String defaultUserBob = "Bob";
	private ArrayList<String> defaultOwners = new ArrayList<>();
	@PostConstruct
	public void init() {
		defaultOwners.add(defaultUserBob);
		defaultOwners.add(defaultUserJohn);
		
		for(String defaultOwner : defaultOwners) {
			try {
				statementService.addDefaultAccountStatement(acountStatement, defaultOwner);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@PostMapping("/trader/statement/add/{owner}")
	public String addServiceStatement(@PathVariable String owner, @RequestParam("title") String title, 
	  @RequestParam("file") MultipartFile file) throws IOException {
	    String id = statementService.addStatement(title, file, owner);
	    return id;
	}
//	Model model
//	@GetMapping("/statementservice/{ownername}")
//	public String getStatementService(@PathVariable String ownername, Model model) throws Exception {
//	    AccountStatement statement = statementService.getStatement(ownername);
//	    model.addAttribute("title", statement.getTitle());
//	    model.addAttribute("url", "/statementservice/stream/" + ownername);
//	    return model.getAttribute("url").toString();
//	}
	
	@GetMapping("/trader/statement/{ownername}")
	public void streamStatementService(@PathVariable String ownername, HttpServletResponse response) throws Exception {
	    AccountStatement statement = statementService.getStatement(ownername);
	    FileCopyUtils.copy(statement.getStream(), response.getOutputStream());        
	}

}
