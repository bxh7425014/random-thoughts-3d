package com.test.GridDrawable;

import java.io.InputStream;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.demo.Interface.ILoadTexImage;
import com.demo.Interface.LoadTexImage_ARGB;
import com.demo.Interface.LoadTexImage_Bitmap;
import com.demo.Interface.LoadTexImage_CDT;
import com.demo.launcher3d.R;
import com.demo.log.LogExt;
import com.demo.render.DisPlayList;
import com.demo.render.DisplayItem;
import com.demo.render.GridQuad;
import com.demo.render.RenderView;
import com.demo.render.Texture;
import com.demo.render.GridQuad.GridLocation;
import com.ophone.ogles.lib.Rotate4f;
import com.ophone.ogles.lib.Vector3f;

public class GridDrawable {
	public static void initDisplayItem(final RenderView renderView) {
		final DisPlayList disPlayList = DisPlayList.getInstance(); 
		final int ITEM_NUM = 5;
		for (int index = 0; index < ITEM_NUM; index++) {
			DisplayItem displayItem = new DisplayItem();
			ArrayList<GridQuad> gridQuads = displayItem.getGridQuads();
			
			Texture[] textures = new Texture[1];
			textures[0] = new Texture() {
				protected ILoadTexImage load() {
					Bitmap bmp = BitmapFactory.decodeStream(renderView.getResources().openRawResource(R.raw.bg));
					return new LoadTexImage_Bitmap(bmp);
				}
			};
			renderView.prime(textures[0], true);
			gridQuads.add(new FrameGrid(textures));
			
			// 给第一个item添加焦点框
			if (index == 0) {
				Texture[] textures0 = new Texture[1];
				textures0[0] = new Texture() {
					protected ILoadTexImage load() {
						Bitmap bmp = BitmapFactory.decodeStream(renderView.getResources().openRawResource(R.raw.frame_focus));
						return new LoadTexImage_Bitmap(bmp);
					}
				};
				renderView.prime(textures0[0], true);
				gridQuads.add(new FocusGrid(textures0));
			}
			
			Texture[] textures1 = new Texture[1];
			textures1[0] = new Texture() {
				protected ILoadTexImage load() {
					Bitmap bmp = BitmapFactory.decodeStream(renderView.getResources().openRawResource(R.raw.logo));
					return new LoadTexImage_Bitmap(bmp);
				}
			};
			renderView.prime(textures1[0], true);
			gridQuads.add(new LogoGrid(textures1));
			
			Texture[] textures2 = new Texture[1];
			textures2[0] = new Texture() {
				protected LoadTexImage_ARGB load() {
					InputStream input = renderView.getResources().openRawResource(R.raw.title);
					return new LoadTexImage_ARGB(input);
				}
			};
			renderView.prime(textures2[0], true);
			gridQuads.add(new TitleGrid(textures2));
			
			Texture[] textures3 = new Texture[1];
			textures3[0] = new Texture() {
				protected LoadTexImage_CDT load() {
					InputStream input = renderView.getResources().openRawResource(R.raw.test2);
					return new LoadTexImage_CDT(input);
				}
			};
			renderView.prime(textures3[0], true);
			gridQuads.add(new PosterGrid(textures3));
			Texture[] textures4 = new Texture[1];
			textures4[0] = new Texture() {
				protected LoadTexImage_CDT load() {
					InputStream input = renderView.getResources().openRawResource(R.raw.test);
					return new LoadTexImage_CDT(input);
				}
			};
			renderView.prime(textures4[0], true);
			gridQuads.add(new PosterGrid(textures4));
			Texture[] textures5 = new Texture[1];
			textures5[0] = new Texture() {
				protected LoadTexImage_CDT load() {
					InputStream input = renderView.getResources().openRawResource(R.raw.test2);
					return new LoadTexImage_CDT(input);
				}
			};
			renderView.prime(textures5[0], true);
			gridQuads.add(new PosterGrid(textures5));
			for (int index2 = 0; index2 < gridQuads.size(); index2++) {
				GridQuad gQuad = gridQuads.get(index2);
				GridLocation srcLocation = new GridLocation();
				srcLocation.SetPosition(new Vector3f(0, 10, 0));
				GridLocation desLocation = new GridLocation();
				desLocation.SetPosition(new Vector3f((float)index-1.0f, 0.0f, -(float)index-2.0f));
				desLocation.SetRotate(new Rotate4f(0, 0.0f, 0.0f, 1.0f));
				gQuad.setSrcLocation(srcLocation);
				gQuad.setDesLocation(desLocation);
			}
			disPlayList.add(displayItem);
			gridQuads = null;
		}
	}
	
	public static void freshDesLocation() {
		freshDesLocation(DisPlayList.getInstance().getFocusItem());
	}
	
	private static void freshDesLocation(int focusItem) {
		DisPlayList disPlayList = DisPlayList.getInstance();
		if ((focusItem < 0) || (focusItem > disPlayList.size() - 1)) {
			return;
		}
		for (int index = 0; index < disPlayList.size(); index++) {
			DisplayItem displayItem = disPlayList.get(index);
			ArrayList<GridQuad> gridQuads = displayItem.getGridQuads();
			for (int index2 = 0; index2 < gridQuads.size(); index2++) {
				int positionIndex = index - focusItem;
				if (positionIndex < 0) {
					positionIndex += disPlayList.size();
				}
				GridQuad gQuad = gridQuads.get(index2);
				GridLocation desLocation = new GridLocation();
				desLocation.SetPosition(new Vector3f((float)positionIndex-1.0f, 0.0f, -(float)positionIndex-2.0f));
				desLocation.SetRotate(new Rotate4f(0, 0.0f, 0.0f, 1.0f));
				gQuad.setDesLocation(desLocation);
			}
		}
	}
}
