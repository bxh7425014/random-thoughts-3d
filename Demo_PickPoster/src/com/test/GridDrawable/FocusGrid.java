package com.test.GridDrawable;

import com.demo.render.GridQuad;
import com.demo.render.Texture;
import com.demo.utils.Utils;
import com.ophone.ogles.lib.Vector3f;

public class FocusGrid extends GridQuad {
	public FocusGrid(Texture[] textures) {
		super(4.28f/2, 2.8f/2, Utils.TEXCOORDS_BMP, new Vector3f(0, 0, 0.001f), 0, textures);
	}
}
