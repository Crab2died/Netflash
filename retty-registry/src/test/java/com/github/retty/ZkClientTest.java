package com.github.retty;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ZkClientTest {

    class MyZkSerializer implements ZkSerializer {
        @Override
        public byte[] serialize(Object data) throws ZkMarshallingError {
            try {
                return String.valueOf(data).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Object deserialize(byte[] bytes) throws ZkMarshallingError {
            try {
                return new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @Test
    public void testZkClient() {
        String server = "localhost:2181,127.0.0.1:2181";
        final String zkNode = "/goblin/web";
        ZkClient zkClient = new ZkClient(server, 5000, 5000, new MyZkSerializer());
        zkClient.createPersistent("/goblin", true);
        zkClient.create(zkNode, "{\"user\":\"root\"}", CreateMode.PERSISTENT);
        zkClient.subscribeDataChanges(zkNode, new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println(String.format("%s 节点被修改:%s!", s, o));
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println(String.format("%s 节点删除！", s));
            }
        });

        zkClient.writeData(zkNode, "测试");

        //zkClient.deleteRecursive("/goblin");

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}