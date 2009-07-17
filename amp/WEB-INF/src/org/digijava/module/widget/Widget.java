package org.digijava.module.widget;

import java.util.Set;

import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.web.HtmlGenerator;

/**
 * Abstract Widget. This is just parent for all kind of widgets.
 * It can be assigned to several place objects - this means that same widget can be displayed on several places.
 * Ideally all widgets should be ablye to generate HTML to use in ajax actions,
 * so this class implements {@link HtmlGenerator} interface, but does not define its method to force concrete subclasses do this.
 * @author Irakli Kobiashvili
 *
 */
public abstract class Widget implements HtmlGenerator{
	private Long id;
	private String name;
	private Set<AmpDaWidgetPlace> places;
	
	@Override
	public String toString() {
		return ""+getName()+" ("+getId()+")";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<AmpDaWidgetPlace> getPlaces() {
		return places;
	}
	public void setPlaces(Set<AmpDaWidgetPlace> places) {
		this.places = places;
	}
}
