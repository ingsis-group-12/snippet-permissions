package ingsis.group12.snippetpermissons.controller

import ingsis.group12.snippetpermissons.input.PermissionInput
import ingsis.group12.snippetpermissons.model.Permission
import ingsis.group12.snippetpermissons.service.PermissionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/permissions")
@Tag(name = "Permissions")
class PermissionController(
    @Autowired private val permissionService: PermissionService,
) {
    @PostMapping("/{snippetId}/user/{userId}")
    @Operation(summary = "Create user permission for specific snippet")
    @ApiResponse(responseCode = "201", description = "Successfully created permissions for snipet")
    fun createPermission(
        @PathVariable("snippetId") snippetId: UUID,
        @PathVariable("userId") userId: String,
        @Valid @RequestBody permissions: PermissionInput,
    ): ResponseEntity<Permission> {
        val newPermission = permissionService.createPermission(userId, snippetId, permissions.permission!!)
        return ResponseEntity.ok(newPermission)
    }

    @GetMapping("/{snippetId}/user/{userId}")
    @Operation(summary = "Get user permissions for specific snippet")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user permission for snippet")
    fun getPermissionsBySnippetId(
        @PathVariable("snippetId") snippetId: UUID,
        @PathVariable("userId") userId: String,
    ): ResponseEntity<Permission> {
        val permissionFound = permissionService.getPermissionsBySnippetId(snippetId, userId)
        return ResponseEntity.ok(permissionFound)
    }

    @PatchMapping("/{snippetId}/user/{userId}")
    @Operation(summary = "Update user permissions for specific snippet")
    @ApiResponse(responseCode = "200", description = "Successfully updated permissions")
    fun updatePermission(
        @PathVariable("snippetId") snippetId: UUID,
        @PathVariable("userId") userId: String,
        @Valid @RequestBody permissions: PermissionInput,
    ): ResponseEntity<Permission> {
        val updatedPermission = permissionService.updatePermissions(userId, snippetId, permissions.permission!!)
        return ResponseEntity.ok(updatedPermission)
    }

    @DeleteMapping("/{snippetId}")
    @Operation(summary = "Delete all permissions for specific snippet")
    @ApiResponse(responseCode = "204", description = "Successfully deleted all permissions")
    fun deletePermissionsBySnippetId(
        @PathVariable("snippetId") snippetId: UUID,
    ): ResponseEntity<Unit> {
        permissionService.deletePermissionsBySnippetId(snippetId)
        return ResponseEntity.noContent().build()
    }
}
