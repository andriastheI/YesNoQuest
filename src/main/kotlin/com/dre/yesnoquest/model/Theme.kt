/**
 * Filename: Theme.kt
 * Author: Andrias Zelele
 * Date: 2026-03-03
 *
 * Description:
 * Enum representing the available visual themes for generated Yes/No HTML files
 * in the YesNoQuest application.
 *
 * Each theme contains:
 * - A programmatic enum name (used internally)
 * - A human-readable label (used for UI display)
 *
 * Responsibilities:
 * - Define the supported themes in a type-safe way
 * - Provide display-friendly names for frontend templates
 * - Prevent invalid theme values from being submitted
 *
 * Design Notes:
 * - Used in HomeController as a @RequestParam
 * - Spring automatically converts request parameter strings to enum values
 * - HtmlBuilder uses this enum to determine styling logic
 */

package com.dre.yesnoquest.model

/**
 * Defines the available visual themes for HTML generation.
 *
 * @property label Human-readable name displayed in UI dropdown.
 */
enum class Theme(val label: String) {

    /** Neon-inspired glowing UI theme */
    NEON("Neon Night"),

    /** Dark mode styled theme */
    DARK("Dark Mode"),

    /** Clean and minimal modern theme */
    MINIMAL("Minimal Clean"),

    /** Retro arcade aesthetic theme */
    RETRO("Retro Arcade")
}