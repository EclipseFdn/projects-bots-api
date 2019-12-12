package org.eclipsefoundation.projectsbots.model;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.squareup.moshi.Moshi;

@ApplicationScoped
public class MoshiProducers {

	@Produces
  public Moshi moshi(){
      return new Moshi.Builder().add(BotAdapterFactory.create()).build();
  }
}
