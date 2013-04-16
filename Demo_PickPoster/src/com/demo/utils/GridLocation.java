package com.demo.utils;

import com.ophone.ogles.lib.Vector3f;
import com.ophone.ogles.lib.Vector4f;

/**
 * @author bianxh
 * 存放Grid的位置信息
 */
public class GridLocation {
	private Vector3f mLocation = new Vector3f(0, 0, 0);
	private Vector4f mRotate = new Vector4f((float) Math.PI / 2, 0, 1, 0); // (x,y,z,angle)
	public void SetLocation(Vector3f vector3f) {
		mLocation.set(vector3f);
	}
	
	public Vector3f getLocation() {
		return mLocation;
	}
	
	public void SetRotate(Vector4f vector4f) {
		mRotate.set(vector4f);
	}
	
	public Vector4f getRotate() {
		return mRotate;
	}
	
	public boolean equals(GridLocation desLocation) {
		final float THRESOLD = (float)10e-3;
		Vector3f vector3f = desLocation.getLocation();
		Vector4f vector4f = desLocation.getRotate();
		if (((mLocation.x - vector3f.x) <= THRESOLD)
			&& ((mLocation.y - vector3f.y) <= THRESOLD) 
			&& ((mLocation.z - vector3f.z) <= THRESOLD) 
			&& ((mRotate.w - vector4f.w) <= THRESOLD)
			&& ((mRotate.x - vector4f.x) <= THRESOLD) 
			&& ((mRotate.y - vector4f.y) <= THRESOLD)
			&& ((mRotate.z - vector4f.z) <= THRESOLD)) {
			return true;
		} else {
			return false;
		}
	}
}
