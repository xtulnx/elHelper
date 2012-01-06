package cn.fmsoft.launcher2.helper;

import java.util.ArrayList;
import java.util.List;

public class NodeInfo {

	ItemInfo mInfo;

	List<NodeInfo> mChildren;
	
	public NodeInfo() {
		mChildren = new ArrayList<NodeInfo>();
	}
	
	public int getChildCount() {
		return mChildren.size();
	}

	public NodeInfo getChildAt(int index) {
		return mChildren.get(index);
	}
}
