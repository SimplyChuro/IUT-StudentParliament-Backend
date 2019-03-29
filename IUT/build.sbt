name := """International University Of Travnik"""
organization := "IUT"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.8"

crossScalaVersions := Seq("2.11.12", "2.12.8")

libraryDependencies += guice

// Database
libraryDependencies += javaJdbc
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"