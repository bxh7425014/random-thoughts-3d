package com.test.GridDrawable;

import com.demo.render.GridQuad;
import com.demo.render.Texture;
import com.demo.utils.Utils;
import com.ophone.ogles.lib.Vector3f;

public class PosterGrid extends GridQuad{
	public PosterGrid(Texture[] textures) {
		super(1.08f/2, 1.56f/2, Utils.TEXCOORDS_FILE,
				new Vector3f(Utils.generateNum(2, 7)/10, Utils.generateNum(-2, 0)/10, Utils.generateNum(1, 5)/100),
				Utils.generateNum(-6, 6)/10, textures);
	}
}
