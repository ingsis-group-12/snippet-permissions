package ingsis.group12.snippetpermissons.repository

import ingsis.group12.snippetpermissons.model.Permission
import ingsis.group12.snippetpermissons.model.PermissionType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

interface PermissionRepository : JpaRepository<Permission, UUID> {
    fun findByUserIdAndSnippetId(
        userId: UUID,
        snippetId: UUID,
    ): Permission?

    @Modifying
    @Transactional
    @Query("UPDATE Permission p SET p.permission = :permission WHERE p.id = :id")
    fun updateById(
        id: UUID,
        permission: PermissionType,
    ): Permission
}
