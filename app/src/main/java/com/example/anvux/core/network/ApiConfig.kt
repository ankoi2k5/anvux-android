package com.example.anvux.core.network

object ApiConfig {
    // Emulator gọi về máy tính qua 10.0.2.2. Bản release sẽ đổi sang địa chỉ HTTPS thật.
    const val BASE_URL = "http://10.0.2.2:8080/"

    /** true khi đang trỏ về máy dev trong mạng nội bộ (cần quyền mạng nội bộ trên Android 17). */
    val isLocalServer: Boolean
        get() = BASE_URL.startsWith("http://10.0.2.2") || BASE_URL.startsWith("http://localhost")
}