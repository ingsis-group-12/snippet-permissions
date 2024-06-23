package ingsis.group12.snippetpermissons.service

import ingsis.group12.snippetpermissons.input.PermissionInput
import ingsis.group12.snippetpermissons.model.Permission
import ingsis.group12.snippetpermissons.model.PermissionType
import ingsis.group12.snippetpermissons.model.UserWithoutPermission
import java.util.UUID

interface PermissionService {
    fun createPermission(
        userId: String,
        snippetId: UUID,
        permissionInput: PermissionInput,
    ): Permission

    fun getPermissionsBySnippetId(
        snippetId: UUID,
        userId: String,
    ): Permission

    fun getPermissionsByUserId(userId: String): List<Permission>

    fun updatePermissions(
        userId: String,
        snippetId: UUID,
        permission: PermissionType,
    ): Permission

    fun getUsersWhoNotHavePermissionWithAsset(
        snippetId: UUID,
        userId: String,
    ): List<UserWithoutPermission>

    fun deletePermissionsBySnippetId(snippetId: UUID)
}
