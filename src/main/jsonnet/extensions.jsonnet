local dependabot = {
  "github.com-dependabot": {
    username: "dependabot[bot]",
    email: "49699333+dependabot[bot]@users.noreply.github.com",
  },
};

local projectsWithDependabot = [
  "adoptium",
  "adoptium.temurin",
  "adoptium.aqavit",
  "adoptium.incubator",
  "automotive.sumo",
  "ecd.cdt-cloud",
  "eclipse.equinox",
  "eclipse.jdt",
  "eclipse.pde",
  "eclipse.platform",
  "ee4j.glassfish",
  "ee4j.jakartaconfig",
  "ee4j.openmq",
  "iot.kiso-testing",
  "iot.vorto",
  "rt.jetty",
  "technology.cbi",
  "technology.dash",
  "technology.edc",
  "technology.lemminx",
  "technology.m2e",
  "technology.tm4e",
  "technology.tycho",
  "tools.cdt",
  "tools.ajdt",
  "tools.aspectj",
  "tools.linuxtools",
  "tools.wildwebdeveloper",
];

[ bot + (
  if std.member(projectsWithDependabot, bot.projectId) then
    dependabot
  else {}
  ) + (
  if (bot.projectId == "ecd.che") then {
    "github.com-openshift-ci-robot": {
      email: "openshift-ci-robot@users.noreply.github.com",
      username: "openshift-ci-robot",
    },
    "github.com-openshift-merge-robot": {
      email: "openshift-merge-robot@users.noreply.github.com",
      username: "openshift-merge-robot",
    },
  } else if (bot.projectId == "eclipse.equinox" || bot.projectId == "eclipse.jdt" || bot.projectId == "eclipse.pde" || bot.projectId == "eclipse.platform") then {
    "github.com-releng": {
      email: "releng-bot@eclipse.org",
      username: "eclipse-releng-bot",
    },
    "eclipse.org-eclipse-platform-releng": {
      email: "releng-bot@eclipse.org",
      username: "genie.releng",
    },
  } else if (bot.projectId == "eclipse.platform.swt") then {
    "eclipse.org-eclipse-platform-releng": {
      email: "releng-bot@eclipse.org",
      username: "genie.releng",
    },
  } else if (bot.projectId == "automotive.tractusx") then {
    "github.com-workflow-bot": {
      email: "41898282+github-actions[bot]@users.noreply.github.com",
      username: "github-actions[bot]",
    },
  } else {}
) for bot in import "../../../bots.db.new.jsonnet"]
