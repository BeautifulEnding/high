/**
 * �������е�Activity�Ա���ȫ�˳�����͵õ�Activity��ʵ��
 */
package com.example.asus.activity;

import android.app.Activity;

import java.util.HashMap;

public class ManageActivity {
	private static HashMap allActiviy=new HashMap<String,Activity>();

	public static void addActiviy(String name, Activity activity){
		allActiviy.put(name, activity);
	}
	
	public static Activity getActivity(String name){
		return (Activity)allActiviy.get(name);
	}
}
