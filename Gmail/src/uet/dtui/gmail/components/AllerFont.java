package uet.dtui.gmail.components;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

public class AllerFont {
	private static final String TAG = "Typefaces";
	public static final String ALLER_BOLD = "fonts/Aller_bd.TTF";
	public static final String ALLER_BOLDLIGHT = "fonts/Aller_bdlt.TTF";
	public static final String ALLER_LIGHT = "fonts/Aller_lt.TTF";
	public static final String ALLER_LIGHTITALIC = "fonts/Aller_Ltlt.TTF";
	public static final String ALLER_REGULAR = "fonts/Aller_Rg.TTF";
	public static final String ALLERDISPLAY = "fonts/AllerDisplay.TTF";
	
	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();
	
	public static Typeface get(Context c, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(),
                            assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
                    Log.e(TAG, "Could not get typeface '" + assetPath
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }
}
