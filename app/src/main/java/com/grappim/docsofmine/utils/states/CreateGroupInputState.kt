package com.grappim.docsofmine.utils.states

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.grappim.docsofmine.uikit.theme.Dom_Aero
import com.grappim.docsofmine.uikit.utils.toDomString
import com.grappim.docsofmine.uikit.utils.toColor

interface CreateGroupInputState {
    var name: String
    var colorString: String
    val color: Color
}

class CreateGroupInputStateImpl(
    initialName: String = "",
    initialColorString: String = Dom_Aero.toDomString(),
) : CreateGroupInputState {

    private var _name by mutableStateOf(initialName)
    private var _color: String by mutableStateOf(initialColorString)

    override var name: String
        get() = _name
        set(value) {
            _name = value
        }

    override var colorString: String
        get() = _color
        set(value) {
            _color = value
        }

    override val color: Color by derivedStateOf { _color.toColor() }

}