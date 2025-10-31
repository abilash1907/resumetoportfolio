package com.api.portfolio_java_services.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Request {
	private boolean stream;
	private List<MessagesItem> messages;
	private String model;

	public void setStream(boolean stream){
		this.stream = stream;
	}
	public boolean isStream(){
		return stream;
	}

	public void setMessages(List<MessagesItem> messages){
		this.messages = messages;
	}

	public List<MessagesItem> getMessages(){
		return messages;
	}

	public void setModel(String model){
		this.model = model;
	}

	public String getModel(){
		return model;
	}
}