package ingsis.group12.snippetpermissons.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import ingsis.group12.snippetpermissons.exception.InvalidPermissionException

enum class PermissionType(val value: String) {
    READ("read"),
    WRITE("write"),
    READ_WRITE("read:write"),
    CHANGE_RULES("change:rules"),
    ;

    @JsonValue
    fun toValue(): String {
        return this.value
    }

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromValue(value: String): PermissionType {
            return entries.firstOrNull { it.value == value }
                ?: throw InvalidPermissionException(value)
        }
    }
}
