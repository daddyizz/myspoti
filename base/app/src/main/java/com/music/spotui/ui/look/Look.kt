package com.music.spotui.ui.look

import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import app.myspoti.engine.R
import com.music.spotui.data.preferences.Look
import com.music.spotui.data.preferences.LookPref

/** The look the screens below are drawn in; see [Look]. */
val LocalLook = compositionLocalOf { Look.CLASSIC }

/** Hands the chosen look ([LookPref.look]) to everything inside. */
@Composable
fun ProvideLook(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLook provides LookPref.look, content = content)
}

/**
 * The Immersive look's typefaces (IRAMA phase 3, replacing upstream Inter):
 * display = Sora, body = Plus Jakarta Sans, both SIL Open Font License 1.1
 * (licenses/Sora-OFL.txt, licenses/PlusJakartaSans-OFL.txt). Both are variable
 * files with a weight axis only (no optical-size axis, unlike Inter), so the
 * variation settings carry weight alone. Classic is untouched: it does not use
 * these families.
 */
@OptIn(androidx.compose.ui.text.ExperimentalTextApi::class)
private fun variableFont(resId: Int, weight: Int) = Font(
    resId,
    weight = FontWeight(weight),
    variationSettings = androidx.compose.ui.text.font.FontVariation.Settings(
        androidx.compose.ui.text.font.FontVariation.weight(weight),
    ),
)

private val weights = listOf(400, 500, 600, 700, 800)

/** Body text, captions, buttons. */
val ImmersiveSans = FontFamily(weights.map { variableFont(R.font.plus_jakarta_sans, it) })

/** Titles, the player's title and the lyrics (20sp and up). */
val ImmersiveDisplay = FontFamily(weights.map { variableFont(R.font.sora, it) })

/**
 * The phone's "Remove animations" setting, read once at the root (see App) for both looks.
 * Null where nothing provides it: [rememberReduceMotion] then reads the setting itself.
 */
val LocalReduceMotion = compositionLocalOf<Boolean?> { null }

/**
 * True when the phone asks for no animation ("Remove animations": the animator duration
 * scale is 0). Read once per composition; the setting rarely changes while a screen is up.
 */
@Composable
fun rememberReduceMotion(): Boolean = LocalReduceMotion.current ?: rememberReduceMotionSetting()

@Composable
private fun rememberReduceMotionSetting(): Boolean {
    val context = LocalContext.current
    return remember(context) {
        Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
    }
}
