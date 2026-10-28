HALLAZGOS

Fallo 1: Dependencia ausente en el slice test

Síntoma: La carga del contexto falla con el mensaje: Parameter 1 of constructor in SupplierController required a bean of type 'SupplierWebMapper' that could not be found.
Causa raíz: @WebMvcTest crea un contexto parcial centrado en la capa web. SupplierController dependía de SupplierWebMapper, pero dicha dependencia no estaba disponible dentro del slice de prueba. Como resultado, Spring no pudo construir el controlador y el `ApplicationContext` falló durante el arranque.
Corrección: Se registró explícitamente la dependencia requerida dentro del contexto de prueba para permitir la creación del controlador.
Prevención: Verificar que todas las dependencias requeridas por los controladores estén disponibles dentro del contexto de `@WebMvcTest` y evitar dependencias innecesarias administradas por Spring.

Fallo 2 : Construcción incorrecta del header Location

Síntoma: El test de creación fallaba porque el header Location esperado era: http://localhost/api/suppliers/SUP-001 pero la aplicación generaba: http://localhost/api/suppliersSUP-001
Causa raíz: La URI se construía utilizando: .path("{id}") lo que concatenaba el identificador directamente a la URL actual sin incluir el separador /.
Corrección: Se modificó la construcción de la URI a: .path("/{id}") generando correctamente la ubicación del recurso creado.
Prevención: Mantener pruebas de contrato que validen explícitamente el contenido del header Location.

Fallo 3: Validación del request no ejecutada

Síntoma: El endpoint devolvía 500 Internal Server Error cuando el test esperaba 400 Bad Request
para solicitudes inválidas.

Causa raíz: El parámetro CreateSupplierRequest no estaba anotado con @Valid, por lo que Bean Validation no ejecutaba las restricciones declaradas mediante @NotBlank, @Email y @Pattern.
Corrección: Se agregó la anotación @Valid al parámetro del endpoint POST.
Prevención: Aplicar sistemáticamente `@Valid` en todos los DTOs de entrada que utilicen Bean Validation y mantener pruebas para escenarios de validación fallida.

Fallo 4: Código HTTP incorrecto en DELETE

Síntoma: El endpoint respondía con 200 OK cuando el contrato de la API exigía 204 No Content
Causa raíz: El controlador utilizaba ResponseEntity.ok().build(); para responder a las eliminaciones exitosas.
Corrección: Se reemplazó por ResponseEntity.noContent().build(); cumpliendo el contrato REST esperado.
Prevención: Mantener pruebas de contrato HTTP que verifiquen explícitamente los códigos de respuesta definidos para cada operación.

Refactors aplicados

| Principio / práctica | Antes | Después | Test que lo respalda |
| DIP | `SupplierController` dependía de `SupplierServiceImpl` | `SupplierController` depende de `SupplierService` | `SupplierControllerTest` |
| DIP | `SupplierServiceImpl` instanciaba `InMemorySupplierRepository` mediante `new` | El repositorio se inyecta mediante constructor y abstracción | `SupplierServiceImplTest` |
| SRP | La normalización de nombre y NIT estaba en el controller | La normalización se trasladó al servicio de aplicación | `SupplierServiceImplTest.shouldNormalizeSupplierBeforeSaving()` |
| Dominio | `Supplier` podía crearse con datos inválidos | El dominio protege sus invariantes mediante validaciones de construcción | `SupplierTest` |
| Seguridad | Se exponía `exception.toString()` al cliente | Se devuelve un mensaje genérico para errores inesperados | `ApiExceptionHandlerTest` |
| Diseño web | `SupplierWebMapper` dependía del contenedor Spring | El mapper se desacopló de Spring al no requerir gestión de beans | `SupplierControllerTest` |
| Testing | Tests con nombres genéricos (`test1`, `test2`) | Tests orientados a comportamiento (`shouldReturnSupplierWhenSupplierExists`) | Suite completa |
| Testing | Comparación manual del JSON como String | Uso de `jsonPath` para verificar el contrato de respuesta | `SupplierControllerTest` |
| Testing | La normalización se validaba indirectamente desde el controller | La regla se prueba en un test unitario del servicio | `SupplierServiceImplTest` |

# Conclusión

La solución final mantiene los tests de controller utilizando `@WebMvcTest`, respeta el contrato HTTP definido por la API y mejora la calidad del diseño aplicando principios SOLID. Las responsabilidades quedaron correctamente distribuidas entre las capas web, aplicación y dominio, reduciendo el acoplamiento y aumentando la mantenibilidad del código.
