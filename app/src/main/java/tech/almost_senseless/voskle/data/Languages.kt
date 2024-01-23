package tech.almost_senseless.voskle.data

enum class Languages(val langName: String, val modelPath: String) {
    BRETON("Brezhoneg", "vosk-model-br-0.7"),
    CATALAN("català", "vosk-model-small-ca-0.4"),
    CHINESE("標準漢語", "vosk-model-small-cn-0.22"),
    CZECH("čeština", "vosk-model-small-cs-0.4-rhasspy"),
    GERMAN("Deutsch", "vosk-model-small-de-0.15"),
    ENGLISH_IN("English (India)", "vosk-model-small-en-in-0.4"),
    ENGLISH_US("English (United states)", "vosk-model-small-en-us-0.15"),
    SPANISH("Español", "vosk-model-small-es-0.42"),
    PERSIAN("فارسی ", "vosk-model-small-fa-0.4"),
    FRENCH("Français", "vosk-model-small-fr-0.22"),
    ITALIAN("Italiano", "vosk-model-small-it-0.22"),
    KOREAN("한국어", "vosk-model-small-ko-0.22"),
    HINDI("हिन्दी", "vosk-model-small-hi-0.22"),
    POLISH("język polski", "vosk-model-small-pl-0.22"),
    KAZAKH("қазақша", "vosk-model-small-kz-0.15"),
    DUTCH("Nederlands", "vosk-model-small-nl-0.22"),
    JAPANESE("日本語", "vosk-model-small-ja-0.22"),
    UZBEK("Oʻzbekcha", "vosk-model-small-uz-0.22"),
    PORTUGUESE_BR("Português (Brasil)", "vosk-model-small-pt-0.3"),
    RUSSIAN("Русский", "vosk-model-small-ru-0.22"),
    SWEDISH("svenska", "vosk-model-small-sv-rhasspy-0.15"),
    VIETNAMESE("tiếng Việt", "vosk-model-small-vn-0.4"),
    TURKISH("Türkçe", "vosk-model-small-tr-0.3"),
    UKRAINIAN("українська мова", "vosk-model-small-uk-v3-nano")
}