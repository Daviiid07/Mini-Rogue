package org.example.garrido_david_u6a1.controller;

import org.example.garrido_david_u6a1.model.Dado;
import org.example.garrido_david_u6a1.model.Personaje;
import org.example.garrido_david_u6a1.model.Zona;
import org.example.garrido_david_u6a1.model.carta.*;
import org.example.garrido_david_u6a1.service.GameManager;
import org.example.garrido_david_u6a1.service.GameService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    // Stats personaje
    @FXML private Label lblNombre;
    @FXML private Label lblPV;
    @FXML private Label lblPX;
    @FXML private Label lblOro;
    @FXML private Label lblArmadura;
    @FXML private Label lblComida;
    @FXML private Label lblNivel;
    @FXML private Label lblZona;

    // Grid
    @FXML private GridPane gridPane;

    // Panel info + acciones
    @FXML private TextArea txtInfoSala;

    // Botones dinámicos de combate
    @FXML private Button btnResolver;
    @FXML private Button btnProezaPX;
    @FXML private Button btnProezaVida;
    @FXML private Button btnCriticoSi;
    @FXML private Button btnCriticoNo;

    // Panel choice (Descanso, Mercader, Tesoro-elección)
    @FXML private VBox panelChoice;
    @FXML private Label lblChoiceTitulo;
    @FXML private ChoiceBox<String> choiceAccion;
    @FXML private ChoiceBox<String> choiceOpcion;
    @FXML private Button btnConfirmar;

    // Botón avanzar zona
    @FXML private Button btnAvanzarZona;

    // Log + movimiento
    @FXML private TextArea txtLog;
    @FXML private Button btnDerecha;
    @FXML private Button btnAbajo;

    // Estado interno
    private final ImageView[][] ivSalas = new ImageView[3][3];
    private final boolean[][] visible  = new boolean[3][3];

    private final GameManager gameManager = new GameManager();
    private final GameService gameService = new GameService(gameManager);

    private Zona zonaActual;
    private Personaje personaje;
    private Carta cartaEnCurso;

    // Combate turno-a-turno
    private List<Dado> dadosCombate;
    private int dadoIdx;
    private int danioAcumTurno;
    private int danioAcumDado;
    private CartaEnemigo enemigoEnCombate;

    // Logging a fichero
    private BufferedWriter logWriter;

    // Imágenes
    private Image imgEnemigoNormal, imgEnemigoJefe, imgMercader;
    private Image imgTrampa, imgEvento, imgTesoro, imgDescanso, imgVacia;

    // INICIALIZACIÓN
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        abrirFicheroLog();

        personaje  = gameManager.getPersonaje();
        zonaActual = gameManager.getNiveles().get(0)[0];

        cargarImagenes();
        construirGrid();
        actualizarStats();
        ocultarAcciones();

        revelarSala(0, 0);
        resaltarSalaActual();
        actualizarBotonesMover();
        actualizarLblZona();

        log("=== INICIO DE PARTIDA ===");
        log("Zona: " + zonaActual.getNombreZona());

        Carta primera = zonaActual.getSalas()[0][0];
        mostrarInfoCarta(primera);
        cartaEnCurso = primera;
        prepararAcciones(primera);
    }

    // FICHERO LOG
    private void abrirFicheroLog() {
        try {
            File fichero = new File("registro.txt");
            fichero.createNewFile();
            logWriter = new BufferedWriter(new FileWriter(fichero, false));
        } catch (IOException e) {
            System.err.println("No se pudo abrir registro.txt: " + e.getMessage());
        }
    }

    private void log(String texto) {
        txtLog.appendText(texto + "\n");
        txtLog.setScrollTop(Double.MIN_VALUE);
        System.out.println(texto);
        if (logWriter != null) {
            try {
                logWriter.write(texto + "\n");
                logWriter.flush();
            } catch (IOException e) {
                System.err.println("Error escribiendo log: " + e.getMessage());
            }
        }
    }

    private void cerrarFicheroLog() {
        if (logWriter != null) {
            try {
                logWriter.close();
            } catch (IOException ignored) {}
        }
    }

    // IMÁGENES Y GRID
    private void cargarImagenes() {
        imgEnemigoNormal = img("/org/example/img/mounstro_no_jefe.png");
        imgEnemigoJefe = img("/org/example/img/mounstro_jefe.png");
        imgMercader = img("/org/example/img/mercader.png");
        imgTrampa = img("/org/example/img/trampa.png");
        imgEvento = img("/org/example/img/evento.png");
        imgTesoro = img("/org/example/img/tesoro.png");
        imgDescanso = img("/org/example/img/descanso.png");
        imgVacia = img("/org/example/img/sala_no_visitada.png");
    }

    private Image img(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }

    private void construirGrid() {
        for (int f = 0; f < 3; f++) {
            for (int c = 0; c < 3; c++) {
                ImageView iv = new ImageView(imgVacia);
                iv.setFitWidth(160);
                iv.setFitHeight(180);
                iv.setPreserveRatio(true);

                final int ff = f, cc = c;
                iv.setOnMouseClicked(e -> onClickSala(ff, cc));

                ivSalas[f][c] = iv;
                visible[f][c] = false;
                gridPane.add(iv, c, f);
            }
        }
    }

    // REVELAR SALAS
    private void revelarSala(int f, int c) {
        if (visible[f][c]) {
            return;
        }

        Carta carta = zonaActual.getSalas()[f][c];
        if (carta == null) {
            return;
        }

        visible[f][c] = true;
        ponerImagen(f, c, carta);
    }

    private void revelarVecinasPostResolucion() {
        int f = zonaActual.getFilaActual();
        int c = zonaActual.getColumnaActual();

        if (zonaActual.puedeMoverDerecha()) {
            revelarSala(f, c + 1);
        }
        if (zonaActual.puedeMoverAbajo()) {
            revelarSala(f + 1, c);
        }
    }

    // CLICK EN SALA
    private void onClickSala(int f, int c) {
        if (!visible[f][c]) {
            return;
        }

        Carta carta = zonaActual.getSalas()[f][c];
        if (carta == null) {
            return;
        }

        mostrarInfoCarta(carta);

        boolean esSalaActual = (f == zonaActual.getFilaActual() && c == zonaActual.getColumnaActual());
        if (esSalaActual && !carta.estaOcupada()) {
            cartaEnCurso = carta;
            prepararAcciones(carta);
        }
    }

    // INFO DETALLADA DE CARTA
    private void mostrarInfoCarta(Carta carta) {
        if (carta == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[ ").append(carta.getTipoString()).append(" ]\n");

        if (carta instanceof CartaEnemigo e) {
            sb.append(e.isEsJefe() ? "⚠ JEFE DE ZONA\n" : "Esbirro\n");
            sb.append("Descripción: ").append(e.getDescripcion()).append("\n");
            sb.append("Vida: ").append(e.getVida()).append("\n");
            sb.append("Ataque: ").append(e.getAtaque()).append("\n");
            sb.append("Recompensa: +").append(e.getPx()).append(" PX");
            if (e.isEsJefe()) {
                sb.append(", +").append(e.getOro()).append(" oro");
            }

        } else if (carta instanceof CartaTesoro t) {
            sb.append("Oro base: +").append(t.getOro());
            if (t.isSelloRojo()) {
                sb.append(" × 2 (Sello Rojo)");
            }
            sb.append("\nBonificación: dado mazmorra si superas habilidad (≥5)");
            sb.append("\n  1: +oro o +armadura (elige)");
            sb.append("\n  2: +oro×2 3: +armadura×2");
            sb.append("\n  4: +vida 5: +armadura o +PX (elige)");
            sb.append("\n  6: +PX×2");

        } else if (carta instanceof CartaMercader m) {
            sb.append("COMPRAR:\n");
            sb.append("  1 oro → +").append(m.getVida()).append(" vida\n");
            sb.append("  5 oro → +").append(m.getArmadura()).append(" armadura\n");
            sb.append("  3 oro → +").append(m.getComida()).append(" comida\n");
            sb.append("  6 oro → +").append(m.getComida() * 2).append(" comida\n");
            sb.append("VENDER:\n");
            sb.append("  1 armadura → +").append(m.getOro()).append(" oro\n");
            sb.append("  1 comida → +").append(m.getOro()).append(" oro");

        } else if (carta instanceof CartaTrampa tr) {
            sb.append("Habilidad ≥5 → efecto positivo\n");
            sb.append("Dado mazmorra:\n");
            sb.append("  1-2: comida ±").append(tr.getComida()).append("\n");
            sb.append("  3-4: armadura ±").append(tr.getArmadura()).append("\n");
            sb.append("  5-6: vida ±").append(tr.getVida());

        } else if (carta instanceof CartaDescanso d) {
            sb.append("Elige UNO:\n");
            sb.append("  +").append(d.getPx()).append(" PX\n");
            sb.append("  +").append(d.getComida()).append(" Comida\n");
            sb.append("  +").append(d.getVida()).append(" Vida");

        } else if (carta instanceof CartaEvento ev) {
            sb.append("Habilidad ≥5 → lanzar dado mazmorra\n");
            sb.append("  1: +").append(ev.getComida()).append(" comida\n");
            sb.append("  2: +").append(ev.getVida()).append(" vida\n");
            sb.append("  3: +").append(ev.getOro()).append(" oro\n");
            sb.append("  4: +").append(ev.getPx()).append(" PX\n");
            sb.append("  5: +").append(ev.getArmadura()).append(" armadura\n");
            if (ev.getEnemigo() != null) {
                sb.append("  6: ¡COMBATE! ").append(ev.getEnemigo().getDescripcion())
                  .append(" (Vida:").append(ev.getEnemigo().getVida())
                  .append(", Atq:").append(ev.getEnemigo().getAtaque()).append(")");
            }
        }

        if (carta.estaOcupada()) {
            sb.append("\n\n✔ Sala ya resuelta");
        }

        txtInfoSala.setText(sb.toString());
    }

    // PREPARAR ACCIONES
    private void prepararAcciones(Carta carta) {
        if (carta == null) {
            return;
        }
        ocultarAcciones();

        if (carta instanceof CartaEnemigo) {
            mostrarBtn(btnResolver, "⚔ Atacar");
        } else if (carta instanceof CartaTrampa) {
            mostrarBtn(btnResolver, "Resolver Trampa");
        } else if (carta instanceof CartaEvento) {
            mostrarBtn(btnResolver, "Resolver Evento");
        } else if (carta instanceof CartaTesoro) {
            mostrarBtn(btnResolver, "Abrir Tesoro");
        } else if (carta instanceof CartaDescanso d) {
            prepararChoiceDescanso(d);
        } else if (carta instanceof CartaMercader m) {
            prepararChoiceMercader(m);
        }
    }


    // CHOICE BOXES
    private void prepararChoiceDescanso(CartaDescanso carta) {
        mostrarPanel(panelChoice);
        lblChoiceTitulo.setText("¿Qué deseas ganar?");
        choiceAccion.getItems().setAll(
                "+" + carta.getPx() + " PX",
                "+" + carta.getComida() + " Comida",
                "+" + carta.getVida() + " Vida"
        );
        choiceAccion.getSelectionModel().selectFirst();
        mostrarNode(choiceAccion);
        ocultarNode(choiceOpcion);
        mostrarBtn(btnConfirmar, "Confirmar");
    }

    private void prepararChoiceMercader(CartaMercader carta) {
        mostrarPanel(panelChoice);
        lblChoiceTitulo.setText("¿Qué deseas hacer?");
        choiceAccion.getItems().setAll("Comprar", "Vender", "Nada");
        choiceAccion.getSelectionModel().selectFirst();
        mostrarNode(choiceAccion);
        actualizarOpcionesMercader(carta, "Comprar");
        choiceAccion.setOnAction(e -> actualizarOpcionesMercader(carta, choiceAccion.getValue()));
        mostrarBtn(btnConfirmar, "Confirmar");
    }

    private void actualizarOpcionesMercader(CartaMercader carta, String accion) {
        choiceOpcion.getItems().clear();
        if ("Comprar".equals(accion)) {
            choiceOpcion.getItems().setAll(
                    "Vida (1 oro → +"  + carta.getVida() + " PV)",
                    "Armadura (5 oro → +" + carta.getArmadura() + ")",
                    "Comida x1 (3 oro → +" + carta.getComida() + ")",
                    "Comida x2 (6 oro → +" + (carta.getComida()*2) + ")"
            );
            mostrarNode(choiceOpcion);
        } else if ("Vender".equals(accion)) {
            choiceOpcion.getItems().setAll(
                    "1 Armadura → +" + carta.getOro() + " oro",
                    "1 Comida   → +" + carta.getOro() + " oro"
            );
            mostrarNode(choiceOpcion);
        } else {
            ocultarNode(choiceOpcion);
        }

        if (!choiceOpcion.getItems().isEmpty()) {
            choiceOpcion.getSelectionModel().selectFirst();
        }
    }

    // HANDLERS BOTONES PRINCIPALES
    @FXML
    private void onBtnResolver() {
        if (cartaEnCurso == null) {
            return;
        }

        if (cartaEnCurso instanceof CartaEnemigo e) {
            iniciarCombate(e);
        } else if (cartaEnCurso instanceof CartaTrampa) {
            resolverTrampa();
        } else if (cartaEnCurso instanceof CartaEvento) {
            resolverEvento();
        } else if (cartaEnCurso instanceof CartaTesoro) {
            resolverTesoro();
        }
    }

    @FXML
    private void onBtnConfirmar() {
        if (cartaEnCurso == null) {
            return;
        }

        if (cartaEnCurso instanceof CartaDescanso) {
            confirmarDescanso();
        } else if (cartaEnCurso instanceof CartaMercader) {
            confirmarMercader();
        } else if (cartaEnCurso instanceof CartaTesoro) {
            confirmarTesoroEleccion();
        }
    }

    @FXML
    private void onBtnDerecha() {
        mover(0, 1);
    }

    @FXML
    private void onBtnAbajo() {
        mover(1, 0);
    }

    @FXML
    private void onBtnAvanzarZona() {
        avanzarZona();
    }

    // RESOLUCIÓN: TRAMPA
    private void resolverTrampa() {
        log("--- Trampa ---");

        // accion=0, opcion=0 porque la trampa no necesita elección del jugador
        String res = gameService.resolverCarta(cartaEnCurso, personaje, 0, 0);
        log(res);
        finalizarCarta();
    }


    // RESOLUCIÓN: TESORO
    // El controller gestiona los dados (para mostrarlos en log) y luego
    // delega en CartaTesoro.aplicarBonus() para el bonus elegido.
    private void resolverTesoro() {
        CartaTesoro tesoro = (CartaTesoro) cartaEnCurso;
        log("--- Tesoro ---");

        // Oro base
        int oroBase = tesoro.isSelloRojo() ? tesoro.getOro() * 2 : tesoro.getOro();
        personaje.ganarOro(oroBase);
        log("Oro base: +" + oroBase);

        // Prueba de habilidad
        int tiradaHab = gameService.lanzarHabilidad(personaje);
        boolean prueba = tiradaHab >= 5;
        log("Habilidad: tirada " + tiradaHab + (prueba ? " ✓" : " ✗"));

        if (!prueba) {
            log("Sin bonificación.");
            finalizarCarta();
            return;
        }

        // Dado mazmorra
        int tiradaMaz = gameService.lanzarMazmorra(personaje);
        log("Dado mazmorra: " + tiradaMaz);

        if (tiradaMaz == 1 || tiradaMaz == 5) {
            // El jugador debe elegir
            mostrarPanel(panelChoice);
            if (tiradaMaz == 1) {
                lblChoiceTitulo.setText("Mazmorra 1 — Elige recompensa:");
                choiceAccion.getItems().setAll("+" + tesoro.getOro() + " oro",
                                               "+" + tesoro.getArmadura() + " armadura");
            } else {
                lblChoiceTitulo.setText("Mazmorra 5 — Elige recompensa:");
                choiceAccion.getItems().setAll("+" + tesoro.getArmadura() + " armadura",
                                               "+" + tesoro.getPx() + " PX");
            }
            choiceAccion.getSelectionModel().selectFirst();
            mostrarNode(choiceAccion);
            ocultarNode(choiceOpcion);
            btnConfirmar.setUserData(tiradaMaz);
            mostrarBtn(btnConfirmar, "Confirmar recompensa");
            ocultarBtn(btnResolver);
        } else {
            // Sin elección: aplica directamente (opcion=0, no se usa)
            String bonus = tesoro.aplicarBonus(personaje, tiradaMaz, 0);
            log("Bonus: " + bonus);
            finalizarCarta();
        }
    }

    private void confirmarTesoroEleccion() {
        CartaTesoro tesoro = (CartaTesoro) cartaEnCurso;
        int tiradaMaz = (int) btnConfirmar.getUserData();
        int op = choiceAccion.getSelectionModel().getSelectedIndex() + 1;

        String bonus = tesoro.aplicarBonus(personaje, tiradaMaz, op);
        log("Bonus: " + bonus);
        finalizarCarta();
    }

    // RESOLUCIÓN: DESCANSO
    private void confirmarDescanso() {
        int op = choiceAccion.getSelectionModel().getSelectedIndex() + 1;
        log("--- Descanso ---");
        // accion=0 (no usado), opcion=elección del jugador (1/2/3)
        String res = gameService.resolverCarta(cartaEnCurso, personaje, 0, op);
        log(res);
        finalizarCarta();
    }

    // RESOLUCIÓN: MERCADER
    // El jugador elige acción (comprar/vender/nada) y opción en los ChoiceBox.
    private void confirmarMercader() {
        int accion = choiceAccion.getSelectionModel().getSelectedIndex() + 1;
        int opcion = (choiceOpcion.isVisible() && !choiceOpcion.getItems().isEmpty())
                ? choiceOpcion.getSelectionModel().getSelectedIndex() + 1 : 0;
        log("--- Mercader ---");

        String res = gameService.resolverCarta(cartaEnCurso, personaje, accion, opcion);
        log(res);
        finalizarCarta();
    }

    // RESOLUCIÓN: EVENTO
    // El controller lanza los dados (para mostrar en log) y pasa el resultado
    // a aplicarEfecto. Si hay combate, lo inicia aparte.
    private void resolverEvento() {
        log("--- Evento ---");
        CartaEvento cartaEvento = (CartaEvento) cartaEnCurso;

        // Prueba de habilidad
        int tiradaHab = gameService.lanzarHabilidad(personaje);
        boolean prueba = tiradaHab >= 5;
        log("Habilidad: tirada " + tiradaHab + (prueba ? " ✓ superada" : " ✗ fallada"));

        if (!prueba) {
            // accion=0 indica prueba fallida en CartaEvento.aplicarEfecto
            String res = gameService.resolverCarta(cartaEnCurso, personaje, 0, 0);
            log(res);
            finalizarCarta();
            return;
        }

        int tiradaMaz = gameService.lanzarMazmorra(personaje);
        log("Dado mazmorra: " + tiradaMaz);

        if (cartaEvento.necesitaCombate(tiradaMaz)) {
            // Resultado 6: combate con el enemigo del evento
            CartaEnemigo enemigo = cartaEvento.getEnemigo();
            log("¡Aparece enemigo! " + enemigo.getDescripcion()
                    + " Vida:" + enemigo.getVida() + " Atq:" + enemigo.getAtaque());
            iniciarCombate(enemigo);
        } else {
            // Resultado 1-5: recompensa directa (accion = resultado del dado)
            String res = gameService.resolverCarta(cartaEnCurso, personaje, tiradaMaz, 0);
            log(res);
            finalizarCarta();
        }
    }

    // COMBATE TURNO A TURNO
    private void iniciarCombate(CartaEnemigo enemigo) {
        enemigoEnCombate = enemigo;
        danioAcumTurno = 0;
        dadoIdx = 0;
        danioAcumDado = 0;

        dadosCombate = new ArrayList<>();
        for (Dado d : personaje.getDadosAtaque()) {
            if (d.getTipo().equalsIgnoreCase("personaje")) {
                dadosCombate.add(d);
            }
        }

        log("⚔ Combate: " + enemigo.getDescripcion()
                + " | Vida:" + enemigo.getVida() + " Atq:" + enemigo.getAtaque());

        ocultarAcciones();
        siguienteDado();
    }

    private void siguienteDado() {
        if (dadoIdx >= dadosCombate.size()) {
            aplicarDanioEnemigo();
            return;
        }

        Dado dado = dadosCombate.get(dadoIdx);
        int tirada = dado.lanzar();
        dadoIdx++;
        danioAcumDado = tirada;

        log("  Dado " + dadoIdx + " → " + tirada);

        boolean puedeUsarPX = tirada != 6 && personaje.getExperiencia() > 0;
        boolean puedeUsarVida = tirada != 6 && personaje.getVida() > 2;

        if (puedeUsarPX || puedeUsarVida) {
            log("  Tirada: " + tirada + " — ¿Proeza?");
            if (puedeUsarPX) {
                mostrarBtn(btnProezaPX, "Proeza -1 PX (tienes " + personaje.getExperiencia() + ")");
            }
            if (puedeUsarVida) {
                mostrarBtn(btnProezaVida, "Proeza -2 Vida (tienes " + personaje.getVida() + ")");
            }

            mostrarBtn(btnResolver, "No usar proeza (daño: " + tirada + ")");
            btnResolver.setOnAction(e -> {
                ocultarAcciones();
                comprobarCritico(dado);
            });
            return;
        }

        comprobarCritico(dado);
    }

    @FXML
    private void onBtnProezaPX() {
        personaje.perderExperiencia(1);
        actualizarStats();
        log("  Proeza: -1 PX → relanza dado...");
        relanzarTrasProeza();
    }

    @FXML
    private void onBtnProezaVida() {
        personaje.recibirDanio(2);
        actualizarStats();
        log("  Proeza: -2 Vida → relanza dado...");

        relanzarTrasProeza();
    }

    private void relanzarTrasProeza() {
        ocultarAcciones();

        Dado dado = dadosCombate.get(dadoIdx - 1);

        int nueva = dado.lanzar();
        danioAcumDado = nueva;

        log("  Nueva tirada tras proeza: " + nueva);

        if (dado.esFallo()) {
            log("  ¡Fallo! Sin daño.");
            danioAcumDado = 0;
            siguienteDado();
            return;
        }

        comprobarCritico(dado);
    }

    private void comprobarCritico(Dado dado) {
        if (!dado.esCritico()) {
            danioAcumTurno += danioAcumDado;
            siguienteDado();
            return;
        }

        log("  ¡CRÍTICO (6)! Daño acumulado dado: " + danioAcumDado + " — ¿Relanzas?");
        mostrarBtn(btnCriticoSi, "Relanzar (riesgo fallo)");
        mostrarBtn(btnCriticoNo, "Mantener " + danioAcumDado + " de daño");
    }

    @FXML
    private void onBtnCriticoSi() {
        ocultarAcciones();
        Dado dado = dadosCombate.get(dadoIdx - 1);
        int rel = dado.lanzar();
        log("  Relanzado: " + rel);

        if (dado.esFallo()) {
            log("  ¡Fallo! Daño del dado perdido.");
            danioAcumDado = 0;
            siguienteDado();
        } else {
            danioAcumDado += rel;
            log("  Daño acumulado dado: " + danioAcumDado);
            comprobarCritico(dado);
        }
    }

    @FXML
    private void onBtnCriticoNo() {
        ocultarAcciones();
        log("  Mantiene daño: " + danioAcumDado);
        danioAcumTurno += danioAcumDado;
        siguienteDado();
    }

    private void aplicarDanioEnemigo() {
        if (danioAcumTurno == 1){
            log("  Daño al enemigo: 0 (ataque fallido)" + " | Vida restante: " + enemigoEnCombate.getVida());
        } else {
            enemigoEnCombate.recibirDanio(danioAcumTurno);
            log("  Daño al enemigo: " + danioAcumTurno + " | Vida restante: " + enemigoEnCombate.getVida());
        }

        if (!enemigoEnCombate.estaVivo()) {
            log("💀 Enemigo derrotado.");
            victoriaEnemigo();
            return;
        }

        int tiradaE = enemigoEnCombate.atacar();
        log("  Enemigo lanza: " + tiradaE);

        if (tiradaE == 1) {
            log("  ¡Fallo del enemigo! Sin daño.");
        } else if (tiradaE == 6) {
            personaje.recibirDanio(enemigoEnCombate.getAtaque());
            log("  ¡CRÍTICO enemigo! " + enemigoEnCombate.getAtaque()
                    + " daño directo → PV: " + personaje.getVida());
        } else {
            int danio = enemigoEnCombate.getAtaque();
            if (personaje.getArmadura() > 0) {
                int absorcion = Math.min(personaje.getArmadura(), danio);
                danio -= absorcion;
                log("  Armadura absorbe " + absorcion + ".");
            }

            if (danio > 0) {
                personaje.recibirDanio(danio);
            }

            log("  Enemigo inflige " + danio + " → PV: " + personaje.getVida());
        }

        actualizarStats();
        if (!personaje.estaVivo()) {
            mostrarDerrota();
            return;
        }

        danioAcumTurno = 0;
        dadoIdx = 0;
        danioAcumDado = 0;
        log("--- Nuevo turno ---");
        siguienteDado();
    }

    private void victoriaEnemigo() {
        String recomp = enemigoEnCombate.aplicarEfecto(personaje, 0, 0);
        log(recomp + statsStr());
        finalizarCarta();
    }


    // FINALIZAR CARTA
    private void finalizarCarta() {
        zonaActual.marcarCartaVisitada();
        cartaEnCurso = null;
        enemigoEnCombate = null;

        ocultarAcciones();
        actualizarStats();
        revelarVecinasPostResolucion();
        resaltarSalaActual();
        actualizarBotonesMover();

        int f = zonaActual.getFilaActual();
        int c = zonaActual.getColumnaActual();

        if (f == 2 && c == 2) {
            Carta ultima = zonaActual.getSalas()[2][2];
            if (ultima == null || ultima.estaOcupada()) {
                log("Zona completada. ¡Avanza!");
                mostrarBtn(btnAvanzarZona, "Avanzar zona →");
            }
        }
    }

    // MOVIMIENTO
    private void mover(int dFila, int dCol) {
        if (dCol == 1) {
            zonaActual.moverDerecha();
        } else {
            zonaActual.moverAbajo();
        }

        int f = zonaActual.getFilaActual();
        int c = zonaActual.getColumnaActual();
        log("Movimiento → sala [" + f + "][" + c + "]");

        cartaEnCurso = null;
        ocultarAcciones();
        resaltarSalaActual();
        actualizarBotonesMover();

        Carta carta = zonaActual.getSalas()[f][c];

        if (carta == null) {
            txtInfoSala.setText("Sala de avance.\nPulsa el botón para continuar.");
            log("Sala de avance de zona.");
            mostrarBtn(btnAvanzarZona, "Avanzar zona →");
            return;
        }

        mostrarInfoCarta(carta);
        if (!carta.estaOcupada()) {
            cartaEnCurso = carta;
            prepararAcciones(carta);
        }
    }

    // AVANZAR ZONA / NIVEL
    private void avanzarZona() {
        int nivelIdx = -1, zonaIdx = -1;
        boolean encontrado = false;

        for (int n = 0; n < gameManager.getNiveles().size() && !encontrado; n++) {
            Zona[] zonas = gameManager.getNiveles().get(n);

            for (int z = 0; z < zonas.length; z++) {
                if (zonas[z] == zonaActual) {
                    nivelIdx = n;
                    zonaIdx = z;
                    encontrado = true;

                    break;
                }
            }
        }

        if (nivelIdx < 0) {
            return;
        }

        Zona[] zonasNivel = gameManager.getNiveles().get(nivelIdx);

        String faseAvance = personaje.comer();
        log(faseAvance);
        actualizarStats();
        if (!personaje.estaVivo()) {
            mostrarDerrota();
            return;
        }

        if (zonaIdx + 1 < zonasNivel.length) {
            zonaActual = zonasNivel[zonaIdx + 1];
        } else if (nivelIdx + 1 < gameManager.getNiveles().size()) {
            zonaActual = gameManager.getNiveles().get(nivelIdx + 1)[0];
        } else {
            mostrarVictoria();
            return;
        }

        ocultarBtn(btnAvanzarZona);

        recargarGrid();

        actualizarStats();
        actualizarLblZona();
        actualizarBotonesMover();

        cartaEnCurso = null;

        ocultarAcciones();

        revelarSala(0, 0);
        resaltarSalaActual();
        log("=== " + zonaActual.getNombreZona() + " ===");

        Carta primera = zonaActual.getSalas()[0][0];
        if (primera != null) {
            mostrarInfoCarta(primera);
            cartaEnCurso = primera;
            prepararAcciones(primera);
        }
    }

    // FIN DE PARTIDA
    private void mostrarVictoria() {
        log("🏆 ¡VICTORIA! Partida completada." + " PV:" + personaje.getVida()
                + " PX:" + personaje.getExperiencia() + " Oro:" + personaje.getOro());

        bloquearJuego();
        cerrarFicheroLog();
    }

    private void mostrarDerrota() {
        log("💀 DERROTA. PV: 0. Fin de la partida.");

        bloquearJuego();
        cerrarFicheroLog();
    }

    private void bloquearJuego() {
        ocultarAcciones();
        btnDerecha.setDisable(true);
        btnAbajo.setDisable(true);
        ocultarBtn(btnAvanzarZona);
    }

    // UTILIDADES UI
    private void actualizarStats() {
        lblNombre.setText("🧙 " + personaje.getNombre());
        lblPV.setText("❤ " + personaje.getVida());
        lblPX.setText("⭐ " + personaje.getExperiencia() + " PX");
        lblOro.setText("💰 " + personaje.getOro());
        lblArmadura.setText("🛡 " + personaje.getArmadura());
        lblComida.setText("🍖 " + personaje.getComida());
        lblNivel.setText("Nv." + personaje.getNivel());
    }

    private void actualizarLblZona() {
        int nivelIdx = 0, zonaIdx = 0;
        boolean encontrado = false;

        for (int n = 0; n < gameManager.getNiveles().size() && !encontrado; n++) {
            Zona[] zs = gameManager.getNiveles().get(n);
            for (int z = 0; z < zs.length; z++) {
                if (zs[z] == zonaActual) {
                    nivelIdx = n;
                    zonaIdx = z;
                    encontrado = true;
                    break;
                }
            }
        }

        lblZona.setText("Nivel " + (nivelIdx + 1) + " – Zona " + (zonaIdx + 1));
    }

    private void actualizarBotonesMover() {
        int f = zonaActual.getFilaActual();
        int c = zonaActual.getColumnaActual();

        Carta actual = zonaActual.getSalas()[f][c];

        boolean resuelta = (actual == null || actual.estaOcupada());

        btnDerecha.setDisable(!resuelta || !zonaActual.puedeMoverDerecha());
        btnAbajo.setDisable(!resuelta || !zonaActual.puedeMoverAbajo());
    }

    private void ocultarAcciones() {
        ocultarBtn(btnResolver);
        ocultarBtn(btnProezaPX);
        ocultarBtn(btnProezaVida);
        ocultarBtn(btnCriticoSi);
        ocultarBtn(btnCriticoNo);
        ocultarPanel(panelChoice);
        btnResolver.setOnAction(e -> onBtnResolver());
    }

    private void resaltarSalaActual() {
        int fA = zonaActual.getFilaActual();
        int cA = zonaActual.getColumnaActual();

        for (int f = 0; f < 3; f++) {
            for (int c = 0; c < 3; c++) {
                if (f == fA && c == cA) {
                    ivSalas[f][c].setStyle("-fx-effect: dropshadow(gaussian, gold, 14, 0.9, 0, 0);");
                } else if (visible[f][c] && ((f == fA && c == cA + 1) || (f == fA + 1 && c == cA))) {
                    ivSalas[f][c].setStyle("-fx-effect: dropshadow(gaussian, deepskyblue, 8, 0.5, 0, 0);");
                } else {
                    ivSalas[f][c].setStyle("");
                }
            }
        }
    }

    private void recargarGrid() {
        gridPane.getChildren().clear();
        for (int f = 0; f < 3; f++) {
            for (int c = 0; c < 3; c++) {
                ivSalas[f][c].setImage(imgVacia);
                ivSalas[f][c].setStyle("");
                visible[f][c] = false;
                gridPane.add(ivSalas[f][c], c, f);
            }
        }
    }

    private void ponerImagen(int f, int c, Carta carta) {
        Image im;
        if (carta instanceof CartaEnemigo e) {
            im = e.isEsJefe() ? imgEnemigoJefe : imgEnemigoNormal;
        } else if (carta instanceof CartaMercader) {
            im = imgMercader;
        } else if (carta instanceof CartaTrampa) {
            im = imgTrampa;
        } else if (carta instanceof CartaEvento) {
            im = imgEvento;
        } else if (carta instanceof CartaTesoro) {
            im = imgTesoro;
        } else if (carta instanceof CartaDescanso) {
            im = imgDescanso;
        } else {
            im = imgVacia;
        }

        ivSalas[f][c].setImage(im);
    }

    private void mostrarBtn(Button btn, String texto) {
        btn.setText(texto);
        btn.setVisible(true);
        btn.setManaged(true);
    }

    private void ocultarBtn(Button btn) {
        btn.setVisible(false);
        btn.setManaged(false);
    }

    private void mostrarPanel(VBox panel) {
        panel.setVisible(true);
        panel.setManaged(true);
    }

    private void ocultarPanel(VBox panel) {
        panel.setVisible(false);
        panel.setManaged(false);
    }

    private void mostrarNode(Node n) {
        n.setVisible(true);
        n.setManaged(true);
    }

    private void ocultarNode(Node n) {
        n.setVisible(false);
        n.setManaged(false);
    }

    private String statsStr() {
        return " | PV:" + personaje.getVida() + " PX:" + personaje.getExperiencia() + " Oro:" + personaje.getOro()
                + " Arm:" + personaje.getArmadura() + " Com:" + personaje.getComida() + " Nv:" + personaje.getNivel();
    }
}
