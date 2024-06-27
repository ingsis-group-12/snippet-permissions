package ingsis.group12.snippetpermissons.permissions.controller

import ingsis.group12.snippetpermissons.controller.PermissionController
import ingsis.group12.snippetpermissons.input.PermissionInput
import ingsis.group12.snippetpermissons.model.Permission
import ingsis.group12.snippetpermissons.model.PermissionType
import ingsis.group12.snippetpermissons.model.UserWithoutPermission
import ingsis.group12.snippetpermissons.service.PermissionService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.util.UUID


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PermissionControllerTest {
    @Mock
    lateinit var permissionService: PermissionService

    lateinit var permissionController: PermissionController

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        permissionController = PermissionController(permissionService)
    }

    @Test
    fun `POST createPermission should return ResponseEntity with SnippetDTO when successful`() {
        val snippetId = UUID.randomUUID()
        val userId = "user1"
        val permission = PermissionInput(PermissionType.READ, "username1")
        val permissionDTO = Permission(userId, snippetId, permission.permission, UUID.randomUUID())

        `when`(permissionService.createPermission(userId, snippetId, permission)).thenReturn(permissionDTO)

        val result = permissionController.createPermission(snippetId, userId, permission)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(permissionDTO, result.body)
    }


    @Test
    fun `GET getPermissionsBySnippetId should return ResponseEntity with SnippetDTO when successful`() {
        val snippetId = UUID.randomUUID()
        val userId = "user1"
        val permissionDTO = Permission(userId, snippetId, PermissionType.READ, UUID.randomUUID())

        `when`(permissionService.getPermissionsBySnippetId(snippetId, userId)).thenReturn(permissionDTO)

        val result = permissionController.getPermissionsBySnippetId(snippetId, userId)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(permissionDTO, result.body)
    }

    @Test
    fun `PATCH updatePermission should return ResponseEntity with SnippetDTO when successful`() {
        val snippetId = UUID.randomUUID()
        val userId = "user1"
        val newPermission = PermissionInput(PermissionType.READ, "username1")
        val permissionDTO = Permission(userId, snippetId, PermissionType.READ, UUID.randomUUID())

        `when`(permissionService.updatePermissions(userId, snippetId, newPermission.permission!!)).thenReturn(permissionDTO)

        val result = permissionController.updatePermission(snippetId, userId, newPermission)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(permissionDTO, result.body)
    }
    @Test
    fun `DELETE deletePermissionsBySnippetId should return ResponseEntity with SnippetDTO when successful`() {
        val snippetId = UUID.randomUUID()

        doNothing().`when`(permissionService).deletePermissionsBySnippetId(snippetId)

        val result = permissionController.deletePermissionsBySnippetId(snippetId)

        assertEquals(HttpStatus.NO_CONTENT, result.statusCode)

    }

    @Test
    fun `GET getPermissionsByUserId should return ResponseEntity with SnippetDTO when successful`() {
        val snippetId = UUID.randomUUID()
        val userId = "user1"
        val permission = Permission(userId, snippetId, PermissionType.READ, UUID.randomUUID())

        `when`(permissionService.getPermissionsByUserId(userId)).thenReturn(listOf(permission))

        val result = permissionController.getPermissionsByUserId(userId)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(listOf(permission) , result.body)
    }

    @Test
    fun `GET getUsersWhoNotHavePermissionWithAsset should return ResponseEntity with SnippetDTO when successful`() {
        val snippetId = UUID.randomUUID()
        val userId = "user1"

        val users = emptyList<UserWithoutPermission>()

        `when`(permissionService.getUsersWhoNotHavePermissionWithAsset(snippetId, userId)).thenReturn(users)

        val result = permissionController.getUsersWhoNotHavePermissionWithAsset(snippetId, userId)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(users, result.body)
    }

}