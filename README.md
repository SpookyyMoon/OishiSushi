# Oishii Sushi APP

Oishii Sushi es un sistema completo de gestión para restaurante que combina una APP móvil para clientes y una APP de escritorio(https://github.com/SpookyyMoon/OishiiSushiDesktop) para empleados, conectadas mediante una API REST desarrollada con Express y MongoDB(https://github.com/SpookyyMoon/OishiSushiBackend).
La aplicación móvil permite a los usuarios seleccionar una mesa, consultar la carta, añadir platos al carrito y enviar la comanda al restaurante.

## Características principales:

- Selección de mesa  – Al iniciar la aplicación móvil, el usuario puede elegir una mesa disponible; se comprueba si está ocupada y se controla el acceso.

- Visualización de la carta  – Se muestra la lista de platos obtenidos desde el backend, permitiendo añadir unidades al carrito y navegar entre vistas.

- Carrito de pedidos  – Permite revisar los platos seleccionados, modificar cantidades y enviar la comanda al servidor mediante la API.

- Gestión de la cuenta  – Se muestra el total del pedido, permitiendo tramitar el pago y liberar la mesa.

- Actualización del estado de las mesas – Ambas aplicaciones gestionan el estado de cada mesa (ocupada/libre) a través de la API y la base de datos.

- Sincronización con backend – Todas las operaciones (mesas, comandas y platos) se sincronizan mediante peticiones REST con Express y MongoDB.

- Arquitectura por capas – Separación clara entre interfaz, lógica y capa de datos tanto en Android Studio como en JavaFX.

- Control de errores en formularios y acciones – Validación de datos enviados, control de botones que no deben aparecer en determinadas situaciones y manejo de errores de conexión.

- Adaptadores y listas dinámicas – Uso de RecyclerView con adaptadores personalizados para mostrar platos, carrito y cuenta con actualización automática.


---

## Diseño en figma:

https://www.figma.com/design/9dLPDXMIL2y7yJOScwnj3y/OishiSushi?node-id=0-1&t=E6AiOLfoNC8NQhxM-1

---

## Tecnologías utilizadas:

- **Lenguaje principal:** Java  
- **Interfaz gráfica:** JavaFX + XML  
- **Comunicación con backend:** Retrofit  
- **Backend:** Node.js + Express + MongoDB Atlas (API REST)  
- **Gestión de datos:** Mongoose  
  
---

## Imágenes en ejecución:

### Vista de inicio
<img width="513" height="997" alt="{F7888F2B-A615-4642-ACDE-81E9EF9DA8E2}" src="https://github.com/user-attachments/assets/758b8b79-8172-448b-82cc-7451eda267e0" />

### Selección de mesa
<img width="514" height="989" alt="{F53483F8-21FA-4F7B-85E0-730F3BB7BD82}" src="https://github.com/user-attachments/assets/890c452d-209e-479c-a4ef-c9bdc36bf7cd" />

### Carta
<img width="512" height="993" alt="{D043107C-F4E4-4855-AF27-13ACC605A312}" src="https://github.com/user-attachments/assets/0a9bf401-7129-40b7-9db6-00766eecb37a" />

### Carrito vacio
<img width="538" height="997" alt="{9344FD0F-7998-4850-A9DB-B98A9DB6BE00}" src="https://github.com/user-attachments/assets/293b4ead-9896-482a-a821-5fbb55333ec5" />

---

## Video de demostración (Interacción entre APP móvil y escritorio)

[Ver video de demostración](./OishiiSushiDemo.mp4)

---

## Documentación en PDF (Latex)

[Ver documentación en PDF](./docu.pdf)

