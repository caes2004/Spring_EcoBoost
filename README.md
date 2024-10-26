Actualmente, debido a que JPA/Hibernate está configurado en modo create-drop, es necesario insertar manualmente los roles de usuario en la base de datos cada vez que se reinicia la aplicación.
Esto se debe a que el esquema se vuelve a crear y se eliminan los datos preexistentes en cada ejecución. A continuación, se presenta el código SQL necesario para realizar esta inserción:

select * from roles; --Validar los roles, en la primera ejecución debería aparecer vacios.

INSERT INTO roles (nombre) VALUES ('comprador');
INSERT INTO roles (nombre) VALUES ('vendedor');
