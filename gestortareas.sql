-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 05-06-2020 a las 20:33:36
-- Versión del servidor: 10.4.10-MariaDB
-- Versión de PHP: 7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gestortareas`
--
CREATE DATABASE IF NOT EXISTS `gestortareas` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `gestortareas`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `superadmin`
--

CREATE TABLE `superadmin` (
  `idusuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `superadmin`
--

INSERT INTO `superadmin` (`idusuario`) VALUES
(32);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tareas`
--

CREATE TABLE `tareas` (
  `id` int(11) NOT NULL,
  `titulo` varchar(50) NOT NULL,
  `descripcion` varchar(500) NOT NULL,
  `prioridad` enum('alta','media','baja') NOT NULL,
  `estado` enum('pendiente','en curso','finalizada') NOT NULL,
  `creador` varchar(50) NOT NULL,
  `asignacion` varchar(50) NOT NULL,
  `progreso` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `tareas`
--

INSERT INTO `tareas` (`id`, `titulo`, `descripcion`, `prioridad`, `estado`, `creador`, `asignacion`, `progreso`) VALUES
(1, 'Tarea de Alex', 'descripcio11', 'alta', 'pendiente', 'alex', 'admin', 100),
(4, 'Acabar el proyecto de Java', 'Para el día 5', 'alta', 'pendiente', 'usuario', 'usuario', 25),
(6, 'Rellenar comboBox', 'En la ventana tareas\n', 'alta', 'pendiente', 'usuario', 'usuario', 44),
(8, 'hola', 'w', 'alta', 'pendiente', 'usuario', 'usuario', 100),
(11, '213', '23131', 'alta', 'pendiente', 'admin', 'admin', 56),
(14, 'Tarea para ax', 'hola', 'media', 'pendiente', 'admin', 'ax', 45),
(22, 'Pagarle una cena a Alex Profesor', 'En un sitio no muy caro', 'baja', 'en curso', 'usuario', 'usuario', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(5) NOT NULL,
  `usuario` varchar(50) NOT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `apellidos` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `contrasenya` varchar(150) NOT NULL,
  `rol` enum('Administrador','Usuario') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `usuario`, `nombre`, `apellidos`, `email`, `contrasenya`, `rol`) VALUES
(1, 'admin', 'Alex', 'Clemente', 'alexinadamas@gmail.com', 'x61Ey612Kl2gpFL56FT9weDnpSo4AV8j8+qx2AuTHdRyY036xxzTTrw10Wq3+4qQyB+XURPWx1ONxp3Y3pB37A==', 'Administrador'),
(32, 'aclemente', 'Alex', 'Clemente', 'alexclementefornas95@gmail.com', 'oiti6ozZfSSpFMbO08uqFX9vIHSNlwUwfKlzl70YFNaapKqzt5T49pOmdYMo+DYyw+J13kMIxFQdiBZA8CEiEA==', 'Administrador'),
(35, 'usuario', '', '', '', '2elP0rTFUi5bt5lqpN9Io/a48bDD5/rbX8xyTjq22F3EAbCieJ/lbCCbgOhhArIY/3T/hhTzFVmaGAaRhGE4tg==', 'Usuario');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `superadmin`
--
ALTER TABLE `superadmin`
  ADD PRIMARY KEY (`idusuario`);

--
-- Indices de la tabla `tareas`
--
ALTER TABLE `tareas`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `tareas`
--
ALTER TABLE `tareas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
