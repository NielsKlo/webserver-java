package nl.sogyo.webserver;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

public class WebSocketUtil {
    private static final String MAGIC_STRING = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

    static String processIncomingWebsocketFrame(final byte[] input) {
        final int firstPart = input[1];
        final int length = (byte) firstPart & 127;

        final int indexFirstMark;
        if (length < 126) indexFirstMark = 2;
        else if (length == 126) indexFirstMark = 4;
        else indexFirstMark = 10;

        final int indexFirstDataByte = indexFirstMark + 4;
        final byte[] masks = Arrays.copyOfRange(input, indexFirstMark, indexFirstDataByte);

        final byte[] decoded = new byte[length];
        for (int i = indexFirstDataByte, j = 0; i < length + indexFirstDataByte; i++, j++) {
            decoded[j] = (byte) (input[i] ^ masks[j % 4]);
        }
        return new String(decoded);
    }

    static byte[] createOutgoingWebsocketFrame(final String message) {
        final int length = message.length();

        final int rawDataIndex;
        if (length <= 125) {
            rawDataIndex = 2;
        } else if (length <= 65535) {
            rawDataIndex = 4;
        } else {
            rawDataIndex = 10;
        }

        final byte[] frame = new byte[length + rawDataIndex];
        frame[0] = (byte) 129;
        if (rawDataIndex == 2) {
            frame[1] = (byte) length;
        } else if (rawDataIndex == 4) {
            frame[1] = (byte) 126;
            frame[2] = (byte) (length / 256);
            frame[3] = (byte) (length % 256);
        } else {
            frame[1] = (byte) 127;

            int left = length;
            final int unit = 256;

            for (int i = 9; i > 1; i--) {
                frame[i] = (byte) (left % unit);
                left = left / unit;

                if (left == 0) break;
            }
        }

        for (int i = 0; i < length; i++) {
            frame[rawDataIndex + i] = (byte) message.charAt(i);
        }
        return frame;
    }

    static Optional<String> generateSecWebsocketAccept(final String secWebsocketKey) {
        try {
            final String start = secWebsocketKey + MAGIC_STRING;
            final String base64Encoded = DatatypeConverter
                    .printBase64Binary(MessageDigest
                            .getInstance("SHA-1")
                            .digest(start.getBytes("UTF-8")));

            return Optional.of(base64Encoded);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            System.out.println("Could not generate SecWebSocketAccept, because UTF-8 or SHA-1 is not supported.");
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
