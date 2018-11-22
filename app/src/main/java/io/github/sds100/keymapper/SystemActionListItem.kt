package io.github.sds100.keymapper

import io.github.sds100.keymapper.Utils.SystemActionUtils

/**
 * Created by sds100 on 31/07/2018.
 */

data class SystemActionListItem(@SystemAction.SystemActionId val action: String,
                                val stringId: Int,
                                val iconId: Int?,
                                val isValid: Boolean = true) {

    constructor(@SystemAction.SystemActionId action: String, isValid: Boolean = true) : this(
            action,
            SystemActionUtils.getDescription(action),
            SystemActionUtils.getIconResource(action),
            isValid
    )
}