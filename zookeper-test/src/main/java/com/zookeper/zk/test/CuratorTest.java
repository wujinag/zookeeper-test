package com.zookeper.zk.test;

import com.alibaba.fastjson.JSON;
import com.zookeper.zk.ZookeperTestApplication;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZookeperTestApplication.class)
public class CuratorTest {

    @Autowired
    private CuratorFramework client;

    // 测试连接
    @Test
    public void contextLoads() {
        System.out.println(client.toString());
    }

    // 创建节点
    @Test
    public void createPath() throws Exception {
        // 父节点不存在则创建
        String path = client.create().creatingParentsIfNeeded().forPath("/wujnote/p1",
                "Java博客".getBytes(StandardCharsets.UTF_8));
        System.out.println(path);
        byte[] data = client.getData().forPath("/wujnote/p1");
        System.out.println(new String(data));
    }

    // 赋值，修改数据
    @Test
    public void setData() throws Exception {
        int version = 0; // 当前节点的版本信息
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath("/wujnote/p1");
        version = stat.getVersion();
        // 如果版本信息不一致，说明当前数据被修改过，则修改失败程序报错
        client.setData().withVersion(version).forPath("/wujnote/p1",
                "Java林sss的博客".getBytes(StandardCharsets.UTF_8));
        byte[] data = client.getData().forPath("/wujnote/p1");
        System.out.println(new String(data));
    }

    // 查询节点
    @Test
    public void getPath() throws Exception {
        // 查内容
        byte[] data = client.getData().forPath("/zkblog/p1");
        System.out.println(new String(data));

        // 查状态
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath("/zkblog/p1");
        System.out.println(JSON.toJSONString(stat, true));
    }

    // 删除节点
    @Test
    public void deletePath() throws Exception {
        // deletingChildrenIfNeeded如果有子节点一并删除
        // guaranteed必须成功比如网络抖动时造成命令失败
        client.delete().guaranteed().deletingChildrenIfNeeded().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                System.out.println("删除成功");
                // { "path":"/javacui/p1","resultCode":0,"type":"DELETE"}
                System.out.println(JSON.toJSONString(curatorEvent, true));
            }
        }).forPath("/zkblog/p1");
    }

    // 查询子节点
    @Test
    public void getPaths() throws Exception {
        List<String> paths = client.getChildren().forPath("/wujnote");
        for (String p : paths) {
            System.out.println(p);
        }
    }

    @Test
    public void createNote() throws Exception {
//        client.create().forPath("/wuj/note/0001");
//        client.create().withMode(CreateMode.PERSISTENT).forPath("/wuj/0001");
//        client.create().withMode(CreateMode.PERSISTENT).forPath("/wuj/0001/00001");
        client.delete().forPath("/wuj/0001");
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/wuj/0001","你妈妈吗".getBytes());
        System.out.printf( "--====>"+client.checkExists().forPath("/wuj/0001"));
//        System.out.println("aaaa===>"+client.getData().forPath("/wuj/0001/00001"));
        //client.delete().deletingChildrenIfNeeded().forPath("/wuj");
    }


}
