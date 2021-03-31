repositories {
    jcenter()
}

plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.31")
    jacoco
    idea
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.2")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        setExceptionFormat("full")
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = false
        csv.isEnabled = false
        html.isEnabled = true
        html.destination = file("$buildDir/reports/coverage")
    }
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude("tf/virtual/dojo/algo/SolutionKt.class")
            exclude("tf/virtual/dojo/algo/core/Actions.class")
        }
    )
}

// task merging all source files
tasks.register("export") {
    doLast {
        val imports = HashSet<String>()
        val code = ArrayList<String>()
        val rxpImport = Regex("^import .+$")
        val rxpPackage = Regex("^package .+$")

        val merge = fun(basePath: String) {
            println("\nFrom: $basePath")
            File(basePath).walk().forEach { file ->
                if (file.isFile && file.name.endsWith(".kt")) {
                    println("    Importing ${file.path.substringAfter(basePath)}")
                    file.forEachLine { line ->
                        if (line.matches(rxpImport)) {
                            imports.add(line)
                        } else if (!line.matches(rxpPackage) && line.isNotBlank()) {
                            code.add(line)
                        }
                    }
                }
            }
        }

        merge("src/main/kotlin/tf/virtual/dojo/algo")

        val outFile = File("./build/solution.kt")
        outFile.parentFile.mkdirs()
        outFile.printWriter().use { pw ->
            imports.filterNot { it.contains("tf.virtual") }.forEach(pw::println)
            code.filterNot { it.startsWith("@file:") }.forEach(pw::println)
        }
    }
}
