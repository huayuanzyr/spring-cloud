package m2_work;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.126.129");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");
        Channel c = f.newConnection().createChannel();
        c.queueDeclare("helloworld01",true,false,false,null);
        DeliverCallback deliverCallback = new DeliverCallback() {

            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String msg = new String(delivery.getBody());
                System.out.println("收到："+msg);
                for (int i = 0; i <msg.length(); i++) {
                    if (msg.charAt(i) == '.') {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //ack只能让服务器知道请求是否处理完了，并不能解决将数据不发送给繁忙的消费者
                c.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                System.out.println("消息处理完成！！！");
            }
        };
        CancelCallback cancelCallback = new CancelCallback() {

            @Override
            public void handle(String s) throws IOException {
                
            }
        };
        // QOS: Quality of Service
        // 理解：每次抓取的消息数量
        // 如果设置成1，每次只抓取一条消息，这条消息处理完之前，不会继续抓取下一条
        // 必须在手动ACK模式下才有效
        c.basicQos(1);
        c.basicConsume("helloworld01",false,deliverCallback, cancelCallback);
    }
}
