package uet.dtui.gmail.components;

import android.content.Context;
import android.graphics.Typeface;

public class AllerFontFamily {
	public static Typeface AllerBold(Context context) {
		return Typeface.createFromAsset(context.getAssets(), "fonts/Aller_bd.TTF");
	}
	
	public static Typeface AllerBoldLight(Context context) {
		return Typeface.createFromAsset(context.getAssets(), "fonts/Aller_bdlt.TTF");
	}
	
	public static Typeface AllerLight(Context context) {
		return Typeface.createFromAsset(context.getAssets(), "fonts/Aller_lt.TTF");
	}
	
	public static Typeface AllerLightItalic(Context context) {
		return Typeface.createFromAsset(context.getAssets(), "fonts/Aller_Ltlt.TTF");
	}

	public static Typeface AllerRegular(Context context) {
		return Typeface.createFromAsset(context.getAssets(), "fonts/Aller_Rg.TTF");
	}
	
	public static Typeface AllerDisplay(Context context) {
		return Typeface.createFromAsset(context.getAssets(), "fonts/AllerDisplay.TTF");
	}
}
