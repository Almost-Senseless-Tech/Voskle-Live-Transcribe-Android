package tech.almost_senseless.voskle.data

import android.content.Context
import tech.almost_senseless.voskle.R

enum class Languages(val id: Int, val modelPath: String) {
    ARABIC(R.string.lang_ar, "vosk-model-ar-mgb2-0.4"),
    BRETON(R.string.lang_br, "vosk-model-br-0.7"),
    CATALAN(R.string.lang_ca, "vosk-model-small-ca-0.4"),
    CHINESE(R.string.lang_cn, "vosk-model-small-cn-0.22"),
    CZECH(R.string.lang_cs, "vosk-model-small-cs-0.4-rhasspy"),
    GERMAN(R.string.lang_de, "vosk-model-small-de-0.15"),
    ENGLISH_IN(R.string.lang_en_in, "vosk-model-small-en-in-0.4"),
    ENGLISH_US(R.string.lang_en_us, "vosk-model-small-en-us-0.15"),
    SPANISH(R.string.lang_es, "vosk-model-small-es-0.42"),
    PERSIAN(R.string.lang_fa, "vosk-model-small-fa-0.4"),
    FRENCH(R.string.lang_fr, "vosk-model-small-fr-0.22"),
    GUJARATI(R.string.lang_gu, "vosk-model-small-gu-0.42"),
    HINDI(R.string.lang_hi, "vosk-model-small-hi-0.22"),
    ITALIAN(R.string.lang_it, "vosk-model-small-it-0.22"),
    JAPANESE(R.string.lang_ja, "vosk-model-small-ja-0.22"),
    KOREAN(R.string.lang_ko, "vosk-model-small-ko-0.22"),
    KAZAKH(R.string.lang_kz, "vosk-model-small-kz-0.15"),
    POLISH(R.string.lang_pl, "vosk-model-small-pl-0.22"),
    PORTUGUESE_BR(R.string.lang_pt, "vosk-model-small-pt-0.3"),
    DUTCH(R.string.lang_nl, "vosk-model-small-nl-0.22"),
    RUSSIAN(R.string.lang_ru, "vosk-model-small-ru-0.22"),
    SWEDISH(R.string.lang_sv, "vosk-model-small-sv-rhasspy-0.15"),
    TURKISH(R.string.lang_tr, "vosk-model-small-tr-0.3"),
    UKRAINIAN(R.string.lang_uk, "vosk-model-small-uk-v3-nano"),
    UZBEK(R.string.lang_uz, "vosk-model-small-uz-0.22"),
    VIETNAMESE(R.string.lang_vn, "vosk-model-small-vn-0.4");

    lateinit var langName: String
        private set

    companion object {
        fun initialize(context: Context) {
            for (value in Languages.values()) value.langName = context.getString(value.id)
        }
    }
}