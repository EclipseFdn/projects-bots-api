/* Copyright (c) 2019 Eclipse Foundation and others.
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License 2.0
 * which is available at http://www.eclipse.org/legal/epl-v20.html,
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipsefoundation.projectsbots;

import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/bots")
@Produces(MediaType.APPLICATION_JSON)
public class BotsResource {

	
	private Set<Bot> bots = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

	public BotsResource() {
		bots.add(new GitHubBot(1, "ecd.che", "che-bot", "che-bot@eclipse.org"));
		bots.add(new GitHubBot(2, "ecd.sprotty", "eclipse-sprotty-bot", "sprotty-bot@eclipse.org"));
		bots.add(new GitHubBot(3, "eclipse.jdt.ls", "genie-ls", "ls-github@eclipse.org"));
		bots.add(new GitHubBot(4, "ee4j", "ee4j-bot", "ee4j-bot@eclipse.org"));
		bots.add(new GitHubBot(5, "ee4j.ca", "ca-bot", "ca-bot@eclipse.org"));
		bots.add(new GitHubBot(6, "ee4j.cu", "cu-bot", "cu-bot@eclipse.org"));
		bots.add(new GitHubBot(7, "ee4j.ejb", "ejb-bot", "ejb-bot@eclipse.org"));
		bots.add(new GitHubBot(8, "ee4j.el", "el-bot", "el-bot@eclipse.org"));
		bots.add(new GitHubBot(9, "ee4j.es", "eclipse-es-bot", "es-bot@eclipse.org"));
		bots.add(new GitHubBot(10, "ee4j.glassfish", "glassfish-bot", "glassfish-bot@eclipse.org"));
		bots.add(new GitHubBot(11, "ee4j.grizzly", "eclipse-grizzly-bot", "grizzly-bot@eclipse.org"));
		bots.add(new GitHubBot(12, "ee4j.interceptors", "interceptors-bot", "interceptors-bot@eclipse.org"));
		bots.add(new GitHubBot(13, "ee4j.jacc", "jacc-bot", "jacc-bot@eclipse.org"));
		bots.add(new GitHubBot(14, "ee4j.jaf", "jaf-bot", "jaf-bot@eclipse.org"));
		bots.add(new GitHubBot(15, "ee4j.jakartaee-platform", "eclipse-jakartaee-platform-bot", "jakartaee-platform-bot@eclipse.org"));
		bots.add(new GitHubBot(16, "ee4j.jakartaee-stable", "jakartaee-stable-bot", "jakartaee-stable-bot@eclipse.org"));
		bots.add(new GitHubBot(17, "ee4j.jaspic", "jaspic-bot", "jaspic-bot@eclipse.org"));
		bots.add(new GitHubBot(18, "ee4j.javamail", "javamail-bot", "javamail-bot@eclipse.org"));
		bots.add(new GitHubBot(19, "ee4j.jaxb", "jaxb-bot", "jaxb-bot@eclipse.org"));
		bots.add(new GitHubBot(20, "ee4j.jaxb-impl", "eclipse-jaxb-impl-bot", "jaxb-impl-bot@eclipse.org"));
		bots.add(new GitHubBot(21, "ee4j.jaxrs", "jaxrs-bot", "jaxrs-bot@eclipse.org"));
		bots.add(new GitHubBot(22, "ee4j.jaxws", "jaxws-bot", "jaxws-bot@eclipse.org"));
		bots.add(new GitHubBot(23, "ee4j.jca", "jca-bot", "jca-bot@eclipse.org"));
		bots.add(new GitHubBot(24, "ee4j.jersey", "jersey-bot", "jersey-bot@eclipse.org"));
		bots.add(new GitHubBot(25, "ee4j.jms", "jms-bot", "jms-bot@eclipse.org"));
		bots.add(new GitHubBot(26, "ee4j.jpa", "jpa-bot", "jpa-bot@eclipse.org"));
		bots.add(new GitHubBot(27, "ee4j.jsonb", "eclipse-jsonb-bot", "jsonb-bot@eclipse.org"));
		bots.add(new GitHubBot(28, "ee4j.jsonp", "jsonp-bot", "jsonp-bot@eclipse.org"));
		bots.add(new GitHubBot(29, "ee4j.jsp", "jsp-bot", "jsp-bot@eclipse.org"));
		bots.add(new GitHubBot(30, "ee4j.jstl", "jstl-bot", "jstl-bot@eclipse.org"));
		bots.add(new GitHubBot(31, "ee4j.jta", "jta-bot", "jta-bot@eclipse.org"));
		bots.add(new GitHubBot(32, "ee4j.krazo", "eclipse-krazo-bot", "krazo-bot@eclipse.org"));
		bots.add(new GitHubBot(33, "ee4j.metro", "eclipse-metro-bot", "metro-bot@eclipse.org"));
		bots.add(new GitHubBot(34, "ee4j.mojarra", "mojarra-bot", "mojarra-bot@eclipse.org"));
		bots.add(new GitHubBot(35, "ee4j.openmq", "openmq-bot", "openmq-bot@eclipse.org"));
		bots.add(new GitHubBot(36, "ee4j.orb", "eclipse-orb-bot", "orb-bot@eclipse.org"));
		bots.add(new GitHubBot(37, "ee4j.servlet", "servlet-bot", "servlet-bot@eclipse.org"));
		bots.add(new GitHubBot(38, "ee4j.soteria", "soteria-bot", "soteria-bot@eclipse.org"));
		bots.add(new GitHubBot(39, "ee4j.tyrus", "tyrus-bot", "tyrus-bot@eclipse.org"));
		bots.add(new GitHubBot(40, "ee4j.websocket", "websocket-bot", "websocket-bot@eclipse.org"));
		bots.add(new GitHubBot(41, "ee4j.yasson", "yasson-bot", "yasson-bot@eclipse.org"));
		bots.add(new GitHubBot(42, "iot.ditto", "eclipse-ditto-bot", "ditto-bot@eclipse.org"));
		bots.add(new GitHubBot(43, "iot.hawkbit", "hawkbit-bot", "hawkbit-bot@eclipse.org"));
		bots.add(new GitHubBot(44, "iot.mita", "mita-bot", "mita-bot@eclipse.org"));
		bots.add(new GitHubBot(45, "modeling.tmf.xtext", "genie-xtext", "xtext-github@eclipse.org"));
		bots.add(new GitHubBot(46, "modeling.xsemantics", "xsemantics-bot", "xsemantics-bot@eclipse.org"));
		bots.add(new GitHubBot(47, "science.texlipse", "genie-texlipse", "texclipse-github@eclipse.org"));
		bots.add(new GitHubBot(48, "soa.winery", "genie-winery", "winery-github@eclipse.org"));
		bots.add(new GitHubBot(49, "technology.cognicrypt", "eclipse-cognicrypt-bot", "cognicrypt-bot@eclipse.org"));
		bots.add(new GitHubBot(50, "technology.jnosql", "genie-jnosql", "jnosql-github@eclipse.org"));
		bots.add(new GitHubBot(51, "technology.lsp4j", "eclipse-lsp4j-bot", "lsp4j-bot@eclipse.org"));
		bots.add(new GitHubBot(52, "technology.microprofile", "genie-microprofile", "microprofile-github@eclipse.org"));
		bots.add(new GitHubBot(53, "technology.nebula", "eclipse-nebula-bot", "nebula-bot@eclipse.org"));
		bots.add(new GitHubBot(54, "technology.omr", "genie-omr", "omr-github@eclipse.org"));
		bots.add(new GitHubBot(55, "technology.openj9", "genie-openj9", "openj9-github@eclipse.org"));
		bots.add(new GitHubBot(56, "technology.rdf4j", "rdf4j-bot", "rdf4j-bot@eclipse.org"));
		bots.add(new GitHubBot(57, "technology.reddeer", "genie-reddeer", "reddeer-github@eclipse.org"));
		bots.add(new GitHubBot(58, "technology.sw360", "sw360-bot", "sw360-bot@eclipse.org"));
		bots.add(new GitHubBot(59, "tools.acute", "genie-acute", "acute-github@eclipse.org"));
		bots.add(new GitHubBot(60, "tools.cdt", "eclipse-cdt-bot", "cdt-bot@eclipse.org"));
	}

	@GET
	public Set<Bot> list(@QueryParam("kind") String kind, @QueryParam("projectId") String projectId, @QueryParam("username") String username, @QueryParam("email") String email) {
		Predicate<Bot> filter = bot -> true;
		if (kind != null) {
			switch (kind) {
				case "github":
					filter = filter.and(GitHubBot.class::isInstance);
					break;
				default:
					filter = filter.and(bot -> false);
					break;
			}
		}

		if (projectId != null) {
			filter = filter.and(bot -> projectId.equals(bot.getProjectId()));
		}

		if (username != null) {
			filter = filter.and(bot -> username.equals(bot.getUsername()));		
		}
		
		if (email != null) {
			filter = filter.and(bot -> email.equals(bot.getEmail()));		
		}
		
		return bots.stream().filter(filter).collect(Collectors.toSet());
	}
	
	@GET
	@Path("/{id}")
	public Bot get(@PathParam("id") Integer id) {
		return this.bots.stream().filter(b -> b.getId() == id.intValue()).findFirst().orElseThrow(NotFoundException::new);
	}
}
