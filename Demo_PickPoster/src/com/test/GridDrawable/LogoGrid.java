package com.test.GridDrawable;

import com.demo.render.GridQuad;
import com.demo.render.Texture;
import com.demo.utils.Utils;
import com.ophone.ogles.lib.Vector3f;

public class LogoGrid extends GridQuad {
	private static final Vector3f mOffsetPosition = new Vector3f(-0.65f, -0.1f, 0.002f);
	public LogoGrid(Texture[] textures) {
		super(1.45f/2, 1.55f/2, Utils.TEXCOORDS_BMP, mOffsetPosition, 0, textures);
	}

}
