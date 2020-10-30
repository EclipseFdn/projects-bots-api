package org.eclipsefoundation.projectsbots.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipsefoundation.projectsbots.model.Bot;

@ApplicationScoped
public class BotsDB {

	@Inject
	BotDBReader dbReader; 
	
	private List<Bot> bots = new ArrayList<>();
	
	public void refresh() throws IOException {
		this.bots = dbReader.readDB();
	}
	
	public Optional<Bot> findByProjectID(String projectId) {
		return bots.stream()
				.filter(b -> projectId.equals(b.projectId()))
				.findFirst();
	}
	
	public Optional<Bot> findByUsername(String username) {
		return bots.stream()
				.filter(b -> username.equals(b.username()))
				.findFirst();
	}
	
	public Optional<Bot> findByEmail(String email) {
		return bots.stream()
				.filter(b -> email.equals(b.email()))
				.findFirst();
	}
	
	public Optional<Bot> findById(int id) {
		return bots.stream()
				.filter(b -> id == b.id())
				.findFirst();
	}
	
	public Optional<Bot> findByGithubUsername(String githubUsername) {
		return bots.stream()
				.filter(b -> b.gitHub() != null)
				.filter(b -> githubUsername.equals(b.gitHub().username()))
				.findFirst();
	}

	public Optional<Bot> findByGitlabUsername(String gitlabUsername) {
		return bots.stream()
				.filter(b -> b.gitLab() != null)
				.filter(b -> gitlabUsername.equals(b.gitLab().username()))
				.findFirst();
	}
	
	public Optional<Bot> findByOSSRHUsername(String ossrhUsername) {
		return bots.stream()
				.filter(b -> b.ossrh() != null)
				.filter(b -> ossrhUsername.equals(b.ossrh().username()))
				.findFirst();
	}
	
	public Optional<Bot> findByDockerHubUsername(String dockerhubUsername) {
		return bots.stream()
				.filter(b -> b.dockerHub() != null)
				.filter(b -> dockerhubUsername.equals(b.dockerHub().username()))
				.findFirst();
	}
	
	public List<Bot> search(String searchStr) {
		Pattern pattern = Pattern.compile(".*"+searchStr+".*");
		
		Predicate<Bot> p = (b) -> pattern.matcher(b.username()).matches();
		p = p.or(b -> Objects.nonNull(b.email()) && pattern.matcher(b.email()).matches());
		p = p.or(b -> pattern.matcher(b.projectId()).matches());
		
		p = p.or(b -> b.gitHub() != null && b.gitHub().matches(pattern));
		p = p.or(b -> b.gitLab() != null && b.gitLab().matches(pattern));
		p = p.or(b -> b.ossrh() != null && b.ossrh().matches(pattern));
		p = p.or(b -> b.dockerHub() != null && b.dockerHub().matches(pattern));
		p = p.or(b -> b.dependabot() != null && b.dependabot().matches(pattern));

		return bots.stream()
			.filter(p)
			.collect(Collectors.toList());
	}
}
