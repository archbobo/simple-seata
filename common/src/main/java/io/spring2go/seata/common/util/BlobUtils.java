package io.spring2go.seata.common.util;

import io.spring2go.seata.common.exception.ShouldNeverHappenException;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;

public class BlobUtils {
    private BlobUtils() {

    }

    public static Blob string2blob(String str) {
        if (str == null) {
            return null;
        }

        try {
            return new SerialBlob(str.getBytes());
        } catch (Exception e) {
            throw new ShouldNeverHappenException(e);
        }
    }

    public static String blob2string(Blob blob) {
        if (blob == null) {
            return null;
        }

        try {
            return new String(blob.getBytes((long) 1, (int) blob.length()));
        } catch (Exception e) {
            throw new ShouldNeverHappenException(e);
        }
    }

    public static String inputStream2String(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i = -1;
            while ((i = is.read()) != -1) {
                baos.write(i);
            }
            return baos.toString();
        } catch (Exception e) {
            throw new ShouldNeverHappenException(e);
        }
    }
}
