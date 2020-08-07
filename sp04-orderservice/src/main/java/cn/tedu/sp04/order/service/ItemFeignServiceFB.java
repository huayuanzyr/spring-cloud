package cn.tedu.sp04.order.service;

import cn.tedu.sp01.pojo.Item;
import cn.tedu.web.util.JsonResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class ItemFeignServiceFB implements ItemFeignService {
    @Override
    public JsonResult<List<Item>> getItems(String orderId) {
        if (Math.random()<0.5){
            ArrayList<Item> items=new ArrayList<>();
            items.add(new Item(1,"缓存商品1",2));
            items.add(new Item(2,"缓存商品2",1));
            items.add(new Item(3,"缓存商品3",5));
            items.add(new Item(4,"缓存商品4",1));
            items.add(new Item(5,"缓存商品5",3));
            return JsonResult.ok().data(items);
        }
        return JsonResult.err().msg("无法获取订单商品列表");
    }

    @Override
    public JsonResult decreaseNumber(List<Item> items) {

        return JsonResult.err("无法修改商品库存");
    }
}
