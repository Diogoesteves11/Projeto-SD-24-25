import java.io.*;
import java.net.*;
import java.util.*;

public class ClientBatch {
    private static final int PORT = 8080;
    private static final String HOST = "localhost";

    private static final byte CMD_PUT = 1;
    private static final byte CMD_GET = 2;
    private static final byte CMD_EXIT = 3;
    private static final byte CMD_LOGIN = 4;
    private static final byte CMD_REGISTER = 5;
    private static final byte CMD_MULTIPUT = 6;
    private static final byte CMD_MULTIGET = 7;
    private static final byte CMD_GETWHEN = 8;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Modo de execução e operação não fornecidos.");
            return;
        }

        String mode = args[0];
        String operation = args[1];

        if ("batch".equalsIgnoreCase(mode)) {
            executeBatchMode(operation);
        } else {
            System.out.println("Modo desconhecido: " + mode);
        }
    }

    private static void executeBatchMode(String operation) {
        List<String> keys = Arrays.asList("key1", "key2", "key3", "key4", "key5");
        List<byte[]> values = Arrays.asList(
                "value1".getBytes(),
                "value2".getBytes(),
                "value3".getBytes(),
                "value4".getBytes(),
                "value5".getBytes()
        );

        Random random = new Random();

        try (Socket socket = new Socket(HOST, PORT);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            int iteration = 0;

            while (true) {
                System.out.println("Iteration: " + (++iteration));
                try {
                    switch (operation.toLowerCase()) {
                        case "put":
                            String randomKeyPut = keys.get(random.nextInt(keys.size()));
                            byte[] randomValue = values.get(random.nextInt(values.size()));
                            out.writeByte(CMD_PUT);
                            out.writeUTF(randomKeyPut);
                            out.writeInt(randomValue.length);
                            out.write(randomValue);
                            boolean putSuccess = in.readBoolean();
                            System.out.println("Put key: " + randomKeyPut + " value: " + new String(randomValue) + ", success: " + putSuccess);
                            break;

                        case "get":
                            String randomKeyGet = keys.get(random.nextInt(keys.size()));
                            out.writeByte(CMD_GET);
                            out.writeUTF(randomKeyGet);
                            boolean getSuccess = in.readBoolean();
                            if (getSuccess) {
                                int valueLength = in.readInt();
                                byte[] value = new byte[valueLength];
                                in.readFully(value);
                                System.out.println("Get key: " + randomKeyGet + " value: " + new String(value));
                            } else {
                                System.out.println("Key " + randomKeyGet + " not found.");
                            }
                            break;

                        case "multiget":
                            out.writeByte(CMD_MULTIGET);
                            int numKeys = random.nextInt(keys.size()) + 1;
                            out.writeInt(numKeys);
                            Set<String> randomKeys = new HashSet<>();
                            for (int i = 0; i < numKeys; i++) {
                                String key = keys.get(random.nextInt(keys.size()));
                                if (randomKeys.add(key)) {
                                    out.writeUTF(key);
                                }
                            }
                            System.out.println("Sending " + numKeys + " keys to server");
                            System.out.println("MultiGet Result: ");
                            int responseSize = in.readInt();
                            for (int i = 0; i < responseSize; i++) {
                                String key = in.readUTF();
                                int valueLength = in.readInt();
                                byte[] value = new byte[valueLength];
                                in.readFully(value);
                                System.out.println("Key: " + key + " Value: " + new String(value));
                            }
                            break;

                        default:
                            System.out.println("Operação desconhecida: " + operation);
                            return;
                    }
                } catch (EOFException e) {
                    System.out.println("Conexão encerrada inesperadamente pelo servidor.");
                    break;
                } catch (IOException e) {
                    System.out.println("Erro de I/O durante a operação: " + e.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Falha ao conectar ao servidor: " + e.getMessage());
        }
    }

}
