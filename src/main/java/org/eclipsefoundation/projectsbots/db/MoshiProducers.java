package org.eclipsefoundation.projectsbots.db;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.squareup.moshi.Moshi;

import org.eclipsefoundation.projectsbots.model.BotAdapterFactory;

@ApplicationScoped
public class MoshiProducers {

  @Produces
  public Moshi moshi() {
      return new Moshi.Builder()
        .add(BotAdapterFactory.create())
        .build();
  }
}
