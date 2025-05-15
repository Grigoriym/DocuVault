import com.grappim.docuvault.buildlogic.configureKotlinJvm
import com.grappim.docuvault.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class JavaLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("java-library")
                apply("org.jetbrains.kotlin.jvm")
                apply("org.jetbrains.kotlinx.kover")
            }
            configureKotlinJvm()

            dependencies {
                "implementation"(libs.findLibrary("kotlinx.coroutines.core").get())
            }
        }
    }
}
