package com.github.netflash;

import com.alibaba.fastjson.JSONArray;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
        List<String> servers = new ArrayList<>();
        servers.add("127.0.0.1:4321");
        servers.add("localhost:4321");
        List<String> services = new ArrayList<>();
        services.add("URL:127.0.0.1:4321:com.github.crab2died.netflash.demo.service.impl.DemoServiceImpl[]{100}");
        services.add("URL:localhost:4321:com.github.crab2died.netflash.demo.service.impl.DemoServiceImpl[v1.0]{100}");

        final String serviceNode = "/netflash/registry/service";
        final String serverNode = "/netflash/registry/server";

        ZkClient zkClient = new ZkClient(server, 5000, 5000, new MyZkSerializer());
        zkClient.createPersistent("/netflash/registry", true);

        // server node
        if (!zkClient.exists(serverNode)) {
            zkClient.create(serverNode, JSONArray.toJSONString(servers), CreateMode.PERSISTENT);
        } else {
            String existsServer = zkClient.readData(serverNode);
            if (null != existsServer && !existsServer.equals("[]")){
                servers.addAll(JSONArray.parseArray(existsServer, String.class));
            }
            zkClient.writeData(serverNode, JSONArray.toJSONString(servers));
        }

        zkClient.subscribeDataChanges(serverNode, new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println(String.format("%s 节点被修改:%s!", s, o));
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println(String.format("%s 节点删除！", s));
            }
        });

        // service node
        zkClient.create(serviceNode, JSONArray.toJSONString(services), CreateMode.PERSISTENT);

        zkClient.subscribeDataChanges(serviceNode, new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println(String.format("%s 节点被修改:%s!", s, o));
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println(String.format("%s 节点删除！", s));
            }
        });


        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        String server = "localhost:2181,127.0.0.1:2181";
        ZkClient zkClient = new ZkClient(server, 5000, 5000, new MyZkSerializer());
        zkClient.createPersistent("/netflash/registry", true);
        List<String> servers = new ArrayList<>();
        servers.add("127.0.0.1:4321");
        servers.add("localhost:4321");
        zkClient.create("/netflash/registry/service", "aa", CreateMode.EPHEMERAL_SEQUENTIAL);
        zkClient.create("/netflash/registry/service", "bb", CreateMode.EPHEMERAL_SEQUENTIAL);
        zkClient.subscribeDataChanges("/netflash/registry/service", new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println(String.format("%s 节点被修改:%s!", s, o));
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println(String.format("%s 节点删除！", s));
            }
        });

       // zkClient.create("/netflash/registry/service", "CC", CreateMode.EPHEMERAL_SEQUENTIAL);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}