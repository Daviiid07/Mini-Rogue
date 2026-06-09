# Mini Rogue

Mini Rogue es una versión ligera de un juego tipo roguelike desarrollada con **JavaFX**, centrada en la exploración de mazmorras por salas, combate basado en dados, gestión de recursos y toma de decisiones. Cada partida genera un mapa aleatorio a partir de un fichero de cartas externo, garantizando alta rejugabilidad.

---

## 🎮 Descripción del juego

El jugador controla a un héroe que avanza a través de dos niveles de mazmorra, cada uno dividido en dos zonas de 3×3 salas. En cada sala puede ocurrir un evento diferente según el tipo de carta que la ocupa. El objetivo es sobrevivir hasta la sala final de cada zona superando al jefe que la custodia.

Al avanzar de sala el personaje consume **comida** (o pierde vida si no tiene) y puede subir de nivel si acumula suficiente experiencia, ganando dados adicionales de ataque.

---

## 🗂️ Estructura del proyecto

```
Mini-Rogue/
├── src/main/java/org/example/garrido_david_u6a1/
│   ├── Application.java                  # Punto de entrada JavaFX
│   ├── controller/
│   │   └── GameController.java           # Controlador FXML principal
│   ├── model/
│   │   ├── Dado.java                     # Dado de 6 caras (tipos: personaje / mazmorra)
│   │   ├── Personaje.java                # Estado y lógica del héroe
│   │   ├── Zona.java                     # Malla 3×3 de salas con navegación
│   │   └── carta/
│   │       ├── Carta.java                # Clase abstracta base
│   │       ├── CartaDescanso.java
│   │       ├── CartaEnemigo.java         # Enemigos normales y jefes de zona
│   │       ├── CartaEvento.java
│   │       ├── CartaMercader.java
│   │       ├── CartaTesoro.java
│   │       └── CartaTrampa.java
│   └── service/
│       ├── GameManager.java              # Inicialización del juego y lectura de cartas
│       └── GameService.java              # Lanzamiento de dados y resolución de cartas
├── src/main/resources/
│   ├── game-view.fxml                    # Vista principal de la interfaz
│   └── img/                             # Imágenes de cada tipo de carta
├── cartas.txt                            # Definición del mazo (leído en tiempo de ejecución)
└── registro.txt                          # Log de la última partida
```

---

## ⚙️ Tecnologías y requisitos

| Componente | Versión |
|---|---|
| Java | 25 |
| JavaFX | 17.0.6 |
| Maven | 3.x (incluido wrapper `mvnw`) |
| JUnit Jupiter | 5.10.2 |

> El proyecto usa el **Maven Wrapper** (`mvnw` / `mvnw.cmd`), por lo que no es necesario tener Maven instalado globalmente.

---

## ▶️ Cómo ejecutar

```bash
# Clonar / descomprimir el proyecto y situarse en la raíz
./mvnw clean javafx:run        # Linux / macOS
mvnw.cmd clean javafx:run      # Windows
```

La aplicación arranca en **modo pantalla completa** automáticamente.

> **Importante:** el fichero `cartas.txt` debe encontrarse en el **directorio de trabajo** desde el que se lanza la aplicación (la raíz del proyecto). Si se ejecuta desde un IDE, comprueba que el working directory apunta a la raíz.

---

## 🃏 Tipos de cartas

| Tipo | Descripción |
|---|---|
| **Enemigo** | Combate por turnos basado en dados. Dos variantes: esbirro normal y jefe de zona. |
| **Tesoro** | Recompensa inmediata de oro y/o experiencia. |
| **Mercader** | Permite comprar vida con oro o vender armadura. |
| **Trampa** | Penalización en vida, comida, oro o armadura. |
| **Descanso** | Recupera vida y/o comida. |
| **Evento** | Combinación de recompensas y un combate encadenado. |

El mazo se genera leyendo `cartas.txt` línea a línea, se baraja y se distribuye entre las dos zonas de cada nivel. El formato de cada línea es:

```
TIPO;campo1;campo2;...
```

---

## 🧍 Personaje

El héroe comienza con los siguientes atributos (valores entre paréntesis indican el máximo permitido):

| Atributo | Valor inicial | Máximo |
|---|---|---|
| Vida | 10 | 20 |
| Armadura | 1 | 4 |
| Oro | 3 | 10 |
| Comida | 3 | 4 |
| Experiencia | 0 | — |
| Nivel | 1 | 4 |

**Progresión de nivel** (por experiencia acumulada):

| Nivel | XP necesaria | Dados ganados |
|---|---|---|
| 2 | 6 | +1 dado personaje |
| 3 | 12 | +1 dado personaje |
| 4 | 18 | +1 dado personaje |

El personaje comienza con 1 dado de personaje y 1 dado de mazmorra. Al subir de nivel gana dados de personaje adicionales que aumentan su potencial de ataque.

---

## 🎲 Sistema de combate

1. El jugador lanza sus **dados de personaje** (cara ≥ 5 = éxito).
2. El enemigo lanza su **dado de ataque** y aplica daño si supera la armadura del héroe.
3. El combate continúa hasta que el enemigo muere o el héroe pierde toda la vida.
4. Los **jefes de zona** tienen vida propia y otorgan oro adicional al morir.

---

## 🗺️ Mapa y zonas

Cada nivel está compuesto por 2 zonas de 3×3 salas. El jugador se mueve hacia la **derecha** o hacia **abajo** (sin retroceso). La sala [2][2] de cada zona contiene al jefe; cuando está completada, la zona se marca como finalizada y se pasa a la siguiente.

---

## 🏗️ Diseño y patrones aplicados

- **Herencia y polimorfismo:** `Carta` es una clase abstracta con el método `aplicarEfecto()` que cada subtipo implementa de forma independiente.
- **Separación de responsabilidades (MVC):**
  - `model/` — entidades del dominio sin lógica de presentación.
  - `service/` — lógica de negocio (inicialización, dados, resolución de cartas).
  - `controller/` — gestión de la interfaz FXML y coordinación entre capas.
- **Fichero de datos externo:** el mazo se carga desde `cartas.txt`, lo que permite modificar el contenido del juego sin recompilar.

---

## 🛠️ Posibles mejoras futuras

- Implementación de pociones que alteren los valores de los enemigos y del heroe
- Posibilidad de elegir entre diferentes heroes al comenzar una partida
- Más tipos de enemigos, habilidades y eventos
- Persistencia de partidas y tabla de puntuaciones

---

## 👨‍💻 Autor

Proyecto desarrollado por **David Garrido** como proyecto final del modulo de **Programación** del grado superior **Desarrollo de Aplicaciones Multiplataforma (DAM)**.

---

## 📌 Notas

Mini Rogue está inspirado en el juego de mesa *[Mini Rogue](https://boardgamegeek.com/boardgame/311715/mini-rogue)* y adaptado como práctica de programación orientada a objetos en Java con JavaFX.
