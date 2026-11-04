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

## Apa yang diubah dalam branch `irama-redesign` (langkah 1 sahaja)
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

## Status fork di GitHub (disemak 2026-11-04, 10:44+)
- Identiti GitHub disahkan: `daddyizz` (https://github.com/daddyizz).
- **Fork melalui connector Muse GAGAL: 403 "Resource not accessible by
  integration".** Token GitHub App Muse tidak ada kebenaran fork repo ini.
- Jadi fork di github.com BELUM wujud. Kerja di atas hanya wujud sebagai
  branch tempatan `irama-redesign` dalam `workspace/irama-fork/myspoti/`
  (clone upstream commit `8e8898a`).
- Untuk push ke GitHub, Daddy perlu, dari browser sendiri:
  1. Buka https://github.com/kota-zo/myspoti dan tekan **Fork** → akaun daddyizz.
  2. Dalam GitHub App Muse, pilih repo fork itu (Select repositories).
  3. Beritahu Oyen "fork dah siap" — barulah Oyen cuba push branch ini melalui
     connector, dan semak semula di GitHub sebelum kata siap.

## Build APK (belum boleh disahkan di VM ini)
- VM ini tiada JDK dan tiada Android SDK (`java: command not found`, tiada
  `~/Android`), jadi `./gradlew assembleDebug` TIDAK dapat dijalankan di sini.
- Jangan anggap branch ini "siap compile" sehingga build sebenar dijalankan.
- Build sebenar (ikut README upstream): JDK 17+, Android SDK, compileSdk 37,
  kemudian dari `base/`: `./gradlew assembleDebug`.
  Atau guna GitHub Actions dalam fork selepas push — itu jalan paling bersih
  untuk Daddy tanpa Android Studio.
