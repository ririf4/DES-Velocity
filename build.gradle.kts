plugins {
	kotlin("jvm") version "2.1.10"
}

group = "net.ririfa"
version = "1.0.0"

repositories {
	mavenCentral()
	maven("https://jitpack.io")
	maven("https://repo.velocitypowered.com/snapshots/")
}

dependencies {
	compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
}