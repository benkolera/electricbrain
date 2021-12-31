buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        classpath("com.android.tools.build:gradle:7.0.0")
    }
}

group = "com.benkolera"
version = "1.0"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    // This has to go in here rather than in backend. See: https://netflix.github.io/dgs/generating-code-from-schema/#fixing-the-could-not-initialize-class-graphqlparserantlrgraphqllexer-problem
    id("com.netflix.dgs.codegen") version "5.1.14"
}