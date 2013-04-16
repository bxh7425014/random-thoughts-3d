package com.demo.log;
import android.util.Log;

public class LogExt {
	private static final String TAG="demo";

	private static void Log(Object obj, String functionName, String strInfo) {
		if (obj == null) {
			Log.e(TAG, "Input obj Error(null)");
		} else {
			LogExt.Log(obj.getClass(), functionName, strInfo);
		}
	}
	
	private static void Log(Object obj, String functionName) {
		if (obj == null) {
			Log.e(TAG, "Input obj Error(null)");
		} else {
			LogExt.Log(obj.getClass(), functionName, null);
		}
	}
	
	private static void Log(Class cls, String functionName, String strInfo){
		if(!CanDebug()) {
			return ;
		}
		
		if (cls == null) {
			Log.e(TAG, "Input Class Error(null)");
		} else {
			if (strInfo == null) {
				Log.i(TAG, cls.toString() + "::" + functionName);
			} else {
				Log.i(TAG, cls.toString() + "::" + functionName + "(), " + strInfo);
			}
		}
	}
	
	public static void Log(Object obj, StackTraceElement[] stackTraceElement) {
		String functionName = stackTraceElement[2].getMethodName();
		Log(obj, functionName);
	}
	
	public static void Log(Object obj, StackTraceElement[] stackTraceElement, String strInfo) {
		String functionName = stackTraceElement[2].getMethodName();
		Log(obj, functionName, strInfo);
	}
	
	private static boolean CanDebug(){return bEnableDebug;};
	private static boolean bEnableDebug = true;
}
