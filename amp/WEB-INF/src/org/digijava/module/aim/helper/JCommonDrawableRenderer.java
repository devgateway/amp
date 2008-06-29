package org.digijava.module.aim.helper;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import net.sf.jasperreports.engine.JRAbstractSvgRenderer;

import org.jfree.ui.Drawable;


public class JCommonDrawableRenderer extends JRAbstractSvgRenderer
{

	private Drawable drawable = null;

	public JCommonDrawableRenderer(Drawable drawable) 
	{
		////System.out.println("Inside Jfree Webapp CommonDrawable....UNKNOWN(may come here-7)");
		this.drawable = drawable;
	}

	public void render(Graphics2D grx, Rectangle2D rectangle) 
	{
		if (drawable != null) 
		{
			drawable.draw(grx, rectangle);
		}
	}

	
}
