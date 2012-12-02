package uet.dtui.gmail.components;

import java.util.List;

import uet.dtui.gmail.model.MessageEmail;
import android.os.Parcel;
import android.os.Parcelable;

public class ListParcelable implements Parcelable{
	
	public List<MessageEmail> list;
	
	public ListParcelable(List<MessageEmail> mail_list){
		list = mail_list;
	}
	
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
