import java.io.*;

public class Pacote implements Serializable {
    private String metodo;
    private String username;
    private String password;

    // Construtor
    public Pacote(String metodo, String username, String password) {
        this.metodo = metodo;
        this.username = username;
        this.password = password;
    }

    // Métodos Getters e Setters
    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Método para converter o pacote em bytes
    public byte[] converterPacoteParaBytes() throws IOException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(this);
            return byteStream.toByteArray();
        }
    }

    // Método para reconstruir um pacote a partir de bytes
    public static Pacote convertBytesParaPacote(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectStream = new ObjectInputStream(byteStream)) {
            return (Pacote) objectStream.readObject();
        }
    }

    // Método para enviar o pacote para um servidor via socket
    public void enviarPeloSocketParaServidor(OutputStream outputStream) throws IOException {
        byte[] pacoteBytes = converterPacoteParaBytes();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);
        dataOutput.writeInt(pacoteBytes.length);
        dataOutput.write(pacoteBytes);          
        dataOutput.flush();
    }

    // Método para receber um pacote do servidor via socket
    public static Pacote receberPacoteDoSocket(InputStream inputStream) throws IOException, ClassNotFoundException {
        DataInputStream dataInput = new DataInputStream(inputStream);
        int tamanho = dataInput.readInt();
        byte[] pacoteBytes = new byte[tamanho];
        dataInput.readFully(pacoteBytes); 
        return convertBytesParaPacote(pacoteBytes);
    }
}
