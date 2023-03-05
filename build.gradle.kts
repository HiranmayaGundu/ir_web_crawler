plugins {
    id("java")
    application
}

group = "org.webcrawler"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url=uri("https://download.oracle.com/maven")
    }
}


application {
    applicationDefaultJvmArgs = listOf("--add-exports=java.management/sun.management=ALL-UNNAMED")
    mainClass.set("org.webcrawler.Main");
}

dependencies {
    implementation("edu.uci.ics:crawler4j:4.4.0")
    implementation("org.apache.commons:commons-csv:1.5")

//    for colors in terminal
    implementation("org.codehaus.janino:janino:3.0.7")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs = listOf("--add-exports=java.management/sun.management=ALL-UNNAMED")
}