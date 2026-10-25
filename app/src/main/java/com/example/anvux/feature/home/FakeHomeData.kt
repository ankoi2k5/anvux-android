package com.example.anvux.feature.home

import com.example.anvux.domain.model.TaskPriority

// Dữ liệu giả, sẽ thay bằng dữ liệu thật ở tuần 3.
fun fakeHomeState() = HomeUiState(
    userName = "bạn",
    completedToday = 2,
    totalToday = 5,
    dueSoonCount = 4,
    overdueCount = 1,
    upcomingTasks = listOf(
        HomeTask(
            id = "1",
            title = "Nộp báo cáo tiến độ đồ án",
            projectName = "Đồ án tốt nghiệp",
            dueLabel = "Hôm nay, 20:00",
            priority = TaskPriority.HIGH,
        ),
        HomeTask(
            id = "2",
            title = "Ôn tập giữa kỳ Cơ sở dữ liệu",
            projectName = "Học tập",
            dueLabel = "Ngày mai, 09:00",
            priority = TaskPriority.MEDIUM,
        ),
        HomeTask(
            id = "3",
            title = "Chuẩn bị phỏng vấn thực tập",
            projectName = "Sự nghiệp",
            dueLabel = "Thứ Sáu, 14:00",
            priority = TaskPriority.HIGH,
        ),
        HomeTask(
            id = "4",
            title = "Đọc chương 5 Kiến trúc phần mềm",
            projectName = "Học tập",
            dueLabel = "Chủ nhật",
            priority = TaskPriority.LOW,
        ),
        HomeTask(
            id = "5",
            title = "Gửi bài tập nhóm",
            projectName = "Học tập",
            dueLabel = "Trễ 1 ngày",
            priority = TaskPriority.MEDIUM,
            isOverdue = true,
        ),
    ),
)