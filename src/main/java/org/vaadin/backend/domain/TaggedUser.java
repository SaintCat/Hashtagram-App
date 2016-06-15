package org.vaadin.backend.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.vividsolutions.jts.geom.Point;

@NamedQueries({ @NamedQuery(name = "TaggedUser.findAll", query = "SELECT c FROM TaggedUser c") })
@Entity
public class TaggedUser implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String name;

	@Lob
	private Point location;

	@Lob
	@Basic(fetch=FetchType.LAZY)	
	private byte[] photo;

	private String tags;

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(final Point location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(final byte[] photo) {
		this.photo = photo;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(final String tags) {
		this.tags = tags;
	}
}
