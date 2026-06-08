package com.peterclement.sabiwritersapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BrandGreen = Color(0xFF0B3D2E)
private val BrandGold = Color(0xFFF4C430)
private val BrandGreenLight = Color(0xFFE8F5E9)
private val SurfaceLight = Color(0xFFF8F9FA)
private val TextPrimary = Color(0xFF1C1C1E)
private val TextSecondary = Color(0xFF6B7280)
private val PendingOrange = Color(0xFFF59E0B)
private val CompletedGreen = Color(0xFF059669)
private val ErrorRed = Color(0xFFDC2626)

// Mock data for demo - replace with real ViewModel data
data class BookingItem(
    val id: String,
    val name: String,
    val email: String,
    val service: String,
    val date: String,
    val status: BookingStatus
)

enum class BookingStatus {
    PENDING, COMPLETED, CANCELLED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen() {
    // Mock data - replace with actual ViewModel state
    val bookings = remember {
        listOf(
            BookingItem("1", "John Doe", "john@email.com", "Academic Writing", "Jun 8, 2026", BookingStatus.PENDING),
            BookingItem("2", "Jane Smith", "jane@email.com", "Business Writing", "Jun 7, 2026", BookingStatus.COMPLETED),
            BookingItem("3", "Mike Johnson", "mike@email.com", "Resume Writing", "Jun 6, 2026", BookingStatus.PENDING),
            BookingItem("4", "Sarah Williams", "sarah@email.com", "Thesis/Dissertation", "Jun 5, 2026", BookingStatus.COMPLETED),
            BookingItem("5", "David Brown", "david@email.com", "Content Writing", "Jun 4, 2026", BookingStatus.CANCELLED)
        )
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("All", "Pending", "Completed", "Cancelled")

    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { contentVisible = true }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Admin Dashboard",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BrandGreen
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(SurfaceLight)
        ) {
            // Stats Row
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(600)) + slideInVertically(
                    initialOffsetY = { -20 },
                    animationSpec = tween(600)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Total",
                        value = "24",
                        icon = Icons.Default.Book,
                        color = BrandGreen
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Pending",
                        value = "8",
                        icon = Icons.Default.Schedule,
                        color = PendingOrange
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Done",
                        value = "14",
                        icon = Icons.Default.CheckCircle,
                        color = CompletedGreen
                    )
                }
            }

            // Tab Row
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(600, delayMillis = 200))
            ) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = BrandGreen,
                    edgePadding = 16.dp,
                    indicator = { tabPositions ->
                        // FIXED: Use TabRowDefaults.Indicator with custom modifier
                        // tabIndicatorOffset is accessed via TabRowDefaults.tabIndicatorOffset
                        TabRowDefaults.Indicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.Start)
                                .offset(x = tabPositions[selectedTab].left),
                            color = BrandGold,
                            height = 3.dp
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    title,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            }
                        )
                    }
                }
            }

            // Bookings List
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(600, delayMillis = 300)) + slideInVertically(
                    initialOffsetY = { 30 },
                    animationSpec = tween(600, delayMillis = 300)
                )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val filteredBookings = when (selectedTab) {
                        1 -> bookings.filter { it.status == BookingStatus.PENDING }
                        2 -> bookings.filter { it.status == BookingStatus.COMPLETED }
                        3 -> bookings.filter { it.status == BookingStatus.CANCELLED }
                        else -> bookings
                    }

                    items(filteredBookings, key = { it.id }) { booking ->
                        BookingCard(booking = booking)
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )

            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary
                )
            )
        }
    }
}

@Composable
private fun BookingCard(booking: BookingItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status Indicator
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        when (booking.status) {
                            BookingStatus.PENDING -> PendingOrange.copy(alpha = 0.1f)
                            BookingStatus.COMPLETED -> CompletedGreen.copy(alpha = 0.1f)
                            BookingStatus.CANCELLED -> ErrorRed.copy(alpha = 0.1f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (booking.status) {
                        BookingStatus.PENDING -> Icons.Default.Schedule
                        BookingStatus.COMPLETED -> Icons.Default.CheckCircle
                        BookingStatus.CANCELLED -> Icons.Default.Cancel
                    },
                    contentDescription = null,
                    tint = when (booking.status) {
                        BookingStatus.PENDING -> PendingOrange
                        BookingStatus.COMPLETED -> CompletedGreen
                        BookingStatus.CANCELLED -> ErrorRed
                    },
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = booking.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = booking.service,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary
                    )
                )
                Text(
                    text = booking.date,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary
                    )
                )
            }

            // Status Chip
            AssistChip(
                onClick = { },
                label = {
                    Text(
                        text = booking.status.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = when (booking.status) {
                        BookingStatus.PENDING -> PendingOrange.copy(alpha = 0.15f)
                        BookingStatus.COMPLETED -> CompletedGreen.copy(alpha = 0.15f)
                        BookingStatus.CANCELLED -> ErrorRed.copy(alpha = 0.15f)
                    },
                    labelColor = when (booking.status) {
                        BookingStatus.PENDING -> PendingOrange
                        BookingStatus.COMPLETED -> CompletedGreen
                        BookingStatus.CANCELLED -> ErrorRed
                    }
                ),
                border = null
            )
        }
    }
}