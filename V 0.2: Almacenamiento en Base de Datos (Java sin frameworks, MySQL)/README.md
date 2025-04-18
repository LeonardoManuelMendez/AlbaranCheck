## V 0.2: Almacenamiento en Base de Datos (Java sin frameworks, MySQL)

### Objetivos de esta versión:
El objetivo principal de esta versión es implementar el almacenamiento persistente de datos utilizando una base de datos MySQL. Esto permitirá que la aplicación conserve información de los albaranes, productos escaneados, verificaciones y resultados, incluso después de cerrar la aplicación.  
Se busca mantener una estructura de datos eficiente y relacional para garantizar la integridad de la información y facilitar futuras consultas y operaciones.

### Diseño

#### Interfaz de usuario
La interfaz gráfica se modificará para incluir:
- Formulario para cargar datos del albarán y almacenarlos en la base de datos.
- Visualización de productos vinculados a un albarán desde la base de datos.
- Botón para generar informes de verificación directamente desde los datos almacenados.

#### Modelo entidad-relación (ER)
El modelo entidad-relación está diseñado para representar las entidades clave de la aplicación: **Albarán**, **Producto**, **ProductoEnAlbaran**, **ProductoVerificado** y **Usuario**. Las relaciones entre estas entidades permiten realizar operaciones de consulta y almacenamiento de manera eficiente.

- **Albarán**: Representa los documentos de entrega cargados en el sistema.
- **Producto**: Contiene la información de cada producto registrado.
- **ProductoEnAlbaran**: Representa la relación entre un albarán y los productos que contiene.
- **ProductoVerificado**: Almacena los productos escaneados y su estado de verificación.
- **Usuario**: Representa a los usuarios que interactúan con el sistema.

#### Descripción de las tablas y relaciones

- **Tabla Albarán**:  
  - `id_albaran` (PK): Identificador único del albarán.  
  - `numero` (VARCHAR): Número del albarán.  
  - `fecha` (DATE): Fecha del albarán.  
  - `proveedor` (VARCHAR): Nombre del proveedor.  

- **Tabla Producto**:  
  - `id_producto` (PK): Identificador único del producto.  
  - `codigo_barras_unidad` (VARCHAR): Código de barras del producto.  
  - `codigo_barras_bulto` (VARCHAR): Código de barras del producto.  
  - `descripcion` (VARCHAR): Descripción del producto.  

- **Tabla ProductoEnAlbaran**:  
  - `id_relacion` (PK): Identificador único de la relación.  
  - `id_albaran` (FK): Relación con el albarán asociado.  
  - `id_producto` (FK): Relación con el producto asociado.  
  - `cantidad_esperada` (INT): Cantidad esperada del producto según el albarán.  

- **Tabla ProductoVerificado**:  
  - `id_verificacion` (PK): Identificador único de la verificación.  
  - `id_albaran` (FK): Relación con el albarán asociado.  
  - `id_producto` (FK): Relación con el producto escaneado.  
  - `cantidad_recibida` (INT): Cantidad recibida del producto.  

- **Tabla Usuario**:  
  - `id_usuario` (PK): Identificador único del usuario.  
  - `nombre_usuario` (VARCHAR): Nombre del usuario.  
  - `contrasena` (VARCHAR): Contraseña del usuario.  

**Relaciones principales**:  
- `Albarán` (1:N) `ProductoEnAlbaran`.  
- `Producto` (1:N) `ProductoEnAlbaran`.  
- `ProductoEnAlbaran` (1:N) `ProductoVerificado`.  
- `Producto` (1:N) `ProductoVerificado`.

### Implementación
#### Código SQL para la creación de la Base de Datos

```sql
CREATE TABLE Albaran (
    id_albaran INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(255) NOT NULL,
    fecha DATE NOT NULL,
    proveedor VARCHAR(255) NOT NULL
);

CREATE TABLE Producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    codigo_barras_unidad VARCHAR(255) NOT NULL,
    codigo_barras_bulto VARCHAR(255),
    descripcion VARCHAR(255) NOT NULL
);

CREATE TABLE ProductoEnAlbaran (
    id_relacion INT AUTO_INCREMENT PRIMARY KEY,
    id_albaran INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad_esperada INT NOT NULL,
    FOREIGN KEY (id_albaran) REFERENCES Albaran(id_albaran) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES Producto(id_producto) ON DELETE CASCADE
);

CREATE TABLE ProductoVerificado (
    id_verificacion INT AUTO_INCREMENT PRIMARY KEY,
    id_albaran INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad_recibida INT NOT NULL,
    FOREIGN KEY (id_albaran) REFERENCES Albaran(id_albaran) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES Producto(id_producto) ON DELETE CASCADE
);
```

#### Conexión a la Base de Datos
La conexión a la base de datos se realizará utilizando el controlador JDBC de MySQL. El código incluirá la configuración de las credenciales y la URL de conexión:
```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_albaranes";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "password";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}
```

#### Consultas SQL para guardar y recuperar datos

- **Insertar un albarán**:
  ```sql
  INSERT INTO Albaran (numero, fecha, proveedor) VALUES (?, ?, ?);
  ```
- **Insertar un productoenalbarán**:
```sql
INSERT INTO ProductoEnAlbaran (id_albaran, id_producto, cantidad_esperada) VALUES (?, ?, ?);
```
- **Recuperar productosenalbarán**:
```sql
SELECT p.codigo_barras, p.descripcion, pa.cantidad_esperada, pv.cantidad_recibida FROM ProductoEnAlbaran pa JOIN Producto p ON pa.id_producto = p.id_producto LEFT JOIN ProductoVerificado pv ON pa.id_producto = pv.id_producto AND pa.id_albaran = pv.id_albaran WHERE pa.id_albaran = ?;
```
- **Registrar una verificación**:
```sql
INSERT INTO ProductoVerificado (id_albaran, id_producto, cantidad_recibida) VALUES (?, ?, ?);
```
