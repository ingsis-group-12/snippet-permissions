package ingsis.group12.snippetpermissons.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "permission")
data class Permission(
    @Column(name = "userId", nullable = false)
    val userId: String? = null,
    @Column(name = "snippetId", nullable = false)
    val snippetId: UUID? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false)
    val permission: PermissionType? = PermissionType.READ,
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    @Column(name = "userName", nullable = false)
    val userName: String? = null,
)
