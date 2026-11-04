package com.music.spotui.ui.immersive.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.music.spotui.ui.immersive.Immersive
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

/**
 * IRAMA waveform scrubber — the fork's signature player control.
 *
 * This is an ADDITIVE component: it does not replace the upstream player yet.
 * Wire it into the Immersive player by passing the player's real progress and
 * seek callback; until then it is a self-contained, compile-safe composable.
 * Played bars use the IRAMA accent, unplayed bars use the muted violet —
 * never a thin Spotify-style progress line, and never Spotify green.
 */
@Composable
fun IramaWaveformScrubber(
    progress: Float,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    // Stable pseudo-random bar heights (fixed seed, so the wave does not jump).
    val bars = remember {
        List(64) { i ->
            0.25f + 0.75f * abs(sin(i * 0.55f)) * (0.6f + Random(42 + i).nextFloat() * 0.4f)
        }
    }
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onSeek((offset.x / size.width).coerceIn(0f, 1f))
                }
            }
    ) {
        val gap = size.width / bars.size
        bars.forEachIndexed { i, h ->
            val x = i * gap + gap / 2
            val barH = size.height * h
            val played = (i.toFloat() / bars.size) <= progress.coerceIn(0f, 1f)
            drawLine(
                color = if (played) Immersive.Accent else Immersive.UnguRedup,
                start = Offset(x, center.y - barH / 2),
                end = Offset(x, center.y + barH / 2),
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}
