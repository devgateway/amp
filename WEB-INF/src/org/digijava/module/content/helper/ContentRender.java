package org.digijava.module.content.helper;

import java.lang.reflect.InvocationTargetException;

import org.digijava.module.content.dbentity.AmpContentItem;
import org.digijava.module.content.util.DbUtil;

public class ContentRender {

	public static String getRenderedContent(String pageCode, String attribute){
		AmpContentItem contentItem = DbUtil.getContentItemByPageCode(pageCode);
		Class<AmpContentItem> c = AmpContentItem.class;
		String attributeValue = "";
		Class params[] = {};
		try {
			java.lang.reflect.Method accessor = c.getDeclaredMethod("get" + attribute,  params);
			attributeValue = (String) accessor.invoke(contentItem);
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return attributeValue + "";
	}

	public static String getHomePageCode() {
		AmpContentItem contentItem = DbUtil.getHomePage();
		return contentItem.getPageCode();
	}

	public static Integer getThumbnailCount(String pageCode) {
		AmpContentItem contentItem = DbUtil.getContentItemByPageCode(pageCode);
		if (contentItem == null || contentItem.getContentThumbnails() == null)
			return 0;
		return contentItem.getContentThumbnails().size();
	}

}
