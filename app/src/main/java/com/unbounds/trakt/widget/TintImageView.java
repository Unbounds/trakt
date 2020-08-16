package com.unbounds.trakt.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by maclir on 2015-11-21.
 */
public class TintImageView extends AppCompatImageView {

	private ColorStateList tint;

	public TintImageView(final Context context) {
		super(context);
	}

	public TintImageView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public TintImageView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		if (tint != null && tint.isStateful())
			updateTintColor();
	}

	private void updateTintColor() {
		final int color = tint.getColorForState(getDrawableState(), 0);
		setColorFilter(color);
	}

}
