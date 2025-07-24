package deuce.by.malbona

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform