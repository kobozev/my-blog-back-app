plugins {
    java
    war
}

group = "ru.practicum.kobozevva"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

repositories {
    mavenCentral()
}

dependencies {

    // Spring MVC
    implementation("org.springframework:spring-context:6.2.14")
    implementation("org.springframework:spring-webmvc:6.2.14")

    // Spring JDBC + TX
    implementation("org.springframework:spring-jdbc:6.2.14")
    implementation("org.springframework:spring-tx:6.2.14")

    // PostgreSQL
    implementation("org.postgresql:postgresql:42.7.7")

    // Servlet API (provided by container)
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")

    // JSON (Java Time)
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.2")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.16")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.13")

    // Tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.0")
    testImplementation("org.springframework:spring-test:6.2.14")
    testImplementation("jakarta.servlet:jakarta.servlet-api:6.0.0")

    // Mockito
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")

    // H2
    testImplementation("com.h2database:h2:2.3.232")

    // Hamcrest
    testImplementation("org.hamcrest:hamcrest:2.2")

    // JsonPath
    testImplementation("com.jayway.jsonpath:json-path:2.9.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.war {
    archiveFileName.set("blog-backend.war")
}

tasks.jar {
    enabled = false
}