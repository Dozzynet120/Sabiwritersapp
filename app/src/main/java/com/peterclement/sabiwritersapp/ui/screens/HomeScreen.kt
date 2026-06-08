package com.peterclement.sabiwritersapp.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.peterclement.sabiwritersapp.R
import com.peterclement.sabiwritersapp.ui.navigation.Routes

// ============================================
// DATA MODEL
// ============================================
data class ServiceItem(
    val id: Int,
    val title: String,
    val description: String,
    val imageRes: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color
)

// ============================================
// COLOR PALETTE - Modern Professional Theme
// ============================================
object AppColors {
    val Primary = Color(0xFF0D47A1)        // Deep Blue
    val PrimaryDark = Color(0xFF0A1929)    // Navy
    val Secondary = Color(0xFF00BFA5)      // Teal Accent
    val Accent = Color(0xFFFF6F00)         // Orange
    val Surface = Color(0xFFF8FAFC)        // Light Gray-White
    val OnSurface = Color(0xFF1E293B)      // Dark Slate
    val OnSurfaceVariant = Color(0xFF64748B) // Gray
    val CardBackground = Color(0xFFFFFFFF) // White
    val Success = Color(0xFF10B981)          // Green
    val Warning = Color(0xFFF59E0B)          // Amber
}

// ============================================
// MAIN HOMESCREEN
// ============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    val services = remember {
        listOf(
            ServiceItem(1, "Book Writing", "Professional book writing & publishing support.", R.drawable.bookwriting, Icons.Outlined.MenuBook, Color(0xFF3B82F6)),
            ServiceItem(2, "Business Writing", "Reports, proposals, and corporate documents.", R.drawable.businesswriting, Icons.Outlined.Business, Color(0xFF10B981)),
            ServiceItem(3, "Editing", "Error-free and polished documents.", R.drawable.editing_proofreading, Icons.Outlined.Edit, Color(0xFFF59E0B)),
            ServiceItem(4, "Academic Writing", "Research papers and thesis assistance.", R.drawable.academic, Icons.Outlined.School, Color(0xFF8B5CF6)),
            ServiceItem(5, "Content Writing", "Blogs, articles, and web content.", R.drawable.content, Icons.Outlined.Article, Color(0xFFEC4899)),
            ServiceItem(6, "Script Writing", "Film, media, and stage scripts.", R.drawable.script, Icons.Outlined.Movie, Color(0xFF06B6D4)),
            ServiceItem(7, "Political Writing", "Speeches, campaigns, and policy documents.", R.drawable.political, Icons.Outlined.Campaign, Color(0xFFEF4444)),
            ServiceItem(8, "Transcription", "Audio & video to text services.", R.drawable.transcript, Icons.Outlined.Mic, Color(0xFF84CC16)),
            ServiceItem(9, "Content Design", "Creative content layout & visuals.", R.drawable.content, Icons.Outlined.DesignServices, Color(0xFF14B8A6)),
            ServiceItem(10, "Consultancy", "Professional writing guidance.", R.drawable.writing, Icons.Outlined.Lightbulb, Color(0xFFF97316))
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "SabiWriters",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AppColors.Primary
                            )
                        )
                        Text(
                            text = "Welcome back, ${currentUser?.displayName ?: "Writer"}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = AppColors.OnSurfaceVariant
                            )
                        )
                    }
                },
                navigationIcon = {
                    Image(
                        painter = painterResource(R.drawable.sabiwriterslogo),
                        contentDescription = "Sabi Writers Logo",
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            auth.signOut()
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = AppColors.Primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.Surface
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = AppColors.CardBackground,
                tonalElevation = 8.dp
            ) {
                Button(
                    onClick = { navController.navigate(Routes.BOOKING) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.Primary,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Book a Service", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        },
        containerColor = AppColors.Surface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Stats Row
            StatsRow()

            Spacer(modifier = Modifier.height(20.dp))

            // Section Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Our Services",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = AppColors.OnSurface
                    )
                )
                Text(
                    text = "${services.size} available",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = AppColors.OnSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Service Grid with PlaceCards
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(services, key = { it.id }) { service ->
                    PlaceCard(service = service)
                }
            }
        }
    }
}

// ============================================
// STATS ROW - Modern Dashboard Style
// ============================================
@Composable
fun StatsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Book,
            value = "10+",
            label = "Services",
            color = AppColors.Primary
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Star,
            value = "4.9",
            label = "Rating",
            color = AppColors.Accent
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.People,
            value = "500+",
            label = "Clients",
            color = AppColors.Success
        )
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = AppColors.OnSurface
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = AppColors.OnSurfaceVariant
                )
            )
        }
    }
}

// ============================================
// PLACECARD - Modern Professional Service Card
// ============================================
@Composable
fun PlaceCard(service: ServiceItem) {
    var expanded by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable {
                expanded = !expanded
                pressed = true
            }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardBackground),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Column {
            // Image Section with Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Image(
                    painter = painterResource(service.imageRes),
                    contentDescription = service.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Gradient overlay for text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    service.color.copy(alpha = 0.8f)
                                ),
                                startY = 50f
                            )
                        )
                )

                // Service Icon Badge
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .size(36.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.95f),
                            shape = CircleShape
                        )
                        .align(Alignment.TopStart),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = service.icon,
                        contentDescription = null,
                        tint = service.color,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Title on image
                Text(
                    text = service.title,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Content Section
            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                // Category Tag
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = service.color.copy(alpha = 0.12f),
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(
                        text = "Writing Service",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = service.color,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Description (always visible, truncated)
                Text(
                    text = service.description,
                    color = AppColors.OnSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    maxLines = if (expanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Expanded Content
                if (expanded) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Divider(color = AppColors.Surface, thickness = 1.dp)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = null,
                                tint = AppColors.Success,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Fast Delivery",
                                color = AppColors.OnSurfaceVariant,
                                fontSize = 12.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = null,
                                tint = AppColors.Primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Expert Writers",
                                color = AppColors.OnSurfaceVariant,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { /* Navigate to service detail */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = service.color,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Learn More", fontWeight = FontWeight.Medium)
                    }
                }

                // Expand/Collapse Indicator
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = AppColors.OnSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }

    // Reset pressed state after animation
    LaunchedEffect(pressed) {
        if (pressed) {
            kotlinx.coroutines.delay(150)
            pressed = false
        }
    }
}