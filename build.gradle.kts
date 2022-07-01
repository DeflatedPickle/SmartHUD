import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("maven-publish")

    id("org.jetbrains.kotlin.jvm")

    id("fabric-loom")

    id("com.diffplug.spotless")
    id("net.kyori.blossom")
}

val archives_base_name: String by project
val yarn_mappings: String by project
val loader_version: String by project
val fabric_version: String by project
val fabric_kotlin_version: String by project
val kotlin_version: String by project
val modmenu_version: String by project
val minecraft_major: String by project
val minecraft_minor: String by project
val minecraft_patch: String by project
val mod_author: String by project
val license_type: String by project
val java_version: String by project
val branch: String by project
val license_header: String by project
val ktlint_version: String by project
val mod_name: String by project
val mod_desc: String by project
val mod_site: String by project
val mod_src: String by project
val maven_group: String by project
val mod_environment: String by project
val mod_version: String by project
val loom_version: String by project

base {
    archivesBaseName = archives_base_name
}

version = project.property("mod_version")!!
group = project.property("maven_group")!!

repositories {
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.terraformersmc.com") }
    mavenCentral()
}

val includeImplementation: Configuration by configurations.creating {
    configurations.implementation.configure { extendsFrom(this@creating) }
}

dependencies {
    includeImplementation("org.jetbrains.kotlin:kotlin-scripting-jvm:1.6.0")
    includeImplementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host:1.7.0")

    minecraft("com.mojang:minecraft:${minecraftVersion()}")
    mappings("net.fabricmc:yarn:${minecraftVersion()}+${yarn_mappings}:v2")
    modImplementation("net.fabricmc:fabric-loader:${loader_version}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabric_version}+${minecraftVersionShort()}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${fabric_kotlin_version}+kotlin.${kotlin_version}")

    // https://fabricmc.net/wiki/documentation:libraries
    modImplementation("com.terraformersmc:modmenu:${modmenu_version}")

    handleIncludes(includeImplementation)
}

loom {
    accessWidenerPath.set(file("src/main/resources/${archives_base_name}.accesswidener"))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = java_version
        targetCompatibility = java_version

        dependsOn("spotlessApply")
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = java_version
            sourceCompatibility = java_version
            targetCompatibility = java_version
        }

        dependsOn("spotlessApply")
    }

    withType<ProcessResources> {
        filesMatching("*.json") {
            expand(
                mapOf(
                    "id" to archives_base_name,
                    "name" to mod_name,
                    "version" to version,
                    "author" to mod_author,
                    "desc" to mod_desc,
                    "site" to mod_site,
                    "src" to mod_src,
                    "minecraft" to minecraftVersion(),
                    "fabric" to fabric_version,
                    "loader" to loader_version,
                    "java" to java_version,
                    "adapter" to fabric_kotlin_version,
                    "group" to maven_group,
                    "license" to license_type,
                    "environment" to mod_environment,
                    "entry" to if (mod_environment == "*") "main" else mod_environment,
                )
            )
        }
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${archives_base_name}" }
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(java_version))
    }
    withSourcesJar()
}

spotless {
    ratchetFrom("origin/${branch}")

    java {
        importOrder()
        // removeUnusedImports()
        // googleJavaFormat()

        licenseHeader("/* ${expandLicense(license_header)} */\n\n")
    }

    groovyGradle {
        target("*.gradle")
        greclipse()
    }

    kotlin {
        // ktlint(ktlint_version)

        licenseHeader("/* ${expandLicense(license_header)} */\n\n")
    }

    format("misc") {
        target("*.md", ".gitignore")

        trimTrailingWhitespace()
        indentWithTabs()
        endWithNewline()
    }
}

blossom {
    replaceToken("$[name]", mod_name)
    replaceToken("$[author]", mod_author)
    replaceToken("$[version]", mod_version)

    replaceToken("$[id]", archives_base_name)
    replaceToken("$[group]", maven_group)

    replaceToken("$[java]", java_version)
    replaceToken("$[kotlin]", kotlin_version)

    replaceToken("$[minecraft]", minecraftVersion())
    replaceToken("$[yarn]", yarn_mappings)
    replaceToken("$[loader]", loader_version)
    replaceToken("$[loom]", loom_version)
    replaceToken("$[fabric_version]", fabric_version)
}

fun minecraftVersion() = "$minecraft_major.$minecraft_minor.$minecraft_patch"
fun minecraftVersionShort() = "$minecraft_major.$minecraft_minor"
fun expandLicense(old: String) =
    old.replace("\$DEVELOPER", mod_author)
        .replace("\$LICENSE", license_type.toUpperCase())

fun DependencyHandlerScope.includeTransitive(
    dependencies: Set<ResolvedDependency>,
    fabricLanguageKotlinDependency: ResolvedDependency,
    checkedDependencies: MutableSet<ResolvedDependency> = HashSet()
) {
    dependencies.forEach {
        if (checkedDependencies.contains(it)/* || (it.moduleGroup == "org.jetbrains.kotlin" && it.moduleName.startsWith("kotlin-stdlib"))*/) return@forEach

        if (fabricLanguageKotlinDependency.children.any { kotlinDep -> kotlinDep.name == it.name }) {
            println("Skipping -> ${it.name}")
        } else {
            include(it.name)
            println("Including -> ${it.name}")
        }
        checkedDependencies += it

        includeTransitive(it.children, fabricLanguageKotlinDependency, checkedDependencies)
    }
}

fun DependencyHandlerScope.handleIncludes(configuration: Configuration) {
    includeTransitive(
        configuration.resolvedConfiguration.firstLevelModuleDependencies,
        configurations.modImplementation.get().resolvedConfiguration.firstLevelModuleDependencies
            .first { it.moduleGroup == "net.fabricmc" && it.moduleName == "fabric-language-kotlin" }
    )
}
