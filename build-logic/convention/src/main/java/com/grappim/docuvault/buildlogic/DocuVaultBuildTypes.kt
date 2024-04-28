package com.grappim.docuvault.buildlogic

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class DocuVaultBuildTypes(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}
