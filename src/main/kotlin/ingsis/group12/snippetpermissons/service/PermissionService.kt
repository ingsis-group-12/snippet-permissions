package ingsis.group12.snippetpermissons.service

import ingsis.group12.snippetpermissons.model.Permission
import ingsis.group12.snippetpermissons.model.PermissionType
import java.util.UUID

interface PermissionService {
    fun createPermission(
        userId: UUID,
        snippetId: UUID,
        permission: PermissionType,
    ): Permission

    fun getPermissionsBySnippetId(
        snippetId: UUID,
        userId: UUID,
    ): Permission

    fun updatePermissions(
        userId: UUID,
        snippetId: UUID,
        permission: PermissionType,
    ): Permission
}
