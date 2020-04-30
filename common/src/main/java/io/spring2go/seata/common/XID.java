package io.spring2go.seata.common;

public class XID {
    private static int port;

    private static String ipAddress;

    public static void setPort(int port) {
        XID.port = port;
    }

    public static void setIpAddress(String ipAddress) {
        XID.ipAddress = ipAddress;
    }

    public static String generateXID(long tranId) {
        return ipAddress + ":" + port + ":" + tranId;
    }

    public static long getTransactionId(String xid) {
        if (xid == null) {
            return -1;
        }

        int idx = xid.lastIndexOf(":");
        return Long.parseLong(xid.substring(idx + 1));
    }

    public static String getServerAddress(String xid) {
        if (xid == null) {
            return null;
        }

        int idx = xid.lastIndexOf(":");
        return xid.substring(0, idx);
    }
}
