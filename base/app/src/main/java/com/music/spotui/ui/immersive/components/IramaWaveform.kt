package com.music.spotui.ui.immersive.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.unit.dp
import com.music.spotui.ui.immersive.Immersive
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

/**
 * IRAMA waveform scrubber — the fork's signature player control.
 *
 * Wired into the Immersive player since step 2: [progress] is the player's real
 * position fraction and a tap or drag seeks for real. When [onScrubChange] is
 * supplied, a drag reports every move through it and [onScrubEnd] fires on
 * release (the player's scrub state holds the handle there until the reported
 * position settles); without those callbacks a tap seeks immediately via
 * [onSeek]. Played bars use the IRAMA accent, unplayed bars use the muted
 * violet — never a thin Spotify-style progress line, and never Spotify green.
 *
 * One gesture loop for taps and drags, as the upstream ThinSlider does: two
 * separate detectors meant taps never landed, because the drag detector took
 * the pointer and a tap has no drag to report.
 */
@Composable
fun IramaWaveformScrubber(
    progress: Float,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    onScrubChange: ((Float) -> Unit)? = null,
    onScrubEnd: (() -> Unit)? = null,
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
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val first = (down.position.x / size.width).coerceIn(0f, 1f)
                    if (onScrubChange != null) onScrubChange(first) else onSeek(first)
                    while (true) {
                        val event = awaitPointerEvent()
                        val pointer = event.changes.firstOrNull { it.id == down.id } ?: break
                        if (!pointer.pressed) {
                            pointer.consume()
                            break
                        }
                        if (pointer.positionChanged()) {
                            val fraction = (pointer.position.x / size.width).coerceIn(0f, 1f)
                            if (onScrubChange != null) onScrubChange(fraction)
                            pointer.consume()
                        }
                    }
                    if (onScrubChange != null) onScrubEnd?.invoke()
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
