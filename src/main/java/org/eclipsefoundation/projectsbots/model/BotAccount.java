/* Copyright (c) 2019 Eclipse Foundation and others.
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License 2.0
 * which is available at http://www.eclipse.org/legal/epl-v20.html,
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipsefoundation.projectsbots.model;

import java.util.Objects;
import java.util.regex.Pattern;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.wildfly.common.annotation.Nullable;

@AutoValue
public abstract class BotAccount {

	@Nullable
	public abstract String username();
	@Nullable
	public abstract String email();
	
	public boolean matches(Pattern pattern) {
		return (Objects.nonNull(username()) && pattern.matcher(username()).matches()) || 
			(Objects.nonNull(email()) && pattern.matcher(email()).matches());
	}

	public static JsonAdapter<BotAccount> jsonAdapter(Moshi moshi) {
		return new AutoValue_BotAccount.MoshiJsonAdapter(moshi);
	}
}
