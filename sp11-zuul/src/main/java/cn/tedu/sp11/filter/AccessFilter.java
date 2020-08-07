package cn.tedu.sp11.filter;

import cn.tedu.web.util.JsonResult;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

@Component
public class AccessFilter extends ZuulFilter {
    /**
     * 过滤器类型：前置，后置，路由，错误
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 过滤器添加的顺序号
     * pre拦截里面有5个拦截器，而想要获取的item-service的值对应的key为serviceID在第5个拦截器里
     * 所有只有把拦截器添加顺序设置为6才能获取到值
     * @return
     */
    @Override
    public int filterOrder() {
        return 6;
    }

    /**
     * 针对当前请求进行判断，判断当前请求是否要执行这个过滤器的过滤代码，返回false则不判断
     * 如果访问 item-service 要检查权限
     * 如果访问其他服务则不检查权限，直接访问
     * 对指定的serviceid过滤，如果要过滤所有服务，直接返回 true
     * @return
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        //获取当前请求的服务id
        String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);
        if (serviceId.equals("item-service")){
            return true;//要执行过滤代码
        }
        return false;//不执行过滤代码
    }

    /**
     * 过滤代码，对用户权限进行检查
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)){
            //没有token，阻止继续调用
            ctx.setSendZuulResponse(false);
            //发送提示用户没有登录
            ctx.setResponseStatusCode(JsonResult.NOT_LOGIN);
            ctx.setResponseBody(JsonResult.err().code(JsonResult.NOT_LOGIN).msg("not login!").toString());
        }
        //zuul过滤器返回的数据设计为以后扩展使用，
        //目前该返回值没有被使用
        return null;
    }
}
