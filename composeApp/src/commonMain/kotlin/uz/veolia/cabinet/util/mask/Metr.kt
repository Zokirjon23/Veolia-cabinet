package uz.veolia.cabinet.util.mask

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CurrencyMaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText = maskFilter(text)

    private fun maskFilter(text: AnnotatedString): TransformedText {
        var out = ""
        for (i in text.text.indices) {
            if (i == 0) out += "$ "
            out += text.text[i]
        }

        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return offset
                if (offset <= 2) return offset + 1
                return text.text.length + 2
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return offset
                if (offset <= 2) return offset - 1
                return text.text.length
            }
        }

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }
}