package uet.dtui.gmail.components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class ClearableEditText extends EditText {
	private Drawable btnClear;
	private boolean clearable = false;

	public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public ClearableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public ClearableEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP && btnClear != null) {
			final int actionX = (int) event.getX();
			if (actionX > this.getWidth() - btnClear.getIntrinsicWidth() - 10) {
				this.setText("");
				event.setAction(MotionEvent.ACTION_CANCEL);
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void setCompoundDrawablesWithIntrinsicBounds(int left, int top,
			int right, int bottom) {
		if (right != 0) {
			btnClear = getResources().getDrawable(right);
			btnClear.setBounds(0, 0, btnClear.getIntrinsicWidth(), btnClear.getIntrinsicHeight());
		}
		super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
	}

	public void setClearable(boolean bool) {
		clearable = bool;
	}
	
	public boolean getClearable() {
		return clearable;
	}
}
