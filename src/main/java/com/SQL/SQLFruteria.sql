-- ####################################################################
-- # SCRIPT FRUTERIA
-- ####################################################################

-- Database: proyectobd_fruteria
-- NOTA: La creación de la base de datos se recomienda hacerla desde la interfaz de PostgreSQL (pgAdmin/psql)
-- DROP DATABASE IF EXISTS proyectobd_fruteria;

-- 1. LIMPIEZA INTERNA
-------------------------------------------------------------
-- Elimina el esquema completo y todo su contenido si existe
DROP SCHEMA IF EXISTS fruteria CASCADE;

-- 2. CREACIÓN DEL ESQUEMA
-------------------------------------------------------------
CREATE SCHEMA fruteria AUTHORIZATION postgres;
SET search_path TO fruteria;


-- 3. CREACIÓN DE TABLAS (DDL)
-------------------------------------------------------------

CREATE TABLE producto(
    codigo SERIAL PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL UNIQUE, -- Se recomienda que la descripción sea única
    categoria VARCHAR(50),
    unidad_medida VARCHAR(20),
    existencia INTEGER DEFAULT 0 CHECK (existencia >= 0),
    precio_c NUMERIC (8,2) CHECK (precio_c >= 0),
    precio_v NUMERIC (8,2) CHECK (precio_v >= 0)
);

CREATE TABLE proveedor(
    id_p SERIAL PRIMARY KEY, -- Usamos SERIAL para evitar el manejo manual de IDs
    nombre VARCHAR(80) NOT NULL,
    ciudad VARCHAR(30),
    contacto VARCHAR(70),
    tel_contacto VARCHAR(20)
);

-- Llave compuesta: un producto puede ser suministrado por varios proveedores
CREATE TABLE producto_proveedor(
    codigo INTEGER,
    id_p INTEGER,
    PRIMARY KEY (codigo, id_p), -- CRÍTICO: Llave primaria compuesta
    FOREIGN KEY (codigo) REFERENCES producto(codigo),
    FOREIGN KEY (id_p) REFERENCES proveedor(id_p)
);

CREATE TABLE cliente(
    id_c SERIAL PRIMARY KEY, -- Usamos SERIAL para evitar el manejo manual de IDs
    telefono VARCHAR(12),
    rfc VARCHAR(16) UNIQUE, -- El RFC debe ser único
    domicilio VARCHAR(50),
    estado BOOLEAN NOT NULL DEFAULT TRUE
);

-- Cliente de Tipo Persona Moral
CREATE TABLE p_moral(
    id_c INTEGER PRIMARY KEY,
    razon_social VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_c) REFERENCES cliente(id_c)
);

-- Cliente de Tipo Persona Física
CREATE TABLE p_fisica(
    id_c INTEGER PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL, -- Se amplió a 50 para nombres completos
    FOREIGN KEY (id_c) REFERENCES cliente(id_c)
);

CREATE TABLE empleado(
    id_e SERIAL PRIMARY KEY, -- Usamos SERIAL para evitar el manejo manual de IDs
    nombre VARCHAR(60) NOT NULL,
    turno VARCHAR(20) CHECK (turno IN ('matutino', 'vespertino')),
    salario NUMERIC (14,2) CHECK (salario > 0)
);

-- Relación de supervisión (recursiva)
CREATE TABLE supervisor(
    id_e INTEGER PRIMARY KEY, -- Empleado supervisado
    id_s INTEGER NOT NULL,    -- Empleado supervisor
    FOREIGN KEY (id_e) REFERENCES empleado(id_e),
    FOREIGN KEY (id_s) REFERENCES empleado(id_e)
);

CREATE TABLE venta(
    folio_v INTEGER PRIMARY KEY, -- Se manejará con secuencia
    fecha DATE NOT NULL,
    id_c INTEGER REFERENCES cliente(id_c),
    id_e INTEGER REFERENCES empleado(id_e)
);

-- Llave compuesta: CRÍTICO para evitar que un producto se venda dos veces en el mismo ticket
CREATE TABLE detalle_venta(
    codigo INTEGER REFERENCES producto(codigo),
    folio_v INTEGER REFERENCES venta(folio_v),
    observaciones VARCHAR(50),
    cantidad INTEGER CHECK (cantidad > 0),
    PRIMARY KEY (folio_v, codigo)
);

CREATE TABLE compra(
    folio_c INTEGER PRIMARY KEY, -- Se manejará con secuencia
    no_lote INTEGER,
    fecha DATE NOT NULL,
    id_p INTEGER REFERENCES proveedor(id_p),
    id_e INTEGER REFERENCES empleado(id_e)
);

-- Llave compuesta: CRÍTICO para evitar que un producto se compre dos veces en el mismo lote
CREATE TABLE detalle_compra(
    folio_c INTEGER REFERENCES compra(folio_c),
    codigo INTEGER REFERENCES producto(codigo),
    cantidad INTEGER CHECK (cantidad > 0),
    PRIMARY KEY (folio_c, codigo)
);

-- Tabla de Auditoría (Para los Triggers)
CREATE TABLE auditoria (
    id SERIAL PRIMARY KEY,
    nombre_tabla VARCHAR(50) NOT NULL,
    operacion VARCHAR(10) NOT NULL,
    usuario_bd VARCHAR(50) NOT NULL,
    fecha_auditoria TIMESTAMP NOT NULL DEFAULT NOW(),
    datos_anteriores JSONB,
    datos_nuevos JSONB
);


-- 4. INSERCIÓN DE DATOS DE PRUEBA (DML - 50+ datos por tabla)
-------------------------------------------------------------
-- Se mantiene el uso de setval() para continuar la numeración secuencial.

-- Productos (51 datos)
INSERT INTO producto (codigo, descripcion, categoria, unidad_medida, existencia, precio_c, precio_v) VALUES 
(5000, 'manzana roja','fruta','kilogramo', 15, 40.50, 65),
(5001, 'manzana verde','fruta','kilogramo', 8, 50, 60),
(5002, 'manzana amarilla','fruta','kilogramo', 10, 35, 50),
(5003, 'pera roja','fruta','kilogramo', 7, 50, 65),
(5004, 'pera de anjou','fruta','kilogramo', 5, 45, 56),
(5005, 'papaya','fruta','kilogramo', 20, 25, 30),
(5006, 'melón','fruta','pieza', 18, 25, 40),
(5007, 'naranja','fruta','kilogramo', 30, 20, 35),
(5008, 'plátano tabasco','fruta','kilogramo', 20, 20, 28),
(5009, 'platano macho','fruta','kilogramo', 30, 18, 28),
(5010, 'piña','fruta','pieza', 13, 32, 45),
(5011, 'calabacita italiana','verdura','kilogramo', 12, 10, 20),
(5012, 'chile serrano','verdura','kilogramo', 5, 15, 20),
(5013, 'chile jalapeño','verdura','kilogramo', 3, 22, 34),
(5014, 'papa','verdura','kilogramo', 20, 19, 27),
(5015, 'espinaca','verdura','ramito', 12, 4, 8),
(5016, 'lechuga romanita','verdura','pieza', 15, 5, 12),
(5017, 'ejote','verdura','kilogramo', 3, 17, 23),
(5018, 'jicama','fruta','kilogramo', 20, 28, 35),
(5019, 'sandia','fruta','kilogramo', 18, 10, 18),
(5020, 'chile poblano','verdura','kilogramo', 5, 35, 50),
(5022, 'limon','fruta','kilogramo', 10, 18, 22),
(5023, 'cebolla','verdura','kilogramo', 5, 18, 23),
(5024, 'tomate rojo','verdura','kilogramo', 30, 15, 23),
(5025, 'zanahoria','verdura','kilogramo', 40, 10, 18),
(5026, 'pepino','verdura','kilogramo', 25, 12, 20),
(5027, 'aguacate hass','fruta','kilogramo', 10, 70, 95),
(5028, 'mango manila','fruta','kilogramo', 18, 25, 35),
(5029, 'fresa','fruta','caja', 12, 45, 60),
(5030, 'arándano','fruta','caja', 5, 60, 85),
(5031, 'uva verde','fruta','kilogramo', 15, 55, 75),
(5032, 'champiñón','verdura','caja', 8, 30, 45),
(5033, 'cilantro','verdura','manojo', 50, 2, 5),
(5034, 'perejil','verdura','manojo', 45, 2, 5),
(5035, 'brócoli','verdura','pieza', 15, 20, 30),
(5036, 'coliflor','verdura','pieza', 10, 25, 35),
(5037, 'apio','verdura','pieza', 20, 15, 25),
(5038, 'ajo','verdura','cabeza', 30, 5, 10),
(5039, 'jengibre','verdura','kilogramo', 10, 40, 55),
(5040, 'coco','fruta','pieza', 15, 25, 40),
(5041, 'durazno','fruta','kilogramo', 8, 60, 80),
(5042, 'chayote','verdura','pieza', 25, 5, 10),
(5043, 'betabel','verdura','kilogramo', 15, 18, 28),
(5044, 'rábano','verdura','manojo', 30, 8, 15),
(5045, 'tomate verde','verdura','kilogramo', 20, 12, 20),
(5046, 'pimiento rojo','verdura','kilogramo', 10, 45, 65),
(5047, 'pimiento verde','verdura','kilogramo', 15, 35, 50),
(5048, 'elote','verdura','pieza', 50, 6, 10),
(5049, 'chile de árbol','verdura','gramo', 5, 80, 120),
(5050, 'chile guajillo','verdura','gramo', 8, 70, 100);

-- Proveedores (51 datos)
INSERT INTO proveedor (id_p, nombre, ciudad, contacto, tel_contacto) VALUES 
(2000, 'la manzanita', 'oaxaca','sr. carlos pérez','9512356789'),
(2001, 'la poblanita', 'puebla','sra. guadalupe hernández','2221357698'),
(2002, 'disribuidor el campo', 'tehuacán','sra. pilar martinez','2385763210'),
(2003, 'central de frutas', 'oaxaca','sr. arnulfo lópez','9518041200'),
(2004, 'verduras la guadaluoana', 'puebla','sra. araceli juárez','2223561211'),
(2005, 'surtidora s.a.', 'oaxaca','sr. joaquín solís','9518972344'),
(2006, 'las verduras a su mesa', 'puebla','sra. perla mijangos','2226751201'),
(2007, 'la piña loca', 'oaxaca','sr. fernando ortiz','9515431256'),
(2008, 'la juquilita', 'veracruz','sr. alfonso gómez','2293145798'),
(2009, 'la inmaculada', 'córdoba','sra. maría soriano','2717431155'),
(2010, 'el mango dulce', 'veracruz','sra. juana pascasio','2224671233'),
(2011, 'el sazón poblano', 'tehuacán','sr. jorge buenfil','2382340907'),
(2012, 'la naranjota', 'oaxaca','sra. paola dorantes','9515871244'),
(2013, 'el sazón poblano 2', 'tehuacán','sr. jorge buenfil','2382340907'),
(2014, 'mixtequita', 'oaxaca','sra. abigail manzano','9518765439'),
(2015, 'el sureño', 'oaxaca','sr. ruben anastasio','9516750908'),
(2016, 'las ánimas', 'tehuacán','sr. juan lópez','2382650078'),
(2017, 'los huacales', 'puebla','sra. bertha gutierrez','2229876501'),
(2018, 'los tenates', 'oaxaca','sra. virginia buenrostro','9512345633'),
(2019, 'frutas y verduras don toño', 'oaxaca','sr. antonio ranírez','9517654388'),
(2020, 'frutería altamirano', 'oaxaca','sr. tomás altamirano','9517659080'),
(2021, 'Distribuidora del Golfo', 'Veracruz', 'Lic. Eduardo Sáenz', '2291002000'),
(2022, 'Cosecha Fresca SA', 'Puebla', 'Ing. Silvia Torres', '2227003000'),
(2023, 'Fincas de Oaxaca', 'Oaxaca', 'Sra. Elena Ruiz', '9514004000'),
(2024, 'Agro Tehuacán', 'Tehuacán', 'Sr. Rogelio Vera', '2385005000'),
(2025, 'Hortifrutis', 'Córdoba', 'Arq. Carlos Rivera', '2716006000'),
(2026, 'Insumos Verdes', 'Puebla', 'C.P. Lilia Gómez', '2228007000'),
(2027, 'El Campesino Feliz', 'Oaxaca', 'Sr. Luis Morales', '9519008000'),
(2028, 'Granos y Semillas', 'Veracruz', 'Sra. Patricia Reyes', '2292009000'),
(2029, 'Productos del Valle', 'Tehuacán', 'Dr. Javier Soto', '2383010000'),
(2030, 'Sazon Natural', 'Oaxaca', 'Mtra. Ana Díaz', '9514011000'),
(2031, 'Verdulería Express', 'Puebla', 'Sr. Miguel Castro', '2225012000'),
(2032, 'Frutas de la Montaña', 'Veracruz', 'Sra. Isabel Flores', '2296013000'),
(2033, 'El Oasis', 'Córdoba', 'Ing. Fernando Pardo', '2717014000'),
(2034, 'Sol y Campo', 'Oaxaca', 'Lic. Laura Méndez', '9518015000'),
(2035, 'El Huerto Familiar', 'Puebla', 'Sr. José Aguilar', '2229016000'),
(2036, 'Los Tres Hermanos', 'Tehuacán', 'Sra. Martha Chávez', '2381017000'),
(2037, 'Frescura Garantizada', 'Veracruz', 'Sr. Ricardo Bernal', '2292018000'),
(2038, 'Oasis Verde', 'Oaxaca', 'Ing. Sofía Guerrero', '9513019000'),
(2039, 'La Casa del Chile', 'Puebla', 'Sr. Pedro Castillo', '2224020000'),
(2040, 'El Platanero', 'Veracruz', 'Sra. Gabriela López', '2295021000'),
(2041, 'Frutas Exóticas', 'Oaxaca', 'Dr. Arturo Montes', '9516022000'),
(2042, 'Verduras del Chef', 'Puebla', 'Mtra. Karla Ríos', '2227023000'),
(2043, 'El Naranjal', 'Veracruz', 'Sr. Juan Torres', '2298024000'),
(2044, 'Sabor Oaxaqueño', 'Oaxaca', 'Sra. Carmen Cruz', '9519025000'),
(2045, 'El Manantial', 'Puebla', 'Lic. Roberto Solís', '2221026000'),
(2046, 'Tierras Altas', 'Tehuacán', 'Ing. Valeria Soto', '2382027000'),
(2047, 'El Maizal', 'Oaxaca', 'Sr. Diego Herrera', '9513028000'),
(2048, 'La Flor de Liz', 'Veracruz', 'Sra. Rebeca Gómez', '2294029000'),
(2049, 'El Sabor de México', 'Puebla', 'Sr. Martín Reyes', '2225030000'),
(2050, 'AgroMex', 'Oaxaca', 'Sra. Mónica Díaz', '9516031000');


-- Asignación de IDs SERIAL a tablas que lo usan (para que los INSERTs siguientes funcionen)
SELECT setval('fruteria.producto_codigo_seq', (SELECT MAX(codigo) FROM fruteria.producto), true);
SELECT setval('fruteria.proveedor_id_p_seq', (SELECT MAX(id_p) FROM fruteria.proveedor), true);


-- Producto_Proveedor (50 datos)
INSERT INTO producto_proveedor VALUES 
(5000, 2008), (5012, 2019), (5024, 2013), (5016, 2006), (5015, 2006),
(5008, 2015), (5009, 2015), (5001, 2003), (5007, 2003), (5009, 2005),
(5002, 2009), (5010, 2009), (5018, 2018), (5019, 2015), (5020, 2019),
(5003, 2000), (5004, 2000), (5005, 2008), (5006, 2002), (5010, 2020),
(5011, 2003), (5012, 2004), (5013, 2003), (5014, 2009), (5015, 2020),
-- Nuevos datos para llegar a 50
(5025, 2021), (5026, 2022), (5027, 2023), (5028, 2024), (5029, 2025),
(5030, 2026), (5031, 2027), (5032, 2028), (5033, 2029), (5034, 2030),
(5035, 2031), (5036, 2032), (5037, 2033), (5038, 2034), (5039, 2035),
(5040, 2036), (5041, 2037), (5042, 2038), (5043, 2039), (5044, 2040),
(5045, 2041), (5046, 2042), (5047, 2043), (5048, 2044), (5049, 2045);


-- Clientes (100 datos totales) - CORREGIDO
INSERT INTO cliente (id_c, telefono, rfc, domicilio) VALUES 
-- Clientes 3000 - 3049 (Usados para Persona Moral)
(3000,'9713234566', 'ABCD123456', 'juárez 505'), (3001,'9512067788', 'EFGH789123', 'morelos 1205'),(3002,'9562899901', 'AIJK345678', 'independencia 2202'),
(3003,'9716234533', 'MABC123456', 'matamoros 345'), (3004,'9518231133', 'KLGH789123', 'lindavista 235'),(3005,'9563214422', 'WZKT345678', 'iturbide 2202'),
(3006,'9713234567', 'ABCD123457', 'juárez 505'), (3007,'9518065511', 'AMLN889123', 'pico de orizaba 333'),(3008,'9571459901', 'OPQR235678', 'anillo periférico 15'),
(3009,'9516451323', 'XYZW234565', 'emiliano zapata 303'), (3010,'9512067789', 'IJKH833415', 'emilio carranza 890'),(3011,'9519870022', 'QRST897654', 'río de la plata 297'),
(3012,'9786745321', 'PABC901203', 'justo sierra 1111'), (3013,'9516452300', 'MARZ130921', 'cayetano 23'),(3014,'9589128801', 'XJYT150623', 'guadalupe hinojosa 2300'),
(3015,'9717234866', 'RTUO170312', 'periférico 5671'), (3016,'9585691277', 'HAIO141120', 'alfiles 285'),(3017,'9514866901', 'ZDOP130630', 'armonía 1302'),
(3018,'9511230907', 'CUBO100418', 'regueira 3456'), (3019,'9566231222', 'GPLM110918', 'faisanes 23'),(3020,'9511599871', 'LEWI170211', 'brasil 803'),
(3021,'9713234588', 'WERT101010', 'Calle A 1'), (3022,'9512067799', 'ASDF202020', 'Calle B 2'), (3023,'9562899911', 'ZXCV303030', 'Calle C 3'),
(3024,'9716234544', 'QWER404040', 'Calle D 4'), (3025,'9518231144', 'TYUI505050', 'Calle E 5'), (3026,'9563214433', 'PLOK606060', 'Calle F 6'),
(3027,'9713234599', 'MJNB707070', 'Calle G 7'), (3028,'9518065522', 'YHGT808080', 'Calle H 8'), (3029,'9571459911', 'RFDE909090', 'Calle I 9'),
(3030,'9516451333', 'VCSA102030', 'Calle J 10'), (3031,'9512067700', 'LPOK405060', 'Calle K 11'), (3032,'9519870033', 'IJHU708090', 'Calle L 12'),
(3033,'9786745333', 'BVNM112233', 'Calle M 13'), (3034,'9516452311', 'QAZX445566', 'Calle N 14'), (3035,'9589128811', 'SWED778899', 'Calle O 15'),
(3036,'9717234877', 'RFVT123456', 'Calle P 16'), (3037,'9585691288', 'GYHU789012', 'Calle Q 17'), (3038,'9514866911', 'TGBN345678', 'Calle R 18'),
(3039,'9511230911', 'ZAQW901234', 'Calle S 19'), (3040,'9566231233', 'SDFG567890', 'Calle T 20'), (3041,'9511599888', 'HJKL123456', 'Calle U 21'),
(3042,'9713234500', 'ZXCV789012', 'Calle V 22'), (3043,'9512067701', 'BNMM345678', 'Calle W 23'), (3044,'9562899902', 'ASDQ901234', 'Calle X 24'),
(3045,'9716234503', 'FGHJ567890', 'Calle Y 25'), (3046,'9518231104', 'QWER123456', 'Calle Z 26'), (3047,'9563214405', 'TYUI789012', 'Calle AA 27'),
(3048,'9713234506', 'PLOK345678', 'Calle BB 28'), (3049,'9518065507', 'MJNB901234', 'Calle CC 29'),
-- Clientes 3050 - 3099 (Usados para Persona Física) - Añadidos para completar los 100 clientes
(3050,'9711001100', 'PRRA800101XYZ', 'Calle 1 de Mayo 10'),
(3051,'9512002200', 'MMCS800202ABC', 'Av. Juárez 20'),
(3052,'9563003300', 'RMSM800303DEF', 'Independencia 30'),
(3053,'9714004400', 'BRGG800404GHI', 'Matamoros 40'),
(3054,'9515005500', 'ASLL800505JKL', 'Lindavista 50'),
(3055,'9566006600', 'CMPP800606MNO', 'Iturbide 60'),
(3056,'9717007700', 'MLHH800707PQR', 'Calle G 70'),
(3057,'9518008800', 'AHHS800808STU', 'Pico de Orizaba 80'),
(3058,'9579009900', 'RVCC800909VWX', 'Anillo Periférico 90'),
(3059,'9511010101', 'GSRR801010YZA', 'Emiliano Zapata 100'),
(3060,'9512020202', 'SRTT801111BCD', 'Emilio Carranza 110'),
(3061,'9513030303', 'JNMM801212EFG', 'Río de la Plata 120'),
(3062,'9784040404', 'BSDD801313HIJ', 'Justo Sierra 130'),
(3063,'9515050505', 'MÁRV801414KLM', 'Cayetano 140'),
(3064,'9586060606', 'LGPP801515NOP', 'Guadalupe Hinojosa 150'),
(3065,'9717070707', 'RHRR801616QRS', 'Periférico 160'),
(3066,'9588080808', 'SJLL801717TUV', 'Alfiles 170'),
(3067,'9519090909', 'EVTT801818WXY', 'Armonía 180'),
(3068,'9511111111', 'XFSS801919ZAB', 'Regueira 190'),
(3069,'9562222222', 'DARJ802020CDE', 'Faisanes 200'),
(3070,'9513333333', 'IABP802121FGH', 'Brasil 210'),
(3071,'9714444444', 'CDGG802222IJK', 'Calle A 220'),
(3072,'9515555555', 'EMRR802323LMN', 'Calle B 230'),
(3073,'9566666666', 'FGIO802424OPQ', 'Calle C 240'),
(3074,'9717777777', 'GPNÑ802525RST', 'Calle D 250'),
(3075,'9518888888', 'HQPP802626UVW', 'Calle E 260'),
(3076,'9579999999', 'ISTS802727XYZ', 'Calle F 270'),
(3077,'9511212121', 'JTVV802828ZAA', 'Calle G 280'),
(3078,'9513434343', 'KUZZ802929ABB', 'Calle H 290'),
(3079,'9785656565', 'LVCC803030ACC', 'Calle I 300'),
(3080,'9517878787', 'MWEE803131ADD', 'Calle J 310'),
(3081,'9589090909', 'NYFF803232AEE', 'Calle K 320'),
(3082,'9711313131', 'OZZL803333AFF', 'Calle L 330'),
(3083,'9582424242', 'PAMM803434AGG', 'Calle M 340'),
(3084,'9513535353', 'QBSS803535AHH', 'Calle N 350'),
(3085,'9564646464', 'RCDD803636AII', 'Calle O 360'),
(3086,'9715757575', 'SDGG803737AJJ', 'Calle P 370'),
(3087,'9516868686', 'TELE803838AKK', 'Calle Q 380'),
(3088,'9577979797', 'UFNÑ803939ALL', 'Calle R 390'),
(3089,'9518080808', 'VGGR804040AMM', 'Calle S 400'),
(3090,'9519191919', 'WHII804141ANN', 'Calle T 410'),
(3091,'9781010101', 'YIJG804242AOO', 'Calle U 420'),
(3092,'9512323232', 'ZJKK804343APP', 'Calle V 430'),
(3093,'9584545454', 'ALMM804444AQQ', 'Calle W 440'),
(3094,'9716767676', 'BNNO804545ARR', 'Calle X 450'),
(3095,'9587878787', 'CQPP804646ASS', 'Calle Y 460'),
(3096,'9518989898', 'DRSS804747ATT', 'Calle Z 470'),
(3097,'9561212121', 'ESTT804848AUU', 'Calle AA 480'),
(3098,'9713434343', 'FUVI804949AVV', 'Calle BB 490'),
(3099,'9515656565', 'GZAA805050AWW', 'Calle CC 500');

SELECT setval('fruteria.cliente_id_c_seq', (SELECT MAX(id_c) FROM fruteria.cliente), true);
        
-- Clientes Persona Moral (50 datos) - IDs 3000 a 3049
INSERT INTO fruteria.p_moral (id_c, razon_social) VALUES 
(3000,'La Espiga de Oro S.A.'), (3001,'La Calabaza Feliz S.C.'),  (3002, 'Grupo Frutero del Bajío'), (3003, 'Viandas del Centro Mayorista'), (3004,'Frutas y Verduras del Sur S.P.R.'), 
(3005,'Grupo Surtidores del Norte'), (3006,'La Pera Verde Distribuciones'), (3007,'La Central de Verduras Frescas'),  (3008,'Frutas La Poblana Express'),
(3009,'El Mercadito de Don Pepe'), (3010, 'Fruteria La Tortuga Veloz'),  (3011, 'Alimentos Selectos Gourmet'),
(3012, 'Comercializadora El Manzano'),
(3013, 'Proveedora Agrícola Integral'),
(3014, 'Vegetales de la Huerta S.A.'),
(3015, 'Distribuidora Fénix Fresco'),
(3016, 'Insumos Culinarios MX'),
(3017, 'Mercado de Abastos Digital'),
(3018, 'Cadena de Tiendas El Limón'),
(3019, 'Suministros del Campo Verde'),
(3020, 'Logística Frutal Rápida'),
(3021, 'Bodega de Semillas y Granos'),
(3022, 'El Rincón del Aguacate S.C.'),
(3023, 'Fondo de Abarrotes y Víveres'),
(3024, 'Organización Cosecha Segura'),
(3025, 'Empresarial de Alimentos Puros'),
(3026, 'Conexión Hortícola Azteca'),
(3027, 'Mayoristas del Trópico'),
(3028, 'Frutales de Exportación Global'),
(3029, 'La Naranja Mecánica S.A.'),
(3030, 'El Plátano Festivo'),
(3031, 'Productora de Jugos Naturales'),
(3032, 'Supermercado La Canasta'),
(3033, 'Servicios de Catering Frescura'),
(3034, 'El Granero de Mamá Lucha'),
(3035, 'Agropecuaria Tierra Fértil'),
(3036, 'Distribución de Productos Orgánicos'),
(3037, 'La Tienda de la Esquina S.A.'),
(3038, 'Abarrotes Don Chuy'),
(3039, 'Central de Carnes y Frutas'),
(3040, 'Mi Rancho Chico S.P.R.'),
(3041, 'Frutiverduras La Palma'),
(3042, 'Empacadora El Durazno'),
(3043, 'Mayorista El Camión'),
(3044, 'Hortifrutícola del Mar'),
(3045, 'El Huerto Secreto'),
(3046, 'Cooperativa La Milpa'),
(3047, 'Comercio Exterior de Alimentos'),
(3048, 'Tienda de Conveniencia 24/7'),
(3049, 'Distribuidora de Hortalizas Finas');


-- Clientes Persona Física (50 datos) - IDs 3050 a 3099
INSERT INTO fruteria.p_fisica (id_c, nombre) VALUES 
(3050,'Armando Pérez Rocha'), 
(3051,'Margarita Martínez Castro'),
(3052,'Rogelio Medina Soto'), 
(3053, 'Braulio Robles Guzmán'), 
(3054, 'Andrés Suárez López'),
(3055,'Celia Mayoral Pineda'), 
(3056,'Mauricio Lugo Herrera'), 
(3057,'Adriana Herrera Salas'), 
(3058,'Roberto Valladares Cruz'), 
(3059, 'Guadalupe Santibañez Rico'),
(3060,'Silvia Ramírez Torres'), 
(3061,'Javier Nájera Mora'), 
(3062,'Brenda Solís Díaz'), 
(3063,'Miguel Ángel Ruíz Vega'), 
(3064,'Laura García Pérez'),
(3065,'Ricardo Hernández Ríos'), 
(3066,'Sofía Jiménez Luna'), 
(3067,'Emilio Vargas Trejo'), 
(3068,'Ximena Flores Soto'), 
(3069,'Diego Alonso Reyes'),
(3070,'Ana Isabel Bravo Pérez'), 
(3071,'Carlos Dávila Guzmán'), 
(3072,'Elena Mendoza Rivas'), 
(3073,'Fernando Gil Olvera'), 
(3074,'Gloria Peña Núñez'),
(3075,'Héctor Quiróz Ponce'), 
(3076,'Irma Salgado Torres'), 
(3077,'Jorge Téllez Vera'), 
(3078,'Karla Uribe Zúñiga'), 
(3079,'Luis Valdés Castillo'),
(3080,'Mónica Wong Estrada'), 
(3081,'Noé Yáñez Fuentes'), 
(3082,'Óscar Zárate Leal'), 
(3083,'Patricia Acosta Mora'), 
(3084,'Quetzali Bravo Solís'),
(3085,'Raúl Cortés Delgado'), 
(3086,'Susana Durán Gómez'), 
(3087,'Tomás Enríquez López'), 
(3088,'Úrsula Franco Núñez'), 
(3089,'Vicente Galván Rojas'),
(3090,'Wendy Haro Islas'), 
(3091,'Yolanda Ibarra Juárez'), 
(3092,'Zacarías Jasso King'), 
(3093,'Alma Lira Montes'), 
(3094,'Benito Nieto Ochoa'),
(3095,'Carmen Quiroz Palma'), 
(3096,'David Robles Segura'), 
(3097,'Eva Sosa Tapia'), 
(3098,'Felipe Urbina Vidal'), 
(3099,'Gabriela Zapata Aguirre');


-- Empleados (51 datos)
INSERT INTO empleado (id_e, nombre, turno, salario) VALUES 
(7000,'ricardo valencia','matutino',5500), (7001,'juan gonzález','matutino',5200), (7002,'susana lara','matutino',3500),	
(7003,'maria fernández','vespertino',5500), (7004,'andrés rubio','vespertino',4300), (7005,'rosalba zárate','vespertino',3800),
(7006,'bryan smith','matutino',4800), (7007,'will blades','matutino',5100), (7008,'cinthya house','matutino',3700),
(7009,'robert de niro','vespertino',5230), (7010,'william trace','vespertino',3770), (7011,'marie wonk','vespertino',3580),
(7012,'susan right','matutino',3200), (7013,'fabricio cortés','matutino',4800), (7014,'randy wells','matutino',4600),
(7015,'maya frost','vespertino',5500), (7016,'ramses white','vespertino',5150), (7017,'rose felp','vespertino',4300),
(7018,'raúl peniche','matutino',4500), (7019,'magie swift','matutino',3850), (7020,'manuel gómez','matutino',4200),
-- Nuevos empleados para llegar a 50
(7021,'Ana López','matutino',4100), (7022,'Carlos Pérez','vespertino',4400), (7023,'Diana Torres','matutino',3900),
(7024,'Elías Ruiz','vespertino',5000), (7025,'Fernanda Soto','matutino',4700), (7026,'Gabriel Flores','vespertino',4000),
(7027,'Hilda García','matutino',4550), (7028,'Iker Díaz','vespertino',4850), (7029,'Julia Castro','matutino',3600),
(7030,'Kevin Cruz','vespertino',5300), (7031,'Laura Morales','matutino',4250), (7032,'Mario Reyes','vespertino',3950),
(7033,'Nadia Herrera','matutino',4650), (7034,'Omar Jiménez','vespertino',4900), (7035,'Paula Vargas','matutino',3800),
(7036,'Quique Montes','vespertino',5400), (7037,'Rita Núñez','matutino',4150), (7038,'Saúl Bravo','vespertino',4450),
(7039,'Tania Solís','matutino',3750), (7040,'Uriel Ramos','vespertino',5100), (7041,'Valeria Luna','matutino',4750),
(7042,'Xavier Robles','vespertino',4050), (7043,'Yolanda Pardo','matutino',4600), (7044,'Zoe Vidal','vespertino',4800),
(7045,'Alan Guerrero','matutino',3550), (7046,'Beto Flores','vespertino',5200), (7047,'Ceci López','matutino',4300),
(7048,'David Pérez','vespertino',3850), (7049,'Eva Torres','matutino',4950), (7050,'Irving Perez','matutino',4650);

SELECT setval('fruteria.empleado_id_e_seq', (SELECT MAX(id_e) FROM fruteria.empleado), true);

-- Supervisor (51 datos)
INSERT INTO supervisor VALUES 
-- Supervisa Robert de Niro (7009)
(7000, 7009),(7001, 7009), (7002, 7009), (7003, 7009), (7004, 7009),(7005, 7009),(7006, 7009),(7007, 7009),(7008, 7009),
-- Supervisa Manuel Gómez (7020)
(7010, 7020),(7011, 7020), (7012, 7020), (7013, 7020), (7014, 7020),(7015, 7020),(7016, 7020),(7017, 7020),(7018, 7020),(7019, 7020),
-- Nuevos datos para llegar a 51 (Usando 7000 y 7020 como supervisores)
(7021, 7000), (7022, 7020), (7023, 7000), (7024, 7020), (7025, 7000),
(7026, 7020), (7027, 7000), (7028, 7020), (7029, 7000), (7030, 7020),
(7031, 7000), (7032, 7020), (7033, 7000), (7034, 7020), (7035, 7000),
(7036, 7020), (7037, 7000), (7038, 7020), (7039, 7000), (7040, 7020),
(7041, 7000), (7042, 7020), (7043, 7000), (7044, 7020), (7045, 7000),
(7046, 7020), (7047, 7000), (7048, 7020), (7049, 7000), (7050, 7020); 

-- Ventas (50 datos)
INSERT INTO venta VALUES 
(1,'2025-01-01',3000, 7010), (2,'2025-01-02',3001,7019),(3,'2025-01-03',3002,7000), (4,'2025-01-04',3003,7001),(5,'2025-01-05',3003,7002),
(6,'2025-02-10',3050, 7011), (7,'2025-02-12',3051,7006),(8,'2025-02-15',3052,7007), (9,'2025-02-17',3053,7008),(10,'2025-02-20',3054,7011),
(11,'2025-03-15',3000, 7001), (12,'2025-03-17',3055,7012),(13,'2025-03-18',3056,7002), (14,'2025-03-20',3057,7001),(15,'2025-03-25',3058,7015),
(16,'2025-04-06',3059, 7003), (17,'2025-04-07',3060,7018),(18,'2025-04-08',3061,7011), (19,'2025-04-12',3062,7004),(20,'2025-04-15',3063,7002),
(21,'2025-05-01',3021,7021), (22,'2025-05-02',3022,7022),(23,'2025-05-03',3023,7023), (24,'2025-05-04',3024,7024),(25,'2025-05-05',3025,7025),
(26,'2025-05-06',3026,7026), (27,'2025-05-07',3027,7027),(28,'2025-05-08',3028,7028), (29,'2025-05-09',3029,7029),(30,'2025-05-10',3030,7030),
(31,'2025-05-11',3031,7031), (32,'2025-05-12',3032,7032),(33,'2025-05-13',3033,7033), (34,'2025-05-14',3034,7034),(35,'2025-05-15',3035,7035),
(36,'2025-05-16',3036,7036), (37,'2025-05-17',3037,7037),(38,'2025-05-18',3038,7038), (39,'2025-05-19',3039,7039),(40,'2025-05-20',3040,7040),
(41,'2025-05-21',3041,7041), (42,'2025-05-22',3042,7042),(43,'2025-05-23',3043,7043), (44,'2025-05-24',3044,7044),(45,'2025-05-25',3045,7045),
(46,'2025-05-26',3046,7046), (47,'2025-05-27',3047,7047),(48,'2025-05-28',3048,7048), (49,'2025-05-29',3049,7049),(50,'2025-05-30',3000,7000);

-- Detalle_Venta (69 filas)
INSERT INTO detalle_venta VALUES 
(5000, 1,'',3), (5013, 1,'',2), (5008, 1,'',5),
(5004, 2,'',1), (5011, 2,'',3), (5003, 2,'',2),
(5005, 3,'',1), (5006, 3,'',1), (5007, 3,'',1),
(5009, 4,'',2), (5007, 4,'',1), (5012, 4,'',2),
(5001, 5,'',1), (5014, 5,'',2), (5009, 5,'',2),
(5003, 6,'',4), (5004, 6,'',2), (5016, 6,'',5),
(5010, 7,'',3), (5011, 7,'',2), (5012, 7,'',2),
(5001, 8,'',1), (5002, 8,'',2), (5003, 8,'',1),
(5007, 9,'',2), (5008, 9,'',1), (5014, 9,'',2),
(5017, 10,'',1), (5018, 10,'',2), (5019, 10,'',3),
(5008, 11,'',2), (5009, 11,'',2), (5010, 11,'',2),
(5000, 12,'',1), (5020, 12,'',1), (5015, 12,'',1),
(5007, 13,'',3), (5009, 13,'',2), (5013, 13,'',1),
(5017, 14,'',2), (5018, 14,'',2), (5019, 14,'',2),
(5003, 15,'',4), (5004, 15,'',2), (5016, 15,'',5),
(5012, 16,'',1), (5013, 16,'',2), (5014, 16,'',2),
(5005, 17,'',1), (5006, 17,'',1), (5013, 17,'',1),
(5014, 18,'',2), (5015, 18,'',1),	
(5009, 19,'',6), (5020, 19,'',3),	
(5008, 20,'',3), (5004, 20,'',2),
-- Nuevos detalles de venta (para completar los 50 folios)
(5025, 21,'',5), (5026, 21,'',2), (5027, 22,'',1), (5028, 22,'',3),
(5029, 23,'',2), (5030, 23,'',1), (5031, 24,'',4), (5032, 24,'',2),
(5033, 25,'',10), (5034, 25,'',8), (5035, 26,'',3), (5036, 26,'',1),
(5037, 27,'',5), (5038, 27,'',10), (5039, 28,'',2), (5040, 28,'',3),
(5041, 29,'',1), (5042, 29,'',4), (5043, 30,'',2), (5044, 30,'',6),
(5045, 31,'',3), (5046, 31,'',1), (5047, 32,'',5), (5048, 32,'',10),
(5049, 33,'',1), (5050, 33,'',2), (5000, 34,'',3), (5001, 34,'',4),
(5002, 35,'',2), (5003, 35,'',1), (5004, 36,'',5), (5005, 36,'',3),
(5006, 37,'',2), (5007, 37,'',5), (5008, 38,'',1), (5009, 38,'',2),
(5010, 39,'',3), (5011, 39,'',4), (5012, 40,'',1), (5013, 40,'',2),
(5014, 41,'',3), (5015, 41,'',2), (5016, 42,'',5), (5017, 42,'',3),
(5018, 43,'',2), (5019, 43,'',1), (5020, 44,'',4), (5022, 44,'',2),
(5023, 45,'',3), (5024, 45,'',5), (5025, 46,'',2), (5026, 46,'',1),
(5027, 47,'',3), (5028, 47,'',2), (5029, 48,'',4), (5030, 48,'',1),
(5031, 49,'',3), (5032, 49,'',2), (5033, 50,'',5), (5034, 50,'',3);


-- Compras (50 datos)
INSERT INTO compra VALUES 
(8000,4000,'2024-12-01',2015,7002), (8001,4020,'2024-12-10',2003,7005), (8002,300,'2024-12-15',2008,7010),
(8003,897,'2024-12-30',2007,7009), (8004,1250,'2025-01-5',2012,7000), (8005,8432,'2025-01-8',2005,7001),
(8006,390,'2025-01-11',2010,7006), (8007,125,'2025-01-16',2011,7002), (8008,54000,'2025-02-12',2004,7017),
(8009,8234,'2025-02-18',2000,7004), (8010,565,'2025-02-20',2006,7002), (8011,1934,'2025-02-21',2013,7005),
(8012,509,'2025-03-01',2002,7015), (8013,29865,'2025-03-04',2001,7004), (8014,19823,'2025-03-08',2003,7008),
(8015,2376,'2025-03-01',2016,7011), (8016,4093,'2025-03-01',2015,7005), (8017,9870,'2025-04-10',2017,7001),
(8018,5234,'2025-04-12',2005,7002), (8019,345,'2025-04-15',2012,7018), (8020,87654,'2025-04-17',2012,7006),
(8021,3123,'2025-05-01',2018,7007), (8022,1567,'2025-05-03',2004,7008),
-- Nuevas compras para llegar a 50
(8023, 100,'2025-05-04',2000,7021), (8024, 200,'2025-05-05',2001,7022), (8025, 300,'2025-05-06',2002,7023),
(8026, 400,'2025-05-07',2003,7024), (8027, 500,'2025-05-08',2004,7025), (8028, 600,'2025-05-09',2005,7026),
(8029, 700,'2025-05-10',2006,7027), (8030, 800,'2025-05-11',2007,7028), (8031, 900,'2025-05-12',2008,7029),
(8032, 1000,'2025-05-13',2009,7030), (8033, 1100,'2025-05-14',2010,7031), (8034, 1200,'2025-05-15',2011,7032),
(8035, 1300,'2025-05-16',2012,7033), (8036, 1400,'2025-05-17',2013,7034), (8037, 1500,'2025-05-18',2014,7035),
(8038, 1600,'2025-05-19',2015,7036), (8039, 1700,'2025-05-20',2016,7037), (8040, 1800,'2025-05-21',2017,7038),
(8041, 1900,'2025-05-22',2018,7039), (8042, 2000,'2025-05-23',2019,7040), (8043, 2100,'2025-05-24',2020,7041),
(8044, 2200,'2025-05-25',2021,7042), (8045, 2300,'2025-05-26',2022,7043), (8046, 2400,'2025-05-27',2023,7044),
(8047, 2500,'2025-05-28',2024,7045), (8048, 2600,'2025-05-29',2025,7046), (8049, 2700,'2025-05-30',2026,7047);

-- Detalle_Compra (69 filas)
INSERT INTO detalle_compra VALUES 
(8000, 5018,30),(8000,5017,10),(8000,5005,18),(8000,5010,20),
(8001, 5000,20),(8001,5010,20),(8001,5008,10),(8001,5014,15),
(8002, 5015,40),(8002,5007,22),(8002,5002,20),
(8003, 5006,15),(8003,5005,20),
(8004, 5001,20),(8004,5007,17),(8004,5009,20),(8004,5013,25),
(8005, 5014,10),(8005,5011,15),(8005,5010,26),(8005,5008,30),
(8006, 5008,10),(8006,5009,10),
(8007, 5002,25),(8007,5004,17),(8007,5006,26),
(8008, 5002,20),(8008,5005,20),(8008,5009,10),(8008,5010,10),
(8009, 5003,20),(8009,5005,10),(8009,5007,10),(8009,5012,22),
(8010, 5005,10),(8010,5006,10),(8010,5009,10),
(8011, 5006,20),(8011,5012,20),(8011,5015,20),
(8012, 5002,20),(8012,5013,15),(8012,5004,20),
(8013, 5015,25),(8013,5018,10),(8013,5006,20),
(8014, 5000,15),(8014,5001,10),(8014,5003,10),
(8015, 5004,10),(8015,5005,10),(8015,5006,10),
(8016, 5010,25),(8016,5011,20),(8016,5012,10),
(8017, 5017,20),(8017,5018,20),(8017,5019,15),
(8018, 5001,25),(8018,5019,10),(8018,5020,10),
(8019, 5004,15),(8019,5006,12),(8019,5008,10),
(8020, 5009,20),(8020,5010,15),(8020,5014,18),
(8021, 5000,10),(8021,5002,10),(8021,5006,28),
(8022, 5015,15),(8022,5016,10),(8022,5017,20),
-- Nuevos detalles de compra (para llegar a 50+ filas)
(8023, 5025, 10), (8024, 5026, 12), (8025, 5027, 8), (8026, 5028, 15), (8027, 5029, 6),
(8028, 5030, 4), (8029, 5031, 10), (8030, 5032, 5), (8031, 5033, 50), (8032, 5034, 45),
(8033, 5035, 10), (8034, 5036, 8), (8035, 5037, 15), (8036, 5038, 20), (8037, 5039, 10),
(8038, 5040, 15), (8039, 5041, 7), (8040, 5042, 30), (8041, 5043, 10), (8042, 5044, 25),
(8043, 5045, 15), (8044, 5046, 8), (8045, 5047, 12), (8046, 5048, 40), (8047, 5049, 4),
(8048, 5050, 6), (8049, 5000, 10);


-- 5. CONFIGURACIÓN DE SECUENCIAS (Folios)
-------------------------------------------------------------
-- Venta
CREATE SEQUENCE fruteria.folio_venta_seq START 1;
-- Asegura que la secuencia continúe desde el último valor insertado
SELECT setval('fruteria.folio_venta_seq', (SELECT COALESCE(MAX(folio_v), 0) FROM fruteria.venta), true); 
ALTER TABLE fruteria.venta	
ALTER COLUMN folio_v SET DEFAULT nextval('fruteria.folio_venta_seq');

-- Compra
CREATE SEQUENCE fruteria.folio_compra_seq START 1;
-- Asegura que la secuencia continúe desde el último valor insertado
SELECT setval('fruteria.folio_compra_seq', (SELECT COALESCE(MAX(folio_c), 0) FROM fruteria.compra), true); 
ALTER TABLE fruteria.compra	
ALTER COLUMN folio_c SET DEFAULT nextval('fruteria.folio_compra_seq');


-- 6. CREACIÓN DE FUNCIONES (PL/pgSQL)
-------------------------------------------------------------

-- FUNCIÓN 1: registra un producto en detalle_venta y descuenta su existencia.
-- Utiliza la cláusula FOR UPDATE para prevenir errores de stock en entornos concurrentes.
CREATE OR REPLACE FUNCTION fruteria.registrar_detalle_y_stock(
    p_folio_v INTEGER,
    p_codigo INTEGER,
    p_cantidad INTEGER,
    p_observaciones VARCHAR(50)
)
RETURNS VOID AS $$
DECLARE
    v_existencia_actual INTEGER;
BEGIN
    -- Verificar existencia antes de la venta
    -- Bloquear y obtener la existencia actual. El FOR UPDATE previene 
    -- que otras transacciones modifiquen la existencia hasta que esta termine.
    SELECT existencia INTO v_existencia_actual FROM fruteria.producto WHERE codigo = p_codigo FOR UPDATE;
 
    -- Verificar que el producto existe y que la cantidad solicitada no supera el stock.
    IF v_existencia_actual IS NULL THEN
        RAISE EXCEPTION 'Error: El producto con código % no existe.', p_codigo;
    ELSIF v_existencia_actual < p_cantidad THEN
        RAISE EXCEPTION 'Error de Stock: Solo quedan % unidades del producto % (se solicitó %).', 
            v_existencia_actual, p_codigo, p_cantidad;
    END IF;

    -- 1. INSERTAR en la tabla detalle_venta
    INSERT INTO fruteria.detalle_venta (codigo, folio_v, observaciones, cantidad)
    VALUES (p_codigo, p_folio_v, p_observaciones, p_cantidad);

    -- 2. ACTUALIZAR la existencia del producto (Descuento de stock)
    UPDATE fruteria.producto
    SET existencia = existencia - p_cantidad
    WHERE codigo = p_codigo;

END;
$$ LANGUAGE plpgsql;

-- FUNCIÓN 2: Maneja la transacción completa de una Compra.
CREATE OR REPLACE FUNCTION fruteria.registrar_detalle_compra_y_stock(
    p_folio_c INTEGER,
    p_codigo_prod INTEGER,
    p_cantidad INTEGER
)
RETURNS VOID AS $$
BEGIN
    -- 1. Insertar en detalle_compra
    INSERT INTO fruteria.detalle_compra (folio_c, codigo, cantidad)
    VALUES (p_folio_c, p_codigo_prod, p_cantidad);

    -- 2. Actualizar existencia en producto
    UPDATE fruteria.producto
    SET existencia = existencia + p_cantidad
    WHERE codigo = p_codigo_prod;

END;
$$ LANGUAGE plpgsql;

-- FUNCIÓN 3: BUSCAR ID DE CLIENTE POR RFC O NOMBRE
-- Devuelve el ID del cliente o NULL si no se encuentra.
CREATE OR REPLACE FUNCTION fruteria.obtener_id_cliente_por_identificador(
    p_identificador TEXT -- Puede ser RFC, Nombre, o Razón Social
)
RETURNS INTEGER AS $$
DECLARE
    v_id_c INTEGER;
BEGIN
    -- 1. Buscar en la tabla principal de cliente por RFC (esto debería ser la clave)
    SELECT id_c INTO v_id_c
    FROM fruteria.cliente
    WHERE rfc ILIKE '%' || p_identificador || '%' -- Búsqueda exacta o parcial del RFC
    LIMIT 1;

    IF v_id_c IS NOT NULL THEN
        RETURN v_id_c;
    END IF;
    
    -- 2. Buscar en Persona Física por nombre o apellidos
    SELECT id_c INTO v_id_c
    FROM fruteria.p_fisica
    WHERE nombre ILIKE '%' || p_identificador || '%'
    LIMIT 1;

    IF v_id_c IS NOT NULL THEN
        RETURN v_id_c;
    END IF;

    -- 3. Buscar en Persona Moral por Razón Social
    SELECT id_c INTO v_id_c
    FROM fruteria.p_moral
    WHERE razon_social ILIKE '%' || p_identificador || '%'
    LIMIT 1;

    RETURN v_id_c; -- Devuelve el ID encontrado (o NULL si no se encuentra)
END;
$$ LANGUAGE plpgsql;

-- FUNCIÓN 4: Esta función calcula el monto total de una venta específica, 
-- identificada por su folio_v.
CREATE OR REPLACE FUNCTION fruteria.calcular_total_venta(
    p_folio_v INTEGER
)
RETURNS NUMERIC AS $$
DECLARE
    v_total NUMERIC := 0.0;
BEGIN
    SELECT SUM(dv.cantidad * p.precio_v)
    INTO v_total
    FROM fruteria.detalle_venta dv
    JOIN fruteria.producto p ON dv.codigo = p.codigo
    WHERE dv.folio_v = p_folio_v;

    RETURN COALESCE(v_total, 0.0);
END;
$$ LANGUAGE plpgsql;

--TIGGER FUCTIONS

---1. FUNCION: Registra el nombre de la tabla, la operación y el usuario que hizo el cambio
CREATE OR REPLACE FUNCTION fruteria.auditar_cambios() 
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO fruteria.auditoria(nombre_tabla, operacion, usuario_bd)
    VALUES (TG_TABLE_NAME, TG_OP, current_user);

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- TIGGERS DE AUDITORIA EN TABLAS
CREATE TRIGGER tr_auditoria_producto
AFTER INSERT OR UPDATE OR DELETE ON fruteria.producto
FOR EACH ROW
EXECUTE PROCEDURE fruteria.auditar_cambios();


CREATE TRIGGER tr_aud_cliente
AFTER INSERT OR UPDATE OR DELETE ON fruteria.cliente
FOR EACH ROW
EXECUTE FUNCTION fruteria.auditar_cambios();


CREATE TRIGGER tr_aud_p_fisica
AFTER INSERT OR UPDATE OR DELETE ON fruteria.p_fisica
FOR EACH ROW
EXECUTE FUNCTION fruteria.auditar_cambios();


CREATE TRIGGER tr_aud_p_moral
AFTER INSERT OR UPDATE OR DELETE ON fruteria.p_moral
FOR EACH ROW
EXECUTE FUNCTION fruteria.auditar_cambios();


CREATE TRIGGER tr_aud_empleado
AFTER INSERT OR UPDATE OR DELETE ON fruteria.empleado
FOR EACH ROW
EXECUTE FUNCTION fruteria.auditar_cambios();


CREATE TRIGGER tr_aud_venta
AFTER INSERT OR UPDATE OR DELETE ON fruteria.venta
FOR EACH ROW
EXECUTE FUNCTION fruteria.auditar_cambios();


CREATE TRIGGER tr_aud_detalle_venta
AFTER INSERT OR UPDATE OR DELETE ON fruteria.detalle_venta
FOR EACH ROW
EXECUTE FUNCTION fruteria.auditar_cambios();


CREATE TRIGGER tr_aud_detalle_compra
AFTER INSERT OR UPDATE OR DELETE ON fruteria.detalle_compra
FOR EACH ROW
EXECUTE FUNCTION fruteria.auditar_cambios();


-- 2. FUNCION: Resta del inventario la cantidad vendida
CREATE OR REPLACE FUNCTION fruteria.restar_stock()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE fruteria.producto
    SET existencia = existencia - NEW.cantidad
    WHERE codigo = NEW.codigo;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- TRIGGER: Se activa después de insertar un detalle de venta
CREATE TRIGGER tr_restar_stock
AFTER INSERT ON fruteria.detalle_venta
FOR EACH ROW
EXECUTE FUNCTION fruteria.restar_stock();


-- 3. FUNCION: Suma al inventario la cantidad comprada
CREATE OR REPLACE FUNCTION fruteria.sumar_stock()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE fruteria.producto
    SET existencia = existencia + NEW.cantidad
    WHERE codigo = NEW.codigo;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- TRIGGER: Se activa después de insertar un detalle de compra
CREATE TRIGGER tr_sumar_stock
AFTER INSERT ON fruteria.detalle_compra
FOR EACH ROW
EXECUTE FUNCTION fruteria.sumar_stock();

-- 4. FUNCION:Evitar que se venda más producto del que hay en inventario.
CREATE OR REPLACE FUNCTION fruteria.validar_stock_venta()
RETURNS TRIGGER AS $$
DECLARE
    stock_actual INTEGER;
BEGIN
    -- Obtener existencia actual
    SELECT existencia INTO stock_actual
    FROM fruteria.producto
    WHERE codigo = NEW.codigo;

    -- Validar stock
    IF stock_actual < NEW.cantidad THEN
        RAISE EXCEPTION 
        'Stock insuficiente para el producto % (existencia: %, solicitado: %)', 
        NEW.codigo, stock_actual, NEW.cantidad;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

--TIGGGER
CREATE TRIGGER tr_validar_stock_venta
BEFORE INSERT ON fruteria.detalle_venta
FOR EACH ROW
EXECUTE FUNCTION fruteria.validar_stock_venta();
