package com.ztth.message.gate.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.ztth.message.gate.biz.RedisBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;

@Component
public class SessionAccessFilter extends ZuulFilter {

    @Autowired
    private RedisBiz redisBiz;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Autowired
    public JedisPool  jedisPool;

    @Override
    public Object run(){
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        final String requestUri = request.getRequestURI();
        final String method = request.getMethod();

        //url 验证是否 需要拦截
        //过滤 请求链接的有效性
        //操作在转发之前
        /*System.out.println(requestUri);
        System.out.println(request.getRemoteAddr());
        System.out.println(request.getRemoteHost());*/
        if(requestUri.indexOf("/user/getToken")==-1){
            //判断token 是否过期
            String token  =  request.getParameter("token");
            if(redisBiz.get(token)== null || redisBiz.get(token) == ""){
                setFailedRequest("403 Forbidden,invalid token!", 403);
            }
        }
        return null;
    }

    private void setFailedRequest(String body, int code) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(code);
        ctx.setResponseBody("{code:403,msg: \"403 Forbidden!\"}");
        ctx.setSendZuulResponse(false);

     /*   if (ctx.getResponseBody() == null) {
            ctx.setResponseBody(body);
            ctx.setSendZuulResponse(false);
           throw new RuntimeException("Code: " + code + ", " + body); //optional
        }*/
    }
}
