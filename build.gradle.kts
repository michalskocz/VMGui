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
    implementation(libs.batik.all)

    testImplementation(libs.jupiter.lib)
    testRuntimeOnly(libs.jupiter.platform)

}

javafx {
    version = libs.versions.javafx.get()
    modules("javafx.controls", "javafx.fxml", "javafx.swing")
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

val skipSvgConvert = project.hasProperty("skipSvgConvert")
val svgDir = file("external/octicons/icons")
val pngDir = layout.buildDirectory.dir("generated/resources/external/octicons")

val selectedSvgNames = setOf(
    "file-directory-24.svg",
    "compose-24.svg",
    "eye-24.svg",
    "tools-24.svg",
    "plus-24.svg",
    "lockup-github-24.svg",
)

val selectedSvgDir = layout.buildDirectory.dir("tmp/selected-octicons-svg")

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

val prepareSelectedOcticons by tasks.registering(Sync::class) {
    from(svgDir) {
        include(selectedSvgNames)
    }
    into(selectedSvgDir)
}

val convertOcticonsToPng by tasks.registering(JavaExec::class) {
    group = "build"
    description = "Konwertuje wybrane SVG Octicons do PNG"
    dependsOn(compileSvgConvert, prepareSelectedOcticons)

    mainClass.set("build.utils.SvgToPngConventer")
    classpath = files(compileSvgConvert.map { it.destinationDirectory }) + sourceSets["svgConvert"].runtimeClasspath

    inputs.files(selectedSvgNames.map { File(svgDir, it) })
    outputs.dir(pngDir)

    args = listOf(
        selectedSvgDir.get().asFile.absolutePath,
        pngDir.get().asFile.absolutePath
    )
}

tasks.named<ProcessResources>("processResources") {
    if (!skipSvgConvert) {
        dependsOn(convertOcticonsToPng)
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    from(pngDir) {
        into("external/octicons")
    }
}