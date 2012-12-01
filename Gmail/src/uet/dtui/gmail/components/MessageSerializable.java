package uet.dtui.gmail.components;

import java.io.Serializable;

import uet.dtui.gmail.model.MessageEmail;

public class MessageSerializable implements Serializable{
	MessageEmail email;
	public void setEmail(MessageEmail mess) {
		email = mess;
	}
	
	public MessageEmail getEmail() {
		return email;
	}
}
