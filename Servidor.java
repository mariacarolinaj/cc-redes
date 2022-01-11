import java.io.*;
import java.net.*;

class Servidor {
   private static int portaServidor = 6789;

   public static void main(String argv[]) throws Exception {
      // Efetua a primitiva socket
      ServerSocket socket = new ServerSocket(portaServidor);
      Socket[] conexoes = new Socket[2];

      System.out.println("Servidor ativo na porta " + portaServidor);

      // Estabele conexão dos dois clientes
      for (int i = 0; i < conexoes.length; i++) {
         // Efetua a primitiva accept
         conexoes[i] = socket.accept();
         System.out.println("Cliente " + i + " conectado");
      }

      boolean conexaoAtivaCliente0, conexaoAtivaCliente1;

      // Inicia a comunicação no cliente 0
      conexaoAtivaCliente0 = conecta(conexoes, 0, 0);

      // escuta o meio para encaminhar as mensagens, um cliente de cada vez
      do {
         conexaoAtivaCliente1 = conecta(conexoes, 1, 0);
         conexaoAtivaCliente0 = conecta(conexoes, 0, 1);
      } while (conexaoAtivaCliente0 && conexaoAtivaCliente1);

      // Fecha todas as conexões
      for (int i = 0; i < conexoes.length; i++) {
         conexoes[i].close();
      }

      socket.close();
   }

   private static boolean conecta(Socket[] conexoes, int clienteAtual, int destinatario) throws IOException {
      // Dá start no sistema de envio de mensagem com um send e redefine o
      // destinatário, que será o cliente oposto
      DataOutputStream saida = new DataOutputStream(conexoes[destinatario].getOutputStream());
      if (clienteAtual == destinatario) {
         saida.writeBytes("inicio" + '\n');
         if (clienteAtual == 0) {
            destinatario = 1;
         } else {
            destinatario = 0;
         }
      }

      System.out.println("Aguardando datagrama do cliente " + clienteAtual + "....");

      // Efetua a primitiva receive
      BufferedReader entrada = new BufferedReader(new InputStreamReader(conexoes[clienteAtual].getInputStream()));
      String str = entrada.readLine();

      System.out.println("Recebeu do cliente " + clienteAtual + " para o cliente " + destinatario + ": " + str);

      // Efetua a primitiva send
      try {
         saida = new DataOutputStream(conexoes[destinatario].getOutputStream());
         saida.writeBytes(str + '\n');
      } catch (Exception e) {
         return false; // A conexão do outro cliente está fechada; deve encerrar a comunicação
      }

      if (str == null || str.equals("sair")) {
         return false; // A conexão deve ser encerrada
      }

      return true; // A conexão continua ativa
   }
}
