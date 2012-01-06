/**
 * Copyright (C) 2011-2012, FMSoft.Launcher.Helper
 * 
 * Model:
 *   Load all application's icon; 
 *   Read layout from EspierLauncher;
 *   Check untracked icon;
 *   
 *   Build icon list for adapter.
 * 
 * @author nxliao
 */
package cn.fmsoft.launcher2.helper;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.content.Intent;

public class IconModel {
	static final boolean DEBUG = true;
	static final String DBG_TAG = "IconModel";
	
	private Context mContext;
	
	private NodeInfo mNodeInfo;
	
	private HashMap<Intent, ItemInfo> mAllApps;
	private HashMap<Intent, ItemInfo> mShortcut;
	private HashMap<Intent, ItemInfo> mUntracked;

	public IconModel(Context context) {
		mContext = context;

		mAllApps = new HashMap<Intent, ItemInfo>();
		mShortcut = new HashMap<Intent, ItemInfo>();
		mUntracked = new HashMap<Intent, ItemInfo>();
		
		mNodeInfo = new NodeInfo();
	}
	
	public NodeInfo getIconList() {
		return mNodeInfo;
	}

	public boolean rebuildIconTree(int type) {
		return true;
	}
	
	
	
	public void startLoad() {
		loadEspierLauncher();
	}
	
	
	private void loadEspierLauncher() {
		ArrayList<ItemInfo> list = new EspierLauncherLoader().load(mContext);
	}
}
