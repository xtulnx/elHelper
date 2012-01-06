/**
 * Copyright (C) 2011-2012, FMSoft.Launcher.Helper
 * 
 * Adapter.
 * 
 * @author nxliao
 */
package cn.fmsoft.launcher2.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class ExpandableAdapterIcon extends BaseExpandableListAdapter{
	private Context mContext;
	private NodeInfo mTR;

	public ExpandableAdapterIcon(Context context, NodeInfo root) {
		mContext = context;
		mTR = root;
	}

	@Override
	public int getGroupCount() {
		return mTR.getChildCount();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		final NodeInfo nodeInfo = mTR.getChildAt(groupPosition);
		if (nodeInfo != null) {
			return nodeInfo.getChildCount();
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mTR.getChildAt(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		final NodeInfo nodeInfo = mTR.getChildAt(groupPosition);
		if (nodeInfo != null) {
			return nodeInfo.getChildAt(childPosition);
		}
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
