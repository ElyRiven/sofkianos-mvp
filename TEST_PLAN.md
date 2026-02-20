# Estrategia de Calidad: Diseño de Pruebas

## 1. Teoría Aplicada: Principio Fundamental

Tras auditar el sistema distribuido (RabbitMQ + Arquitectura Hexagonal), se ha determinado el principio rector para el diseño de pruebas.

### Principio Seleccionado: Las pruebas dependen del contexto

### Justificación

En un entorno asíncrono y desacoplado, la calidad no se limita a la lógica funcional, sino a la **integridad de los datos** a lo largo de su ciclo de vida.

El incidente del **“Kudo Fantasma”** (fallo de serialización) demuestra que el riesgo real reside en los *bordes del sistema* y en cómo los datos sobreviven al transporte entre microservicios.

---

## Definición de Niveles de Prueba

### Diseño del Nivel Unitario

El enfoque principal estará en el componente **`Kudo.Builder`** dentro del *Core Domain*.

Este componente actúa como el **“portero” del sistema**:
- Si una regla de negocio falla aquí, el proceso se detiene inmediatamente
- Se evita que datos inválidos contaminen la infraestructura
- Se protege la consistencia del dominio, independientemente del origen del mensaje

Al validar primero este nivel, garantizamos que el sistema falle **rápido, barato y de forma explícita**, alineado con los principios de TDD y Arquitectura Hexagonal.