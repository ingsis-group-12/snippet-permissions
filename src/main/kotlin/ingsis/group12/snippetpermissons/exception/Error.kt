package ingsis.group12.snippetpermissons.exception

class PermissionAlreadyExistsException : Exception()

class PermissionNotFoundException() : Exception()

class InvalidPermissionException(val value: String) : RuntimeException()
