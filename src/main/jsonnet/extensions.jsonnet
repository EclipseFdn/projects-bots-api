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
  "adoptium",
  "adoptium.temurin",
  "adoptium.aqavit",
  "adoptium.incubator",
];

[ bot + (
  if std.member(projectsWithDependabot, bot.projectId) then 
    dependabot 
  else 
    {}
) for bot in import "../../../bots.db.new.jsonnet"]