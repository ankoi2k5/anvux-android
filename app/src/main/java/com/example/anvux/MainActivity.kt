package com.example.anvux

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.anvux.core.network.ApiConfig
import com.example.anvux.core.ui.theme.AnVuxTheme
import com.example.anvux.navigation.AnVuxNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val localNetworkPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            // Không cần xử lý kết quả ở đây: nếu từ chối, kết nối tới máy dev sẽ bị chặn.
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        requestLocalNetworkAccessIfNeeded()
        setContent {
            AnVuxTheme {
                AnVuxNavHost()
            }
        }
    }

    /**
     * Android 17 trở lên chặn app (targetSdk 37) kết nối tới mạng nội bộ, kể cả 10.0.2.2 của emulator,
     * nếu chưa có quyền này. Chỉ cần khi backend đang chạy trên máy dev.
     */
    private fun requestLocalNetworkAccessIfNeeded() {
        if (!ApiConfig.isLocalServer) return
        if (Build.VERSION.SDK_INT < 37) return
        if (checkSelfPermission(LOCAL_NETWORK_PERMISSION) == PackageManager.PERMISSION_GRANTED) return
        localNetworkPermissionLauncher.launch(LOCAL_NETWORK_PERMISSION)
    }

    private companion object {
        const val LOCAL_NETWORK_PERMISSION = "android.permission.ACCESS_LOCAL_NETWORK"
    }
}