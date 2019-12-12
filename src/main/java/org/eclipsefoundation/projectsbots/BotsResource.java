/* Copyright (c) 2019 Eclipse Foundation and others.
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License 2.0
 * which is available at http://www.eclipse.org/legal/epl-v20.html,
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipsefoundation.projectsbots;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipsefoundation.projectsbots.db.BotsDB;
import org.eclipsefoundation.projectsbots.model.Bot;

import com.squareup.moshi.Moshi;

@Path("/bots")
@Produces(MediaType.APPLICATION_JSON)
public class BotsResource {

	@Inject 
	BotsDB db;
	
	@Inject Moshi moshi;
		
	@GET
	public String search(@QueryParam("q") String q) {
		return moshi.adapter(List.class).toJson(db.search(q));
	}
	
	@GET
	@Path("/{id}")
	public String get(@PathParam("id") int id) {
		Optional<Bot> bot = db.findById(id);
		if (bot.isPresent())
			return moshi.adapter(Bot.class).toJson(bot.get());
		return "{}";
	}
}
