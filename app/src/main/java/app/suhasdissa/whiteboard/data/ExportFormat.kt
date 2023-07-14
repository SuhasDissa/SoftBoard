package app.suhasdissa.whiteboard.data

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint

enum class ExportFormat(val format: Bitmap.CompressFormat, val extension: String, var backgroundColor:Paint?) {
    PNG(Bitmap.CompressFormat.PNG, "png",null),
    JPEG(Bitmap.CompressFormat.JPEG, "jpg",Paint().apply { color = Color.WHITE })
}