package ingsis.group12.snippetpermissons.service

import ingsis.group12.snippetpermissons.exception.PermissionAlreadyExistsException
import ingsis.group12.snippetpermissons.exception.PermissionNotFoundException
import ingsis.group12.snippetpermissons.model.Permission
import ingsis.group12.snippetpermissons.model.PermissionType
import ingsis.group12.snippetpermissons.repository.PermissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PermissionServiceImpl(
    @Autowired private val permissionRepository: PermissionRepository,
) : PermissionService {
    override fun createPermission(
        userId: UUID,
        snippetId: UUID,
        permission: PermissionType,
    ): Permission {
        val existingPermission = permissionRepository.findByUserIdAndSnippetId(userId, snippetId)
        if (existingPermission !== null) {
            throw PermissionAlreadyExistsException()
        }
        val newPermission = Permission(userId, snippetId, permission)
        return permissionRepository.save(newPermission)
    }

    override fun getPermissionsBySnippetId(
        snippetId: UUID,
        userId: UUID,
    ): Permission {
        return permissionRepository.findByUserIdAndSnippetId(userId, snippetId) ?: throw PermissionNotFoundException()
    }

    override fun updatePermissions(
        userId: UUID,
        snippetId: UUID,
        permission: PermissionType,
    ): Permission {
        val existingPermission = permissionRepository.findByUserIdAndSnippetId(userId, snippetId) ?: throw PermissionNotFoundException()
        return permissionRepository.updateById(existingPermission.id, permission)
    }
}
