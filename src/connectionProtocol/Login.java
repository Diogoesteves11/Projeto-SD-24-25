package connectionProtocol;

import java.io.*;

public class Login extends Package{
    private String password;

    public Login(String clientKey, String password)
    {
        super(clientKey, 1);
        this.password = password;
    }

    public Connection getConnection(){
        return super.getConnection();
    }

    public String getPassword(){
        return this.password;
    }

    public String getClientKey(){
        return super.getClientKey();
    }

    public static Login convertBytesToPackage(byte[] bytes) {
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes))) {
            return Login.deserialize(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert bytes to Package", e);
        }
    }

    public byte[] convertPackageToBytes() {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             DataOutputStream dataOut = new DataOutputStream(byteOut)) {
            this.serialize(dataOut);
            return byteOut.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert Package to bytes", e);
        }
    }

    public void serialize(DataOutputStream out) throws IOException {
        super.serialize(out);
        out.writeUTF(this.password);
    }

    public static Login deserialize(DataInputStream in) throws IOException {
        Package pkg = Package.deserialize(in);
        String password = in.readUTF();
        return new Login(pkg.getClientKey(), password);
    }

}