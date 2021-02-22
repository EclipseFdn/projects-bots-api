local dependabot = {
  "github.com-dependabot": {
    username: "dependabot[bot]",
    email: "support@github.com",
  },
};

local projectsWithDependabot = [
  "rt.jetty", 
  "technology.cbi",
  "tools.cdt",
];

[ bot + (
  if std.member(projectsWithDependabot, bot.projectId) then 
    dependabot 
  else 
    {}
) for bot in import "../../../bots.db.new.jsonnet"]