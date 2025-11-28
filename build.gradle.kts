import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    java
}

// ==============
// ==== CONFIGURATION ====
val modName = "Better Revived S-Mods"
val modVersion = "1.0.0"
val jarFileName = "BetterRevivedSMods.jar"
val modId = "better_revived_smods"

// Starsector installation directory
val starsectorDirectory = if(providers.gradleProperty("starsector.dir").isPresent) 
    providers.gradleProperty("starsector.dir").get() 
else "C:/Starsector"

val starsectorCoreDirectory = if(Os.isFamily(Os.FAMILY_UNIX)) 
    starsectorDirectory 
else "${starsectorDirectory}/starsector-core"

// ==============
// ==== REPOSITORIES ====
repositories {
    mavenCentral()
    flatDir {
        dirs("libs", starsectorCoreDirectory)
    }
}

// ==============
// ==== DEPENDENCIES ====
dependencies {
    // Starsector public API
    compileOnly(fileTree(starsectorCoreDirectory) {
        include("starfarer.api.jar")
        include("json.jar")
        include("xstream-1.4.10.jar")
        include("log4j-1.2.9.jar")
        include("lwjgl.jar")
        include("lwjgl_util.jar")
    })
    
    // Starsector internal APIs
    compileOnly(fileTree(starsectorCoreDirectory) {
        include("starfarer_obf.jar")
        include("fs.common_obf.jar")
    })
    
    // Mod dependencies from libs folder (if any)
    compileOnly(fileTree("libs") {
        include("*.jar")
    })
}

// ==============
// ==== JAVA CONFIGURATION ====
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

// ==============
// ==== SOURCE SETS ====
sourceSets {
    main {
        java {
            setSrcDirs(listOf("src"))
        }
        resources {
            setSrcDirs(listOf("data"))
        }
    }
}

// ==============
// ==== JAR TASK ====
tasks.jar {
    archiveBaseName.set("BetterRevivedSMods")
    archiveVersion.set("")
    destinationDirectory.set(file("jars"))
    
    from(sourceSets.main.get().output)
    
    exclude("**/*.java")
    exclude("**/*.gradle")
    exclude("**/build/**")
    
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    
    doFirst {
        val jarFile = file("jars/${jarFileName}")
        if (jarFile.exists()) {
            try {
                val backupFile = file("jars/${jarFileName}.old")
                if (backupFile.exists()) backupFile.delete()
                jarFile.renameTo(backupFile)
            } catch (e: Exception) {
                println("Warning: Could not rename existing ${jarFileName}")
            }
        }
    }
}

// ==============
// ==== COMPILATION ====
tasks.compileJava {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf(
        "-Xlint:none",
        "-Xlint:-options"
    ))
}

// ==============
// ==== CLEAN TASK ====
tasks.clean {
    delete("build")
    val jarFile = file("jars/${jarFileName}")
    if (jarFile.exists()) {
        try {
            jarFile.delete()
        } catch (e: Exception) {
            println("Warning: Could not delete ${jarFileName} (file may be locked)")
        }
    }
}

// ==============
// ==== BUILD TASK ====
tasks.register("buildMod") {
    dependsOn(tasks.jar)
    doLast {
        println("✓ ${jarFileName} created successfully in jars/")
        
        val jarFile = file("jars/${jarFileName}")
        if (jarFile.exists()) {
            println("\n========================================")
            println("BUILD SUCCESSFUL - NO ERRORS")
            println("========================================")
        }
    }
}

defaultTasks("buildMod")

