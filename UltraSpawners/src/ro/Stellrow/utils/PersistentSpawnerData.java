package ro.Stellrow.utils;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.io.*;

public class PersistentSpawnerData implements PersistentDataType<byte[],SpawnerData> {

    private byte[] convertToBytes(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (
                ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bos.toByteArray();
    }

    private Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInput in = new ObjectInputStream(bis))
        {
            return in.readObject();

        }
    }



    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<SpawnerData> getComplexType() {
        return SpawnerData.class;
    }

    @Override
    public byte[] toPrimitive(SpawnerData spawnerData, PersistentDataAdapterContext persistentDataAdapterContext) {
        return convertToBytes(spawnerData);
    }

    @Override
    public SpawnerData fromPrimitive(byte[] bytes, PersistentDataAdapterContext persistentDataAdapterContext) {
        SpawnerData toReturn = null;
        try {
           toReturn= (SpawnerData) convertFromBytes(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
