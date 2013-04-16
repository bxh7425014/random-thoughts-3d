package com.demo.main;

import java.util.ArrayList;

import com.demo.render.DisPlayList;
import com.demo.render.RenderView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	private RenderView mRenderView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRenderView = new RenderView(this);
		setContentView(mRenderView);
    }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		DisPlayList.getInstance().clear();
	}
}