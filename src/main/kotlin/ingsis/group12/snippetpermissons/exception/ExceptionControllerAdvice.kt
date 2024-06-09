package ingsis.group12.snippetpermissons.exception

import org.springframework.context.support.DefaultMessageSourceResolvable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.stream.Collectors

@ControllerAdvice
class ExceptionControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<MutableMap<String, MutableList<String?>?>> {
        val body: MutableMap<String, MutableList<String?>?> = HashMap()

        val errors: MutableList<String?>? =
            ex.bindingResult
                .fieldErrors
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList())

        body["errors"] = errors

        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(PermissionAlreadyExistsException::class)
    fun handlePermissionAlreadyExistsException(exception: PermissionAlreadyExistsException): ResponseEntity<ErrorMessage> {
        val error = ErrorMessage("User already has permissions for this snippet", HttpStatus.CONFLICT.value())
        return ResponseEntity(error, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(PermissionNotFoundException::class)
    fun handlePermissionNotFoundException(exception: PermissionNotFoundException): ResponseEntity<ErrorMessage> {
        val error = ErrorMessage("User does not have permissions for this snippet", HttpStatus.NOT_FOUND.value())
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidPermissionException::class)
    fun handleInvalidPermissionException(exception: InvalidPermissionException): ResponseEntity<ErrorMessage> {
        val error = ErrorMessage("Invalid permission value: ${exception.value}", HttpStatus.BAD_REQUEST.value())
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(exception: HttpMessageNotReadableException): ResponseEntity<ErrorMessage> {
        val error = ErrorMessage("Request body is empty", HttpStatus.BAD_REQUEST.value())
        when (exception.cause) {
            is InvalidPermissionException -> return handleInvalidPermissionException(exception.cause as InvalidPermissionException)
            else -> return ResponseEntity(error, HttpStatus.BAD_REQUEST)
        }
    }
}
