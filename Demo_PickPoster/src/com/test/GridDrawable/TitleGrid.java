package com.test.GridDrawable;

import com.demo.render.GridQuad;
import com.demo.render.Texture;
import com.demo.utils.Utils;
import com.ophone.ogles.lib.Vector3f;

public class TitleGrid extends GridQuad {
	private static final Vector3f mOffsetPosition = new Vector3f(0, 0.57f, 0.01f);
	public TitleGrid(Texture[] textures) {
		super(4.26f/2, 0.54f/2, Utils.TEXCOORDS_FILE, mOffsetPosition, 0, textures);
	}
}
