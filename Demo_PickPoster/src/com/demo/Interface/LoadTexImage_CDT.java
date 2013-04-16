package com.demo.Interface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.nio.ByteBuffer;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL10Ext;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;
import android.opengl.GLES11Ext;
import android.opengl.GLUtils;

import com.demo.log.LogExt;
import com.demo.render.RenderView;
import com.demo.render.Texture;
import com.demo.render.TextureReference;
import com.demo.utils.DirectLinkedList;

public class LoadTexImage_CDT implements ILoadTexImage{
	private InputStream mCDTStream;
	private ByteArrayOutputStream mBytestream;
	public LoadTexImage_CDT(InputStream stream) {
		mCDTStream = stream;
	}
	@Override
	public void loadTextureAsync(Texture texture) {
		InputStream input = mCDTStream;
		try {
			int ch;
			int width = 0;
			int height = 0;
			int index = 0;
			StringBuilder sb = new StringBuilder();
			mBytestream = new ByteArrayOutputStream();
			while ((ch = input.read()) != -1) {
				// 从cdt文件中读取纹理的宽高数据
				if (index < 8) {
					sb.insert(0, Integer.toHexString(ch));
					if (ch < 16) {
						sb.insert(0, "0");
					}
					if (index == 3) {
						width = Integer.valueOf(sb.toString(), 16);
						sb.delete(0, sb.length());
					} else if (index == 7) {
						height = Integer.valueOf(sb.toString(), 16);
					}
					index ++;
				} else {
					mBytestream.write(ch);
				}
			}
			sb = null;
			texture.mWidth = width;
			texture.mHeight = height;
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			mCDTStream = null;
		} catch (OutOfMemoryError eMem) {
			LogExt.Log(this, Thread.currentThread().getStackTrace(),
					"texImage power of 2 creation fail, outofmemory");
			handleLowMemory();
		} finally {
			mCDTStream = null;
		}
		LogExt.Log(this, Thread.currentThread().getStackTrace(), "mBytestream = " + mBytestream);
	}

	@Override
	public void uploadTexture(GL11 gl, Texture texture, int[] textureId,
			DirectLinkedList<TextureReference> mActiveTextureList,
			ReferenceQueue unRefQueue, RenderView renderView) {
		LogExt.Log(this, Thread.currentThread().getStackTrace(), "mBytestream = " + mBytestream);
		int glError = GL11.GL_NO_ERROR;
		if (mBytestream != null) {
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
			byte[] imgdata = mBytestream.toByteArray();
			LogExt.Log(this, Thread.currentThread().getStackTrace(), "width = " + width + ", height = " + height);
			gl.glCompressedTexImage2D(GL10.GL_TEXTURE_2D, 0, GLES11Ext.GL_ETC1_RGB8_OES, width, height, 0, mBytestream.size(), ByteBuffer.wrap(imgdata));
			imgdata = null;
			try {
				mBytestream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			LoadCDTTexture(mCDTStream, gl);
//			LoadETC1Texture(input, gl);
			
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
				imgdata = null;
				texture.mState = Texture.STATE_UNLOADED;
			} else {
				// Update texture state.
				imgdata = null;
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
		if (mCDTStream != null) {
			try {
				mCDTStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mCDTStream = null;
		}
		if (mBytestream != null) {
			try {
				mBytestream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mBytestream = null;
		}
	}

//	/**
//	 * @author bianxh
//	 * @param input
//	 * @param gl
//	 */
//	private void LoadCDTTexture(InputStream input, GL11 gl) {
//		try {
//			int ch;
//			int width = 0;
//			int height = 0;
//			int index = 0;
//			StringBuilder sb = new StringBuilder();
//			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
//			while ((ch = input.read()) != -1) {
//				// 从cdt文件中读取纹理的宽高数据
//				if (index < 8) {
//					sb.insert(0, Integer.toHexString(ch));
//					if (ch < 16) {
//						sb.insert(0, "0");
//					}
//					if (index == 3) {
//						width = Integer.valueOf(sb.toString(), 16);
//						sb.delete(0, sb.length());
//					} else if (index == 7) {
//						height = Integer.valueOf(sb.toString(), 16);
//					}
//					index ++;
//				} else {
//					bytestream.write(ch);
//				}
//			}
//			sb = null;
//			byte imgdata[] = bytestream.toByteArray();
//			TCLLog.Log(this, Thread.currentThread().getStackTrace(), "width = " + width + ", height = " + height);
//			gl.glCompressedTexImage2D(GL10.GL_TEXTURE_2D, 0, GLES11Ext.GL_ETC1_RGB8_OES, width, height, 0, bytestream.size(), ByteBuffer.wrap(imgdata));
//			imgdata = null;
//			bytestream.close();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//	}
}
