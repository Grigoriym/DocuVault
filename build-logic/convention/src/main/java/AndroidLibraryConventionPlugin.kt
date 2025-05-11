import com.android.build.gradle.LibraryExtension
import com.grappim.docuvault.buildlogic.configureFlavors
import com.grappim.docuvault.buildlogic.configureKotlinAndroid
import com.grappim.docuvault.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                defaultConfig.targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
                configureKotlinAndroid(this)
                configureFlavors(this)
            }

//            dependencies {
//                add("testImplementation", kotlin("test"))
//                add("testImplementation", project(":testing"))
//                add("androidTestImplementation", kotlin("test"))
//                add("androidTestImplementation", project(":testing"))
//            }
        }
    }
}
