package ingsis.group12.snippetpermissons.permissions.service

import ingsis.group12.snippetpermissons.exception.PermissionAlreadyExistsException
import ingsis.group12.snippetpermissons.exception.PermissionNotFoundException
import ingsis.group12.snippetpermissons.input.PermissionInput
import ingsis.group12.snippetpermissons.model.Permission
import ingsis.group12.snippetpermissons.model.PermissionType
import ingsis.group12.snippetpermissons.repository.PermissionRepository
import ingsis.group12.snippetpermissons.service.PermissionService
import ingsis.group12.snippetpermissons.service.PermissionServiceImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.times
import org.mockito.Mockito.verify


@SpringBootTest
@ExtendWith(SpringExtension::class)
class PermissionServiceTest{
    private val repository: PermissionRepository = mock(PermissionRepository::class.java)
    private val service: PermissionService = PermissionServiceImpl(repository)

    @Test
    fun `createPermission should create user permissions for specific snippet`() {
        val permissionInput = PermissionInput(PermissionType.READ, "username")
        val userId = "user1"
        val snippetId = UUID.randomUUID()
        val permission = Permission(userId, snippetId, PermissionType.READ, UUID.randomUUID())

        `when`(repository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(null)
        `when`(repository.save(any())).thenReturn(permission)

        val result = service.createPermission(userId, snippetId, permissionInput)

        assertNotNull(result)
        assertEquals(permission.userId, result.userId)
        assertEquals(permission.snippetId, result.snippetId)
        assertEquals(permission.permission, result.permission)
        assertEquals(permission.userName, result.userName)
    }

    @Test
    fun `createPermission should throw PermissionAlreadyExistsException if permission already exists`() {
        val permissionInput = PermissionInput(PermissionType.READ, "username")
        val userId = "user1"
        val snippetId = UUID.randomUUID()
        val existingPermission = Permission(userId, snippetId, PermissionType.READ, UUID.randomUUID())

        `when`(repository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(existingPermission)

        assertThrows(PermissionAlreadyExistsException::class.java) {
            service.createPermission(userId, snippetId, permissionInput)
        }
    }

    @Test
    fun `getPermissionsBySnippetId should return permissions for given snippet and user`() {
        val userId = "user1"
        val snippetId = UUID.randomUUID()
        val permission = Permission(userId, snippetId, PermissionType.READ, UUID.randomUUID())

        `when`(repository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(permission)

        val result = service.getPermissionsBySnippetId(snippetId, userId)

        assertNotNull(result)
        assertEquals(permission.userId, result.userId)
        assertEquals(permission.snippetId, result.snippetId)
        assertEquals(permission.permission, result.permission)
    }

    @Test
    fun `getPermissionsBySnippetId should throw PermissionNotFoundException if permission not found`() {
        val userId = "user1"
        val snippetId = UUID.randomUUID()

        `when`(repository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(null)

        assertThrows(PermissionNotFoundException::class.java) {
            service.getPermissionsBySnippetId(snippetId, userId)
        }
    }

    @Test
    fun `getPermissionsByUserId should return all permissions for given user`() {
        val userId = "user1"
        val permissions = listOf(
            Permission(userId, UUID.randomUUID(), PermissionType.READ, UUID.randomUUID()),
            Permission(userId, UUID.randomUUID(), PermissionType.OWNER, UUID.randomUUID())
        )

        `when`(repository.findAllByUserId(userId)).thenReturn(permissions)

        val result = service.getPermissionsByUserId(userId)

        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals(permissions, result)
    }

    @Test
    fun `updatePermissions should update permissions for given snippet and user`() {
        val userId = "user1"
        val snippetId = UUID.randomUUID()
        val existingPermission = Permission(userId, snippetId, PermissionType.READ, UUID.randomUUID())
        val updatedPermission = existingPermission.copy(permission = PermissionType.OWNER)

        `when`(repository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(existingPermission)
        `when`(repository.updateById(existingPermission.id, PermissionType.OWNER)).thenReturn(updatedPermission)

        val result = service.updatePermissions(userId, snippetId, PermissionType.OWNER)

        assertNotNull(result)
        assertEquals(updatedPermission.permission, result.permission)
    }

    @Test
    fun `updatePermissions should throw PermissionNotFoundException if permission not found`() {
        val userId = "user1"
        val snippetId = UUID.randomUUID()

        `when`(repository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(null)

        assertThrows(PermissionNotFoundException::class.java) {
            service.updatePermissions(userId, snippetId, PermissionType.OWNER)
        }
    }

    @Test
    fun `getUsersWhoNotHavePermissionWithAsset should return users without permissions`() {
        val snippetId = UUID.randomUUID()
        val userId = "user1"
        val permissions = listOf(
            Permission("user2", UUID.randomUUID(), PermissionType.READ, UUID.randomUUID(), "USERNAME"),
            Permission("user3", UUID.randomUUID(), PermissionType.OWNER, UUID.randomUUID(), "username2")
        )

        `when`(repository.findAll()).thenReturn(permissions)

        val result = service.getUsersWhoNotHavePermissionWithAsset(snippetId, userId)

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `deletePermissionsBySnippetId should delete all permissions for given snippet`() {
        val snippetId = UUID.randomUUID()
        val permissions = listOf(
            Permission("user1", snippetId, PermissionType.READ, UUID.randomUUID()),
            Permission("user2", snippetId, PermissionType.OWNER, UUID.randomUUID())
        )

        `when`(repository.findAllBySnippetId(snippetId)).thenReturn(permissions)
        doNothing().`when`(repository).deleteAll(permissions)

        service.deletePermissionsBySnippetId(snippetId)

        verify(repository, times(1)).deleteAll(permissions)
    }

}