import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author q--co
 */
public class Server {

    /**
     * Puerto
     */
    private final static int PORT = 5000;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //Socket de servidor para esperar peticiones de la red
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor> Servidor iniciado");
            System.out.println("Servidor> En esperando clientes...");
            //Socket de cliente
            Socket clientSocket;
            while(true){
                //en espera de conexion, si existe la acepta
                clientSocket = serverSocket.accept();
                //Para leer lo que envie el cliente
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //para imprimir datos de salida
                PrintStream output = new PrintStream(clientSocket.getOutputStream());
                //se lee peticion del cliente
                String request = input.readLine();
                //System.out.println("Cliente> petición [" + request +  "]");
                //se procesa la peticion y se espera resultado
                String strOutput = decompress(request);
                //Se imprime en consola "servidor"
                System.out.println("Servidor> Resultado de petición");
                System.out.println("Servidor> \"" + strOutput + "\"");
                //se imprime en cliente
                output.flush();//vacia contenido
                output.println(strOutput);
                //cierra conexion
                clientSocket.close();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }


    private static String decompress(String compressedText) {
        if (compressedText.length() <= 1) {
            return compressedText;
        }

        char c = compressedText.charAt(0);

        if (Character.isDigit(c)) {
            return String.join("", Collections.nCopies(Character.digit(c, 10), compressedText.substring(1, 2))) + decompress(compressedText.substring(2));
        }

        return compressedText.charAt(0) + decompress(compressedText.substring(1));
    }
}
