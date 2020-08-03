package cn.tedu.sp06.controller;

import cn.tedu.sp01.pojo.Item;
import cn.tedu.sp01.pojo.Order;
import cn.tedu.sp01.pojo.User;
import cn.tedu.web.util.JsonResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class RibbonController {
    @Autowired
    private RestTemplate restTemplate;
    @GetMapping("/item-service/{orderId}")
    @HystrixCommand(fallbackMethod = "getItemFB")//降级方法
    public JsonResult<List<Item>> getItem(@PathVariable String orderId){
        //向指定微服务地址发送 get 请求，并获得该服务的返回结果
        //{1} 占位符，用 orderId 填充
        //return restTemplate.getForObject("http://localhost:8001/{1}",JsonResult.class,orderId);
        //使用ribbon实现负载均衡
        //从eureka可以得到"item-service"对应的主机地址列表
        return restTemplate.getForObject("http://item-service/{1}",JsonResult.class,orderId);
    }
    @PostMapping("/item-service/decreaseNumber")
    @HystrixCommand(fallbackMethod = "decreaseNumber")//降级方法
    public JsonResult<?> decreaseNumber(@RequestBody List<Item> items){
        //发送 post 请求
        //return restTemplate.postForObject("http://localhost:8001/decreaseNumber",items,JsonResult.class);
        //使用ribbon实现负载均衡
        return restTemplate.postForObject("http://item-service/decreaseNumber",items,JsonResult.class);
    }

    @GetMapping("/user-service/{userId}")
    @HystrixCommand(fallbackMethod = "getUserFB")//降级方法
    public JsonResult<User> getUser(@PathVariable Integer userId){
        return restTemplate.getForObject("http://user-service/{1}",JsonResult.class,userId);
    }
    @GetMapping("/user-service/{userId}/score")
    @HystrixCommand(fallbackMethod = "addScoreFB")//降级方法
    public JsonResult addScore(@PathVariable Integer userId,Integer score){
        return restTemplate.getForObject("http://user-service/{1}/score?score={2}", JsonResult.class, userId, score);
    }

    @GetMapping("/order-service/{orderId}")
    @HystrixCommand(fallbackMethod = "getOrderFB")//降级方法
    public JsonResult<Order> getOrder(@PathVariable String orderId){
        return restTemplate.getForObject("http://order-service/{1}",JsonResult.class,orderId);
    }
    @GetMapping("/order-service/")
    @HystrixCommand(fallbackMethod = "addOrderFB")//降级方法
    public JsonResult addOrder(){
        return restTemplate.getForObject("http://order-service/",JsonResult.class);
    }


    public JsonResult<List<Item>> getItemFB(String orderId) {
        return JsonResult.err().msg("获取订单商品列表失败");
    }
    public JsonResult decreaseNumberFB(List<Item> items) {
        return JsonResult.err().msg("更新商品库存失败");
    }
    public JsonResult<User> getUserFB(Integer userId) {
        return JsonResult.err().msg("获取用户信息失败");
    }
    public JsonResult addScoreFB(Integer userId, Integer score) {
        return JsonResult.err().msg("增加用户积分失败");
    }
    public JsonResult<Order> getOrderFB(String orderId) {
        return JsonResult.err().msg("获取订单失败");
    }
    public JsonResult addOrderFB() {
        return JsonResult.err().msg("添加订单失败");
    }
}
