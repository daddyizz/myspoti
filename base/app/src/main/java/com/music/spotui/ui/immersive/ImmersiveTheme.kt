package com.music.spotui.ui.immersive

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.music.spotui.ui.look.ImmersiveDisplay
import com.music.spotui.ui.look.ImmersiveSans

/*
 * The Immersive look's tokens, shared by every Immersive screen so they read as one app.
 *
 * IRAMA redesign (fork of Myspoti, GPL-3.0 — see FORK_IRAMA.md): the Immersive look
 * is re-skinned, not rewritten. Same object, same field names, so all 700+ Immersive
 * call sites keep compiling and the :api contract is untouched.
 * Before: black page, pink accent, 14dp cards.
 * After: "Malam Gelora" violet night, Jingga Senja accent, 28dp cards, orb covers.
 * Fonts are Sora (display) + Plus Jakarta Sans (body) since phase 3 (Look.kt).
 *
 * IRAMA phase 4a — Senja: the colour tokens below are getters over [SenjaPref], not
 * fixed values, so flipping the "Senja light theme" switch in Settings repaints every
 * one of the 700+ call sites without touching them. The switch is Compose state
 * (see SenjaPref), so composables reading a token redraw when it flips. The text
 * styles are getters for the same reason: a style captured once at startup would
 * keep Malam's cream text on Senja's cream page. Malam is the default, so nothing
 * changes for anyone who has not turned Senja on.
 */
object Immersive {
    // IRAMA colours — Malam Gelora. No Spotify green anywhere in this theme.
    val Malam = Color(0xFF140F2B)
    val Mentari = Color(0xFFFFC53D)
    val Lavender = Color(0xFF9B8CFF)
    val Krim = Color(0xFFFFF8F0)
    /** Senja (light) ink, from DESIGN_SYSTEM.md. */
    val SenjaInk = Color(0xFF1D1536)

    // Accent and muted lavender follow the mode: the darkened Jingga Senja (#E8491A)
    // keeps 4.5:1 contrast for text and buttons on the light page, and the muted
    // lavender is darkened so the waveform's unplayed bars still read on cream.
    val JinggaSenja: Color
        get() = if (com.music.spotui.data.preferences.SenjaPref.on) Color(0xFFE8491A) else Color(0xFFFF6A3D)
    val UnguRedup: Color
        get() = if (com.music.spotui.data.preferences.SenjaPref.on) Color(0xFF7A6FB0) else Color(0xFFB8B0D6)

    // Colours (re-skinned, same names as upstream).
    val Page: Color get() = if (com.music.spotui.data.preferences.SenjaPref.on) Color(0xFFFFF3E6) else Malam
    val Raised: Color get() = if (com.music.spotui.data.preferences.SenjaPref.on) Color.White else Color(0xFF221A45)
    val Separator: Color
        get() = if (com.music.spotui.data.preferences.SenjaPref.on) Color.Black.copy(alpha = 0.10f)
        else Color.White.copy(alpha = 0.10f)
    val Text: Color get() = if (com.music.spotui.data.preferences.SenjaPref.on) SenjaInk else Krim
    val TextSecondary: Color
        get() = if (com.music.spotui.data.preferences.SenjaPref.on) SenjaInk.copy(alpha = 0.65f)
        else UnguRedup.copy(alpha = 0.85f)
    val TextTertiary: Color
        get() = if (com.music.spotui.data.preferences.SenjaPref.on) Color.Black.copy(alpha = 0.38f)
        else Color.White.copy(alpha = 0.38f)
    /** IRAMA's own Jingga Senja until an Immersive accent colour is chosen in Settings, then that colour (links, the selected tab, toggles). */
    val Accent: Color get() = com.music.spotui.data.api.SupportReminder.run {
        if (immersiveAccentChosen) immersiveAccent.color else JinggaSenja
    }
    val GlassFill: Color
        get() = if (com.music.spotui.data.preferences.SenjaPref.on) Color.White.copy(alpha = 0.88f)
        else Color(0xFF2E2360).copy(alpha = 0.88f)
    val GlassEdge: Color
        get() = if (com.music.spotui.data.preferences.SenjaPref.on) Color.Black.copy(alpha = 0.12f)
        else Color.White.copy(alpha = 0.12f)
    /** The red of a destructive swipe, menu entry or confirmation (Leave, Remove, an error). */
    val Destructive = Color(0xFFFF453A)

    // Shapes — IRAMA: softer, larger radii; orb is a full circle for the player.
    val CoverSmall: Shape = RoundedCornerShape(12.dp)
    val CoverMedium: Shape = RoundedCornerShape(20.dp)
    val CoverLarge: Shape = RoundedCornerShape(28.dp)
    val OrbCover: Shape = CircleShape
    val Card: Shape = RoundedCornerShape(28.dp)
    val Button: Shape = RoundedCornerShape(28.dp)
    /** Floating dock capsule (bottom navigation), not a full-width bar. */
    val Dock: Shape = RoundedCornerShape(999.dp)

    // Type (Sora display + Plus Jakarta Sans body, since phase 3). Getters, not vals:
    // a style captured once would freeze the text colour of the mode active at startup.
    private val base: TextStyle get() = TextStyle(fontFamily = ImmersiveSans, color = Text)
    private val display: TextStyle get() = TextStyle(fontFamily = ImmersiveDisplay, color = Text)
    val LargeTitle: TextStyle get() = display.copy(fontSize = 32.sp, lineHeight = 38.sp, fontWeight = FontWeight.Bold)
    val Title: TextStyle get() = display.copy(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold)
    val Headline: TextStyle get() = display.copy(fontSize = 20.sp, lineHeight = 25.sp, fontWeight = FontWeight.SemiBold)
    val Body: TextStyle get() = base.copy(fontSize = 16.sp, lineHeight = 21.sp)
    val BodyStrong: TextStyle get() = Body.copy(fontWeight = FontWeight.SemiBold)
    val Subhead: TextStyle get() = base.copy(fontSize = 14.sp, lineHeight = 19.sp, color = TextSecondary)
    val Caption: TextStyle get() = base.copy(fontSize = 12.sp, lineHeight = 16.sp, color = TextSecondary)
    val Lyric: TextStyle get() = display.copy(fontSize = 30.sp, lineHeight = 36.sp, fontWeight = FontWeight.Bold)

    // Spacing.
    val Gutter = 20.dp
    val RowHeight = 60.dp
    /** Room the floating tab bar and mini player take at the bottom of every page. */
    val BottomChrome = 170.dp
}

/** A translucent bar or capsule (tab bar, mini player, menus). */
fun Modifier.glass(shape: Shape = CircleShape): Modifier =
    this.background(Immersive.GlassFill, shape).border(1.dp, Immersive.GlassEdge, shape)
