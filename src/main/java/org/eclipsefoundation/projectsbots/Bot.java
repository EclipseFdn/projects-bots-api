/* Copyright (c) 2019 Eclipse Foundation and others.
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License 2.0
 * which is available at http://www.eclipse.org/legal/epl-v20.html,
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipsefoundation.projectsbots;

import java.util.Objects;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;
import javax.xml.bind.annotation.XmlElement;

@JsonbPropertyOrder(value = {"kind", "id"})
public abstract class Bot {

	private int id;
	private String kind;
	private String projectId;
	private String username;
	private String email;

	public Bot(int id, String kind, String projectId, String username, String email) {
		this.id = id;
		this.kind = kind;
		this.projectId = projectId;
		this.username = username;
		this.email = email;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getKind() {
		return kind;
	}
	
	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(projectId, username, email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Bot)) {
			return false;
		}
		Bot other = (Bot) obj;
		return Objects.equals(projectId, other.projectId) && Objects.equals(username, other.username) && Objects.equals(email, other.email);
	}
}
