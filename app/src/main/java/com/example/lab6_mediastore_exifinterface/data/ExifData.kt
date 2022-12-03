package com.example.lab6_mediastore_exifinterface.data

data class ExifData(
    var date: String?,
    var latitude: String?,
    var latitudeRef: String?,
    var longitude: String?,
    var longitudeRef: String?,
    var device: String?,
    var model: String?,
)

data class Geo(
    val latitude: Float,
    val longitude: Float,
)

fun Geo?.toString(): String {
    if (this == null) {
        return "N/A"
    }
    return "($latitude, $longitude)"
}

fun ExifData.getGeo(): Geo? {
    if (latitude == null || longitude == null) {
        return null
    }
    val latitudeDegrees = convertToDegree(latitude!!)
    val longitudeDegrees = convertToDegree(longitude!!)

    return Geo(
        if (latitudeRef == "N") latitudeDegrees else -latitudeDegrees,
        if (longitudeRef == "E") longitudeDegrees else -longitudeDegrees,
    )
}

fun ExifData.toStringPretty(): String {
    return """
        date: ${date ?: "N/A"}
        device: ${device ?: "N/A"}
        model: ${model ?: "N/A"}
        geo: ${getGeo().toString()}
    """.trimIndent()
}

private fun convertToDegree(stringDMS: String): Float {
    val dms = stringDMS.split(",")

    val floatD = dms[0].split("/")
    val d0 = floatD[0].toFloat()
    val d1 = floatD[1].toFloat()
    val d = d0 / d1

    val floatM = dms[1].split("/")
    val m0 = floatM[0].toFloat()
    val m1 = floatM[1].toFloat()
    val m = m0 / m1

    val floatS = dms[2].split("/")
    val s0 = floatS[0].toFloat()
    val s1 = floatS[1].toFloat()
    val s = s0 / s1

    return d + m / 60 + s / 3600
}

//fun EmptyExifData(): ExifData {
//    return ExifData(
//        null,
//        null,
//        null,
//        null,
//        null,
//    )
//}