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

## Fasa 3 + tandatangan tetap (2026-11-05)
- Fasa 3, font: `Look.kt` kini pakai Sora untuk display
  (`ImmersiveDisplay`) dan Plus Jakarta Sans untuk body
  (`ImmersiveSans`), kedua-duanya OFL (lesen di
  `base/licenses/Sora-OFL.txt` dan
  `base/licenses/PlusJakartaSans-OFL.txt`). Kedua-duanya fail variable
  dengan paksi weight sahaja (tiada opsz seperti Inter), jadi
  `variationSettings` kini bawa weight sahaja. Classic tidak disentuh.
- Nota teknikal: tool snapshot push tidak boleh bawa fail binari, jadi
  fail TTF tidak di-commit. Workflow `android.yml` (job build) kini
  muat turun kedua-dua TTF dari google/fonts pada masa build, sebelum
  sebarang tugas Gradle — `Look.kt` merujuk `R.font.sora` dan
  `R.font.plus_jakarta_sans`, dan fail itu wujud pada masa compile.
- Tandatangan tetap: masalah "package conflicts" selama ini berpunca
  daripada setiap runner Actions menandatangan APK debug dengan debug
  key sendiri. Mulai build ini, job build decode keystore tetap Daddy
  dari secret `MYSPOTI_KEYSTORE_BASE64` (nama sama yang job release
  sudah guna, bersama `MYSPOTI_KEYSTORE_PASSWORD`,
  `MYSPOTI_KEY_ALIAS`, `MYSPOTI_KEY_PASSWORD`) dan `build.gradle.kts`
  debug kini pakai signingConfig "release" bila key itu wujud, debug
  key bila tiada. `versionCode` debug kini `1000 + GITHUB_RUN_NUMBER`
  (menaik, melebihi 200 lama), supaya installer kenali build baharu
  sebagai update.
- Amaran jujur: APK pertama dengan key baharu ini MASIH memerlukan
  uninstall sekali, kerana keynya berbeza dari key runner lama yang
  menandatangan app sedang terpasang. Selepas sekali itu, APK debug
  berikutnya yang ditandatangan dengan key yang sama akan update
  tanpa uninstall — selagi keystore dan password itu tidak hilang.
  Keystore itu sendiri TIDAK di-commit ke repo.

## Fasa 4b — Home baharu: Mood hero + Sungai Baru (2026-11-05)
- Turutan Fasa 4 diputuskan Daddy 2026-11-05: Home dan Player dulu,
  tema cerah Senja kemudian (Senja perlu seni bina suis tema kerana
  `Immersive` ialah objek singleton dengan nilai tetap, dirujuk 700+
  tempat — bukan sekadar token baharu).
- `screens/ImmersiveHomeScreen.kt` sahaja disentuh; data, klik,
  prefetch dan kontrak `:api` tidak berubah:
  - Mood hero di atas grid shortcut: label "Mood hari ini", nama
    item utama pertama (bukan artis) dalam `Immersive.LargeTitle`
    (Sora), tagline IRAMA "Dengar ikut mood, bukan ikut senarai.",
    dan cover item itu sebagai orb 96dp dalam halo Jingga Senja.
    Ketik hero membuka item itu, sama seperti kadnya.
  - Carousel kedua (bukan baris artis, hanya album/playlist) kini
    baris orb "Sungai Baru": cover bulat 96dp (`Immersive.OrbCover`)
    dengan nama di bawah, mendatar, bukan kad segi empat.
    Carousel utama (kad besar) dan shelf lain tidak berubah.
- Belum dibuat dalam 4b: "Cerita Bunyi" editorial dan "Koleksi
  Anda" mini-waveform (perlu sumber data / isyarat mainan sebenar,
  bukan hiasan palsu) — dicatat sebagai kerja lanjut, bukan
  didakwa siap.

## Fasa 4c — Player baharu: orb Play + cover berputar (2026-11-05)
- `player/PlayerControls.kt` sahaja untuk kawalan: butang Play/Pause
  kini orb 72dp berisi Jingga Senja dengan ikon 36dp di tengah;
  kawasan sentuh kekal 92dp, Previous/Next kekal glyph 64dp tanpa
  isi (bentuk lengkung penuh untuk Prev/Next tidak dibuat — tiada
  bentuk lengkung dalam kod asal untuk diguna semula, dan menambah
  bentuk baharu di sini berisiko sentuh salah).
- `player/ImmersivePlayer.kt` sahaja untuk cover: orb cover (sudah
  bulat sejak Step 2) kini berputar perlahan seperti piring hitam
  selagi lagu main (satu pusingan 20 saat, Animatable dikekalkan
  supaya pause berhenti di sudut itu, bukan melompat ke sifar);
  putaran hanya di player penuh, bukan thumbnail header, bukan
  mod landskap, dan tidak kelihatan bila Canvas video sedang main
  (cover pudar di bawah klip — kelakuan asal).
- Lirik TIDAK disentuh dalam 4c: panel lirik asal sudahpun karaoke
  tengah (baris semasa Sora 30sp tebal, baris lain lebih kecil dan
  redup, teks tengah) — warna baris semasa ikut tint artwork asal,
  bukan dipaksa Jingga Senja, supaya highlight perkataan yang
  disapu (swept) tidak rosak. Waveform scrubber pula sudah siap
  sejak Step 2.

## Fasa 4a — Senja: tema cerah (2026-11-05)
- Turutan: Senja dibuat LAST dalam Fasa 4 atas pilihan Daddy
  (Home/Player dulu). Seni bina: `Immersive` ialah objek singleton
  dengan 717 tapak panggilan, jadi Senja TIDAK dibuat sebagai objek
  kedua — token warna dan style teks dalam `ImmersiveTheme.kt` kini
  getter atas `SenjaPref` (Compose state, SharedPreferences key
  `irama_senja`, default OFF = Malam tidak berubah). Suis "Senja
  light theme" dalam Settings > Look, cuma bila look Immersive.
- Palet Senja (DESIGN_SYSTEM.md): latar #FFF3E6, permukaan #FFFFFF,
  dakwat #1D1536, aksen gelap #E8491A (4.5:1, bukan #FF6A3D mentah
  atas latar cerah), lavender redup digelapkan #7A6FB0 supaya
  baki waveform masih nampak atas krim, glass putih 88%.
- Perangkap yang dielak: style teks (LargeTitle/Body/dll) pun
  getter — style yang ditangkap sekali masa startup akan kekalkan
  teks krim Malam atas latar krim Senja. Aksen pilihan pengguna
  dalam Settings (SupportReminder) kekal diutamakan atas aksen
  default kedua-dua mod.

## Benda kecil design — siap sekali gus (2026-11-05)
- Dock Play mini: `shell/FloatingTabBar.kt` dapat slot `centerAction`
  pilihan (antara pill tab dan Search), dan `shell/ImmersiveTabBar.kt`
  isikannya dengan orb Jingga Senja 48dp + ikon Play yang membuka
  player penuh (`PlayerSheet.show()`, tindakan sama seperti ketik
  mini player). Ia butang navigasi, bukan toggle play.
- Cerita Bunyi: carousel ketiga Home (`ImmersiveHomeScreen.kt`)
  kini kad editorial menegak — 3 item pertama, satu visual besar
  180dp + tajuk + subtajuk, data Spotify sebenar, tiada editorial
  rekaan. Baris artis kekal shelf.
- Koleksi Anda mini-waveform: tile shortcut yang sedang main sahaja
  tunjuk 4 bar waveform oren di sebelah nama (penunjuk sedang main);
  tile lain tiada bentuk audio palsu.
- Lengkung Prev/Next: `PlayerControls.kt` guna semula parameter orb
  4c — Previous/Next kini disk Raised 64dp berisi ikon 36dp,
  mengapit Play orb; konstanta `PLAYER_SKIP_HEIGHT_SCALE` yang
  tidak lagi diguna dibuang.

## Rebrand — nama & ikon IRAMA (2026-11-06)
- Nama: label launcher `IRAMA` (debug: `IRAMA dev`, beta:
  `IRAMA Beta`) melalui `manifestPlaceholders["appLabel"]` dalam
  `base/app/build.gradle.kts`, dan `app_name` dalam
  `res/values/strings.xml`. **applicationId TIDAK berubah**
  (`app.myspoti` beta / `app.myspoti.stable`, debug `.dev`) —
  jadi update terus install atas app lama tanpa uninstall dan
  tanpa login semula.
- Ikon: mark launcher upstream sudah berupa satu garis waveform —
  dikekalkan bentuknya, warnanya sahaja ditukar: garis hijau
  Spotify #1ED760 → Jingga Senja #FF6A3D
  (`ic_launcher_foreground.xml`), latar hitam Spotify #121212 →
  Malam #140F2B (`ic_launcher_background.xml`). Varian warna
  ikon alternatif (blue/orange/pink/purple) tidak disentuh.
- Kredit asal dikekalkan: nama Myspoti sebagai origin kekal dalam
  dokumen ini dan dalam teks About app, selaras GPL-3.0.
