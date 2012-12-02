package uet.dtui.gmail.components;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uet.dtui.gmail.model.MessageEmail;
public class ListSerializable implements Serializable{
	private ArrayList<MessageEmail> arrayNumber;
	public ListSerializable(List<MessageEmail> list) {
		this.arrayNumber = (ArrayList<MessageEmail>) list;
	}
	
	public ArrayList<MessageEmail> getList() {
		return arrayNumber;
	}
	public ArrayList toArrayList(){
		return (ArrayList) arrayNumber;
	}
}
