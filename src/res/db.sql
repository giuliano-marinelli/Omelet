CREATE DATABASE db;

CREATE TABLE IF NOT EXISTS departamento (
    id_departamento INT, 
    numero INT, 
    PRIMARY KEY (id_departamento)
);

CREATE TABLE IF NOT EXISTS empleado (
    nombre INT, 
    persona.id_persona INT FOREIGN KEY REFERENCES persona(id_persona), 
    tiene.departamento.id_departamento INT FOREIGN KEY REFERENCES departamento(id_departamento), 
    PRIMARY KEY (persona.id_persona)
);

CREATE TABLE IF NOT EXISTS profesor (
    nombre INT, 
    apellido INT, 
    persona.id_persona INT FOREIGN KEY REFERENCES persona(id_persona), 
    academico.id_academico INT FOREIGN KEY REFERENCES academico(id_academico), 
    PRIMARY KEY (persona.id_persona)
);

CREATE TABLE IF NOT EXISTS alumno (
    nombre INT, 
    apellido INT, 
    persona.id_persona INT FOREIGN KEY REFERENCES persona(id_persona), 
    academico.id_academico INT FOREIGN KEY REFERENCES academico(id_academico), 
    PRIMARY KEY (persona.id_persona)
);

CREATE TABLE IF NOT EXISTS producto (
    id_producto INT, 
    PRIMARY KEY (id_producto)
);

CREATE TABLE IF NOT EXISTS persona (
    id_persona INT, 
    nombre INT, 
    apellido INT, 
    PRIMARY KEY (id_persona)
);

CREATE TABLE IF NOT EXISTS secretario (
    numero INT, 
    nombre INT, 
    hora INT, 
    empleado.id_persona INT FOREIGN KEY REFERENCES empleado(persona.id_persona), 
    PRIMARY KEY (empleado.id_persona)
);

CREATE TABLE IF NOT EXISTS academico (
    id_academico INT, 
    facultado.id_facultado INT FOREIGN KEY REFERENCES facultado(id_facultado), 
    grado.id_grado INT FOREIGN KEY REFERENCES grado(id_grado), 
    PRIMARY KEY (id_academico)
);

CREATE TABLE IF NOT EXISTS administrativo (
    id_administrativo INT, 
    facultado.id_facultado INT FOREIGN KEY REFERENCES facultado(id_facultado), 
    PRIMARY KEY (id_administrativo)
);

CREATE TABLE IF NOT EXISTS facultado (
    id_facultado INT, 
    PRIMARY KEY (id_facultado)
);

CREATE TABLE IF NOT EXISTS rol (
    id_rol INT, 
    trabaja.academico.id_academico INT FOREIGN KEY REFERENCES academico(id_academico), 
    PRIMARY KEY (id_rol)
);

CREATE TABLE IF NOT EXISTS cara (
    id_cara INT, 
    tipo_ojos INT, 
    es_parte.cabeza.id_cabeza INT FOREIGN KEY REFERENCES cabeza(id_cabeza), 
    es_parte.cabeza.forma_cabeza INT FOREIGN KEY REFERENCES cabeza(forma_cabeza), 
    PRIMARY KEY (id_cara, es_parte.cabeza.id_cabeza, es_parte.cabeza.forma_cabeza)
);

CREATE TABLE IF NOT EXISTS cabeza (
    id_cabeza INT, 
    forma_cabeza INT, 
    tipo_pelo INT, 
    PRIMARY KEY (id_cabeza, forma_cabeza)
);

CREATE TABLE IF NOT EXISTS grado (
    id_grado INT, 
    numero INT, 
    PRIMARY KEY (id_grado)
);

CREATE TABLE IF NOT EXISTS ensena (
    ensena.profesor.id_persona INT FOREIGN KEY REFERENCES profesor(persona.id_persona), 
    ensena.alumno.id_persona INT FOREIGN KEY REFERENCES alumno(persona.id_persona), 
    PRIMARY KEY (ensena.profesor.id_persona, ensena.alumno.id_persona)
);

CREATE TABLE IF NOT EXISTS compra (
    fecha INT, 
    compra.producto.id_producto INT FOREIGN KEY REFERENCES producto(id_producto), 
    compra.respuesta.persona.id_persona INT FOREIGN KEY REFERENCES persona(id_persona), 
    PRIMARY KEY (compra.producto.id_producto)
);


