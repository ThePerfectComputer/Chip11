
name := "spinal-experiments"
version := "1.0"
scalaVersion := "2.11.12"
val spinalVersion = "1.4.4"

libraryDependencies ++= Seq(
  "com.github.spinalhdl" %% "spinalhdl-core" % spinalVersion,
  "com.github.spinalhdl" %% "spinalhdl-lib" % spinalVersion,
  "org.scalatest" %% "scalatest" % "3.2.7" % "test",
  compilerPlugin("com.github.spinalhdl" %% "spinalhdl-idsl-plugin" % spinalVersion)
)
fork := true
