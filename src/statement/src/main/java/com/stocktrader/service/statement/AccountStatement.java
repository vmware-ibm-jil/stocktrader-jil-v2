package com.stocktrader.service.statement;

import java.io.InputStream;

public class AccountStatement {
    private String title;
    private String owner;
    private InputStream stream;
	
    public String getTitle() {
		return title;
	}
	
    public void setTitle(String title) {
		this.title = title;
	}
    
    public String getOwner() {
		return owner;
	}
	
    public void setOwner(String owner) {
		this.owner = owner;
	}
	
    public InputStream getStream() {
		return stream;
	}
	
    public void setStream(InputStream stream) {
		this.stream = stream;
	}
}