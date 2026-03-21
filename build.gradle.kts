plugins {
    java
    application

    alias(libs.plugins.shadowjar)
    alias(libs.plugins.javafx)
}

group = "com.github.mskocz"
version = libs.versions.app.get()

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    }
}

dependencies {
    implementation(libs.atlantafx)
    implementation(libs.xmlgraphic.transcoder)
    implementation(libs.xmlgraphic.codec)

    testImplementation(libs.jupiter.api)
    testRuntimeOnly(libs.jupiter.engine)
}

javafx {
    version = libs.versions.javafx.get()
    modules("javafx.controls", "javafx.fxml")
}

application {
    mainModule.set("com.github.mskocz.vmgui")
    mainClass.set("com.github.mskocz.vmgui.Main")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.github.mskocz.vmgui.Main"
        )
    }
}

tasks.shadowJar {
    archiveClassifier.set("all")
    mergeServiceFiles()
    exclude("module-info.class")
}

tasks.withType<Test> {
    useJUnitPlatform()
    reports {
        html.required.set(true)
        junitXml.required.set(true)
    }
}

tasks.withType<JavaExec> {
    jvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}


val svgDir = file("external/octicons/icons")
val pngDir = file("src/main/resources/external/octicons")


sourceSets {
    create("svgConvert") {
        java.srcDir("src/svgConvert/java")
        compileClasspath += sourceSets["main"].compileClasspath
        runtimeClasspath += compileClasspath
    }
}

val compileSvgConvert by tasks.registering(JavaCompile::class) {
    source = sourceSets["svgConvert"].java
    classpath = sourceSets["svgConvert"].compileClasspath
    destinationDirectory.set(layout.buildDirectory.dir("classes/svgConvert"))
}

val convertOcticonsToPng by tasks.registering(JavaExec::class) {
    group = "build"
    description = "Konwertuje SVG Octicons do PNG"
    dependsOn(compileSvgConvert)
    mainClass.set("build.utils.SvgToPngConventer")
    classpath = files(compileSvgConvert.map { it.destinationDirectory }) + sourceSets["svgConvert"].runtimeClasspath
    args(svgDir.absolutePath, pngDir.absolutePath)
}


tasks.named<ProcessResources>("processResources") {
    dependsOn(convertOcticonsToPng)
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from(pngDir) {
        into("external/octicons")
    }
}