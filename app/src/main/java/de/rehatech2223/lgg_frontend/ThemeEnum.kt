package de.rehatech2223.lgg_frontend

/**
 * Enum for managing available color-schemes.
 * implements "from" infix function for getting the corresponding
 * ThemeEnum when given its integer value.
 *
 * @author Fynn Debus
 */
enum class ThemeEnum(val theme: Int) {
    DEFAULT(R.style.Theme_LGG_Frontend),
    LIGHT(R.style.Theme_LGG_Frontend_Light_Theme),
    HIGH_CONTRAST_ONE(R.style.Theme_LGG_Frontend_High_Contrast_One),
    COLOR_BLIND(R.style.Theme_LGG_Frontend_Color_Blind),
    BLACK_WHITE(R.style.Theme_LGG_Frontend_Black_White);

    companion object{
        infix fun from(value: Int): ThemeEnum = ThemeEnum.values().firstOrNull { it.theme == value } ?: DEFAULT
    }
}