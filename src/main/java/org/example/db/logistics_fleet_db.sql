-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 04-04-2026 a las 17:13:41
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `logistics_fleet_db`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `drivers`
--

CREATE TABLE `drivers` (
  `id_driver` int(11) NOT NULL,
  `num_identification` int(15) NOT NULL,
  `name` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `secondLastname` varchar(50) DEFAULT NULL,
  `contratationDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `drivers`
--

INSERT INTO `drivers` (`id_driver`, `num_identification`, `name`, `lastname`, `secondLastname`, `contratationDate`) VALUES
(1, 10123456, 'Andrés', 'García', 'López', '2022-01-10'),
(2, 10234567, 'Beatriz', 'Mendoza', 'Pérez', '2022-03-15'),
(3, 10345678, 'Camilo', 'Torres', 'Rojas', '2022-06-20'),
(4, 10456789, 'Diana', 'Ramírez', 'Cano', '2022-08-05'),
(5, 10567890, 'Esteban', 'Quintero', 'Mejía', '2022-11-12'),
(6, 20123456, 'Fabián', 'Castro', 'Hernández', '2023-01-30'),
(7, 20234567, 'Gloria', 'Sánchez', 'Vargas', '2023-02-14'),
(8, 20345678, 'Hugo', 'Martínez', 'Osorio', '2023-04-10'),
(9, 20456789, 'Isabel', 'Jiménez', 'Pineda', '2023-05-22'),
(10, 20567890, 'Jorge', 'Gómez', 'Suárez', '2023-07-01'),
(11, 30123456, 'Karen', 'Álvarez', 'Ruiz', '2023-08-15'),
(12, 30234567, 'Luis', 'Moreno', 'Díaz', '2023-10-05'),
(13, 30345678, 'Mónica', 'Ortiz', 'Morales', '2023-12-10'),
(14, 30456789, 'Nelson', 'Herrera', 'Giraldo', '2024-01-20'),
(15, 30567890, 'Paola', 'Valencia', 'Ríos', '2024-02-25');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `driver_licences`
--

CREATE TABLE `driver_licences` (
  `id_licence` int(11) NOT NULL,
  `issue_date` date NOT NULL,
  `expiry_date` date NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `id_category` int(11) NOT NULL,
  `id_driver` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `driver_licences`
--

INSERT INTO `driver_licences` (`id_licence`, `issue_date`, `expiry_date`, `description`, `id_category`, `id_driver`) VALUES
(1, '2022-01-10', '2027-01-10', 'Camión sencillo', 5, 1),
(2, '2022-03-15', '2025-03-15', 'Bus de pasajeros', 6, 2),
(3, '2022-06-20', '2027-06-20', 'Tractomula pesada', 7, 3),
(4, '2022-08-05', '2027-08-05', 'Reparto urbano', 5, 4),
(5, '2022-11-12', '2025-11-12', 'Carga extrapesada', 7, 5),
(6, '2023-01-30', '2028-01-30', 'Transporte intermunicipal', 6, 6),
(7, '2023-02-14', '2028-02-14', 'Furgón mediano', 5, 7),
(8, '2023-04-10', '2026-04-10', 'Articulado', 7, 8),
(9, '2023-05-22', '2028-05-22', 'Buseta', 6, 9),
(10, '2023-07-01', '2028-07-01', 'Estacas', 5, 10),
(11, '2023-08-15', '2026-08-15', 'Remolque', 7, 11),
(12, '2023-10-05', '2028-10-05', 'SITP', 6, 12),
(13, '2023-12-10', '2028-12-10', 'Turbo', 5, 13),
(14, '2024-01-20', '2027-01-20', 'Cisterna', 7, 14),
(15, '2024-02-25', '2029-02-25', 'Escolar', 6, 15),
(16, '2022-01-10', '2027-01-10', 'Categoría moto personal', 2, 1),
(17, '2022-06-20', '2027-06-20', 'Habilitado para vehículo particular', 3, 3),
(18, '2022-06-20', '2032-06-20', 'Moto (Vigencia 10 años por ser joven)', 2, 3),
(19, '2022-11-12', '2025-11-12', 'Recategorización a C1', 5, 5),
(20, '2023-04-10', '2033-04-10', 'Licencia de moto nueva', 2, 8),
(21, '2023-07-01', '2028-07-01', 'Complemento particular', 3, 10),
(22, '2023-10-05', '2033-10-05', 'Maneja moto para llegar al trabajo', 2, 12),
(23, '2024-02-25', '2029-02-25', 'Equivalencia particular', 3, 15);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `license_categories`
--

CREATE TABLE `license_categories` (
  `id_category` int(11) NOT NULL,
  `category_name` varchar(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `license_categories`
--

INSERT INTO `license_categories` (`id_category`, `category_name`) VALUES
(1, 'A1'),
(2, 'A2'),
(3, 'B1'),
(4, 'B2'),
(5, 'B3'),
(6, 'C1'),
(7, 'C2'),
(8, 'C3');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `maintenances`
--

CREATE TABLE `maintenances` (
  `id_maintenance` int(11) NOT NULL,
  `date` date NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  `cost` double NOT NULL,
  `id_vehicle` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `maintenances`
--

INSERT INTO `maintenances` (`id_maintenance`, `date`, `description`, `cost`, `id_vehicle`) VALUES
(1, '2024-01-10', 'Cambio de aceite', 150.5, 1),
(2, '2024-01-15', 'Frenos y discos', 450, 2),
(3, '2024-02-05', 'Llantas delanteras', 300.25, 3),
(4, '2024-02-12', 'Sincronización', 280, 4),
(5, '2024-02-20', 'Reparación motor', 1200.8, 5),
(6, '2024-03-01', 'Suspensión', 350, 6),
(7, '2024-03-05', 'Alineación', 85, 7),
(8, '2024-03-10', 'Cambio aceite', 150.5, 8),
(9, '2024-03-12', 'Sistema eléctrico', 210, 9),
(10, '2024-03-15', 'Frenos', 420, 10),
(11, '2024-03-18', 'Filtros aire', 90, 11),
(12, '2024-03-20', 'Batería nueva', 180, 12),
(13, '2024-03-22', 'Caja de cambios', 850, 13),
(14, '2024-03-25', 'Engrase general', 120, 14),
(15, '2024-03-28', 'Espejos y luces', 75, 15),
(16, '2024-02-15', 'Cambio de pastillas de frenos', 120, 1),
(17, '2024-04-10', 'Revisión de suspensión', 210.5, 1),
(18, '2024-05-05', 'Cambio de aceite y filtros', 145, 1),
(19, '2024-02-20', 'Rotación de llantas', 60, 2),
(20, '2024-03-25', 'Limpieza de inyectores', 180, 2),
(21, '2024-05-12', 'Cambio de kit de embrague', 550, 2),
(22, '2024-01-10', 'Reparación de luces traseras', 45, 3),
(23, '2024-03-05', 'Alineación y balanceo', 90, 3),
(24, '2024-04-20', 'Cambio de batería', 165, 3),
(25, '2024-02-12', 'Mantenimiento aire acondicionado', 110, 4),
(26, '2024-04-15', 'Cambio de aceite', 140, 4),
(27, '2024-05-20', 'Sincronización de motor', 320, 4),
(28, '2024-01-25', 'Cambio de llantas traseras', 800, 5),
(29, '2024-03-30', 'Revisión de frenos ABS', 250, 5),
(30, '2024-05-15', 'Engrase de chasis', 85, 5),
(31, '2024-02-05', 'Cambio de plumillas', 35, 6),
(32, '2024-04-22', 'Cambio de aceite', 150, 6),
(33, '2024-05-25', 'Reparación de radiador', 190, 6),
(34, '2024-02-28', 'Mantenimiento preventivo', 200, 7),
(35, '2024-03-15', 'Ajuste de pernos', 50, 7),
(36, '2024-05-01', 'Cambio de filtros de aire', 75, 7),
(37, '2024-01-15', 'Sustitución de turbo', 950, 8),
(38, '2024-04-05', 'Cambio de aceite', 150, 8),
(39, '2024-05-28', 'Revisión técnica', 120, 8),
(40, '2024-02-10', 'Pintura de bómper', 220, 9),
(41, '2024-03-20', 'Cambio de correas', 180, 9),
(42, '2024-05-10', 'Limpieza de tanque combustible', 130, 9),
(43, '2024-02-22', 'Cambio de sensor de oxígeno', 95, 10),
(44, '2024-04-18', 'Alineación', 80, 10),
(45, '2024-05-22', 'Aceite y valvulina', 190, 10),
(46, '2024-01-30', 'Ajuste de espejos', 30, 11),
(47, '2024-03-12', 'Cambio de aceite', 155, 11),
(48, '2024-05-08', 'Revisión de frenos', 110, 11),
(49, '2024-02-14', 'Lavado de motor', 40, 12),
(50, '2024-04-01', 'Cambio de bombillos', 25, 12),
(51, '2024-05-18', 'Engrase general', 80, 12),
(52, '2024-01-20', 'Reparación de caja', 1100, 13),
(53, '2024-03-25', 'Cambio de aceite', 150, 13),
(54, '2024-05-12', 'Filtro de combustible', 65, 13),
(55, '2024-02-05', 'Cambio de llanta repuesto', 350, 14),
(56, '2024-04-10', 'Mantenimiento preventivo', 210, 14),
(57, '2024-05-28', 'Revisión de suspensión', 240, 14),
(58, '2024-01-18', 'Scanner electrónico', 70, 15),
(59, '2024-03-30', 'Cambio de aceite', 150, 15),
(60, '2024-05-15', 'Cambio de pastillas', 130, 15);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `routes`
--

CREATE TABLE `routes` (
  `id_route` int(11) NOT NULL,
  `origin` varchar(50) NOT NULL,
  `destination` varchar(50) NOT NULL,
  `distance` int(11) NOT NULL,
  `fuel_consumed` int(11) NOT NULL,
  `travelDate` date NOT NULL,
  `id_vehicle` int(11) NOT NULL,
  `id_driver` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `routes`
--

INSERT INTO `routes` (`id_route`, `origin`, `destination`, `distance`, `fuel_consumed`, `travelDate`, `id_vehicle`, `id_driver`) VALUES
(1, 'Bogotá', 'Medellín', 420, 45, '2024-03-01', 1, 1),
(2, 'Medellín', 'Cali', 450, 50, '2024-03-02', 2, 2),
(3, 'Cali', 'Buenaventura', 120, 15, '2024-03-03', 3, 3),
(4, 'Bogotá', 'Villavicencio', 125, 18, '2024-03-04', 4, 4),
(5, 'Barranquilla', 'Cartagena', 120, 14, '2024-03-05', 5, 5),
(6, 'Bucaramanga', 'Cúcuta', 200, 28, '2024-03-06', 6, 6),
(7, 'Pereira', 'Armenia', 45, 6, '2024-03-07', 7, 7),
(8, 'Manizales', 'Bogotá', 300, 38, '2024-03-08', 8, 8),
(9, 'Neiva', 'Pitalito', 180, 22, '2024-03-09', 9, 9),
(10, 'Ibagué', 'Armenia', 85, 12, '2024-03-10', 10, 10),
(11, 'Santa Marta', 'Riohacha', 170, 20, '2024-03-11', 11, 11),
(12, 'Montería', 'Sincelejo', 90, 10, '2024-03-12', 12, 12),
(13, 'Pasto', 'Ipiales', 80, 12, '2024-03-13', 13, 13),
(14, 'Tunja', 'Sogamoso', 80, 9, '2024-03-14', 14, 14),
(15, 'Popayán', 'Cali', 140, 16, '2024-03-15', 15, 15),
(16, 'Bogotá', 'Tunja', 140, 15, '2024-01-05', 1, 5),
(17, 'Medellín', 'Pereira', 215, 25, '2024-01-07', 2, 10),
(18, 'Cali', 'Pastó', 380, 42, '2024-01-10', 3, 15),
(19, 'Barranquilla', 'Santa Marta', 95, 10, '2024-01-12', 4, 1),
(20, 'Bucaramanga', 'Aguachica', 170, 20, '2024-01-15', 5, 2),
(21, 'Villavicencio', 'Bogotá', 125, 18, '2024-01-18', 6, 3),
(22, 'Manizales', 'Medellín', 200, 24, '2024-01-20', 7, 4),
(23, 'Cúcuta', 'Bucaramanga', 200, 30, '2024-01-22', 8, 6),
(24, 'Sincelejo', 'Montería', 90, 11, '2024-01-25', 9, 7),
(25, 'Valledupar', 'Barranquilla', 300, 35, '2024-01-28', 10, 8),
(26, 'Cartagena', 'Barranquilla', 120, 14, '2024-02-02', 11, 9),
(27, 'Bogotá', 'Cali', 460, 52, '2024-02-05', 12, 11),
(28, 'Neiva', 'Bogotá', 310, 35, '2024-02-08', 13, 12),
(29, 'Pereira', 'Quibdó', 230, 32, '2024-02-12', 14, 13),
(30, 'Ibagué', 'Bogotá', 200, 22, '2024-02-15', 15, 14),
(31, 'Medellín', 'Montería', 400, 48, '2024-02-18', 1, 10),
(32, 'Cali', 'Popayán', 140, 16, '2024-02-20', 2, 1),
(33, 'Bogotá', 'Yopal', 335, 40, '2024-02-22', 3, 2),
(34, 'Barranquilla', 'Sincelejo', 220, 25, '2024-02-25', 4, 3),
(35, 'Riohacha', 'Santa Marta', 170, 20, '2024-02-27', 5, 4),
(36, 'Bucaramanga', 'Bogotá', 400, 45, '2024-03-05', 6, 5),
(37, 'Pereira', 'Cali', 210, 24, '2024-03-08', 7, 6),
(38, 'Tunja', 'Bucaramanga', 280, 32, '2024-03-10', 8, 7),
(39, 'Medellín', 'Apartadó', 310, 38, '2024-03-12', 9, 8),
(40, 'Bogotá', 'Honda', 150, 18, '2024-03-14', 10, 9),
(41, 'Cali', 'Ipiales', 470, 55, '2024-03-16', 11, 11),
(42, 'Villavicencio', 'Puerto López', 85, 10, '2024-03-18', 12, 12),
(43, 'Cartagena', 'Magangué', 240, 28, '2024-03-20', 13, 13),
(44, 'Manizales', 'Pereira', 55, 7, '2024-03-22', 14, 14),
(45, 'Bucaramanga', 'Barrancabermeja', 115, 14, '2024-03-25', 15, 15),
(46, 'Bogotá', 'Girardot', 140, 15, '2024-03-26', 1, 1),
(47, 'Girardot', 'Ibagué', 80, 10, '2024-03-27', 1, 1),
(48, 'Ibagué', 'Armenia', 85, 12, '2024-03-28', 1, 1),
(49, 'Armenia', 'Pereira', 45, 6, '2024-03-29', 1, 1),
(50, 'Pereira', 'Manizales', 55, 8, '2024-03-30', 1, 1),
(51, 'Cartagena', 'Sincelejo', 190, 22, '2024-03-26', 2, 2),
(52, 'Sincelejo', 'Caucasia', 150, 18, '2024-03-28', 2, 2),
(53, 'Medellín', 'Bello', 20, 3, '2024-03-26', 3, 3),
(54, 'Bello', 'Barbosa', 30, 4, '2024-03-27', 3, 3),
(55, 'Barbosa', 'Puerto Berrío', 170, 22, '2024-03-29', 3, 3),
(56, 'Bogotá', 'Chía', 25, 3, '2024-04-01', 1, 1),
(57, 'Chía', 'Zipaquirá', 30, 4, '2024-04-02', 1, 1),
(58, 'Medellín', 'Guatapé', 80, 10, '2024-04-03', 2, 2),
(59, 'Cali', 'Yumbo', 20, 3, '2024-04-04', 3, 3),
(60, 'Barranquilla', 'Soledad', 15, 2, '2024-04-05', 4, 4),
(61, 'Bucaramanga', 'Girón', 12, 2, '2024-04-06', 5, 5),
(62, 'Bogotá', 'Anapoima', 90, 11, '2024-04-07', 6, 6),
(63, 'Medellín', 'Sabaneta', 15, 2, '2024-04-08', 7, 7),
(64, 'Pereira', 'Santa Rosa', 15, 2, '2024-04-09', 8, 8),
(65, 'Manizales', 'Chinchiná', 25, 4, '2024-04-10', 9, 9),
(66, 'Cartagena', 'Sincelejo', 190, 23, '2024-04-12', 10, 10),
(67, 'Santa Marta', 'Barranquilla', 95, 11, '2024-04-14', 11, 11),
(68, 'Cúcuta', 'Pamplona', 75, 12, '2024-04-15', 12, 12),
(69, 'Villavicencio', 'Acacías', 28, 4, '2024-04-16', 13, 13),
(70, 'Neiva', 'Espinal', 160, 19, '2024-04-17', 14, 14),
(71, 'Popayán', 'Pastó', 250, 30, '2024-04-18', 15, 15),
(72, 'Bogotá', 'Facatativá', 45, 6, '2024-04-20', 1, 10),
(73, 'Medellín', 'La Ceja', 41, 5, '2024-04-21', 2, 2),
(74, 'Bucaramanga', 'Floridablanca', 10, 1, '2024-04-22', 8, 3),
(75, 'Cali', 'Palmira', 30, 4, '2024-04-23', 11, 15),
(76, 'Medellín', 'Santuario', 60, 7, '2024-04-24', 2, 2),
(77, 'Santuario', 'Doradal', 110, 13, '2024-04-25', 2, 2),
(78, 'Doradal', 'Puerto Salgar', 80, 9, '2024-04-26', 2, 2),
(79, 'Puerto Salgar', 'Bogotá', 190, 22, '2024-04-27', 2, 2),
(80, 'Puerto Berrío', 'Berrío Alto', 40, 5, '2024-04-24', 3, 3),
(81, 'Berrío Alto', 'Remedios', 90, 12, '2024-04-25', 3, 3),
(82, 'Remedios', 'Segovia', 15, 2, '2024-04-26', 3, 3),
(83, 'Segovia', 'Medellín', 200, 26, '2024-04-27', 3, 3),
(84, 'Medellín', 'Envigado', 12, 1, '2024-04-28', 3, 3),
(85, 'SITP Terminal', 'Suba', 25, 3, '2024-04-24', 12, 12),
(86, 'Suba', 'Usaquén', 15, 2, '2024-04-25', 12, 12),
(87, 'Usaquén', 'Fontibón', 20, 2, '2024-04-26', 12, 12),
(88, 'Fontibón', 'Bosa', 18, 2, '2024-04-27', 12, 12),
(89, 'Bogotá', 'Mosquera', 22, 3, '2024-04-24', 4, 4),
(90, 'Mosquera', 'Madrid', 10, 1, '2024-04-25', 4, 4),
(91, 'Madrid', 'Facatativá', 15, 2, '2024-04-26', 4, 4),
(92, 'Facatativá', 'Villeta', 60, 8, '2024-04-27', 4, 4),
(93, 'Villeta', 'Guaduas', 35, 5, '2024-04-28', 4, 4),
(94, 'Yopal', 'Aguazul', 28, 3, '2024-04-24', 10, 8),
(95, 'Aguazul', 'Tauramena', 45, 5, '2024-04-25', 10, 8),
(96, 'Tauramena', 'Monterrey', 35, 4, '2024-04-26', 10, 8),
(97, 'Bogotá', 'Tunja', 140, 16, '2024-05-02', 1, 1),
(98, 'Tunja', 'Sogamoso', 80, 9, '2024-05-05', 1, 1),
(99, 'Medellín', 'Rionegro', 35, 4, '2024-05-01', 2, 2),
(100, 'Rionegro', 'La Ceja', 20, 2, '2024-05-03', 2, 2),
(101, 'La Ceja', 'Medellín', 40, 5, '2024-05-06', 2, 2),
(102, 'Cali', 'Buenaventura', 115, 15, '2024-05-02', 3, 3),
(103, 'Buenaventura', 'Buga', 125, 17, '2024-05-04', 3, 3),
(104, 'Barranquilla', 'Cartagena', 105, 12, '2024-05-02', 4, 4),
(105, 'Cartagena', 'Santa Marta', 220, 26, '2024-05-07', 4, 4),
(106, 'Bogotá', 'Ibagué', 200, 25, '2024-05-01', 8, 8),
(107, 'Ibagué', 'Armenia', 85, 11, '2024-05-03', 8, 8),
(108, 'Armenia', 'Pereira', 45, 6, '2024-05-04', 8, 8),
(109, 'Pereira', 'Manizales', 55, 7, '2024-05-06', 8, 8),
(110, 'Manizales', 'Medellín', 190, 24, '2024-05-08', 8, 8),
(111, 'Villavicencio', 'Puerto López', 85, 11, '2024-05-02', 10, 10),
(112, 'Puerto López', 'Villavicencio', 85, 11, '2024-05-05', 10, 10),
(113, 'Bucaramanga', 'San Gil', 100, 14, '2024-05-03', 12, 12),
(114, 'San Gil', 'Socorro', 25, 3, '2024-05-06', 12, 12),
(115, 'Cúcuta', 'Ocaña', 200, 26, '2024-05-02', 15, 15),
(116, 'Ocaña', 'Aguachica', 50, 7, '2024-05-04', 15, 15),
(117, 'Aguachica', 'Valledupar', 170, 21, '2024-05-06', 15, 15),
(118, 'Valledupar', 'Bosconia', 95, 11, '2024-05-08', 15, 15);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `vehicles`
--

CREATE TABLE `vehicles` (
  `id_vehicle` int(11) NOT NULL,
  `number_plate` varchar(7) NOT NULL,
  `brand` varchar(50) NOT NULL,
  `model` year(4) NOT NULL,
  `load_capacity` int(11) DEFAULT NULL,
  `mileage` int(7) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `vehicles`
--

INSERT INTO `vehicles` (`id_vehicle`, `number_plate`, `brand`, `model`, `load_capacity`, `mileage`) VALUES
(1, 'KGT101', 'Kenworth', '2022', 5000, 12500),
(2, 'LMX202', 'Chevrolet', '2018', 12000, 45000),
(3, 'NBH303', 'Foton', '2021', 2500, 8900),
(4, 'OJP404', 'Hino', '2020', 8000, 22300),
(5, 'PWQ505', 'International', '2015', 15000, 67800),
(6, 'RTY606', 'Jac', '2023', 4500, 15600),
(7, 'SDF707', 'Kenworth', '2019', 10000, 34200),
(8, 'XCV808', 'Chevrolet', '2017', 3500, 9100),
(9, 'VBN909', 'Mercedes-Benz', '2024', 18000, 89000),
(10, 'QWE111', 'Foton', '2021', 6000, 18700),
(11, 'ASD222', 'Hino', '2016', 12000, 52100),
(12, 'ZXC333', 'Chevrolet', '2023', 2000, 4500),
(13, 'YUI444', 'International', '2018', 7500, 29800),
(14, 'HJK555', 'Kenworth', '2020', 14000, 71200),
(15, 'BNM666', 'Jac', '2022', 5500, 13400);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `drivers`
--
ALTER TABLE `drivers`
  ADD PRIMARY KEY (`id_driver`);

--
-- Indices de la tabla `driver_licences`
--
ALTER TABLE `driver_licences`
  ADD PRIMARY KEY (`id_licence`),
  ADD KEY `id_category` (`id_category`),
  ADD KEY `id_driver` (`id_driver`);

--
-- Indices de la tabla `license_categories`
--
ALTER TABLE `license_categories`
  ADD PRIMARY KEY (`id_category`);

--
-- Indices de la tabla `maintenances`
--
ALTER TABLE `maintenances`
  ADD PRIMARY KEY (`id_maintenance`),
  ADD KEY `id_vehicle` (`id_vehicle`);

--
-- Indices de la tabla `routes`
--
ALTER TABLE `routes`
  ADD PRIMARY KEY (`id_route`),
  ADD KEY `id_vehicle` (`id_vehicle`),
  ADD KEY `id_driver` (`id_driver`);

--
-- Indices de la tabla `vehicles`
--
ALTER TABLE `vehicles`
  ADD PRIMARY KEY (`id_vehicle`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `drivers`
--
ALTER TABLE `drivers`
  MODIFY `id_driver` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `driver_licences`
--
ALTER TABLE `driver_licences`
  MODIFY `id_licence` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT de la tabla `license_categories`
--
ALTER TABLE `license_categories`
  MODIFY `id_category` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `maintenances`
--
ALTER TABLE `maintenances`
  MODIFY `id_maintenance` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61;

--
-- AUTO_INCREMENT de la tabla `routes`
--
ALTER TABLE `routes`
  MODIFY `id_route` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=119;

--
-- AUTO_INCREMENT de la tabla `vehicles`
--
ALTER TABLE `vehicles`
  MODIFY `id_vehicle` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `driver_licences`
--
ALTER TABLE `driver_licences`
  ADD CONSTRAINT `driver_licences_ibfk_1` FOREIGN KEY (`id_category`) REFERENCES `license_categories` (`id_category`),
  ADD CONSTRAINT `driver_licences_ibfk_2` FOREIGN KEY (`id_driver`) REFERENCES `drivers` (`id_driver`);

--
-- Filtros para la tabla `maintenances`
--
ALTER TABLE `maintenances`
  ADD CONSTRAINT `maintenances_ibfk_1` FOREIGN KEY (`id_vehicle`) REFERENCES `vehicles` (`id_vehicle`);

--
-- Filtros para la tabla `routes`
--
ALTER TABLE `routes`
  ADD CONSTRAINT `routes_ibfk_1` FOREIGN KEY (`id_vehicle`) REFERENCES `vehicles` (`id_vehicle`),
  ADD CONSTRAINT `routes_ibfk_2` FOREIGN KEY (`id_driver`) REFERENCES `drivers` (`id_driver`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
