# Estimación de PI usando el Método de Monte Carlo Distribuido

Este proyecto implementa una arquitectura distribuida utilizando ZeroC ICE para estimar el valor de π mediante el método de Monte Carlo. La arquitectura sigue el patrón **Publisher-Subscriber** con un modelo **Cliente-Maestro-Trabajador**, donde múltiples nodos trabajan de manera colaborativa para realizar el cálculo.

## Tecnologías Utilizadas
- **Java 11**
- **ZeroC ICE 3.7**: Middleware para comunicaciones distribuidas.
- **Gradle 8.6**: Herramienta de construcción y gestión de dependencias.

## Arquitectura de la Aplicación

La aplicación sigue una arquitectura distribuida basada en el patrón **Publisher-Subscriber** con los siguientes roles:

- **Cliente**: Solicita una estimación de π al maestro, indicando el número de puntos aleatorios que deben generarse.
- **Maestro (Master)**: Coordina la distribución de la tarea, gestionando el proceso de cálculo distribuyendo puntos entre múltiples trabajadores y suscribiéndolos para recibir resultados.
- **Trabajadores (Workers)**: Reciben tareas del maestro para calcular la proporción de puntos dentro de un círculo en un cuadrado dado y reportan los resultados al maestro.

### Patrón Publisher-Subscriber

El patrón Publisher-Subscriber permite que el maestro actúe como "Publisher" al que los trabajadores (subscribers) pueden suscribirse. El maestro mantiene un registro de todos los trabajadores disponibles y distribuye las tareas, asegurándose de recibir los resultados de cada trabajador. Esta arquitectura es eficaz para el cálculo distribuido porque facilita el escalado dinámico del número de trabajadores.

### Componentes
1. **Cliente**: Envía solicitudes al maestro para iniciar una estimación de π.
2. **Maestro**:
   - Recibe la solicitud del cliente y coordina a los trabajadores.
   - Distribuye el cálculo entre los trabajadores mediante el método de Monte Carlo.
   - Recoge los resultados de cada trabajador y calcula la estimación de π.
3. **Trabajador**:
   - Recibe puntos asignados por el maestro y calcula cuántos de ellos caen dentro del círculo.
   - Envía los resultados de su cálculo al maestro.

## Configuración del Proyecto

### Instalación de Dependencias
1. **ZeroC ICE**:
   - Descarga e instala ZeroC ICE 3.7 desde [ZeroC ICE Downloads](https://zeroc.com/ice/downloads/3.7).
   - Asegúrate de que ICE esté correctamente configurado en el sistema.

2. **Configuración de Gradle**:
   - En el archivo `build.gradle`, asegúrate de que se incluyen las dependencias de ICE:
     ```groovy
     plugins {
      id 'com.zeroc.gradle.ice-builder.slice' version '1.5.0' apply false
      }

      subprojects {
      
        apply plugin: 'java'
        apply plugin: 'com.zeroc.gradle.ice-builder.slice'
        
        slice {
        java {
          files = [file("../Myapp.ice")]
        }
     }
    
       repositories {
          mavenCentral()
       }
 
       dependencies {
            implementation 'com.zeroc:ice:3.7.2'
         }
         
       jar {
         manifest {
            attributes(
               "Main-Class": project.name.capitalize(),
               "Class-Path": configurations.runtimeClasspath.resolve().collect { it.toURI() }.join(' ')
               )
            }
         }
      }


     ```

### Inicialización del Proyecto
1. Clona o descarga este repositorio.
2. Navega a la carpeta principal del proyecto y ejecuta el siguiente comando para construir el proyecto:
   ```bash
   ./gradlew build
