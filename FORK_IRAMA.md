# IRAMA — fork Myspoti dengan design baru

## Asal & lesen — baca dulu, ini syarat, bukan hiasan
- Asal: **Myspoti** oleh kota-zo — https://github.com/kota-zo/myspoti
- Lesen asal: **GPL-3.0** (lihat `LICENSE` dan `AGENTS.md` di repo ini).
- Fork ini adalah **kerja terbitan (derivative work)**, bukan app bebas yang
  ditulis dari kosong. Maka:
  1. Kredit asal dikekalkan — nama Myspoti + pautan asal kekal dalam fail ini
     dan dalam kod.
  2. Fork ini mesti kekal **GPL-3.0** bila diedar, dengan kod sumber penuh
     diterbitkan. Tidak boleh ditutup, tidak boleh ditukar ke lesen proprietari
     atau permissive.
  3. Nama "IRAMA" di sini ialah nama design/tema dalam fork ini, bukan dakwaan
     bahawa kod ini ditulis dari kosong atau bebas dari Myspoti.
- Jika Daddy nak edar APK tanpa kredit / tanpa sumber GPL, Oyen tidak akan
  bantu jalan itu — itu melanggar lesen. Jalan patuh ialah di atas.

## Risiko Spotify (dari README Myspoti sendiri)
Myspoti memintas iklan Spotify; itu melanggar Terms of Use Spotify dan akaun
boleh digantung. Fork ini tidak menambah atau membuang enjin itu — ia kekal
seperti upstream. Keputusan guna / edar adalah tanggungjawab Daddy sendiri.

## Apa yang diubah dalam branch `irama-redesign` (langkah 1 + 2)

### Langkah 2 (2026-11-04 malam) — waveform disambung, orb dipakai
1. `IramaWaveform.kt` — gesture kini satu loop `awaitEachGesture` (tap +
   drag), ikut corak ThinSlider upstream: dua detector berasingan akan buat
   tap tak sampai. Callback baru: `onScrubChange` (setiap gerak drag) dan
   `onScrubEnd` (bila lepas); tanpa callback itu, tap terus `onSeek`.
2. `player/PlayerControls.kt` — `PlayerScrubber` Immersive kini guna
   `IramaWaveformScrubber` dengan progress sebenar (`scrub.shown(...)`) dan
   seek sebenar (`scrub.drag` + `scrub.release` → `actions.seekTo`). Bar
   nipis ThinSlider di player Immersive diganti; Classic dan podcast kekal
   kawalan masing-masing. Nota jujur: sheen "loading" ThinSlider semasa
   buffering tiada pada waveform — penunjuk loading masih ada di butang
   transport player.
3. `player/ImmersivePlayer.kt` — `ArtCorner` kini `Immersive.OrbCover`:
   cover full-player (portrait + landscape) jadi bulat penuh, bukan petak
   bucu 8dp. Podcast player punya `ArtCorner` sendiri tidak disentuh.
4. `shell/ImmersiveTabBar.kt` — tab bar terapung kini guna token
   `Immersive.Dock`. Bentuknya sudah kapsul di upstream (50%), jadi ini
   pertukaran token, bukan perubahan siluet.
5. Masih tidak diubah: `:api`, `:api-spotify`, `:host-android`, enjin
   playback, login, extension, `applicationId`, dan Classic look.

### Langkah 1
1. `base/app/.../ui/immersive/ImmersiveTheme.kt` — token Immersive sahaja:
   - Page: hitam → `#140F2B` (Malam Gelora)
   - Raised: `#1C1C1E` → `#221A45`; GlassFill → `#2E2360`
   - Accent default: pink `#FF4F7B` → Jingga Senja `#FF6A3D`
   - Teks: putih → krim `#FFF8F0`; teks redup → ungu redup `#B8B0D6`
   - Card/Button: 14dp → 28dp; Cover: 6/10/14dp → 12/20/28dp
   - Token baru: `OrbCover` (CircleShape), `Dock` (999dp), `Mentari`, `Lavender`
   - Nama field, struktur `object Immersive`, dan semua call site TIDAK diubah
     — jadi 700+ rujukan Immersive terus compile seperti biasa.
2. `base/app/.../ui/immersive/components/IramaWaveform.kt` — BARU, tambahan
   sahaja: waveform scrubber (progress + onSeek). Belum disambung ke player
   sebenar; sambungan itu langkah 2, di skrin player Immersive.
3. Tiada perubahan pada: `:api`, `:api-spotify`, `:host-android`, enjin playback,
   login, extension, atau `applicationId` (`app.myspoti` / `app.myspoti.stable`).

## Apa yang TIDAK diubah (sengaja, untuk elak pecah build)
- Font kekal Inter yang dibundel dalam `ui/look/Look.kt`. Skeleton IRAMA awal
  cadang Sora — itu perlukan fail font baru dan tidak dibuat di langkah ini.
- Classic look tidak disentuh. Hanya Immersive yang di-skin semula.
- Tiada hijau Spotify (`#1DB954` / `#22C55E`) ditambah di mana-mana.

## Eksperimen login Chrome (branch `irama-login-chrome-experiment` — BUKAN `irama-redesign`)
- Masalah: selepas install baru step 2 (2026-11-05), login Spotify melalui
  Google gagal di skrin Google "Couldn't sign you in — This browser or app
  may not be secure". Punca disahkan dari kod: login sudah HTTPS
  (`LOGIN_URL` https, manifest `usesCleartextTraffic="false"`), dan WebView
  ini sudah menyamar sebagai desktop Chrome Linux (`Fingerprint.kt`) —
  Google tetap tolak kerana ia WebView terbenam, bukan kerana http.
- Eksperimen ini (sahaja di branch eksperimen, `irama-redesign` dan backup
  `irama-step2-backup` tidak disentuh): navigasi ke `accounts.google.com`
  dalam `WebPlayerHost` dihalakan ke Custom Tab (komponen browser sebenar,
  kongsi session Chrome) melalui `shouldOverrideUrlLoading` dan, sebagai
  jaring keselamatan untuk redirect, `onPageStarted`. Dependency baru:
  `androidx.browser:browser` 1.8.0 di `:host-android` sahaja.
- Had jujur eksperimen ini: Custom Tab ada cookie jar sendiri dan app
  tidak boleh baca cookie Chrome — jadi session yang selesai di tab TIDAK
  berpindah sendiri ke WebView player. Ujian yang menentukan: (a) page
  Google terbuka dan sign-in berjaya di Custom Tab, (b) selepas balik ke
  app dan reload player, adakah player sudah login DAN lagu boleh main.
  Kalau (b) gagal, eksperimen ini gagal — kekalkan branch step 2.
- Fail diubah eksperimen ini: `WebPlayerHost.kt`,
  `base/host-android/build.gradle.kts`, `base/gradle/libs.versions.toml`,
  dan fail ini.
- v2 (2026-11-05, selepas ujian video): tekan "Continue with Google" tidak
  buat apa-apa langsung — page login kekal, tiada tab, tiada error selama
  10.9s. Punca: butang sosial Spotify buka popup window, dan WebView ini
  tidak benarkan popup (`javaScriptCanOpenWindowsAutomatically` off, tiada
  `onCreateWindow`), jadi popup itu dibuang senyap-senyap dan kod Custom
  Tab v1 tidak pernah nampak URL Google. v2 benarkan popup
  (`javaScriptCanOpenWindowsAutomatically` + `setSupportMultipleWindows`)
  dan tangkapnya di `onCreateWindow` dengan WebView sementara yang pakai
  client yang sama — `onPageStarted` WebView sementara itu akan nampak URL
  Google dan halakan ke Custom Tab seperti navigasi biasa.

## Status fork di GitHub (dikemas kini 2026-11-04 malam)
- Fork wujud: `daddyizz/myspoti`, branch `irama-redesign`, PR #1
  ("IRAMA redesign step 1") masih open, tidak di-merge.
- Langkah 1 dibuktikan compile oleh GitHub Actions: run 41779452628
  (7m 17s, success), artifact `myspoti-debug` 35.4 MB; APK itu dipasang
  dan berjalan lancar di telefon Daddy pada malam 2026-11-04.
- Langkah 2 di atas belum dibuktikan compile sehingga Actions run baharu
  (dari push langkah 2 ke branch yang sama) selesai dengan success.
- VM ini tiada JDK dan tiada Android SDK, jadi `./gradlew assembleDebug`
  TIDAK dapat dijalankan di sini — GitHub Actions kekal jalan bukti build.
