package com.itheima.utils;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.util.Properties;

/**
 * 配置中心工具类
 * PropertyPlaceholderConfigurer：== context:property-placeholder 加载jdbc.properties类
 */
public class SettingCenterUtil extends PropertyPlaceholderConfigurer implements ApplicationContextAware {

    XmlWebApplicationContext xmlWebApplicationContext;

    /**
     * 重写父类方法 加载配置数据
     * @param beanFactoryToProcess
     * @param props
     * @throws BeansException
     */
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        //1.从zk中获取配置数据放到props对象中
        loadZk(props);
        //2.添加监听
        addWatch(props);
        //2.调用父类
        super.processProperties(beanFactoryToProcess, props);
    }

    /**
     * 添加监听
     * @param props
     */
    private void addWatch(Properties props) {
        //1.创建重试对象

        try {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000,3);
            //2.创建客户端对象
            CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",retryPolicy);
            //3.开启客户端
            client.start();
            //4.创建监听对象
            TreeCache treeCache = new TreeCache(client,"/config");
            treeCache.start();//一定要开启监听
            treeCache.getListenable().addListener(new TreeCacheListener() {
                @Override
                    public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                        if(event.getType() == TreeCacheEvent.Type.NODE_UPDATED){
                        //这里不需要重新设置pro 刷新容器
                        xmlWebApplicationContext.refresh();
                    }
                }
            });

        } catch (Exception e) {
            System.out.println("监听失败。。。。。");
            e.printStackTrace();
        }

    }

    /**
     * 从zk中获取配置数据放到props对象中
     * @param props
     */
    private void loadZk(Properties props) {
        try {
            //1.创建重试对象
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000,3);
            //2.创建客户端对象
            CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",retryPolicy);
            //3.开启客户端
            client.start();
            //4.获取节点数据放到props对象中
            String driver = new String(client.getData().forPath("/config/jdbc.driver"));
            String url = new String(client.getData().forPath("/config/jdbc.url"));
            String user = new String(client.getData().forPath("/config/jdbc.user"));
            String password = new String(client.getData().forPath("/config/jdbc.password"));
            props.setProperty("jdbc.driver",driver);
            props.setProperty("jdbc.url",url);
            props.setProperty("jdbc.user",user);
            props.setProperty("jdbc.password",password);
            client.close();
        } catch (Exception e) {
            System.out.println("加载zk配置文件失败了。。。。。。");
            e.printStackTrace();
        }
    }

    /**
     * 启动的时候 将spring容器赋值给当前类的对象
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        xmlWebApplicationContext = (XmlWebApplicationContext)applicationContext;
    }
}