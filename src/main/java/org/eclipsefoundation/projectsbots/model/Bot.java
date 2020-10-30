/* Copyright (c) 2019 Eclipse Foundation and others.
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License 2.0
 * which is available at http://www.eclipse.org/legal/epl-v20.html,
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipsefoundation.projectsbots.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.wildfly.common.annotation.Nullable;

@AutoValue
public abstract class Bot {

	public abstract int id();
	public abstract String projectId();
	public abstract String username();
	@Nullable
	public abstract String email();
	
	@Nullable
	@Json(name = "github.com")
	public abstract BotAccount gitHub();
	@Nullable
	@Json(name = "gitlab.eclipse.org")
	public abstract BotAccount gitLab();
	@Nullable
	@Json(name = "github.com-dependabot")
	public abstract BotAccount dependabot();
	@Nullable
	@Json(name = "oss.sonatype.org")
	public abstract BotAccount ossrh();
	@Nullable
	@Json(name = "docker.com")
	public abstract BotAccount dockerHub();

	public static JsonAdapter<Bot> jsonAdapter(Moshi moshi) {
		return new AutoValue_Bot.MoshiJsonAdapter(moshi);
	}
}
