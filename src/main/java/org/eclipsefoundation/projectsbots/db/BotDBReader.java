package org.eclipsefoundation.projectsbots.db;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipsefoundation.projectsbots.model.Bot;

import okio.BufferedSource;
import okio.Okio;

@ApplicationScoped
public class BotDBReader {

	@ConfigProperty(name="bots.db.path", defaultValue = "/deployments/bots.db.json")
	@Inject
	Path dbpath;
	
	@Inject
	Moshi moshi;
	
	public List<Bot> readDB() throws IOException {
		try (BufferedSource bufferedSource = Okio.buffer(Okio.source(dbpath))) {
			Type type = Types.newParameterizedType(List.class, Bot.class);
			JsonAdapter<List<Bot>> adapter = moshi.adapter(type);
			return adapter.fromJson(bufferedSource);
		}
	}

}
