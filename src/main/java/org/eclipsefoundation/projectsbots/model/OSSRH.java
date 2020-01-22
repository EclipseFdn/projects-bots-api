package org.eclipsefoundation.projectsbots.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class OSSRH extends BotAccount {

	public static JsonAdapter<OSSRH> jsonAdapter(Moshi moshi) {
    return new AutoValue_OSSRH.MoshiJsonAdapter(moshi);
  }
}
