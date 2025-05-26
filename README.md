
# Práctica Obligatoria Tema 8

**Table of Contents**

[TOCM]

[TOC]

# 1. Introducción e Instalación
## 1.1 Instalación
# 2. Menú Inicio
## 2.1 Catálogo
## 2.2 Registro de Clientes
## 2.3 Inicio de sesión
# 3. Menú de Administradores
# 4. Menu de Trabajadores
# 5. Menu de Clientes

# 1. Introducción e Instalación
## 1.1. Instalación
Para empezar con la instalación de este programa empezaremos por descargar e instalar el [[JDK 23.0.1](http://http://https//download.oracle.com/java/23/latest/jdk-23_windows-x64_bin.exe "JDK 23.0.1")]. Durante la instalación deberemos verificar que se instale en el directorio: "c\Program Files\Java".

Después iremos al buscador de Windows y realizaremos la siguiente búsqueda: "Editar las variables de Entorno del sistema".

Buscaremos en "Variables del entorno" y dentro nos dirigiremos a "Variables del sistema". seleccionaremos "Path" y le daremos a editar.

En la ventana que se nos abrirá clicamos en la variable de Java e introducimos la siguiente ruta: "C:\Program Files\Java\jdk-23\bin", le damos a confirmar y aceptar.

### Descarga

Para descargar este programa en GitHub deberemos de cliquear el botón verde de "<> Code" y seleccionar la opción de "Download zip".

Cuando se nos descargue el archivo comprimido, lo descomprimimos y en la nueva carpeta encontraremos archivo Start.bat que será el que ejecute nuestro programa.

# 2. Menú Inicio

Antes de iniciar que el usuario pueda acceder al menu inicial la app cargará los datos de la carpeta ./data que se encuentra en la misma carpeta que el archivo start. En caso de que esta carpeta no exista en el mismo directorio que el archivo start.bat se creará una carpeta y se cargarán unos datos de default.


Cuando iniciemos la aplicación encontraremos un logo representativo de la empresa FernanShop junto con sus opciones de 
1. Mostrar Catálogo
2. Registrarse
3. Iniciar Sesión

![Captura de pantalla 2025-03-23 124422](https://github.com/user-attachments/assets/4ca93ce4-7e7e-474c-b64a-8a8742128af9)

## 2.1. Catálogo

Desde el menú de inicio se accede a esta opción introduciendo el botón 3.

Al acceder el usuario tendrá la opción se desplegará un submenú en el que el usuario tendrá la opción de ver el catalogo completo o elegir la forma en la que busca los productos dependiendo de la opción elegida:

![Captura de pantalla 2025-03-23 165851](https://github.com/user-attachments/assets/a6f270b2-10c1-4b6f-8c6c-cafb4d69a285)

Siendo las opciones las siguientes:

1. Ver el catálogo completo
Se mostrarán todos los productos del catalogo de 5 en 5

2. Mostrar productos por descripción
Se mostrarán los productos cuya descripción incluyan el texto que el usuario le pase por teclado

3. Mostrar productos por marca
En primer lugar se le mostrará un listado de todas las marcas de las que disponemos actualmente. Tras esta lista el usuario podrá buscar productos que en su marca incluyan el texto que el usuario escriba.

4. Mostrar productos por modelo
El usuario tendrá la oportunidad de buscar entre los productos aquellos cuyo modelo incluya el texto que el usuario escriba.

5. Mostrar productos por rango de precio
El usuario puede indicar un rango de precios y se le mostrarán sólo aquellos productos que estén en este rango.

6. Mostrar productos por término
El usuario podrá introducir un texto y se le mostrarán todos los productos que incluya dicho texto ya sea en su marca, modelo o descripción.

Si el producto está en promoción el producto aparecerá resaltado de forma distintiva a los demás.

## 2.2. Registrarse

Desde el menú inicial el usuario podrá registrarse como un cliente nuevo. El usuario debe de indicar su correo electrónico, contraseña, nombre teléfono móvil, dirección, localidad y provincia.

El usuario podrá dejar el formulario en cualquier momento dejando el texto que esté rellenando en ese momento vacío y pulsando enter.

A la hora de ingresar un correo no hay ningún tipo de restricción y podrá usar cualquier dominio que desee.

A la hora de ingresar la contraseña es necesario que esta incluya:
- Al menos una minúscula
- Al menos una mayúscula
- Al menos un número
- Un carácter especial (-_!@/&.:,;·#$€%¬/()=?¿¡)
- Deberá tener una longitud mínima de 8 caracteres

Por último el número de teléfono se comprobará si tiene 9 dígitos y si inicia por 6 o por 7.

Cuando termine el registro el usuario (ahora cliente) recibirá un correo electrónico que incluirá un token que se le necesitará para validar su cuenta la próxima vez que use la tercera opción del menú inicial y tras esto el usuario recién registrado podrá iniciar sesión en su menú de cliente si lo desea pero esto no le eximirá de validar su correo cuando sea necesario.

![Captura de pantalla 2025-03-23 175943](https://github.com/user-attachments/assets/34fd92c7-f507-4935-a4b7-1a7e44e09af0)


## 2.3. Iniciar sesión

Para acceder a la última opción introduciremos el 2 para acceder.
En esta se opción se nos pedirá un correo que sea válido y una contraseña. Antes de poder avanzar se le indicará al usuario tanto si el correo es válido como si la contraseña tiene todos los caracteres correctos. Tras esto se comprobará si el correo y contraseña coinciden con algún usuario. 

Internamente se comprobará si coincide con algún usuario primero comprobando si coincide con un administrador, seguido de los trabajadores y por último los clientes debido a la longitud de las listas y maximizando la velocidad de acceso.

En caso de que el correo y contraseña coincida con un cliente se comprobará si el correo del cliente está validado y en caso contrario recibirá un mensaje de que debe introducir el token que habrá recibido en su correo de bienvenida a la web.

Por último decir que en caso de fallar tres veces en el inicio de sesión se devolverá al usuario al menú de inicio de la app.

![Captura de pantalla 2025-03-23 182222](https://github.com/user-attachments/assets/4b3ae90d-63c0-45cb-9049-84f1b09b882c)

#3. Menu de administrador

Tras realizar con éxito el inicio de sesión si el sistema encuentra coincidencia del usuario y contraseña con alguno de los usuarios administradores se accede al menú de administrador.

![Captura de pantalla 2025-03-23 184612](https://github.com/user-attachments/assets/78cb266b-8ca3-4a0f-a112-74ce5a86f126)

En este menú se le notificará al administrador la cantidad de pedidos que no tengan ningún trabajador asignado.

![image](https://github.com/user-attachments/assets/c12f0b60-3ac8-467b-ba82-67511c0c9f3e)

El menú tendrá un total de 11 opciones:
1. Ver todo el catálogo
2. Editar un producto
3. Ver un resumen de los clientes
4. Ver un resumen de los pedidos
5. Ver un resumen de los trabajadores
6. Ver las estadísticas de la app
7. Cambiar el estado de un pedido
8. Dar de alta un trabajador
9. Dar de baja un trabajador
10. Asignar un pedido a un trabajador
11. Salir

La primera opción actúa igual que el menú de ver el catálogo en el menú de inicio de la app.

##3.1 Editar Producto
Al acceder a la opción 2 del menú el administrador accede al listado de productos del catálogo de forma resumida. 

En este menú se mostrarán los productos de 10 en 10 y el administrador podrá elegir el deseado indicándolo con el número que se encuentra a la izquierda del producto deseado.

![Captura de pantalla 2025-03-23 190349](https://github.com/user-attachments/assets/5d848090-ea25-4dea-9312-6540547781be)


Mostrará el producto expandido y se preguntará al administrador si el producto es correcto.

![Captura de pantalla 2025-03-23 191039](https://github.com/user-attachments/assets/f7d31262-8b41-4f79-b777-e1ab3457f9d5)

Una vez confirmado el producto que el administrador quiere confirmar se desplegará un menú en el que se podrá elegir qué atributo del producto se desea modificar.

1. Marca
2. Modelo
3. Descripción
4. Precio
5. Relevancia

Para salir tendrá que pulsar el 0 y podremos confirmar o no los cambios realizados.

Aquí tenemos unas imágenes de cómo se modifica el producto mientras lo trabajamos y como en caso de guardarlo se verán las modificaciones en el menú de productos resumidos.

![Captura de pantalla 2025-03-23 191953](https://github.com/user-attachments/assets/8a4ffe1b-9fab-4044-b55b-c25902933fb1)

![Captura de pantalla 2025-03-23 192220](https://github.com/user-attachments/assets/7364885d-e683-436b-aeb4-a07cd7e9d0f6)


##3.2 Ver resumen de clientes

Para acceder a esta opción deberemos de introducir 3 en el menú de administrador.

Una vez dentro tendremos una lista de clientes que aparecerán de 5 en 5 y en el que podremos elegir un cliente para verlo de forma más extendida eligiendolo con el número que aparezca al lado izquierdo del cliente deseado.

![Captura de pantalla 2025-03-23 192621](https://github.com/user-attachments/assets/12bcd080-08dd-40b2-9436-e31a8b87f58f)

##3.3 Ver resumen de pedidos
A esta opción se accede introduciendo un 4 en el menú de administradores.

Una vez dentro tendremos un listado pedidos que irán apareciendo de 10 en 10 y que en caso de que el administrador lo desee podremos ver un pedido de forma más extendida introduciendo el número a la izquierda del pedido deseado

![Captura de pantalla 2025-03-23 193209](https://github.com/user-attachments/assets/58e648db-7060-4a97-91ff-89e7cef1c2ce)
![Captura de pantalla 2025-03-23 193310](https://github.com/user-attachments/assets/38c3da27-78b6-42c7-83a1-48b6af249a28)

##3.4 Ver resumen de trabajadores
A esta opción accederemos introduciendo el número 5 en el menú de administradores.

Una vez dentro nos aparecerá un listado de todos los trabajadores de forma resumida. La lista aparecerá de 10 en 10 y podremos ver un trabajador de forma más extendida indicando el trabajador con el número que aparece junto al trabajador deseado.

![Captura de pantalla 2025-03-23 194904](https://github.com/user-attachments/assets/cd3c3a3c-c47c-44bd-83e5-af9b25e61ee0)
![Captura de pantalla 2025-03-23 194926](https://github.com/user-attachments/assets/3e58d1eb-3df5-40cd-9d46-e8bc199fa703)


## 3.5 Estadísticas de la app
Mediante la opción 6 accederemos a una ventana que nos da un pequeño resumen de las estadísticas de la app.

![Captura de pantalla 2025-03-23 195135](https://github.com/user-attachments/assets/27ff8bad-692c-424b-8293-20fea2d57bb2)

## 3.6 Cambiar estado de un pedido
Entrando en la opción 7 entraremos en un menú en el que se mostrarán todos los pedidos que hay actualmente en proceso. El administrador podrá elegir el pedido que desee modificar mediante el número del lateral.

Una vez el administrador confirme el pedido que desea modificar entrará a un submenú en el que podrá elegir si modificar el estado del pedido o añadir un comentario.

![Captura de pantalla 2025-03-23 230540](https://github.com/user-attachments/assets/811212a7-5663-4036-80e7-f4c400eace5f)

Si elegimos la opción de modificar el estado podremos elegir entre 3 estados distintos:
1. En preparación
Al elegir este estado el podremos modificar la nueva fecha de entrega estimada si lo deseamos y añadir un comentario
2. Enviado
Podremos añadir un comentario junto con la modificación
3. Cancelado.
Podremos añadir un comentario pero la fecha de entrega pasará a ser "Cancelado" en próximos resúmenes de pedidos.

La otra opción que podemos elegir es la de añadir comentarios sin necesidad de modificar el estado del pedido.

## 3.7 Dar de alta un trabajador
Para dar de alta a un trabajador primeramente deberemos de acceder a la opción 8.
Tras acceder se nos informa que en caso de que queremos dejar el proceso solo tenemos que dejar vacío el formulario en el que nos encontremos.

Cuando introducimos el correo electrónico el sistema nos informará en caso de que ya existiera el correo nos informará y también comprobará si ese correo pertenece a un trabajador ya existente por si queremos dar de alta a dicho trabajador de nuevo.

![Captura de pantalla 2025-03-23 235059](https://github.com/user-attachments/assets/289aeb17-ea0f-4aa2-8f5d-2ec20bb45529)

En caso de negativa deberemos añadir un nuevo correo que no exista en el sistema.
Tras esto se pedirán los siguientes datos para el trabajador
- Una clave válida que deberá contener:
	* Al menos una minúscula
	* Al menos una mayúscula
	* Al menos un número
	* Un carácter especial (-_!@/&.:,;·#$€%¬/()=?¿¡)
	* Deberá tener una longitud mínima de 8 caracteres
- El nombre del trabajador
- Un número de contacto
- Una id de telegram que el administrador deberá conocer con anterioridad.

Tras la realización de dada de alta se enviará un correo al trabajador para informarle que ya está registrado en el sistema.

##3.8 Dar de baja un trabajador
Para dar de baja a un trabajador accederemos al menú mediante la opción 9.
En este menú nos saldrá todo el listado de los trabajadores y podremos elegir al deseado que queremos dar de baja mediante indicando el trabajador con el número que tiene en el lado izquierdo y tras confirmar que es el deseado será automáticamente dado de baja.

![Captura de pantalla 2025-03-23 233024](https://github.com/user-attachments/assets/b62f9888-cbbc-4ce8-9adf-c96bf8b54aaf)

En caso de que el trabajador tenga pedidos asignados se le informará al administrador y posteriormente se le preguntará si se quiere desasignar los pedidos a dicho trabajador antes de dar de baja al mismo.

##3.9 Asignar pedidos a trabajadores
Para asignar un pedido deberemos acceder a la opción número 10 del menú de administrador. Una vez dentro se nos mostrará un menú de pedidos sin asignación y como administrador deberemos elegir el pedido que deseamos mediante el número que se encuentra a la izquierda del pedido deseado.

Tras confirmar el pedido elegido pasaremos a elegir un trabajador para asignarlo de la misma forma, es decir, mediante el número que lo identifica en el lateral izquierdo de la pantalla. 

Cuando confirmamos el trabajador se asignará a dicho trabajador el pedido deseado.


Con esto concluye la explicación de cómo funciona el menú del administrador.

##3.10 Ultimos inicios de sesión de usuarios
Mediante la opción numero 11 del menu de administrador. El administrador podrá comprobar cuando iniciaron sesión todos los usuarios de la aplicación

![image](https://github.com/user-attachments/assets/27d5fef0-7f4e-44d6-966c-0b9c222ee285)

##3.11 Exportar pedidos en formato excel
Mediante la opción numero 12 el administrador podra exportar los pedidos que se hayan realizado en la aplicación.

Mediante un submenu podrá elegir que pedidos quiere exportar siendo estos los siguientes:
![image](https://github.com/user-attachments/assets/d3672045-0a26-4e07-889f-18ab5eccbed9)

con la opción 1 el administrador recibirá un excel con todos los pedidos que se han realizado el la aplciación
con la opción 2 el administrador recibirá en su correo un excel con todos los pedidos que todavia no han sido asignados a un trabajador
con la opción 3 el excel que recibirá solo contendra los pedidos que ya hayan sido enviados al cliente o que hayan sido cancelados
con la opción 4 el excel contendrá solo los los pedidos que todavia no han sido completados.

##3.12 Realizar copia de seguridad

Con la opción 13 el admin realizará una copia de seguridad de la aplicación en el estado en el que se encuentre actualmente.

##3.13 Consultar rutas de configuración

con la opción 14 el admin podra comprobar cuales son las rutas absolutas en las que se guardan los datos de la app

![image](https://github.com/user-attachments/assets/af86b778-bcfd-4ff8-b45c-26227af81647)

##3.13 Modificar el modo invitado

Con la opción 15 el administrador podrá modificar el modo invitado.

Este modo mostrará la opción de ver el catalogo en el menu de inicio si esta activado:
![image](https://github.com/user-attachments/assets/e6705de5-852c-4d10-bc0c-c9a294d171e1)

o no mostrarlo en caso de que este desactivado:
![image](https://github.com/user-attachments/assets/729ffafe-bb85-4fd7-ab19-ffe9ee9a8ca2)

# 4. Menú de trabajadores
![Captura de pantalla 2025-03-24 004403](https://github.com/user-attachments/assets/c2a99c39-5c75-4d74-9330-63e8d2b718ff)

Al acceder al menú de los trabajadores nos aparecerán 8 opciones:
1. Consultar pedidos asignados
2. Modificar el estado de un pedido
3. Consultar el catálogo de productos
4. Modificar un producto del catálogo
5. Ver mi historial de pedidos terminados
6. Ver mi perfil
7. Modificar mis datos personales
8. Salir

## 4.1 Consultar pedidos asignados
Para acceder a este menú deberemos introducir la opción 1.

En esta opción recibiremos un listado en el que se mostrarán los pedidos asignados que están en estado de "Creado" o "En Preparación". 
Podremos elegir uno de los pedidos de este menú para verlo de forma extendida eligiendo el número que aparece a la izquierda.

![Captura de pantalla 2025-03-24 004822](https://github.com/user-attachments/assets/d042b80a-99e7-4ef9-945f-a8bb72ed5290)

## 4.2 Modificar Estado de un Pedido
Accederemos a esta opción introduciendo la opción 2 del menú.

Una vez dentro podremos elegir un pedido de la lista que se nos mostrará de pedidos que el trabajador tenga asignados.

Una vez confirmado el pedido que queremos modificar podremos modificar el estado o simplemente añadir un nuevo comentario igual que en el caso de los administradores.

![Captura de pantalla 2025-03-24 005020](https://github.com/user-attachments/assets/49028cca-7d62-425e-8b60-664905d37024)

## 4.3 Modificar un producto del catálogo
En esta opción a la que accedemos introduciendo la opción 4 del menú accederemos a un listado de todos los productos donde podremos elegir el que queramos mediante los números que se encuentran a la izquierda de los productos.

Una vez confirmado el producto que queramos modificar entraremos a un submenú en el que podremos elegir el que atributo del producto queremos modificar entre
1. Marca
2. Modelo
3. Descripción
4. Precio
5. Relevancia

![Captura de pantalla 2025-03-24 010550](https://github.com/user-attachments/assets/7b630c96-4905-46d6-86c0-45d424547820)

## 4.4 Ver Histórico de pedidos
En esta opción 5 del menú de trabajadores recibiremos un historial con todos los pedidos cuyo estado sea "Enviado" o "Cancelados".
![Captura de pantalla 2025-03-24 010911](https://github.com/user-attachments/assets/e37cd20c-d7c8-4b8c-930d-f68c4f29080b)

Podremos ver un pedido de forma más extendida eligiendolo con los números que hay a la izquierda de cada pedido.

## 4.5 Ver mi Perfil

En la opción 6 podremos observar los nuestros datos personales.

![image](https://github.com/user-attachments/assets/77beef33-e8c2-4bab-8f10-7355f604261f)


## 4.6 Modificar mis datos personales
Accediendo a la opción 7 podremos ver un submenú en el que podremos modificar nuestros datos personales.
1. Nombre
2. Correo
3. Contraseña
4. Móvil

![Captura de pantalla 2025-03-24 011639](https://github.com/user-attachments/assets/11aa0f75-84cc-499e-b2e7-116f06fca099)

Cuando el trabajador decida salir tendrá la opción de guardar los datos que haya modificado o dejarlos como estaban antes de entrar a esta opción.

# 5. Menu Cliente
{foto 28}

Cuando un cliente acceda a su menú podrá ver la cantidad de pedidos que tiene pendientes de entrega.

Tras esto tendrá varias opciones como son:
1. Consultar catálogo
2. Realizar pedido
3. Ver mis pedidos realizados
4. Ver mis datos personales
5. Modificar mis datos temporales.

## 4.1 Consultar catálogo
El cliente podrá consultar el catálogo en cualquier momento accediendo al mismo menú que tenemos disponible cuando no iniciamos sesión.

![Captura de pantalla 2025-03-24 011845](https://github.com/user-attachments/assets/38333104-cdf4-40d5-85c0-95f8f115c53d)


## 4.2 Realizar un pedido
Cuando accedemos a esta opción mediante la introducción número 2 accedemos a un submenú que nos indica cuántos productos tenemos actualmente en el carrito. 
Junto con esto tendremos varias opciones:
### 1. Buscar un producto:
Podremos consultar el catálogo para localizar el producto deseado
### 2. Ver el carrito actual
Podremos ver que productos contiene nuestro carrito actualmente.
![Captura de pantalla 2025-03-24 013053](https://github.com/user-attachments/assets/93f4cb3d-b8a1-4a49-a853-e77ecf67b3f5)
### 3. Añadir un producto al carrito
Podremos buscar un producto mediante un término que se comprobará si se encuentra entre las marcas, modelos o descripción del producto.

Se nos mostrará un listado con todos los productos que contengan coincidencias y podremos elegir el producto en cuestión mediante el número que aparece a la izquierda de los productos.

![Captura de pantalla 2025-03-24 013410](https://github.com/user-attachments/assets/caf38d7e-ab43-454f-b7cb-3024aba8b8a5)

### 4. Borrar un producto del carrito
Mediante esta opción veremos los productos que contiene el carrito y elegir cual queremos eliminar del carrito mediante la selección mediante los números que indican cada uno.

Tendremos que confirmar si el producto que hemos escogido es el que queremos eliminar y tras podremos eliminarlo sin problemas.

![Captura de pantalla 2025-03-24 015217](https://github.com/user-attachments/assets/6899e56b-aed8-461f-a2ae-b2c3215ea001)


### 5. Confirmar pedido
Con esta opción se nos mostrará el carrito actual y se nos preguntará si queremos finalizar la creación del pedido y realizarlo.

En caso de negativa el carrito se quedará guardado y saldremos al menú del cliente.

![Captura de pantalla 2025-03-24 015726](https://github.com/user-attachments/assets/55731dd9-db1e-43e6-92b0-619adae32ab4)

Cuando el cliente realice un pedido recibirá una factura en pdf en su email con un resumen del pedido que ha realizado.

### 6. Cancelar pedido
Con esta opción se nos mostrará el carrito actual y se nos preguntará si queremos borrar el carrito para empezar de cero la próxima vez que accedemos a este menú.

En caso de negativa el carrito se quedará guardado y saldremos al menú del cliente.

### 7. Salir del menú.
Con esta opción saldremos al menú principal dejando guardado el carrito en caso de que sea necesario.

## 5.3 Ver mis pedidos realizados
En esta opción que se accede mediante la introducción del número 3 nos aparecerá una lista con todos los pedidos que ha realizado el cliente hasta el momento.
Podremos elegir el pedido que queramos para verlo de forma más extendida mediante el número que aparece en el lado izquierdo del menú.
![Captura de pantalla 2025-03-24 020505](https://github.com/user-attachments/assets/274e70d0-7e6d-453c-a334-934969e36a68)
![Captura de pantalla 2025-03-24 020521](https://github.com/user-attachments/assets/b62d8824-f544-4487-9507-a28e1083b88f)


## 5.4 Ver mis datos personales
En la opción 4 del menú principal podremos acceder a un resumen de los datos del cliente que hay registrados en el sistema.


## 5.5 Modiciar Datos personales
En la opción 5 del menú principal el cliente accede su submenú en el que además de ver sus datos podrá modificar todos sus datos:
1. Nombre
2. Dirección
3. Localidad
4. Provincia
5. Teléfono
6. Correo
7. Contraseña

![Captura de pantalla 2025-03-24 021517](https://github.com/user-attachments/assets/cff66fc0-033d-4557-9391-360e0262c15f)


Tras elegir la opción 8 de salir se nos pedirá confirmar los cambios y si se cambió el correo se enviará un correo con un nuevo token para validar el nuevo correo.

