package com.demo.Interface;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.nio.ByteBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.opengl.ETC1Util;
import android.opengl.GLES10;
import android.opengl.GLES11Ext;

import com.demo.log.LogExt;
import com.demo.render.RenderView;
import com.demo.render.Texture;
import com.demo.render.TextureReference;
import com.demo.utils.DirectLinkedList;

public class LoadTexImage_ETC implements ILoadTexImage {
	private InputStream mETCStream;
	public LoadTexImage_ETC(InputStream in) {
		mETCStream = in;
	}
	@Override
	public void loadTextureAsync(Texture texture) {
		// TODO Auto-generated method stub

	}

	@Override
	public void uploadTexture(GL11 gl, Texture texture, int[] textureId,
			DirectLinkedList<TextureReference> mActiveTextureList,
			ReferenceQueue unRefQueue, RenderView renderView) {
		int glError = GL11.GL_NO_ERROR;
		if (mETCStream != null) {
			final int width = texture.mWidth;
			final int height = texture.mHeight;

			// Define a vertically flipped crop rectangle for OES_draw_texture.
			int[] cropRect = { 0, height, width, -height };

			// Upload the bitmap to a new texture.
			gl.glGenTextures(1, textureId, 0);
			gl.glBindTexture(GL11.GL_TEXTURE_2D, textureId[0]);
			gl.glTexParameteriv(GL11.GL_TEXTURE_2D,
					GL11Ext.GL_TEXTURE_CROP_RECT_OES, cropRect, 0);
			gl.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
					GL11.GL_CLAMP_TO_EDGE);
			gl.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
					GL11.GL_CLAMP_TO_EDGE);
//			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);	
//			gl.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_LINEAR);
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
					GL11.GL_LINEAR);
			LoadETC1Texture(mETCStream, gl);
			try {
				mETCStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			glError = gl.glGetError();
			if (glError == GL11.GL_OUT_OF_MEMORY) {
				handleLowMemory();
			}
			if (glError != GL11.GL_NO_ERROR) {
				// There was an error, we need to retry this texture at some
				// later time
				LogExt.Log(this, Thread.currentThread().getStackTrace(),
						"Texture creation fail, glError " + glError);
				texture.mId = 0;
				mETCStream = null;
				texture.mState = Texture.STATE_UNLOADED;
			} else {
				// Update texture state.
				mETCStream = null;
				texture.mId = textureId[0];
				texture.mState = Texture.STATE_LOADED;

				// Add to the active list.
				final TextureReference textureRef = new TextureReference(
						texture, gl, unRefQueue, textureId[0]);
				mActiveTextureList.add(textureRef.activeListEntry);
				texture.bindTextureReference(textureRef);				
				renderView.requestRender();
			}
		} else {
			texture.mState = Texture.STATE_ERROR;
		}
	}

	@Override
	public void handleLowMemory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void recycle() {
		if(mETCStream != null) {
			try {
				mETCStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mETCStream = null;
		}
	}
	/**
	 * @author bianxh
	 * @param input
	 * @param gl
	 */
	private void LoadETC1Texture(InputStream input, GL11 gl) {
		LogExt.Log(this, Thread.currentThread().getStackTrace(), 
				"ETC1 texture support: " + ETC1Util.isETC1Supported());
		try {
			ETC1Util.loadTexture(GLES10.GL_TEXTURE_2D, 0, 0, GLES10.GL_RGB,
					GLES10.GL_UNSIGNED_SHORT_5_6_5, input);
		} catch (IOException e) {
			LogExt.Log(this, Thread.currentThread().getStackTrace(),
					"Could not load texture: " + e);
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// ignore exception thrown from close.
			}
		}
	}
}
