package m2_work;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws Exception {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.126.129");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");
        Channel c = f.newConnection().createChannel();
        c.queueDeclare("helloworld01",true,false,false,null);
        while (true){
            System.out.print("输入消息：");
            String msg = new Scanner(System.in).nextLine();
            c.basicPublish("","helloworld01", MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes());
        }
    }
}
