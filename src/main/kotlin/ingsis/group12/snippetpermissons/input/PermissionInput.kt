package ingsis.group12.snippetpermissons.input

import ingsis.group12.snippetpermissons.model.PermissionType
import jakarta.validation.constraints.NotNull

// This properties must be nullables in order to make the validator works, in case they are null it will send http 400 error
data class PermissionInput(
    @field:NotNull(message = "property 'permissions' cannot be null.")
    val permission: PermissionType?,
)
