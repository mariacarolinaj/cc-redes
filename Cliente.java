import java.io.*;
import java.net.*;

class Cliente {
   private static int portaServidor = 6789;

   public static String lerString() throws Exception {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      return in.readLine();
   }

   private static void fecharConexao(Socket socket) throws IOException {
      System.out.println("Fechando conexão...");
      socket.close();
      System.out.println("Conexão fechada");
   }

   public static void main(String argv[]) throws Exception {
      System.out.println("Estabelecendo conexão");

      System.out.print("Informe seu nome: ");
      String identificador = lerString();

      System.out.println("\nPara encerrar o chat, digite \"sair\" a qualquer momento\n");

      // Efetua a primitiva socket ---- 192.168.0.9
      Socket socket = new Socket("127.0.0.1", portaServidor);

      // Efetua a primitiva send
      DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
      String mensagem = "";
      boolean exibiuMensagemConexao = false;

      while (true) {
         if (!exibiuMensagemConexao) {
            System.out.println("Conexão estabelecida\n");
            exibiuMensagemConexao = true;
         }

         BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         // Efetua a primitiva receive
         entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         String mensagemRecebida = entrada.readLine();
         if (mensagemRecebida.equals("sair")) {
            // Fecha conexão a partir de solicitação do outro cliente
            fecharConexao(socket);
            break;
         }
         if (!mensagemRecebida.equals("inicio")) {
            System.out.println(mensagemRecebida);
         }

         System.out.print("\nDigite sua mensagem: ");
         mensagem = lerString();
         System.out.println();

         if (!mensagem.equals("sair")) {
            saida.writeBytes(identificador + " diz: " + mensagem + '\n');
         } else {
            saida.writeBytes("sair" + '\n');
            fecharConexao(socket);
            break;
         }
      }
   }
}
