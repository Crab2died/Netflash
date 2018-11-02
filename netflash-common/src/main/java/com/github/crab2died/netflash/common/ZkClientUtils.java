package com.github.crab2died.netflash.common;

import com.alibaba.fastjson.JSONArray;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.CreateMode;

import java.io.UnsupportedEncodingException;
import java.util.Set;

public class ZkClientUtils {

    public static class MyZkSerializer implements ZkSerializer {
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

    public static void initNode(ZkClient zkClient, String node, Set<String> set) {

        if (!zkClient.exists(node)) {
            zkClient.create(node, JSONArray.toJSONString(set), CreateMode.EPHEMERAL_SEQUENTIAL);
        } else {
            String existsServer = zkClient.readData(node);
            if (null != existsServer && !existsServer.equals("[]")) {
                set.addAll(JSONArray.parseArray(existsServer, String.class));
            }
            zkClient.writeData(node, JSONArray.toJSONString(set));
        }
    }


}
