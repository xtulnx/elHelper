/**
 * Copyright (C) 2011-2012, FMSoft.Launcher.Helper
 * 
 * ItemInfo:
 *   Include all icon's information;
 * 
 * @author nxliao
 */
package cn.fmsoft.launcher2.helper;

import android.content.Intent;
import android.graphics.Bitmap;

public class ItemInfo {
	public CharSequence mTitle;
	public Bitmap mIcon;

	public int mScreen;
	public int mIndex;
	public long id;
	public int itemType;
	public boolean	customIcon;
	public boolean	usingFallbackIcon;
	public Intent	mIntent;
	public int	container;
	public int	screen;
	public int	cellX;
	public int	cellY;
	public long	installTime;
	public long	mTaskbarOrder;
	public void setIcon(Bitmap icon) {
		// TODO Auto-generated method stub
		mIcon = icon;
	}
	public void setTitle(CharSequence str) {
		mTitle = str;
	}
}
