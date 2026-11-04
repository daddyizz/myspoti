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
 * Font stays bundled Inter (Look.kt) — swapping to Sora would need new font files
 * and is deliberately NOT done in this first fork step, to keep the build intact.
 */
object Immersive {
    // IRAMA colours — Malam Gelora. No Spotify green anywhere in this theme.
    val Malam = Color(0xFF140F2B)
    val JinggaSenja = Color(0xFFFF6A3D)
    val Mentari = Color(0xFFFFC53D)
    val Lavender = Color(0xFF9B8CFF)
    val Krim = Color(0xFFFFF8F0)
    val UnguRedup = Color(0xFFB8B0D6)

    // Colours (re-skinned, same names as upstream).
    val Page = Malam
    val Raised = Color(0xFF221A45)
    val Separator = Color.White.copy(alpha = 0.10f)
    val Text = Krim
    val TextSecondary = UnguRedup.copy(alpha = 0.85f)
    val TextTertiary = Color.White.copy(alpha = 0.38f)
    /** IRAMA's own Jingga Senja until an Immersive accent colour is chosen in Settings, then that colour (links, the selected tab, toggles). */
    val Accent: Color get() = com.music.spotui.data.api.SupportReminder.run {
        if (immersiveAccentChosen) immersiveAccent.color else JinggaSenja
    }
    val GlassFill = Color(0xFF2E2360).copy(alpha = 0.88f)
    val GlassEdge = Color.White.copy(alpha = 0.12f)
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

    // Type (Inter).
    private val base = TextStyle(fontFamily = ImmersiveSans, color = Text)
    private val display = TextStyle(fontFamily = ImmersiveDisplay, color = Text)
    val LargeTitle = display.copy(fontSize = 32.sp, lineHeight = 38.sp, fontWeight = FontWeight.Bold)
    val Title = display.copy(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold)
    val Headline = display.copy(fontSize = 20.sp, lineHeight = 25.sp, fontWeight = FontWeight.SemiBold)
    val Body = base.copy(fontSize = 16.sp, lineHeight = 21.sp)
    val BodyStrong = Body.copy(fontWeight = FontWeight.SemiBold)
    val Subhead = base.copy(fontSize = 14.sp, lineHeight = 19.sp, color = TextSecondary)
    val Caption = base.copy(fontSize = 12.sp, lineHeight = 16.sp, color = TextSecondary)
    val Lyric = display.copy(fontSize = 30.sp, lineHeight = 36.sp, fontWeight = FontWeight.Bold)

    // Spacing.
    val Gutter = 20.dp
    val RowHeight = 60.dp
    /** Room the floating tab bar and mini player take at the bottom of every page. */
    val BottomChrome = 170.dp
}

/** A translucent bar or capsule (tab bar, mini player, menus). */
fun Modifier.glass(shape: Shape = CircleShape): Modifier =
    this.background(Immersive.GlassFill, shape).border(1.dp, Immersive.GlassEdge, shape)
