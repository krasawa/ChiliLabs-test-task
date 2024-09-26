// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hiltPlugin) apply false
    alias(libs.plugins.navigationSafeArgsPlugin) apply false
    alias(libs.plugins.kaptPlugin) apply false
    alias(libs.plugins.kotlinParcelizePlugin) apply false
    alias(libs.plugins.kotlinSerializePlugin) apply false
}