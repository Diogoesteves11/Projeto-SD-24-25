package connectionProtocol;

import Client.*;

import java.io.*;

public class RegisterResponse extends Package {
    private boolean success;

    public RegisterResponse(boolean success) {
        super("None", 3);
        this.success = success;
    }

    public boolean isSuccess() {
        return this.success;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        super.serialize(out);
        out.writeBoolean(this.success);
    }

    public static RegisterResponse deserialize(DataInputStream in) throws IOException {
        Package pkg = Package.deserialize(in);
        boolean success = in.readBoolean();

        return new RegisterResponse(success);
    }

    public static RegisterResponse convertBytesToPackage(byte[] bytes) {
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes))) {
            return RegisterResponse.deserialize(in);
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
