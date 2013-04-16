package com.test.GridDrawable;

import com.demo.render.GridQuad;
import com.demo.render.Texture;
import com.demo.utils.Utils;
import com.ophone.ogles.lib.Vector3f;

public class FrameGrid extends GridQuad {
	private static final Vector3f mOffsetPosition = new Vector3f(0, 0, 0);
	public FrameGrid(Texture[] textures) {
		super(4.28f/2, 2.8f/2, Utils.TEXCOORDS_BMP, mOffsetPosition, 0, textures);
	}
}
