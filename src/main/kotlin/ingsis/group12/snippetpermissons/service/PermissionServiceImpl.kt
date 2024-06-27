package ingsis.group12.snippetpermissons.service

import ingsis.group12.snippetpermissons.exception.PermissionAlreadyExistsException
import ingsis.group12.snippetpermissons.exception.PermissionNotFoundException
import ingsis.group12.snippetpermissons.input.PermissionInput
import ingsis.group12.snippetpermissons.model.Permission
import ingsis.group12.snippetpermissons.model.PermissionType
import ingsis.group12.snippetpermissons.model.UserWithoutPermission
import ingsis.group12.snippetpermissons.repository.PermissionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PermissionServiceImpl(
    @Autowired private val permissionRepository: PermissionRepository,
) : PermissionService {
    private val logger = LoggerFactory.getLogger(PermissionServiceImpl::class.java)
    override fun createPermission(
        userId: String,
        snippetId: UUID,
        permissionInput: PermissionInput,
    ): Permission {
        logger.info("Creating permission for user $userId and snippet $snippetId")
        val existingPermission = permissionRepository.findByUserIdAndSnippetId(userId, snippetId)
        logger.info("Permission found for user $userId and snippet $snippetId")
        if (existingPermission !== null) {
            logger.error("Permission already exists for user $userId and snippet $snippetId")
            throw PermissionAlreadyExistsException()
        }
        logger.info("Creating new permission for user $userId and snippet $snippetId")
        val newPermission = Permission(userId, snippetId, permissionInput.permission, userName = permissionInput.userName)
        logger.info("Permission created for user $userId and snippet $snippetId")
        return permissionRepository.save(newPermission)
    }

    override fun getPermissionsBySnippetId(
        snippetId: UUID,
        userId: String,
    ): Permission {
        logger.info("Getting permission for user $userId and snippet $snippetId")
        return permissionRepository.findByUserIdAndSnippetId(userId, snippetId) ?: throw PermissionNotFoundException()
    }

    override fun getPermissionsByUserId(userId: String): List<Permission> {
        logger.info("Getting permissions for user $userId")
        return permissionRepository.findAllByUserId(userId)
    }

    override fun updatePermissions(
        userId: String,
        snippetId: UUID,
        permission: PermissionType,
    ): Permission {
        logger.info("Updating permission for user $userId and snippet $snippetId")
        val existingPermission = permissionRepository.findByUserIdAndSnippetId(userId, snippetId) ?: throw PermissionNotFoundException()
        logger.info("Permission found for user $userId and snippet $snippetId")
        return permissionRepository.updateById(existingPermission.id, permission)
    }

    override fun getUsersWhoNotHavePermissionWithAsset(
        snippetId: UUID,
        userId: String,
    ): List<UserWithoutPermission> {
        logger.info("Getting users who do not have permission for snippet $snippetId")
        val existingPermissions = permissionRepository.findAll()

        val usersWithoutPermission = existingPermissions.filter { it.snippetId != snippetId && it.userId != userId }

        return if (usersWithoutPermission.isNotEmpty()) {
            usersWithoutPermission.map { UserWithoutPermission(it.userId!!, it.userName!!) }
        } else {
            emptyList()
        }
    }

    override fun deletePermissionsBySnippetId(snippetId: UUID) {
        logger.info("Deleting permissions for snippet $snippetId")
        val existingPermissions = permissionRepository.findAllBySnippetId(snippetId)
        permissionRepository.deleteAll(existingPermissions)
    }
}
