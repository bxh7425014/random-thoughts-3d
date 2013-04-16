package com.demo.Interface;

import java.lang.ref.ReferenceQueue;
import javax.microedition.khronos.opengles.GL11;
import com.demo.render.RenderView;
import com.demo.render.Texture;
import com.demo.render.TextureReference;
import com.demo.utils.DirectLinkedList;

public interface ILoadTexImage{
	void loadTextureAsync(Texture texture);	// 释放(加工)texture纹理数据
	void uploadTexture(GL11 gl, Texture texture, int[] textureId, 
			DirectLinkedList<TextureReference> mActiveTextureList, 
			ReferenceQueue unRefQueue, RenderView renderView);
	void handleLowMemory();
	void recycle();
}
