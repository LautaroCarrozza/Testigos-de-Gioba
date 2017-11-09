import java.time.LocalDate;
import java.util.List;

public class EmployeeApp {



    public static void main(String[] args) {

       server = new ServerMock();
       server.setUp();
       iniciarSesion();
    }
    private static ServerInterface server;
    private static int currentSesion, currentClient, diaSalida, mesSalida, anoSalida, cantidadDePasajeros, diaSalidaIda, diaSalidaVuelta, mesSalidaIda, mesSalidaVuelta, anoSalidaIda, anoSalidaVuelta;
    public static String currentDesde, currentHasta;
    public static Vuelo vueloDeseado, vueloDeseadoIda, vueloDeseadoVuelta;

    private static void iniciarSesion(){

      try {
      currentSesion = Scanner.getInt("Ingrese su numero de empleado: ");
      server.validarSesionEmpleado(currentSesion);
      mostrarMenu();
      }
      catch (RuntimeException e){
          System.out.println(e.getMessage());
          iniciarSesion();
      }
    }

    private static void mostrarMenu() {
        System.out.println("1- Ingresar tipo de avion");
        System.out.println("2- Ingresar avion");
        System.out.println("3- Ingresar Aeropuerto");
        System.out.println("4- Ingresar vuelo");
        System.out.println("- - - - - - - - - - - - - - - - - -");
        System.out.println("5- VenderPasaje");
        System.out.println("- - - - - - - - - - - - - - - - - -");
        System.out.println("6- Cerrar Sesion");
        System.out.println("9- Exit");
        System.out.println();

        try {
            int opcion = Scanner.getInt("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    ingresarTipoDeAvion();
                    break;
                case 2:
                    ingresarAvion();
                    break;
                case 3:
                    ingresarAeropuerto();
                    break;
                case 4:
                    ingresarVuelo();
                    break;
                case 5:
                    menuDeVenta();
                    break;
                case 6:
                    iniciarSesion();
                    break;
                case 9:
                    System.exit(0);
                    break;
                default:
                    throw new RuntimeException("Opcion invalida");
            }
        } catch (RuntimeException e) {
            borrarPantalla();
            System.out.println(e.getMessage());
            mostrarMenu();
        }
    }

    private static void venderPasajeIda() {

        venderACliente();
        venderACuantosPasajeros();
        venderDesde();
        venderHasta();
        venderCuando();
        validarVuelo();
        seleccionarVuelo();
        for (int i = 0; i < cantidadDePasajeros; i++) {
            venderAsiento();
        }

        server.guardarReserva(currentClient, vueloDeseado);
        mostrarMenu();
    }

    private static void menuDeVenta() {
        try {
            System.out.println("1- Vender pasaje de ida solamente");
            System.out.println("2- Vender pasaje de ida y vuela");
            int opcion = Scanner.getInt("Seleccione la opcion deseada: ");
            switch (opcion) {
                case 1:
                    venderPasajeIda();
                    break;
                case 2:
                    venderPasajeIdaYVuelta();
                    break;
                default:
                    throw new RuntimeException("Opcion no disponible");
            }
        }
        catch(RuntimeException e){
            System.out.println(e.getMessage());
            menuDeVenta();
        }
    }

    private static void venderPasajeIdaYVuelta() {

        venderACliente();
        venderACuantosPasajeros();
        venderDesde();
        venderHasta();
        venderCuandoIda();
        validarVueloIda();
        seleccionarVueloIda();
        venderCuandoVuelta();
        validarVueloVuelta();

        for (int i = 0; i < cantidadDePasajeros; i++) {
            venderAsientoIda();
        }

        seleccionarVueloVuelta();
        for (int i = 0; i < cantidadDePasajeros; i++) {
            venderAsientoVuelta();
        }
        server.guardarReserva(currentClient, vueloDeseadoIda);
        server.guardarReserva(currentClient, vueloDeseadoVuelta);
        System.out.println("Las reservas se a guardadon correctamente");

        mostrarMenu();

    }

    private static void seleccionarVuelo() {
        List<Vuelo> vuelosDisponibles = server.buscarVuelos(diaSalida, mesSalida, anoSalida,currentDesde,currentHasta, cantidadDePasajeros);
        int opcion = 1;
        for (Vuelo v: vuelosDisponibles
             ) {
            System.out.println(opcion +" "+ v);
            opcion++;
        }
        int intVueloDeseado = Scanner.getInt("Ingrese vuelo deseado: ") -1 ;/// -1 por que los vuelos empiezan con 0 y se los imprime con un +1
        vueloDeseado = vuelosDisponibles.get(intVueloDeseado);
    }

    private static void seleccionarVueloIda() {
        List<Vuelo> vuelosDisponibles = server.buscarVuelos(diaSalidaIda, mesSalidaIda, anoSalidaIda,currentDesde,currentHasta, cantidadDePasajeros);
        int opcion = 1;
        for (Vuelo v: vuelosDisponibles
                ) {
            System.out.println(opcion +" "+ v);
            opcion++;
        }
        int intVueloDeseado = Scanner.getInt("Ingrese vuelo deseado para la ida: ") -1 ;/// -1 por que los vuelos empiezan con 0 y se los imprime con un +1
        vueloDeseadoIda = vuelosDisponibles.get(intVueloDeseado);
    }

    private static void seleccionarVueloVuelta() {
        List<Vuelo> vuelosDisponibles = server.buscarVuelos(diaSalidaVuelta, mesSalidaVuelta, anoSalidaVuelta,currentHasta,currentDesde, cantidadDePasajeros);
        int opcion = 1;
        for (Vuelo v: vuelosDisponibles
                ) {
            System.out.println(opcion +" "+ v);
            opcion++;
        }
        int intVueloDeseado = Scanner.getInt("Ingrese vuelo deseado para la vuelta: ") -1 ;/// -1 por que los vuelos empiezan con 0 y se los imprime con un +1
        vueloDeseadoVuelta = vuelosDisponibles.get(intVueloDeseado);
    }

    private static void venderAsiento(){
        List<Asiento> asientosDisponibles = vueloDeseado.asientosDisponibles();

        System.out.println("Asientos disponibles: ");
        System.out.println();
        for (Asiento asiento : asientosDisponibles) {
            System.out.println(asiento);
        }
        try {
            int fila = Scanner.getInt("Ingresar flia deseada: ");
            String columna = Scanner.getString("Ingresar columna deseada: ");
            if( !vueloDeseado.getOcupacion(fila, columna)){
                vueloDeseado.ocupar(fila, columna);
                return;
            }
            else {
                throw new RuntimeException("Seleccion de asiento no disponible");
            }
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            venderAsiento();
        }

    }

    private static void venderAsientoIda(){
        List<Asiento> asientosDisponibles = vueloDeseadoIda.asientosDisponibles();

        System.out.println("Asientos disponibles para la ida: ");
        System.out.println();
        for (Asiento asiento : asientosDisponibles) {
            System.out.println(asiento);
        }
        try {
            int fila = Scanner.getInt("Ingresar flia deseada: ");
            String columna = Scanner.getString("Ingresar columna deseada: ");
            if( !vueloDeseadoIda.getOcupacion(fila, columna)){
                vueloDeseadoIda.ocupar(fila, columna);
                return;
            }
            else {
                throw new RuntimeException("Seleccion de asiento no disponible");
            }
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            venderAsientoIda();
        }

    }

    private static void venderAsientoVuelta(){
        List<Asiento> asientosDisponibles = vueloDeseadoVuelta.asientosDisponibles();

        System.out.println("Asientos disponibles para la vuelta: ");
        System.out.println();
        for (Asiento asiento : asientosDisponibles) {
            System.out.println(asiento);
        }
        try {
            int fila = Scanner.getInt("Ingresar flia deseada: ");
            String columna = Scanner.getString("Ingresar columna deseada: ");
            if( !vueloDeseadoVuelta.getOcupacion(fila, columna)){
                vueloDeseadoVuelta.ocupar(fila, columna);
                return;
            }
            else {
                throw new RuntimeException("Seleccion de asiento no disponible");
            }
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            venderAsientoVuelta();
        }

    }

    private static void validarVuelo(){
        try{
            server.buscarVuelos(diaSalida, mesSalida, anoSalida,currentDesde,currentHasta, cantidadDePasajeros);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            mostrarMenu();
        }
    }

    private static void validarVueloIda(){
        try{
            server.buscarVuelos(diaSalidaIda, mesSalidaIda, anoSalidaIda,currentDesde,currentHasta, cantidadDePasajeros);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            mostrarMenu();
        }
    }

    private static void validarVueloVuelta(){
        try{
            server.buscarVuelos(diaSalidaVuelta, mesSalidaVuelta, anoSalidaVuelta,currentHasta,currentDesde, cantidadDePasajeros);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            mostrarMenu();
        }
    }

    private static void venderACuantosPasajeros(){
        cantidadDePasajeros = Scanner.getInt("Ingrese la cantidad de pasajeros: ");
    }

    private static void venderCuando(){
        diaSalida = Scanner.getInt("Ingrese dia de salida: ");
        mesSalida = Scanner.getInt("Ingrese mes de salida: ");
        anoSalida = Scanner.getInt("Ingrese año de salida: ");
    }

    private static void venderCuandoIda(){
        diaSalidaIda = Scanner.getInt("Ingrese dia de salida de la ida: ");
        mesSalidaIda = Scanner.getInt("Ingrese mes de salida de la ida: ");
        anoSalidaIda = Scanner.getInt("Ingrese año de salida de la ida: ");
    }

    private static void venderCuandoVuelta(){
        diaSalidaVuelta = Scanner.getInt("Ingrese dia de salida de la vuelta: ");
        mesSalidaVuelta = Scanner.getInt("Ingrese mes de salida de la vuelta: ");
        anoSalidaVuelta = Scanner.getInt("Ingrese año de salida de la vuelta: ");
    }

    private static void venderHasta() {
        try {
            currentHasta = Scanner.getString("Ingrese lugar de llegada: ");
            server.validarLugarDeLlegada(currentHasta);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            venderHasta();
        }
    }

    private static void venderDesde() {
        try {
            currentDesde = Scanner.getString("Ingrese lugar de partida: ");
            server.validarLugarDePartida(currentDesde);
        }
        catch(RuntimeException e){
            System.out.println(e.getMessage());
            venderDesde();
        }
    }

    private static void venderACliente() {
        try{
            currentClient = Scanner.getInt("Ingresar el numero del cliente: ");
            server.validarCliente(currentClient);
        }
        catch(RuntimeException e){
            System.out.println(e.getMessage());
            venderACliente();
        }
    }

    private static void ingresarVuelo() {

        try {
            String aeropuertoDeSalida = Scanner.getString("Ingrese el codigo del aeropuerto de salida: ");
            String aeropuertoDeLlegada = Scanner.getString("Ingrese el codigo del aeropuerto de llegada: ");
            int dia = Scanner.getInt("Ingrese el dia de salida del vuelo: ");
            int mes = Scanner.getInt("Ingese el mes de salida del vuelo: ");
            int ano = Scanner.getInt("Ingrese el año de salida del vuelo: ");
            int hours = Scanner.getInt("Ingrese la hora de salida del vuelo: ");
            int minutes = Scanner.getInt("ingrese los minutos de la hora de salida del vuelo: ");
            int minutesDuration = Scanner.getInt("Ingrese los minutos de la duracion del viaje: ");
            String plane = Scanner.getString("Ingrese el codigo del avion a utilizar: ");
            int flightCode = Scanner.getInt("Ingrese el codigo del vuelo: ");
            int cantidadDeSemanas = Scanner.getInt("Durante cuantas semanas va a repetirse el vuelo?");
            LocalDate localDate = LocalDate.of(ano, mes, dia);
            server.validarDisponibilidadTripulacion(localDate, server.getAvion(plane).getCantidadDePersonal());
            server.addVuelo(aeropuertoDeSalida, aeropuertoDeLlegada, dia, mes, ano, hours, minutes,minutesDuration, plane, flightCode, cantidadDeSemanas);
            server.getVuelo(flightCode).addTripulacion();
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            mostrarMenu();
        }

        System.out.println("Vuelos cargado");
        mostrarMenu();
    }

    private static void ingresarAeropuerto(){

        try {

            String codigoDeAeropuerto = Scanner.getString("Ingrese el codigo del aeropuerto: ");
            String ubicacion = Scanner.getString("Ingrese la ubicacion del aeropuerto: ");
            String nombre = Scanner.getString("Ingrese el nombre del aeropuerto:");
            server.addAeropuerto(codigoDeAeropuerto, ubicacion, nombre);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            mostrarMenu();
        }
            System.out.println("Aeropuerto cargado");
            mostrarMenu();

    }

    private static void ingresarAvion() {

        String codigoAvion = Scanner.getString("Ingrese el codigo del avion: ");
        String tipoDeAvion = Scanner.getString("Ingrese el tipo de avion para este avion: ");
        try {
            server.addAvion(codigoAvion, tipoDeAvion);
            System.out.println("Avion cargado");
            mostrarMenu();
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            mostrarMenu();
        }


    }

    private static void ingresarTipoDeAvion() {

        try {

            int cantidadFilasEconomy = Scanner.getInt("Ingrese la cantidad de filas para la categoria Economy: ");
            int cantidadAsientosPorFilaDeEconomy = Scanner.getInt("Ingrese la cantidad de asientos por fila para la categoria Economy: ");
            int cantidadFilasBussiness = Scanner.getInt("Ingrese la cantidad de filas para la categoria Bussiness: ");
            int cantidadAsientosPorFilaDeBussiness = Scanner.getInt("Ingrese la cantidad de asientos por fila para la categoria Bussiness: ");
            int cantidadFilasFirst = Scanner.getInt("Ingrese la cantidad de filas para la categoria First: ");
            int cantidadAsientosPorFilaDeFirst = Scanner.getInt("Ingrese la cantidad de asientos por fila para la categoria First: ");
            int cantidadDePersonalAbordo = Scanner.getInt("Ingrese la cantidad de personal de abordo para el tipo de avion: ");
            String nombre = Scanner.getString("Ingrese el nombre para el tipo de avion ingresado: ");

            server.addTipoDeAvion(cantidadFilasEconomy, cantidadAsientosPorFilaDeEconomy, cantidadFilasBussiness, cantidadAsientosPorFilaDeBussiness, cantidadFilasFirst, cantidadAsientosPorFilaDeFirst,cantidadDePersonalAbordo, nombre);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            mostrarMenu();
        }
            System.out.println("Tipo de avion cargado");
            mostrarMenu();


    }

    private static void borrarPantalla() {
        // TODO: 9/10/17

        for (int i = 0; i < 100; i++) {
            System.out.println("*");
        }
    }
}
