package uet.dtui.gmail.model;


public class ItemMenuFolder {
	public int image;
	public int imageName;
	public FolderEmail folder;
	
	public ItemMenuFolder(int image, int nameFolder, FolderEmail folderEmail) {
		this.image = image;
		this.imageName = nameFolder;
		this.folder = folderEmail;
	}
}
