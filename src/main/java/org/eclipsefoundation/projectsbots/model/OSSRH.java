package org.eclipsefoundation.projectsbots.model;

import java.util.regex.Pattern;

import org.wildfly.common.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class OSSRH {

	public abstract String username();
	@Nullable
	public abstract String email();
	
	public boolean matches(Pattern pattern) {
		return pattern.matcher(username()).matches() || 
			(email() != null && pattern.matcher(email()).matches());
	}
	
	public static JsonAdapter<OSSRH> jsonAdapter(Moshi moshi) {
    return new AutoValue_OSSRH.MoshiJsonAdapter(moshi);
  }
}
