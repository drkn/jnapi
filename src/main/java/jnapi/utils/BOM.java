package jnapi.utils;

import java.util.Arrays;

/**
 * UTFx BOMs
 *
 * @author Maciej Dragan
 */
public enum BOM {

    Default("", new byte[]{}),
    UTF32BE("UTF-32BE", new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF}),
    UTF32LE("UTF-32LE", new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00}),
    UTF16BE("UTF-16BE", new byte[]{(byte) 0xFE, (byte) 0xFF}),
    UTF16LE("UTF-16LE", new byte[]{(byte) 0xFF, (byte) 0xFE}),
    UTF8("UTF-8", new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
    private String name;
    private byte[] value;

    BOM(String name, byte[] value) {
        this.name = name;
        this.value = value;
    }

    public static BOM getByName(String name) {
        for (BOM bom : values()) {
            if (bom.name.equalsIgnoreCase(name)) {
                return bom;
            }
        }
        return Default;
    }

    public String getName() {
        return name;
    }

    public byte[] getValue() {
        return Arrays.copyOf(value, value.length);
    }

    public boolean containsBOM(byte[] data) {
        if (data == null || data.length < value.length) {
            return false;
        }
        for (int i = 0; i < value.length; i++) {
            if (data[i] != value[i]) {
                return false;
            }
        }
        return true;
    }

}
