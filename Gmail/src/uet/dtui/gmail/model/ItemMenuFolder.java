package uet.dtui.gmail.model;

import javax.mail.Folder;

public class ItemMenuFolder {
	public int image;
	public Folder folder;
	
	public ItemMenuFolder(int image, Folder folder) {
		this.image = image;
		this.folder = folder;
	}
}
