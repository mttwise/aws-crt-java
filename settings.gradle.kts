/*
 * This file was generated by the Gradle 'init' task.
 */

rootProject.name = "aws-crt"

include(":native")
project(":native").projectDir = File("${settingsDir}/src/native")

include(":smithy-crt")
include(":s3-native-client")
