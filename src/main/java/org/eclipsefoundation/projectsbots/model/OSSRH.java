package org.eclipsefoundation.projectsbots.model;

import java.util.Objects;
import java.util.regex.Pattern;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.wildfly.common.annotation.Nullable;

@AutoValue
public abstract class OSSRH {

	public abstract String username();
	@Nullable
	public abstract String email();
	
	public boolean matches(Pattern pattern) {
		return pattern.matcher(username()).matches() || 
			(Objects.nonNull(email()) && pattern.matcher(email()).matches());
	}
	
	public static JsonAdapter<OSSRH> jsonAdapter(Moshi moshi) {
    return new AutoValue_OSSRH.MoshiJsonAdapter(moshi);
  }
}
