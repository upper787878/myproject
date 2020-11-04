package com.itheima;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

public class ZkDemo {

    /**
     * 节点 增删改查
     * @throws Exception
     */
    @Test
    public void  testZK() throws Exception {
        //RetryPolicy：失败的重试策略的公共接口
        RetryPolicy  retryPolicy = new ExponentialBackoffRetry(3000,3,1000);
        //CuratorFramework：创建客户端对象
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.174.128:2181",3000,3000,retryPolicy);
        //Start():开启客户端
        client.start();
        //Create – delete – set –get :操作

        //1. 创建一个空节点(a)（只能创建一层节点）
        //client.create().forPath("/a");

        //2. 创建一个有内容的b节点（只能创建一层节点）
        //client.create().forPath("/a","a say hello".getBytes());

        //3. 创建持久节点，同时创建多层节点
        //client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/c/d","cd say hello".getBytes());

        //4. 创建带有的序号的持久节点
        //client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/e","e say hello".getBytes());

        //5. 创建临时节点（客户端关闭，节点消失），设置延时5秒关闭（Thread.sleep(5000)）
       //client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/f","f say hello".getBytes());
        //6. 创建临时带序号节点（客户端关闭，节点消失），设置延时5秒关闭（Thread.sleep(5000)）

        //client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/g","g say hello".getBytes());

        //7修改节点数据
        //client.setData().forPath("/a","a say Hello".getBytes());

        //8.查询节点数据
        //System.out.println(new String(client.getData().forPath("/a")));

        //9删除节点 guaranteed():客户端跟服务端 断开的时候 ,也能删除节点下所有节点和数据
        client.delete().guaranteed().deletingChildrenIfNeeded().forPath("/a");

        //Thread.sleep(5000);
        //Close:关闭客户端
        client.close();

    }


    /**
     * 节点监听
     */
    //@Test
    public void testWatch() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000,3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",retryPolicy);
        client.start();
        System.out.println("连接上zk....");

        //创建监听节点
        final NodeCache nodeCache = new NodeCache(client,"/app1");
        //开启监听
        nodeCache.start();
        //添加监听节点，获取节点变化
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                //nodeChanged 只要节点发生变化 此方法都能获取数据
                System.out.println(nodeCache.getPath()+"*******"+new String(nodeCache.getCurrentData().getData()));
            }
        });

        //保证监听一直在
        System.in.read();
    }


    /**
     * 节点监听
     */
    /*@Test
    public void testPathChildrenCache() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000,3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",retryPolicy);
        client.start();
        System.out.println("连接上zk....");

        //创建监听子节点
        final PathChildrenCache pathChildrenCache = new PathChildrenCache(client,"/app1",true);
        //开启监听
        *//**
         * NORMAL:  普通启动方式, 在启动时缓存子节点数据
         * POST_INITIALIZED_EVENT：在启动时缓存子节点数据，提示初始化
         * BUILD_INITIAL_CACHE: 在启动时什么都不会输出
         *  在官方解释中说是因为这种模式会在start执行执行之前先执行rebuild的方法，而rebuild的方法不会发出任何事件通知。
         *//*
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        //开启监听后 能否获取节点数据
        System.out.println("*********************"+pathChildrenCache.getCurrentData());

        //添加监听节点，获取节点变化
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                //监听当前节点子节点变化
                if(event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED){
                    System.out.println("CHILD_ADDED子节点变化了"+pathChildrenCache.getCurrentData());
                }
                //监听修改子节点变化
                else if(event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED){
                    System.out.println("CHILD_UPDATED子节点变化了"+pathChildrenCache.getCurrentData());
                }
                //监听删除子节点变化
                else if(event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED){
                    System.out.println("CHILD_REMOVED子节点变化了"+pathChildrenCache.getCurrentData());
                }
                else if(event.getType() == PathChildrenCacheEvent.Type.CONNECTION_SUSPENDED){
                    System.out.println("连接失效CONNECTION_SUSPENDED.............");
                }
                else if(event.getType() == PathChildrenCacheEvent.Type.CONNECTION_RECONNECTED){
                    System.out.println("重新连接CONNECTION_RECONNECTED.............");
                }
                else if(event.getType() == PathChildrenCacheEvent.Type.CONNECTION_LOST){
                    System.out.println("连接失效后稍等一会儿执行CONNECTION_LOST.............");
                }
                else if(event.getType() == PathChildrenCacheEvent.Type.INITIALIZED){
                    System.out.println("INITIALIZED............");
                }
            }
        });

        //保证监听一直在
        System.in.read();
    }*/



   // @Test
    /*public void testTreeCache() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000,3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",retryPolicy);
        client.start();
        System.out.println("连接上zk....");

        //创建监听子节点
        final TreeCache treeCache = new TreeCache(client,"/app1");
        //开启监听
        treeCache.start();
        //添加监听节点，获取节点变化
        treeCache.getListenable().addListener(new TreeCacheListener() {
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                //监听当前节点子节点变化
                if(event.getType() == TreeCacheEvent.Type.NODE_ADDED){
                    System.out.println("NODE_ADDED 节点变化了"+treeCache.getCurrentData("/app1"));
                }
                //监听修改子节点变化
                else if(event.getType() == TreeCacheEvent.Type.NODE_UPDATED){
                    System.out.println("NODE_UPDATED节点变化了"+treeCache.getCurrentData("/app1"));
                }
                //监听删除子节点变化
                else if(event.getType() == TreeCacheEvent.Type.NODE_REMOVED){
                    System.out.println("NODE_REMOVED节点变化了"+treeCache.getCurrentData("/app1"));
                }
                else if(event.getType() == TreeCacheEvent.Type.CONNECTION_SUSPENDED){
                    System.out.println("连接失效CONNECTION_SUSPENDED.............");
                }
                else if(event.getType() == TreeCacheEvent.Type.CONNECTION_RECONNECTED){
                    System.out.println("重新连接CONNECTION_RECONNECTED.............");
                }
                else if(event.getType() == TreeCacheEvent.Type.CONNECTION_LOST){
                    System.out.println("连接失效后稍等一会儿执行CONNECTION_LOST.............");
                }
                else if(event.getType() == TreeCacheEvent.Type.INITIALIZED){
                    System.out.println("INITIALIZED............");
                }
            }
        });




        //保证监听一直在
        System.in.read();
    }*/


}
