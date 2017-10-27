import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ServerMock implements ServerInterface{

    private List<Cliente> clientes = new ArrayList<>();
    private List<Vuelo> vuelos = new ArrayList<>();

    public void setUpTest(){


        Cliente clienteA = new Cliente(1, "a", 1);
        Cliente clienteB = new Cliente(2, "b", 2);

        addCliente(clienteA);
        addCliente(clienteB);

        Aeropuerto aeropuertoA = new Aeropuerto("aaa", "aaa", "aaa");
        Aeropuerto aeropuertoB = new Aeropuerto("bbb", "bbb", "bbb");

        TipoDeAvion tipoDeAvionA = new TipoDeAvion(10, 2, "a");

        Avion avionA = new Avion("1", tipoDeAvionA);

        Vuelo vueloA = new Vuelo(aeropuertoA, aeropuertoB, 1, 1, 2017, avionA, 1);
        addVuelo(vueloA);

        Reserva reservaA = new Reserva(clienteA, vueloA);
        clienteA.addReserva(reservaA);

    }

    public void addVuelo(Vuelo vuelo){vuelos.add(vuelo);}

    public void validarSesion(int numero) {
        for (Cliente c : clientes) {
            if(c.getNumeroDeCliente() == numero){
                return;
            }
        }
        throw new RuntimeException("Numero de cliente invalido");
    }

    public String printReservas(int numeroDeCliente){

        String result =  "";

        for (Cliente c: clientes ) {
            if(c.getNumeroDeCliente() == numeroDeCliente ){
                Cliente cliente = c;

                if(cliente.getReservas().size() == 0){
                    result =  "El cliente no cuenta con reservas";
                    break;
                }

                System.out.println(c.getNombre());
                for (Reserva r :cliente.getReservas()) {
                    result += r;
                }
            }
        }
        return result;
    }

    public  void addCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public List<Vuelo> buscarVuelos(int dia, int mes, int ano, String lugarSalida, String lugarLlegada, int cantidadPersonas, String categoria){

        List<Vuelo> posiblesVuelos = new ArrayList<>();

        for (Vuelo vuelo: vuelos) {
            if (vuelo.getFechaSalida().get(Calendar.DAY_OF_MONTH) == dia && (vuelo.getFechaSalida().get(Calendar.MONTH)+1 ) == mes && vuelo.getFechaSalida().get(Calendar.YEAR)  == ano && vuelo.getUbicacionSalida().equals(lugarSalida) && vuelo.getUbicacionLlegada().equals(lugarLlegada)
                    && vuelo.cantidadAsientosDisponibles(categoria) >= cantidadPersonas){
                posiblesVuelos.add(vuelo);
            }
        }
        if (posiblesVuelos.size() == 0){throw new RuntimeException("No existen vuelos");}
        return posiblesVuelos;
    }

    public void comprarPasaje(int codigoVuelo, int codigoCliente, int fila, char columna) {
        Vuelo vuelo = getVuelo(codigoVuelo);
        Asiento asiento = vuelo.getAsiento(fila, columna);
        if (!vuelo.getOcupacion(asiento)){vuelo.ocupar(asiento);}
        else{throw new RuntimeException("El asiento esta ocupado");}
    }

    public Vuelo getVuelo(int codigoDeVuelo) {
        for (Vuelo v:vuelos) {
            if (v.getCodigoDeVuelo()== (codigoDeVuelo)){
                return v;
            }
        }
        throw new RuntimeException("No existen vuelos con ese codigo");
    }
}
