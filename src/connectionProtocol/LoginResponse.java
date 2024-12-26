package connectionProtocol;

import Client.*;

import java.io.*;

public class LoginResponse extends Package{
    private boolean success;
    private Client client;

    public LoginResponse(boolean success, Client client) {
        super(client.getUsername(), 2);
        this.success = success;
    }

    public LoginResponse(boolean success) {
        super("None", 2);
        this.success = success;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public Client getClient() {
        return new Client(client);
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        super.serialize(out);
        out.writeBoolean(this.success);

        if (this.client != null) {
            out.writeBoolean(true);
            this.client.serialize(out);
        } else {
            out.writeBoolean(false);
        }
    }

    public static LoginResponse deserialize(DataInputStream in) throws IOException {
        Package pkg = Package.deserialize(in);

        boolean success = in.readBoolean();

        Client client = null;
        if (in.readBoolean()) {
            client = Client.deserialize(in);
            return new LoginResponse(success, client);
        }

        return new LoginResponse(success);
    }

    public static LoginResponse convertBytesToPackage(byte[] bytes) {
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes))) {
            return LoginResponse.deserialize(in);
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
}