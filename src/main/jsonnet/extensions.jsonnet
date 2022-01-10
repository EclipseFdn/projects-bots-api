local dependabot = {
  "github.com-dependabot": {
    username: "dependabot[bot]",
    email: "49699333+dependabot[bot]@users.noreply.github.com",
  },
};

local projectsWithDependabot = [
  "ee4j.openmq",
  "rt.jetty",
  "technology.cbi",
  "technology.dash",
  "technology.tm4e",
  "technology.tycho",
  "tools.cdt",
  "tools.linuxtools",
  "tools.wildwebdeveloper",
  "adoptium",
  "adoptium.temurin",
  "adoptium.aqavit",
  "adoptium.incubator",
];

[ bot + (
  if std.member(projectsWithDependabot, bot.projectId) then
    dependabot
  else if (bot.projectId == "ecd.che") then {
    "github.com-openshift-ci-robot": {
      email: "openshift-ci-robot@users.noreply.github.com",
      username: "openshift-ci-robot",
    },
    "github.com-openshift-merge-robot": {
      email: "openshift-merge-robot@users.noreply.github.com",
      username: "openshift-merge-robot",
    },
  } else if (bot.projectId == "eclipse.platform" ||  bot.projectId == "eclipse.jdt" || bot.projectId == "eclipse.pde" || bot.projectId == "eclipse.equinox" || bot.projectId == "eclipse.platform.swt") then {
    "eclipse.org-eclipse-platform-releng": {
      email: "releng-bot@eclipse.org",
      username: "genie.releng",
    },
  } else {}
) for bot in import "../../../bots.db.new.jsonnet"]