import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

//Fes un programa servidor que:
//
//Accepta la connexió d'un client.
//Suma tots els nombres que li envia fins que repel missatge "FINAL".
//El servidor torna com a resposta el resultat total de la suma.
//Torna a esperar una altra connexió.
//
//El client:
//
//Es connecta al servidor.
//Li envia nombres obtinguts del teclat. Deixa d'enviar nombres
// quan el teclat retorna una cadena buida.
//Mostra per pantalla la resposta del servidor i finalitza.

fun main() {
    val port = 9000
    println("Iniciant servidor sumador al port: $port")

    ServerSocket(port).use { serverSocket ->
        println("[SERVIDOR] Escoltant conexions a ${serverSocket.inetAddress.hostAddress}:$port")
        while (true) {
            println("[SERVIDOR] A la espera de un client...")

            val clientSocket = serverSocket.accept()
            println("[SERVIDOR] Client connectad desde ${clientSocket.remoteSocketAddress}")
            atendreClient(clientSocket)
        }
    }
}

fun atendreClient(clientSocket: Socket) {

    clientSocket.use { client ->
        val reader = client.getInputStream().bufferedReader()
        val writer = client.getOutputStream().bufferedWriter()

        var resultat = 0

    try{
        var linia: String? = reader.readLine()
        while (linia != null && !linia.equals("FINAL", ignoreCase = true)) {
            try {
                resultat += linia.toInt()
            } catch (e: NumberFormatException) {
                writer.write("nombre invalid: $linia")
            }
        }
        if(linia.equals("FINAL", ignoreCase = true)) {
            writer.write("La suma total es: $resultat")
            writer.flush()
        }

    } catch (ex: SocketException) {
            println("[Servidor] La connexió amb el client s'ha interromput inesperadament.")
        }
        println("[Servidor] Finalitzada la sessió amb el client:${client.remoteSocketAddress}")
    }
}