package com.rakamin.newsapp.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================================
// Mandiri News Brand Colors
// ============================================================================

/** Mandiri Royal Blue - Primary action, TopBar, active state */
val MandiriBlue = Color(0xFF0A3977)
val MandiriBlueContainer = Color(0xFF002452)
val OnMandiriBlueContainer = Color(0xFF82A5E9)

/** Radiant Amber Gold - Breaking news, highlight badges, callouts */
val RadiantGold = Color(0xFFFFB81C)
val RadiantGoldContainer = Color(0xFFFEB71A)
val OnRadiantGoldContainer = Color(0xFF6B4B00)

/** Mid-tier Sapphire Blue - Category tags, secondary links */
val SapphireBlue = Color(0xFF005696)
val SapphireBlueContainer = Color(0xFF002647)
val OnSapphireBlueContainer = Color(0xFF6EA8ED)

// ============================================================================
// Neutral & Surface Colors (Light Theme)
// ============================================================================
val BackgroundLight = Color(0xFFF8FAFC)        // Slate Tint 50 (soft canvas)
val OnBackgroundLight = Color(0xFF0F172A)      // Slate 900 (headline & body text)

val SurfaceLight = Color(0xFFFFFFFF)           // Pure White card surface
val OnSurfaceLight = Color(0xFF131B2E)

val SurfaceVariantLight = Color(0xFFDAE2FD)
val OnSurfaceVariantLight = Color(0xFF475569)   // Slate 600 (muted text & meta)

// Surface Container Elevation Scale
val SurfaceContainerLowestLight = Color(0xFFFFFFFF)
val SurfaceContainerLowLight = Color(0xFFF1F5F9)  // Standard feed card / input fill
val SurfaceContainerLight = Color(0xFFEAEDFF)
val SurfaceContainerHighLight = Color(0xFFE2E8F0) // Subtle dividers & borders
val SurfaceContainerHighestLight = Color(0xFFDAE2FD)

val OutlineLight = Color(0xFF747781)
val OutlineVariantLight = Color(0xFFC3C6D2)

// ============================================================================
// Dark Theme Colors
// ============================================================================
val MandiriBlueDark = Color(0xFFACC7FF)
val RadiantGoldDark = Color(0xFFFFBA2C)
val SapphireBlueDark = Color(0xFFA1C9FF)

val BackgroundDark = Color(0xFF10141D)
val OnBackgroundDark = Color(0xFFEEF0FF)

val SurfaceDark = Color(0xFF131B2E)
val OnSurfaceDark = Color(0xFFEEF0FF)
val SurfaceVariantDark = Color(0xFF283044)
val OnSurfaceVariantDark = Color(0xFFC3C6D2)

// ============================================================================
// System Colors (Error)
// ============================================================================
val ErrorRed = Color(0xFFBA1A1A)
val OnErrorWhite = Color(0xFFFFFFFF)
val ErrorContainerRed = Color(0xFFFFDAD6)
val OnErrorContainerRed = Color(0xFF93000A)

// ============================================================================
// Legacy Aliases (agar kode lama yang memanggil Primary/Background tetap jalan)
// ============================================================================
val Primary = MandiriBlue
val Background = BackgroundLight
