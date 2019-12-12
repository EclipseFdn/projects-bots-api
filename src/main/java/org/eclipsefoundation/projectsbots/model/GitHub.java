/* Copyright (c) 2019 Eclipse Foundation and others.
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License 2.0
 * which is available at http://www.eclipse.org/legal/epl-v20.html,
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipsefoundation.projectsbots.model;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.wildfly.common.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class GitHub {

	public abstract String username();
	@Nullable
	public abstract String email();
	
	public boolean matches(Pattern pattern) {
		return pattern.matcher(username()).matches() || 
			(email() != null && pattern.matcher(email()).matches());
	}
	
	public static JsonAdapter<GitHub> jsonAdapter(Moshi moshi) {
    return new AutoValue_GitHub.MoshiJsonAdapter(moshi);
  }
}
