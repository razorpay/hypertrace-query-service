plugins {
  java
  application
  jacoco
  id("org.hypertrace.docker-java-application-plugin") version "0.9.0"
  id("org.hypertrace.docker-publish-plugin") version "0.9.0"
  id("org.hypertrace.integration-test-plugin")
  id("org.hypertrace.jacoco-report-plugin")
}

dependencies {
  implementation(project(":query-service-impl"))
  implementation("org.hypertrace.core.grpcutils:grpc-server-utils:0.7.0")
  implementation("org.hypertrace.core.serviceframework:platform-service-framework:0.1.33")
  implementation("org.slf4j:slf4j-api:1.7.32")
  implementation("com.typesafe:config:1.4.1")

  runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.17.1")
  runtimeOnly("io.grpc:grpc-netty")
  integrationTestImplementation("com.google.protobuf:protobuf-java-util:3.19.2")
  integrationTestImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
  integrationTestImplementation("org.testcontainers:testcontainers:1.16.2")
  integrationTestImplementation("org.testcontainers:junit-jupiter:1.16.2")
  integrationTestImplementation("org.testcontainers:kafka:1.16.2")
  integrationTestImplementation("org.hypertrace.core.serviceframework:integrationtest-service-framework:0.1.33")
  integrationTestImplementation("com.github.stefanbirkner:system-lambda:1.2.0")

  integrationTestImplementation("org.apache.kafka:kafka-clients:5.5.1-ccs")
  integrationTestImplementation("org.apache.kafka:kafka-streams:5.5.1-ccs")
  integrationTestImplementation("org.apache.avro:avro:1.11.0")
  integrationTestImplementation("com.google.guava:guava:30.1.1-jre")
  integrationTestImplementation("org.hypertrace.core.datamodel:data-model:0.1.12")
  integrationTestImplementation("org.hypertrace.core.kafkastreams.framework:kafka-streams-serdes:0.1.13")

  integrationTestImplementation(project(":query-service-client"))
  integrationTestImplementation("org.hypertrace.core.attribute.service:attribute-service-client:0.12.3")
}

application {
  mainClass.set("org.hypertrace.core.serviceframework.PlatformServiceLauncher")
}

// Config for gw run to be able to run this locally. Just execute gw run here on Intellij or on the console.
tasks.run<JavaExec> {
  jvmArgs = listOf("-Dbootstrap.config.uri=file:$projectDir/src/main/resources/configs", "-Dservice.name=${project.name}")
}

tasks.integrationTest {
  useJUnitPlatform()
}

hypertraceDocker {
  defaultImage {
    imageName.set("hypertrace-ui")
    javaApplication {
      port.set(8090)
    }
    namespace.set("razorpay")
  }
  tag("${project.name}" + "_" + versionBanner())
}

fun versionBanner(): String {
  val os = com.bmuschko.gradle.docker.shaded.org.apache.commons.io.output.ByteArrayOutputStream()
  project.exec {
    commandLine = "git rev-parse --verify --short HEAD".split(" ")
    standardOutput = os
  }
  return String(os.toByteArray()).trim()
}

tasks.jacocoIntegrationTestReport {
  sourceSets(project(":query-service-impl").sourceSets.getByName("main"))
  sourceSets(project(":query-service-client").sourceSets.getByName("main"))
}
