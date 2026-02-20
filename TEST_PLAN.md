# TEST PLAN - Estrategia de Calidad SofkianOS MVP

## 1. Análisis de los 7 Principios de Prueba (ISTQB)

Basado en el contexto actual del proyecto (Arquitectura Hexagonal + EDA), se ha determinado el siguiente enfoque prioritario:

*   **Principio Crítico:** **- Pruebas Tempranas (Early Testing)**.
*   **Justificación:** En un sistema distribuido donde los fallos de contrato (serialización) y las reglas de dominio son críticos, mover la validación lo más cerca posible de la creación del dato (TDD en el Producer API) minimiza el costo de corrección y evita la propagación de "Kudos Fantasmas" hacia el Worker y la Base de Datos.

### Definir nivel de prueba

A nivel Unitario, nos enfocaremos en la Lógica de Dominio Pura. Probaremos que el objeto Kudo se autovalida (reglas de negocio) y que el KudoService coordina correctamente las llamadas, pero usaremos Mocks para los Puertos de Salida (RabbitMQ/DB).

