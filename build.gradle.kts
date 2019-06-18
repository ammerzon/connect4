plugins {
    java
}

group = "com.ammerzon.connect4"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile(files("lib/connect4api-1.0.jar"))
    compile("com.jfoenix", "jfoenix", "8.0.8")
    compile("com.airhacks", "afterburner.fx", "1.7.0")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

sourceSets["main"].resources {
    srcDir("src/main/java").includes.addAll(listOf("**/*.fxml", "**/*.css", "**/*.properties", "**/*.png"))
    srcDir("src/main/resources").includes.addAll(listOf("**/*.*"))
}