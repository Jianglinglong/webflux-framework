#webflux初探
###响应对象
+ ```Flux```  集合对象
+ ```Mono<T>``` 单个对象结果或者视图或者无结果
###router
+ ```RouterFunction```
   + 函数式统一配置request映射到```HandlerFunction```
   + 使用RequestPredicates配置映射请求方式（get、post等）、请求url
   + 此处手动关联请求处理函数 ```HandlerFunction```
   >注意
   + 配置多个映射是需要连续使用```andRoute(RequestPredicate predicate, HandlerFunction<T> handlerFunction)```方法
+ ```HandlerFunction```
   + 处理函数
+ ```ServerResponse.ok()```
   + ```body``` 响应数据
   + ```render```  响应视图
   *****
+ ```RouterFunctions``` 映射工具类
   + ```nest(RequestPredicate predicate, RouterFunction<T> routerFunction) )```   可以为router添加公共url  
     ```java
     @Configuration
     public class TimeRouterFunction{
         @Bean
          public Mono<ServerResponse> response(){
                //创建一个"/now"的映射处理函数为handlerFunction
                RouterFunction<ServerResponse> route = RouterFunctions.route(RequestPredicates.GET("/now"), handlerFunction);
                //为上面的请求附加一个公共请求"/nest",则上面的请求URL变更为"/nest/now"
                return RouterFunctions.nest(RequestPredicates.path("/nest"),route);
          }
     }
     ```
+ ```RequestPredicate```请求规则
   + ```and(RequestPredicate other)``` 同时满足另一个规则
   + ```or(RequestPredicate other)``` 或者满足另一个规则 
   >比如多个请求URL映射到同一个处理函数  
+ ```RequestPredicates```请求规则工具类，提供多种语义静态方法创建请求规则
   + ```all()``` 类比 ```RequestMapping``` 不限请求方式
   + ```method(HttpMethod httpMethod)``` 限定为指定的请求方式
   + ```methods(HttpMethod... httpMethods)```限定为某几种请求方式
   + ```path(String path)``` 限定请求路径
   + ```headers(Predicate<ServerRequest.Headers> headersPredicate)```限定请求包含指定headers
   + ```contentType(MediaType... mediaTypes)```限定请求contentType
   + ```accept(MediaType... mediaTypes)```限定请求accept
   + ```GET(String pattern)```指定请求URL（表达式匹配）为GET请求
   + ```HEAD(String pattern)```指定请求URL（表达式匹配）为HEAD请求
   + ```POST(String pattern)```指定请求URL（表达式匹配）为POST请求
   + ```PUT(String pattern)```指定请求URL（表达式匹配）为PUT请求
   + ```PATCH(String pattern)```指定请求URL（表达式匹配）为PATCH请求
   + ```OPTIONS(String pattern)```指定请求URL（表达式匹配）为OPTIONS请求
   + ```pathExtension(String extension)```指定请求后缀
   + ```pathExtension(Predicate<String> extensionPredicate)```指定请求后缀（自定义表达式匹配）
   + ```queryParam(String name, String value)```指定请求包含指定参数name的值为value
   + ``` queryParam(String name, Predicate<String> predicate)```指定请求包含指定参数name的值为predicate规则匹配的
   
 + ```org.springframework.web.server.WebExceptionHandler```全局异常处理接口
    + ```Mono<Void> handle(ServerWebExchange exchange, Throwable ex)```异常处理函数
       响应结果,注意需要添加```@Oreder(-2)```已捕捉NOT_FOUND等其他异常，否则只能捕获HandlerFunction中的异常
       ```java
         @Order(-2)
         @Component
         public class GloableExceptionHandler implements WebExceptionHandler {
              //注入thymeleaf模板引擎,手动渲染数据。用以返回视图数据
             @Autowired
             private SpringWebFluxTemplateEngine templateEngine;
         
             @Override
             public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
                 ServerHttpResponse response = exchange.getResponse();
                 ServerHttpRequest request = exchange.getRequest();
                 HttpHeaders headers = request.getHeaders();
                 boolean contains = headers.getAccept().contains(MediaType.APPLICATION_JSON);
                 //设置响应code
                 response.setStatusCode(HttpStatus.OK);
                 String error = ex.getMessage();
                 if (contains) {
                     //响应json数据
                     response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                 } else {
                     //响应HTML
                     response.getHeaders().setContentType(MediaType.TEXT_HTML);
                     Map<String, Object> map = new HashMap<>();
                     map.put("error", ex.getMessage());
                     //手动渲染视图("error")
                     error = templateEngine.process("error", new Context(Locale.CHINA, map));
                 }
                 return response.writeWith(Mono.just(response.bufferFactory().wrap(error.getBytes())));
             }
         }
      ```
       