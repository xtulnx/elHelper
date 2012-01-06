package cn.fmsoft.launcher2.helper;

import java.net.URISyntaxException;
import java.util.ArrayList;

import cn.fmsoft.launcher2.LauncherSettings;
import cn.fmsoft.launcher2.ShortcutInfo;
import cn.fmsoft.launcher2.util.LogUtil;
import cn.fmsoft.launcher2.util.Utilities;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

public class EspierLauncherLoader {
	static final boolean DEBUG = true;
	static final String DBG_TAG = "EspierLauncherLoader";
	
	static final String EL_AUTHORITY = "cn.fmsoft.launcher2.settings";
	static final String EL_TABLE_FAVORITES = "favorites";
	static final Uri EL_CONTENT_URI = Uri.parse("content://" + EL_AUTHORITY
			+ "/" + EL_TABLE_FAVORITES);


    public static final String _ID = "_id";
    static final String TITLE = "title";
    static final String INTENT = "intent";
    static final String ITEM_TYPE = "itemType";
    static final int ITEM_TYPE_APPLICATION = 0;
    static final int ITEM_TYPE_SHORTCUT = 1;
    static final int ITEM_TYPE_USER_FOLDER = 2;
    static final int ITEM_TYPE_LIVE_FOLDER = 3;
    static final int ITEM_TYPE_APPWIDGET = 4;
    static final String ICON_TYPE = "iconType";
    static final int ICON_TYPE_RESOURCE = 0;
    static final int ICON_TYPE_BITMAP = 1;
    static final String ICON_PACKAGE = "iconPackage";
    static final String ICON_RESOURCE = "iconResource";
    /**
     * The custom icon bitmap, if icon type is ICON_TYPE_BITMAP.
     * <P>Type: BLOB</P>
     */
    static final String ICON = "icon";
    static final String CONTAINER = "container";
    static final String SCREEN = "screen";
    static final String CELLX = "cellX";
    static final String CELLY = "cellY";
    static final String SPANX = "spanX";
    static final String SPANY = "spanY";
    static final String APPWIDGET_ID = "appWidgetId";
    static final String URI = "uri";

    /**
     * The display mode if the item is a live folder.
     * <P>Type: INTEGER</P>
     *
     * @see android.provider.LiveFolders#DISPLAY_MODE_GRID
     * @see android.provider.LiveFolders#DISPLAY_MODE_LIST
     */
    static final String DISPLAY_MODE = "displayMode";
    
    static final String INSTALL_TIME = "installTime";
    
    static final String TASKBAR_ORDER = "taskbarOrder";
    
    private boolean mStopped;

	ArrayList<ItemInfo> load(Context context) {
		final Cursor c = context.getContentResolver().query(EL_CONTENT_URI,
				null, null, null, null);
				
		ArrayList<ItemInfo> items = null;

		try {
			final int idIndex = c.getColumnIndexOrThrow(_ID);
			final int intentIndex = c.getColumnIndexOrThrow(INTENT);
			final int titleIndex = c.getColumnIndexOrThrow(TITLE);
			final int iconTypeIndex = c.getColumnIndexOrThrow(ICON_TYPE);
			final int iconIndex = c.getColumnIndexOrThrow(ICON);
			final int iconPackageIndex = c.getColumnIndexOrThrow(ICON_PACKAGE);
			final int iconResourceIndex = c
					.getColumnIndexOrThrow(ICON_RESOURCE);
			final int containerIndex = c.getColumnIndexOrThrow(CONTAINER);
			final int itemTypeIndex = c.getColumnIndexOrThrow(ITEM_TYPE);
			final int appWidgetIdIndex = c.getColumnIndexOrThrow(APPWIDGET_ID);
			final int screenIndex = c.getColumnIndexOrThrow(SCREEN);
			final int cellXIndex = c.getColumnIndexOrThrow(CELLX);
			final int cellYIndex = c.getColumnIndexOrThrow(CELLY);
			final int spanXIndex = c.getColumnIndexOrThrow(SPANX);
			final int spanYIndex = c.getColumnIndexOrThrow(SPANY);
			final int uriIndex = c.getColumnIndexOrThrow(URI);
			final int displayModeIndex = c.getColumnIndexOrThrow(DISPLAY_MODE);
			final int installTimeIndex = c.getColumnIndexOrThrow(INSTALL_TIME);
			final int taskbarOrderIndex = c
					.getColumnIndexOrThrow(TASKBAR_ORDER);
			
			if (c.getCount() > 0) {
				if (DEBUG) {
					Log.i(DBG_TAG, "count = " + c.getCount());
				}
				items = new ArrayList<ItemInfo>(c.getCount());
			}
				
			String intentDescription;
			int container;
			long id;
			Intent intent;

			while (!mStopped && c.moveToNext()) {
				try {
					int itemType = c.getInt(itemTypeIndex);
					switch (itemType) {
					case ITEM_TYPE_APPLICATION:
					case ITEM_TYPE_SHORTCUT:
                        intentDescription = c.getString(intentIndex);
                        try {
                            intent = Intent.parseUri(intentDescription, 0);
                        } catch (URISyntaxException e) {
                            continue;
                        }

                        info = getShortcutInfo(c, context, iconTypeIndex,
                                iconPackageIndex, iconResourceIndex, iconIndex,
                                titleIndex);

                        if (info != null) {
                        	info.itemType = itemType;
                            updateSavedIcon(context, info, c, iconIndex);
                            
                            info.mIntent = intent;
                            info.id = c.getLong(idIndex);
                            container = c.getInt(containerIndex);
                            info.container = container;
                            info.screen = c.getInt(screenIndex);
                            info.cellX = c.getInt(cellXIndex);
                            info.cellY = c.getInt(cellYIndex);
                            info.installTime = c.getLong(installTimeIndex);
                            info.mTaskbarOrder = c.getLong(taskbarOrderIndex);
                            
                            // check & update map of what's occupied
                            if (!checkItemPlacement(occupied, info)) {
                            	deleteItemFromDatabase(mContext,info);
                                break;
                            }

                            switch (container) {
                            case LauncherSettings.Favorites.CONTAINER_DESKTOP:
                                mItems.add(info);
                                mAllShortcutItems.add(info);
                                break;
                            default:
                            	mAllShortcutItems.add(info);
                                break;
                            }
                            shortcutNum++;
                        } else {
                            // Failed to load the shortcut, probably because the
                            // activity manager couldn't resolve it (maybe the app
                            // was uninstalled), or the db row was somehow screwed up.
                            // Delete it.
                            id = c.getLong(idIndex);
                            LogUtil.e(TAG, "Error loading shortcut " + id + ", removing it");
                            contentResolver.delete(LauncherSettings.Favorites.getContentUri(
                                        id, false), null, null);
                        }
						break;

					case ITEM_TYPE_USER_FOLDER:
						break;

					case ITEM_TYPE_APPWIDGET:
						break;
					}
				} catch (Exception e) {
					Log.w(DBG_TAG, "loading interrupted:", e);
				}
			}
		} finally {
			c.close();
		}
		
		return items;
	}
	
	   private ItemInfo getShortcutInfo(Cursor c, Context context,
	            int iconTypeIndex, int iconPackageIndex, int iconResourceIndex, int iconIndex,
	            int titleIndex) {

	        Bitmap icon = null;
	        final ItemInfo info = new ItemInfo();
	        info.itemType = ITEM_TYPE_SHORTCUT;

	        // TODO: If there's an explicit component and we can't install that, delete it.

	        info.setTitle(c.getString(titleIndex));

	        int iconType = c.getInt(iconTypeIndex);
	        switch (iconType) {
	        case LauncherSettings.Favorites.ICON_TYPE_RESOURCE:
	            String packageName = c.getString(iconPackageIndex);
	            String resourceName = c.getString(iconResourceIndex);
	            PackageManager packageManager = context.getPackageManager();
	            info.customIcon = false;
	            // the resource
	            try {
	                Resources resources = packageManager.getResourcesForApplication(packageName);
	                if (resources != null) {
	                    final int id = resources.getIdentifier(resourceName, null, null);
	                    icon = Utilities.createIconBitmap(resources.getDrawable(id), context, packageName.hashCode());
	                }
	            } catch (Exception e) {
	                // drop this.  we have other places to look for icons
	            }
	            // the db
	            if (icon == null) {
	                icon = getIconFromCursor(c, iconIndex);
	            }
	            // the fallback icon
	            if (icon == null) {
	                icon = getFallbackIcon();
	                info.usingFallbackIcon = true;
	            }
	            break;
	        case LauncherSettings.Favorites.ICON_TYPE_BITMAP:
	            icon = getIconFromCursor(c, iconIndex);
	            if (icon == null) {
	                icon = getFallbackIcon();
	                info.customIcon = false;
	                info.usingFallbackIcon = true;
	            } else {
	                info.customIcon = true;
	            }
	            break;
	        default:
	            icon = getFallbackIcon();
	            info.usingFallbackIcon = true;
	            info.customIcon = false;
	            break;
	        }
	        info.setIcon(icon);
	        return info;
	    }
}
