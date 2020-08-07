package m3_publishsubscribe;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.126.129");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");
        Channel c = f.newConnection().createChannel();
        //先做三步：1.定义交换机 2.定义随机队列 3.绑定到交换机
        c.exchangeDeclare("logs", BuiltinExchangeType.FANOUT);
        //手动用UUID来随机生成队列命名
        //String queue = UUID.randomUUID().toString();
        //c.queueDeclare(queue,false, true, true, null);
        //让Rabbitmq服务器来随机命名，非持久，独占，自动删除
        String queue = c.queueDeclare().getQueue();
        //第三个参数对fanout交换机无效
        c.queueBind(queue,"logs", "");
        //正常消费数据
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                String msg = new String(message.getBody());
                System.out.println("收到："+msg);
            }
        };
        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {

            }
        };
        c.basicConsume(queue,true,deliverCallback,cancelCallback);
    }
}
