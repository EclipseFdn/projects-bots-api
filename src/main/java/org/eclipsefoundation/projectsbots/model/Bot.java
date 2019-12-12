/* Copyright (c) 2019 Eclipse Foundation and others.
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License 2.0
 * which is available at http://www.eclipse.org/legal/epl-v20.html,
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipsefoundation.projectsbots.model;

import org.wildfly.common.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class Bot {

	public abstract int id();
	public abstract String projectId();
	public abstract String username();
	public abstract String email();
	
	@Nullable
	public abstract GitHub gitHub();
	@Nullable
	public abstract OSSRH ossrh();
	@Nullable
	public abstract DockerHub dockerHub();

	public static JsonAdapter<Bot> jsonAdapter(Moshi moshi) {
    return new AutoValue_Bot.MoshiJsonAdapter(moshi);
  }
}
