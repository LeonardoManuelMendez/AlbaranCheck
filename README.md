# ALBARANCHECK
## Proyecto de Desarrollo de Aplicaciones Multiplataformas

<img src="Imagenes/logoalbaranchek.png" alt="Logo del proyecto" style="display: block; margin: 0 auto;">

**Leonardo Manuel Méndez Pérez**  
---
24/06/2024  
C.F.G.S. DESARROLLO DE APLICACIONES MULTIPLATAFORMAS  
Comunidad de Madrid - España  
**Índice:**  
1. [OBJETIVO GENERAL](#objetivo)
2. [V 0.1: Prototipo Básico (Java sin frameworks y sin persistencia)](#v-01-prototipo-básico-java-sin-frameworks-y-sin-persistencia)
3. [V 0.2: Almacenamiento en Base de Datos (Java sin frameworks, MySQL)](#v-02-almacenamiento-en-base-de-datos-java-sin-frameworks-mysql)
4. [V 1.0: Mapeo Objeto-Relacional (Java sin frameworks, MySQL, Hibernate)](#v-10-mapeo-objeto-relacional-java-sin-frameworks-mysql-hibernate)
5. [V 2.0: Versión Web (Java, Spring Boot, Spring MVC, Thymeleaf, MySQL)](#v-20-versión-web-java-spring-boot-spring-mvc-thymeleaf-mysql)
6. [V 3.0: Versión Móvil (FlutterFlow, Supabase)](#v-30-versión-móvil-flutterflow-supabase)
7. [V 4.0: Aplicación Nativa Android (Android Studio y SQLite)](#v-40-aplicación-nativa-android-android-studio-y-sqlite)
8. [V 5.0: Integración de IA (FlutterFlow, Supabase, SQLite)](#v-50-integración-de-ia-flutterflow-supabase-sqlite)
---
<a id="objetivo"></a>

## OBJETIVO GENERAL

Como parte de mi camino educativo, he decidido desarrollar un proyecto completo para una aplicación con utilidad práctica y que cubra una necesidad actual en un grupo de empresas. La idea nace de mi experiencia como franquiciado de DIA y de la necesidad que he observado en los encargados de recepción en múltiples empresas.

El objetivo principal es recorrer todas las fases del diseño de software, utilizando diversas tecnologías en un único proyecto. Esto me permitirá evaluar las diferencias en cuanto al uso y aplicabilidad de cada tecnología según el contexto y tipo de proyecto.

Mi visión es crear una aplicación intuitiva y eficiente para la recepción de productos. La aplicación permitirá a los usuarios:

*   Cargar albaranes en formato PDF.
*   Escanear códigos de barras de productos o bultos.
*   Verificar si todos los productos del albarán han sido recibidos.

El sistema generará un informe detallado indicando los productos recibidos, faltantes o con errores. La aplicación final será accesible tanto en dispositivos Android como iOS, descargable desde las respectivas tiendas de aplicaciones, y podrá utilizarse con la cámara del dispositivo o un lector de códigos de barra Bluetooth. Para el análisis y extracción de los datos de los albaranes se utilizará una API de una IA.

Para lograr esto, seguiré un enfoque iterativo, desarrollando distintas versiones que evolucionarán en complejidad y funcionalidad:

| Versión | Tecnología                              | Plataforma   | Objetivo                                       | Características                                                                                           |
|---------|-----------------------------------------|--------------|------------------------------------------------|-----------------------------------------------------------------------------------------------------------|
| V 0.1   | Java (sin frameworks)                  | Escritorio   | Validar la idea y la funcionalidad principal   | Lectura de códigos de barras, comparación básica con el albarán (cargado manualmente como texto), informe simple de productos recibidos/faltantes |
| V 0.2   | Java (sin frameworks), MySQL           | Escritorio   | Almacenar datos de albaranes y recepciones     | Conexión a base de datos, consultas SQL básicas para guardar y recuperar datos                           |
| V 1.0   | Java (sin frameworks), MySQL, Hibernate      | Escritorio   | Simplificar el acceso a la base de datos       | Mapeo objeto-relacional con Hibernate, carga de albaranes en PDF, informe más completo                         |
| V 2.0   | Java con Spring Boot, Thymeleaf, JPA   | Web          | Hacer la aplicación accesible desde cualquier dispositivo | Interfaz web responsive, autenticación de usuarios, escaneo con lector Bluetooth                 |
| V 3.0   | FlutterFlow, Supabase, SQLite          | Android/iOS  | Ofrecer la mejor experiencia en móviles        | Interfaz para Android/iOS, acceso a cámara y lector Bluetooth, almacenamiento local y en la nube         |
| V 4.0   | Android Studio, SQLite                 | Android      | App nativa optimizada                          | Interfaz optimizada, uso de cámara y lector Bluetooth, almacenamiento local con SQLite                   |
| V 5.0   | IA, FlutterFlow, Supabase, SQLite      | Android/iOS  | Implementar IA para procesar albaranes         | Integración de API de IA para leer y procesar albaranes automáticamente                                  |

---

## V 0.1: Prototipo Básico (Java sin frameworks y sin persistencia)

### Objetivo de esta versión
Desarrollar una aplicación de escritorio que permita gestionar la recepción de albaranes de entrega enviados por una compañía. Las principales funcionalidades del sistema incluyen:
- Permitir la carga de un archivo PDF que contenga el albarán.
- Leer y procesar el contenido del albarán para mostrar la información resumida en pantalla (número de albarán, fecha, proveedor, lista de productos y cantidades).
- Habilitar un campo para escanear los códigos de barras de los productos recibidos, generando una lista de productos verificados.
- Comparar los productos escaneados con la lista del albarán para identificar discrepancias.
- Emitir un informe final que detalle los productos recibidos, faltantes y discrepancias.

#### Alcance y limitaciones
Esta versión inicial (V 0.1) tiene un alcance limitado:
- No se guardará un histórico de albaranes, verificaciones ni usuarios.
- La lista de productos con sus códigos EAN se almacenará en un archivo `.dat`.
- Sin persistencia en bases de datos.
- Sin manejo avanzado de excepciones.

#### Requisitos Funcionales
- **RF.01**: Carga de albaranes en PDF.
- **RF.02**: Lectura y visualización del contenido del albarán.
- **RF.03**: Escaneo de códigos de barras.
- **RF.04**: Verificación de recepción.
- **RF.05**: Generación de informe de recepción.

#### Requisitos No Funcionales
- **RNF.01**: Interfaz intuitiva y fácil de usar.
- **RNF.02**: Procesamiento rápido de albaranes y escaneo.
- **RNF.03**: Portabilidad en diferentes máquinas.

#### Casos de Uso
1. **CU.01**: Cargar albarán.
2. **CU.02**: Ver albarán.
3. **CU.03**: Escanear producto.
4. **CU.04**: Verificar recepción.
5. **CU.05**: Generar informe.
6. **CU.06**: Exportar informe.

### Diseño
#### Diagrama de clases (UML) en PlantUML:  
<img src="Imagenes/clasesv01.png" alt="Diagrama de clases V 0.1" style="display: block; margin: 0 auto;">

### Descripción de la estructura del código y los componentes principales.
- **Main**: Clase principal que inicia la aplicación y crea una instancia de la interfaz gráfica (GUI).
- **GUI**: Clase encargada de la interfaz de usuario, que incluye ventanas para cargar el albarán, escanear códigos de barras y mostrar el informe. Interactúa con el usuario y delega las operaciones al Controlador.
- **Controlador**: Clase que actúa como intermediario entre la interfaz gráfica y la lógica del negocio. Gestiona la carga de albaranes, el escaneo de productos y la generación de informes.
- **Albarán**: Clase que representa un albarán con sus atributos (número, fecha, proveedor y lista de productos esperados).
- **ProductoEnAlbarán**: Clase que asocia un producto con la cantidad esperada en un albarán específico.
- **Producto**: Clase que contiene la información básica de un producto (código de barras y descripción).
- **ProductoVerificado**: Clase que registra los productos escaneados, asociándolos a un albarán y registrando la cantidad recibida.

El flujo de datos comienza en la GUI, pasa al Controlador para procesar la lógica, y utiliza las clases de modelo (Albarán, Producto, etc.) para estructurar la información.

### Implementación

#### Detalles de la implementación de cada componente
- **Main**: Implementado como un punto de entrada simple que instancia la clase GUI y muestra la ventana principal.
- **GUI**: 
  - Utiliza la biblioteca Swing de Java para crear una interfaz gráfica básica.
  - Incluye un botón para cargar el archivo PDF del albarán, un campo de texto para introducir códigos de barras manualmente o mediante un lector, y un área de texto para mostrar el informe.
- **Controlador**: Gestiona la lógica principal utilizando métodos para cada una de las acciones necesarias.
- **Albarán, ProductoEnAlbarán, Producto, ProductoVerificado**: Clases POJO (Plain Old Java Objects) con constructores, getters y setters.

#### Librerías y herramientas utilizadas
- **Java SE (Standard Edition)**: Versión 8 o superior, utilizada como base para el desarrollo.
- **Swing**: Biblioteca nativa de Java para la creación de la interfaz gráfica.
- **Apache PDFBox**: Para la lectura de los albaranes en formato PDF.
- **Eclipse/IntelliJ IDEA**: Entorno de desarrollo integrado (IDE) utilizado para escribir, depurar y compilar el código.
- **Archivo .dat**: Usado como sustituto temporal para simular la carga de datos de albaranes y productos.

En esta versión, se evitó el uso de frameworks para mantener la simplicidad y centrarse en la lógica básica.

### Pruebas y Resultados

#### Pruebas unitarias y de integración realizadas
- **Pruebas unitarias**:
  - **Carga de albarán**: Se probó la capacidad del sistema para cargar un archivo `.dat` con datos simulados de un albarán y mostrarlos en la GUI.
  - **Escaneo de productos**: Se simularon escaneos de códigos de barras válidos e inválidos para verificar que el sistema los registra correctamente.
  - **Generación de informe**: Se comprobó que el informe refleja correctamente los productos escaneados y faltantes.
- **Pruebas de integración**:
  - Se verificó que la GUI interactúa correctamente con el Controlador al cargar un albarán y escanear productos.
  - Se probó el flujo completo: cargar albarán → escanear productos → generar informe.

#### Resultados de las pruebas y análisis de rendimiento
- **Resultados**:
  - La carga de datos desde el archivo `.dat` fue exitosa en el 100% de los casos probados (10 albaranes simulados).
  - El escaneo de productos identificó correctamente los productos recibidos y faltantes en un 95% de las pruebas (fallos menores por entrada manual incorrecta).
  - El informe se generó correctamente, mostrando discrepancias en todos los casos probados.
- **Rendimiento**:
  - Tiempo promedio de carga de un albarán: 0.5 segundos.
  - Tiempo de procesamiento por escaneo: <0.1 segundos.
  - **Limitación**: El sistema se ralentiza con listas de más de 100 productos debido a la falta de optimización en la búsqueda.

<a id="v-02-almacenamiento-en-base-de-datos-java-sin-frameworks-mysql"></a>

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

<a id="v-10-mapeo-objeto-relacional-java-sin-frameworks-mysql-hibernate"></a>

## V 1.0: Mapeo Objeto-Relacional (Java sin frameworks, MySQL, Hibernate)

### Objetivos de esta versión
- **Simplificar el acceso a la base de datos:** Utilizar Hibernate para abstraer y automatizar el mapeo entre las clases Java y las tablas de MySQL.
- **Facilitar la gestión de entidades:** Reducir la cantidad de código repetitivo y mejorar la mantenibilidad mediante el uso de JPA y repositorios.
- **Optimizar consultas:** Permitir el uso de HQL (Hibernate Query Language) para realizar búsquedas más complejas de forma más eficiente.

### Diseño

#### Interfaz de usuario
- Se mantendrá una interfaz similar a la versión anterior, con mejoras en la visualización de datos recuperados de la base de datos.
- Incorporación de formularios de gestión para la inserción, edición y eliminación de registros de albaranes y productos.

#### Estructura del código
- **Entidades JPA:** Clases anotadas con `@Entity` que representan las tablas de la base de datos (Albarán, Producto, ProductoEnAlbaran, ProductoVerificado y Usuario).
- **Repositorios:** Clases o interfaces que implementan operaciones CRUD, utilizando el patrón DAO y aprovechando la abstracción que ofrece Hibernate.
- **Servicios:** Capas de servicio que gestionan la lógica de negocio y orquestan la interacción entre la interfaz y los repositorios.
- **Configuración de Hibernate:** Archivo de configuración (`hibernate.cfg.xml`) para definir parámetros de conexión, dialecto de MySQL y opciones de mapeo.

### Implementación

#### Funcionalidades clave
- **Carga de albaranes en PDF:** Integración de Apache PDFBox para extraer datos y transformarlos en entidades.
- **Gestión de entidades:** Uso de Hibernate para guardar, actualizar y recuperar registros de la base de datos.
- **Generación de informes:** Recopilación de datos mediante consultas HQL y presentación en la GUI.

#### Tecnologías
- **Java SE 8+**
- **Hibernate y JPA:** Para el mapeo objeto-relacional.
- **MySQL:** Sistema de gestión de base de datos.
- **Apache PDFBox:** Para la lectura de archivos PDF.
- **IDE (Eclipse o IntelliJ IDEA):** Herramienta de desarrollo.

---

<a id="v-20-versión-web-java-spring-boot-spring-mvc-thymeleaf-mysql"></a>

## V 2.0: Versión Web (Java, Spring Boot, Spring MVC, Thymeleaf, MySQL)

### Objetivos de esta versión
- **Accesibilidad:** Permitir el acceso a la aplicación desde cualquier dispositivo a través de un navegador web.
- **Modernización del desarrollo:** Utilizar el framework Spring Boot para agilizar la configuración y despliegue de la aplicación.
- **Mejora en la experiencia de usuario:** Implementar una interfaz web responsive y atractiva utilizando Thymeleaf como motor de plantillas.

### Diseño

#### Interfaz de usuario
- **Página de inicio:** Dashboard con resumen de albaranes y notificaciones.
- **Formulario de carga y gestión:** Páginas para la carga de albaranes, verificación de productos y generación de informes.
- **Seguridad:** Integración de un sistema de autenticación y roles para gestionar el acceso a las diferentes secciones.

#### Arquitectura
- **Controladores (Controllers):** Gestionan las peticiones HTTP y delegan la lógica de negocio a los servicios.
- **Servicios (Services):** Encapsulan la lógica de negocio y se comunican con la capa de acceso a datos.
- **Repositorios (Repositories):** Uso de Spring Data JPA para gestionar la persistencia en MySQL.
- **Vistas (Views):** Templates Thymeleaf que renderizan la interfaz web de forma dinámica.

### Implementación

#### Funcionalidades clave
- **Carga de albaranes y verificación online:** Permitir la carga de archivos PDF a través del navegador y mostrar el proceso de verificación en tiempo real.
- **Consulta y gestión de datos:** Visualización y edición de albaranes y productos mediante formularios web.
- **Generación de informes:** Creación de informes descargables (PDF o CSV) desde la interfaz web.

#### Tecnologías
- **Spring Boot y Spring MVC:** Frameworks para la configuración y manejo de la aplicación web.
- **Thymeleaf:** Motor de plantillas para la construcción de vistas.
- **Spring Data JPA:** Para la interacción con MySQL de forma sencilla.
- **MySQL:** Base de datos relacional.
- **Bootstrap (opcional):** Para mejorar la apariencia y la responsividad de la interfaz.

---

<a id="v-30-versión-móvil-flutterflow-supabase"></a>

## V 3.0: Versión Móvil (FlutterFlow, Supabase)

### Objetivos de esta versión
- **Experiencia móvil optimizada:** Ofrecer una interfaz diseñada especialmente para dispositivos móviles.
- **Sincronización en la nube:** Permitir la sincronización de datos entre dispositivos y la nube utilizando Supabase.
- **Interacción nativa:** Aprovechar las capacidades de los dispositivos móviles (cámara, sensores, etc.) para mejorar la experiencia de usuario.

### Diseño

#### Interfaz de usuario
- **Pantalla principal:** Vista inicial con acceso rápido a las funciones principales (carga de albaranes, escaneo de productos y generación de informes).
- **Flujos de navegación:** Diseñados para facilitar el acceso a todas las funcionalidades con un mínimo de clics.
- **Diseño responsivo y adaptativo:** Utilización de widgets y componentes de FlutterFlow para garantizar una experiencia consistente en Android e iOS.

#### Arquitectura
- **FlutterFlow:** Plataforma de desarrollo visual que permite diseñar interfaces y lógica de manera intuitiva.
- **Supabase:** Backend como servicio para autenticación, almacenamiento de datos y sincronización en tiempo real.
- **SQLite:** Base de datos local para el almacenamiento offline y sincronización cuando se recupere la conexión.

### Implementación

#### Funcionalidades clave
- **Carga y procesamiento de albaranes:** Permitir la captura y subida de albaranes en PDF o mediante imagen.
- **Escaneo de códigos de barras:** Integración con la cámara del dispositivo para la lectura de códigos de barras.
- **Sincronización de datos:** Uso de Supabase para almacenar y sincronizar los datos con el servidor, complementado por SQLite para el almacenamiento local.

#### Tecnologías
- **FlutterFlow:** Herramienta de desarrollo de aplicaciones móviles.
- **Supabase:** Plataforma de backend para autenticación y almacenamiento.
- **SQLite:** Base de datos local para el manejo offline.
- **API de IA (opcional en esta versión):** Preparar el terreno para una futura integración de procesamiento inteligente.

---

<a id="v-40-aplicación-nativa-android-android-studio-y-sqlite"></a>

## V 4.0: Aplicación Nativa Android (Android Studio y SQLite)

### Objetivos de esta versión
- **Optimización en Android:** Desarrollar una aplicación nativa que aproveche al máximo las capacidades de los dispositivos Android.
- **Rendimiento y usabilidad:** Garantizar una experiencia de usuario fluida y rápida, optimizada para el entorno Android.
- **Gestión local robusta:** Implementar almacenamiento local robusto con SQLite para la persistencia de datos sin depender de una conexión a internet constante.

### Diseño

#### Interfaz de usuario
- **Pantalla principal:** Con accesos directos a la carga de albaranes, escaneo y generación de informes.
- **Navegación optimizada:** Uso de `Activity` y `Fragment` para una navegación intuitiva.
- **Indicadores visuales:** Empleo de gráficos y notificaciones para evidenciar el estado de la verificación (productos recibidos vs. faltantes).

#### Arquitectura
- **Patrón MVVM:** Separación de la lógica de negocio y la interfaz utilizando ViewModels y LiveData.
- **Uso de Room:** Implementación de la capa de persistencia con Room para simplificar el manejo de SQLite y mejorar la seguridad de los datos.
- **Integración con hardware:** Uso de APIs nativas para el manejo de la cámara y la conexión con dispositivos Bluetooth para la lectura de códigos de barras.

### Implementación

#### Funcionalidades clave
- **Carga de albaranes y escaneo:** Captura de albaranes mediante PDF y escaneo de códigos con la cámara o lector Bluetooth.
- **Almacenamiento local:** Persistencia de albaranes y registros de verificación en SQLite mediante Room.
- **Generación de informes:** Creación de informes detallados exportables en formatos comunes (PDF/CSV) para su posterior análisis.

#### Tecnologías
- **Android Studio:** IDE para el desarrollo de aplicaciones nativas Android.
- **Room:** Biblioteca de persistencia para SQLite.
- **ZXing:** Biblioteca para la lectura de códigos de barras.
- **Bluetooth API:** Para la integración con lectores externos.

---

<a id="v-50-integración-de-ia-flutterflow-supabase-sqlite"></a>

## V 5.0: Integración de IA (FlutterFlow, Supabase, SQLite)

### Objetivos de esta versión
- **Automatización mediante IA:** Incorporar una API de inteligencia artificial para procesar albaranes automáticamente mediante técnicas de OCR y análisis de imágenes.
- **Mejora en la precisión:** Reducir la intervención manual y aumentar la exactitud en la extracción de datos.
- **Multiplataforma avanzada:** Consolidar una solución que funcione de forma fluida tanto en dispositivos móviles (Android/iOS) como en entornos híbridos, integrando datos locales y en la nube.

### Diseño

#### Interfaz de usuario
- **Pantalla de carga:** Interfaz para subir albaranes mediante archivos PDF o imágenes capturadas con la cámara.
- **Vista de resultados:** Muestra dinámica de los datos extraídos (número de albarán, fecha, proveedor, lista de productos) y las discrepancias detectadas.
- **Panel de informes:** Visualización y exportación de informes generados tras la verificación automatizada.

#### Arquitectura
- **FlutterFlow:** Para el desarrollo visual y la lógica multiplataforma de la aplicación.
- **Supabase:** Para la sincronización en la nube, autenticación y almacenamiento centralizado de datos.
- **SQLite:** Manejo de datos de forma offline y sincronización con la base de datos en Supabase cuando haya conectividad.
- **API de IA:** Integración de una solución externa o propia para OCR y procesamiento inteligente de albaranes.

### Implementación

#### Funcionalidades clave
- **Procesamiento automático:** Envío de la imagen o PDF a la API de IA que extrae los datos relevantes del albarán.
- **Sincronización de datos:** Gestión dual de datos (local en SQLite y en la nube con Supabase) para asegurar la disponibilidad y persistencia.
- **Interacción con el usuario:** Retroalimentación en tiempo real sobre el estado del procesamiento, notificaciones de éxito o errores en la extracción y verificación de datos.
- **Optimización de recursos:** Caching y manejo asíncrono de la información para mejorar el rendimiento en dispositivos móviles.

#### Tecnologías
- **FlutterFlow:** Para el diseño y desarrollo de la interfaz móvil.
- **Supabase:** Solución para backend y sincronización en tiempo real.
- **SQLite:** Base de datos local para almacenamiento offline.
- **API de IA/OCR:** Servicio de inteligencia artificial (como Tesseract, Google Vision API, u otro servicio especializado) para la extracción automática de datos de albaranes.
- **Bibliotecas de red:** Para la comunicación segura y eficiente entre la aplicación móvil y los servicios en la nube.
