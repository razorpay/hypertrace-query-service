rootProject.name = "query-service-root"

pluginManagement {
  repositories {
    mavenLocal()
    gradlePluginPortal()
    maven("https://hypertrace.jfrog.io/artifactory/maven")
    maven {
      url = uri("https://repo.repsy.io/mvn/user548/pinot-java-client-060-tls")
    }
  }
}

plugins {
  id("org.hypertrace.version-settings") version "0.2.0"
}

include(":query-service-api")
include(":query-service-client")
include(":query-service-impl")
include(":query-service")
